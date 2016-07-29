package functionality

import scala.io.StdIn.readLine

object start {

  def main(args: Array[String]): Unit = {

    var ln = ""

    do{
      ln = readLine()

      ln.toLowerCase() match {
        case "cider" => Scraper.scrapeCider(Scraper.puttgarden)
        case "beer" => Scraper.scrapeBeer(Scraper.rostock)
        case "spirits" => Scraper.scrapeSpirits(Scraper.rostock)
        case noOpt => println("not an option: " + noOpt)
      }
    }while(ln != null)

    println("exited")
  }
}