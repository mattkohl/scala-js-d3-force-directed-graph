package v4graph

import d3v4._
import org.scalajs.dom.window

import scala.scalajs.js

case class GraphNode(label: String) extends SimulationNodeImpl {
  val id: Int = label.hashCode
  def toId(prefix: String = ""): String = s"$prefix$id"
  def translate: String = s"translate($x, $y)"
}
case class GraphLink(source: GraphNode, value: String, target: GraphNode) extends SimulationLinkImpl[GraphNode, GraphNode] {
  def getSiblings(others: js.Array[GraphLink]): js.Array[GraphLink] = others.filter { link =>
    link.source.id == source.id && link.target.id == target.id || link.source.id == target.id && link.target.id == source.id
  }
  def toId(prefix: String = ""): String = s"$prefix${source.id}-${value.replace(" ", "-").toLowerCase}-${target.id}"
}
case class Graph(links: js.Array[GraphLink]) {

  val nodes: js.Array[GraphNode] = links.flatMap{ link: GraphLink => js.Array(link.source, link.target) }.distinct

  val w: Double = window.innerWidth - 20
  val h: Double = window.innerHeight

  val svg = d3.select("#graph")
    .append("svg:svg")
    .attr("width", w)
    .attr("height", h)
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

  val simulation = d3.forceSimulation()
    .force("link", d3.forceLink())
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(w/2, h/2))
    .force("collide", d3.forceCenter())

  val path = svg.selectAll[GraphLink]("path.link").data(links)

  path
    .enter()
    .append("svg:path")
    .attr("id", (d: GraphLink) => d.toId())
    .attr("class", "link")
    .attr("marker-end", "url(#arrowhead)")

  val pathInvis = svg.selectAll[GraphLink]("path.invis").data(links)

  pathInvis
    .enter()
    .append("svg:path")
    .attr("id", (d: GraphLink) => d.toId("invis_"))
    .attr("class", "invis")

  val pathLabel = svg.selectAll[GraphLink](".pathLabel").data(links)

  pathLabel
    .enter()
    .append("g")
    .append("svg:text")
    .attr("class", "pathLabel")
    .append("svg:textPath")
    .attr("startOffset", "50%")
    .attr("text-anchor", "middle")
    .attr("xlink:href", (d: GraphLink) => d.toId("#invis_"))
    .text((d: GraphLink) => d.value)

  val node = svg.selectAll("g.node").data(nodes)

  node
    .enter()
    .append("g")
    .attr("class", "node")
    .call(d3.drag())
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

  val ticked = () => {
    path.attr("d", (d: GraphLink) => ArcPath(onLeft = true, d, d.getSiblings(links)).toString)
    pathInvis.attr("d", (d: GraphLink) => {
      val dsx = d.source.x.getOrElse(0.0)
      val dtx = d.target.x.getOrElse(0.0)
      ArcPath(dsx < dtx, d, d.getSiblings(links)).toString
    })
    node.attr("transform", (n: GraphNode) => n.translate)
    ()
  }

  simulation
    .on("tick", ticked)
    .forceAs[Link[GraphNode, GraphLink]]("link").links(links)



}
