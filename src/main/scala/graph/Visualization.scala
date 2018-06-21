package graph

import scala.scalajs.js



object Visualization {

  val naughty = GraphNode(0, "Naughty")
  val by = GraphNode(1, "By")
  val nature = GraphNode(2, "Nature")

  val nodes = js.Array(naughty, by, nature)
  val edges: js.Array[GraphLink] = js.Array(
    GraphLink(naughty, by, "You 'bout to feel"),
    GraphLink(naughty, by, "the chronicles of a bionical lyric"),
    GraphLink(naughty, by, "Lyrically splitting dismissing"),
    GraphLink(by, nature, "I'm on a mission of just hitting"),
    GraphLink(by, nature, "Now it's written and"),
    GraphLink(by, nature, "kitten hitting wit mittens"),
    GraphLink(nature, naughty, "I'm missing, wishing, man, listen"),
    GraphLink(nature, naughty, "I glisten like"),
    GraphLink(nature, naughty, "sun and water while fishing")
  )

  def main(args: Array[String]): Unit = Graph(nodes, edges)

}
