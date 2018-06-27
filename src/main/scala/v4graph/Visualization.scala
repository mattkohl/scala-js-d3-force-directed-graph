package v4graph

import scala.scalajs.js

object Visualization {

  val foafAgent = GraphNode("foaf:Agent")
  val foafDocument = GraphNode("foaf:Document")
  val foafImage = GraphNode("foaf:Image")
  val foafOnlineAccount = GraphNode("foaf:OnlineAccount")
  val foafPerson = GraphNode("foaf:Person")
  val owlThing = GraphNode("owl:Thing")
  val skosConcept = GraphNode("skos:Concept")

  val edges: js.Array[GraphLink] = js.Array(
    GraphLink(foafPerson, "foaf:knows", foafPerson),
    GraphLink(skosConcept, "foaf:focus", owlThing),
    GraphLink(foafOnlineAccount, "foaf:accountServiceHomepage", foafDocument),
    GraphLink(owlThing, "foaf:isPrimaryTopicOf", foafDocument),
    GraphLink(foafImage, "foaf:depicts", owlThing),
    GraphLink(foafAgent, "foaf:openid", foafDocument),
    GraphLink(foafAgent, "foaf:account", foafOnlineAccount),
    GraphLink(owlThing, "foaf:page", foafDocument),
    GraphLink(owlThing, "foaf:homepage", foafDocument),
    GraphLink(foafAgent, "foaf:topic_interest", owlThing),
    GraphLink(owlThing, "foaf:logo", owlThing),
    GraphLink(foafPerson, "foaf:img", foafImage),
    GraphLink(owlThing, "foaf:fundedBy", owlThing),
    GraphLink(foafAgent, "foaf:interest", foafDocument),
    GraphLink(foafPerson, "foaf:workInfoHomepage", foafDocument),
    GraphLink(foafPerson, "foaf:currentProject", owlThing),
    GraphLink(foafAgent, "foaf:mbox", owlThing),
    GraphLink(foafPerson, "foaf:schoolHomepage", foafDocument),
    GraphLink(foafAgent, "foaf:holdsAccount", foafOnlineAccount),
    GraphLink(owlThing, "foaf:maker", foafAgent),
    GraphLink(foafPerson, "foaf:pastProject", owlThing)

  )

  def main(args: Array[String]): Unit = Graph(edges)

}
