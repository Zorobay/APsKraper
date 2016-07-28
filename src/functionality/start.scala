package functionality

import GUI.GUI

import scala.io.StdIn.readLine

object start {

  def main(args: Array[String]): Unit = {

    GUI.initiate(args)

    var ln = ""

    do{
      ln = readLine()

      ln.toLowerCase() match {
        case "cider" => Scraper.scrapeCider(Scraper.rostockURL)
        case "beer" => Scraper.scrapeBeer(Scraper.rostockURL, Scraper.swedishBeerURL)
        case "spirits" => Scraper.scrapeSpirits(Scraper.rostockURL, Scraper.swedishBeerURL)
        case noOpt => println("not an option: " + noOpt)
      }
    }while(ln != null)

    println("exited")
  }
}