package functionality

import _root_.drinks.{Drink, DrinkType}


import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.collection.mutable.ArrayBuffer

object Scraper {

  //----maximum number of drinks on a page----
  val hits = 1000

  //----URLs----
  val showAllExtension = "?page=1&hits=" + hits
  val puttgardenURL = "http://www.puttgarden."
  val rostockURL = "http://www.rostock."
  val ciderURL = "border-shop.dk/Cider"
  val danishBeerURL = "border-shop.dk/Ol-Dansk"
  val finishBeerURL = "border-shop.dk/Ol-Finsk"
  val hollandBeerURL = "border-shop.dk/Ol-Holland"
  val swedishBeerURL = "border-shop.dk/Ol-Svensk"
  val germanBeerURL = "border-shop.dk/Ol-Tysk"
  val specialBeerURL = "border-shop.dk/Ol-special"
  val spiritsURL = "border-shop.dk/Spiritus"
  val wineURL = "border-shop.dk/Wines"

  def getPage(loc: String, sort: String): Elements = {
    val page: Document = Jsoup.connect(loc + sort + showAllExtension).get
    page.getElementsByClass("search_result")
  }

  def scrapeCider(location: String): ArrayBuffer[Drink] = {

    val ciders = getPage(location, ciderURL)

    //add all cider to an array
    val ciderArray: ArrayBuffer[Drink] = ArrayBuffer.empty[Drink]

    //if cider.getElementsByClass("info").text() != ""
    for (i <- 0 to ciders.size() - 1; if ciders.get(i).getElementsByClass("info").text() != "") {
      ciderArray += Drink(DrinkType.CIDER, ciders.get(i))
    }

    val sortedCiders = ciderArray.sortWith(_.apSEK > _.apSEK)

    sortedCiders
  }

  def scrapeBeer(loc: String, sort: String): Unit = {

    val beers = getPage(loc, sort)

    //add all cider to an array
    val beerArray: ArrayBuffer[Drink] = ArrayBuffer.empty[Drink]

    //if cider.getElementsByClass("info").text() != ""
    for (i <- 0 to beers.size() - 1; if beers.get(i).getElementsByClass("info").text() != "") {
      beerArray += Drink(DrinkType.BEER, beers.get(i))
    }

    val sortedBeers = beerArray.sortWith(_.apSEK > _.apSEK)
    var num = 1
    for (beer <- sortedBeers) {
      println(f"$num%3d. " + beer)
      num += 1
    }
  }

  def scrapeSpirits(loc: String, sort: String): Unit = {

    val spirits = getPage(loc, sort)

    //add all cider to an array
    val spiritsArray: ArrayBuffer[Drink] = ArrayBuffer.empty[Drink]

    //if cider.getElementsByClass("info").text() != ""
    for (i <- 0 to spirits.size() - 1; if spirits.get(i).getElementsByClass("info").text() != "") {
      spiritsArray += Drink(DrinkType.SPIRITS, spirits.get(i))
    }

    val sortedSpirits = spiritsArray.sortWith(_.apSEK > _.apSEK)
    var num = 1
    for (spirit <- sortedSpirits) {
      println(f"$num%3d. " + spirit.toMinimalString())
      num += 1
    }
  }
}