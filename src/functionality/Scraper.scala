package functionality

import java.util

import functionality.drinks._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.collection.mutable.ArrayBuffer

object Scraper {

  //----maximum number of drinks on a page----
  val hits = 1000

  //----URLs----
  val showAllExtension = "?page=1&hits=" + hits
  val puttgarden = "http://www.puttgarden."
  val rostock = "http://www.rostock."
  val ciderURL = "border-shop.dk/Cider"
  val danishBeerURL = "border-shop.dk/Ol-Dansk"
  val finishBeerURL = "border-shop.dk/Ol-Finsk"
  val hollandBeerURL = "border-shop.dk/Ol-Holland"
  val swedishBeerURL = "border-shop.dk/Ol-Svensk"
  val germanBeerURL = "border-shop.dk/Ol-Tysk"
  val specialBeerURL = "border-shop.dk/Ol-special"
  val spiritsURL = "border-shop.dk/Spiritus"
  val wineURL = "border-shop.dk/Wines"

  //---timeout---
  var timeout = 4000

  private def getPage(loc: String, sort: String): Elements = {
    val page: Document = Jsoup.connect(loc + sort + showAllExtension).timeout(timeout).get()
    page.getElementsByClass("search_result")
  }

  private def scrape(loc: String, product: String): ArrayBuffer[Drink] ={
    val products = getPage(loc, product)

    //add all cider to an array
    val productArray: ArrayBuffer[Drink] = ArrayBuffer.empty

    //if cider.getElementsByClass("info").text() != ""
    for (i <- 0 to products.size() - 1; if products.get(i).getElementsByClass("info").text() != "") {
      productArray += Drink(DrinkType.CIDER, products.get(i))
    }

    productArray
  }

  def scrapeCider(loc: String): ArrayBuffer[Drink] = {
    scrape(loc, ciderURL)
  }

  def scrapeBeer(loc: String): ArrayBuffer[Drink] = {
    scrape(loc, danishBeerURL)
  }

  def scrapeSpirits(loc: String): ArrayBuffer[Drink] = {
   scrape(loc, spiritsURL)
  }
}