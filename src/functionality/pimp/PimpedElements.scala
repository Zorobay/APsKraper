package pimp

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object Pimp {

  implicit class PimpElements(es: Elements) extends Iterator[Element] {
      var currentElem = 0

      def hasNext = currentElem < es.size

      def next(): Element = {
        currentElem += 1
        es.get(currentElem - 1)
    }
  }

  implicit class PimpString(str: String){
    def maxSize(size: Int): String = {
      val len = size.max(str.size)

      str.substring(0, len)
    }
  }

}

