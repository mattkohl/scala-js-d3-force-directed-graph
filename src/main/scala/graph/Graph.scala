package graph

import org.scalajs.dom
import org.scalajs.dom.window
import org.singlespaced.d3js.Ops._
import org.singlespaced.d3js.{Link, Selection, d3, forceModule}
import org.singlespaced.d3js.selection.Update

import scala.scalajs.js

case class GraphNode(id: Int, label: String) extends forceModule.Node {
  def toId(prefix: String = ""): String = s"$prefix$id"

  def translate: String = s"translate($x, $y)"
}

case class GraphLink(source: GraphNode, target: GraphNode, value: String) extends Link[GraphNode] {
  def getSiblings(others: js.Array[GraphLink]): js.Array[GraphLink] = others.filter { link =>
    link.source.id == source.id && link.target.id == target.id || link.source.id == target.id && link.target.id == source.id
  }

  def toId(prefix: String = ""): String = s"$prefix${source.id}-${value.replace(" ", "-").toLowerCase}-${target.id}"
}

case class Graph(nodes: js.Array[GraphNode], links: js.Array[GraphLink]) {

  val w: Double = window.innerWidth - 20
  val h: Double = window.innerHeight

  val svg: Selection[dom.EventTarget] = d3.select("#graph")
    .append("svg:svg")
    .attr("width", w)
    .attr("height", h)
    .style("z-index", -10)
    .attr("id", "svg")

  svg.append("svg:defs").selectAll("marker").data(js.Array("end"))
    .enter()
    .append("svg:marker")
    .attr("id", "arrowhead")
    .attr("viewBox", "0 -5 10 10")
    .attr("refX", 22)
    .attr("refY", 0)
    .attr("orient", "auto")
    .attr("markerWidth", 20)
    .attr("markerHeight",  20)
    .attr("markerUnits", "strokeWidth")
    .attr("xoverflow", "visible")
    .append("svg:path")
    .attr("d", "M0,-5L10,0L0,5")
    .attr("fill", "#ccc")

  val force: forceModule.Force[GraphNode, GraphLink] = {
    d3.layout.force[GraphNode, GraphLink]()
      .charge(-50)
      .friction(0.1)
      .linkDistance(300)
      .size((w, h))
      .nodes(nodes)
      .links(links)
      .start()
  }

  val path: Update[GraphLink] = svg.selectAll[GraphLink]("path.link").data(links)

  path
    .enter()
    .append("svg:path")
    .attr("id", (d: GraphLink) => d.toId())
    .attr("class", "link")
    .attr("marker-end", "url(#arrowhead)")

  val pathInvis: Update[GraphLink] = svg.selectAll[GraphLink]("path.invis").data(force.links)

  pathInvis
    .enter()
    .append("svg:path")
    .attr("id", (d: GraphLink) => d.toId("invis_"))
    .attr("class", "invis")

  val pathLabel: Update[GraphLink] = svg.selectAll[GraphLink](".pathLabel").data(force.links)

  pathLabel
    .enter()
    .append("g")
    .append("svg:text")
    .attr("class", "pathLabel")
    .append("svg:textPath")
    .attr("startOffset", "50%")
    .attr("text-anchor", "middle")
    .attr("xlink:href", (d: GraphLink) => d.toId("#invis_"))
    .style("fill", "#ccccc")
    .style("font-size", 10)
    .text((d: GraphLink) => d.value)

  val node: Update[GraphNode] = svg.selectAll("g.node").data(force.nodes)

  node
    .enter()
    .append("g")
    .attr("class", "node")
    .call(force.drag)
    .append("svg:circle")
    .attr("r", 10)
    .attr("id", (n: GraphNode) => n.toId())
    .attr("class", "nodeStrokeClass")
    .attr("fill", "#0db7ed")

  node
    .append("svg:text")
    .attr("class", "textClass")
    .attr("x", 20)
    .attr("y", ".31em")
    .text((n: GraphNode) => n.label)

  force.on("tick", (_: dom.Event) => {
    path.attr("d", (d: GraphLink) => ArcPath(onLeft = true, d, d.getSiblings(force.links)).toString)
    pathInvis.attr("d", (d: GraphLink) => {
      val dsx = d.source.x.getOrElse(0.0)
      val dtx = d.target.x.getOrElse(0.0)
      ArcPath(dsx < dtx, d, d.getSiblings(force.links)).toString
    })
    node.attr("transform", (n: GraphNode) => n.translate)
    ()
  })

}
