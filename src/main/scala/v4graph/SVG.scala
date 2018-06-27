package v4graph

import org.singlespaced.d3js.d3

import scala.scalajs.js

trait SVG

case class Arc(rx: Double, ry: Double, xAxisRotation: Int, large: Boolean, sweep: Boolean, x: Double, y: Double) extends SVG {
  override def toString: String = s"A$rx, $ry $xAxisRotation, ${if (large) 1 else 0 }, ${ if (sweep) 1 else 0 } $x, $y"
}
case class MoveTo(x: Double, y: Double) extends SVG {
  override def toString: String = s"M$x, $y"
}
case class ArcPath(m: MoveTo, a: Arc) extends SVG {
  override def toString: String = s"$m $a"
}

object ArcPath {
  def apply(onLeft: Boolean, d: GraphLink, siblings: js.Array[GraphLink]): ArcPath = {
    val x1 = if (onLeft) d.source.x else d.target.x
    val y1 = if (onLeft) d.source.y else d.target.y
    val x2 = if (onLeft) d.target.x else d.source.x
    val y2 = if (onLeft) d.target.y else d.source.y

    val dx = x2.getOrElse(0.0) - x1.getOrElse(0.0)
    val dy = y2.getOrElse(0.0) - y1.getOrElse(0.0)
    val dr = math.sqrt(dx * dx + dy * dy)
    val sweep = !onLeft
    val siblingCount = siblings.length.toDouble
    val xAxisRotation = 0
    val largeArc = false
    val moveTo = MoveTo(x1.getOrElse(0.0), y1.getOrElse(0.0))
    val arcScale = d3.scale.ordinal()
      .domain(siblings.map(_.toString))
      .rangePoints((1.0, siblingCount * 1.0))

    val arc = if (siblingCount > 1.0) {
      val drxy = dr / (1.0 + (1.0 / siblingCount) * (arcScale(d.toString) - 1))
      Arc(drxy, drxy, xAxisRotation, largeArc, sweep, x2.getOrElse(0.0), y2.getOrElse(0.0))
    } else {
      Arc(dr, dr, xAxisRotation, largeArc, sweep, x2.getOrElse(0.0), y2.getOrElse(0.0))
    }

    ArcPath(moveTo, arc)
  }
}
