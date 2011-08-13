
package code
package comet

import scala.collection.mutable.{Map, HashMap}

import net.liftweb._
import http._
import actor._
import util.StringHelpers.randomString

//
// Messages that are passed between the client and the server
//
case class UpdateLinks(topLinks: List[Link])
case class AddLink(url: String, title: String)
case class VoteUp(linkId: String)
case class VoteDown(linkId: String)

//
// Data structures
//
case class Link(id: String, entry: LinkEntry)
case class LinkEntry(url: String, title: String, var score: Int)

/**
 * A singleton that maintains global state, processing
 * passed in messages and updating any attached client.
 */
object LinkServer extends LiftActor with ListenerManager {
	private var links: Map[String, LinkEntry] = HashMap[String, LinkEntry]()
	private var topLinks: List[Link] = Nil

	def createUpdate = {
		UpdateLinks(topLinks)
	}

	def updateTopLinks {
		topLinks = links.toList.map(p => Link(p._1, p._2)).
		    sortWith(_.entry.score > _.entry.score).take(20)

		updateListeners(UpdateLinks(topLinks))
	}

	override def lowPriority = {
		case l: AddLink => try {
			links += randomString(12) -> LinkEntry(l.url, l.title, 1)
			updateTopLinks
		}

		case l: VoteUp => try {
			links(l.linkId).score += 1
			updateTopLinks
		}

		case l: VoteDown => try {
			links(l.linkId).score -= 1
			updateTopLinks
		}
	}
}

