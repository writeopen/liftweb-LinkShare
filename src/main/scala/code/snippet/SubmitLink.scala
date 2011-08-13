
package code
package snippet

import net.liftweb._
import http._
import util._
import common._
import Helpers._
import js._
import JsCmds._
import JE._
import net.liftweb.http.js.JsCmds.Noop

import scala.xml.NodeSeq

import comet.AddLink
import comet.LinkServer

object SubmitLink {
	def render = {
		var url = ""
		var name = ""

		def process(): JsCmd = {
			LinkServer ! AddLink(url, name)
			Noop
		}

		"name=url" #> SHtml.text(url, url = _) &
		"name=name" #> (SHtml.text(name, name = _) ++ SHtml.hidden(process))
	}
}

