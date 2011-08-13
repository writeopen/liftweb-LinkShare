
package code
package comet

import scala.xml._

import net.liftweb._
import http._
import util._
import Helpers._

class LinkClient extends CometActor with CometListener {
	private var links: List[Link] = List[Link]()

	def registerWith = LinkServer

	override def lowPriority = {
		case data: UpdateLinks => try {
			links = data.topLinks
			reRender()
		}
	}

	def render = {
		def linkView(link: Link): NodeSeq = {
			<li><a href={link.entry.url}>{link.entry.title}</a>
			[{link.entry.score} {if (link.entry.score == 1) "vote" else "votes"}]
			{SHtml.a(() => { LinkServer ! VoteUp(link.id) }, Text("++"))}
			{SHtml.a(() => { LinkServer ! VoteDown(link.id) }, Text("--"))}
			</li>
		}

		"#view" #> <ol>{links.flatMap(linkView _)}</ol>
	}
}

