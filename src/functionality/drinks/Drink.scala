package functionality.drinks

import functionality.propertyextraction.Extractor

import org.jsoup.nodes.Element

case class Drink(url: String, drinkType: DrinkType.Value, name: String, amount: Int, size: Int, unit: SizeUnit.Value,
                 alc: Double, priceDKK: Double, priceEUR: Double, priceSEK: Double) {

  val apDKK = (amount * size * {if (unit == SizeUnit.CL) 10 else 1000} * (alc / 100)) / priceDKK
  val apEUR = (amount * size * {if (unit == SizeUnit.CL) 10 else 1000} * (alc / 100)) / priceEUR
  val apSEK = (amount * size * {if (unit == SizeUnit.CL) 10 else 1000} * (alc / 100)) / priceSEK

  override def toString() = {
    val alcS = if (alc < 0) "???" else f"$alc%2.1f%%"

    f"url: $url%-60s type: $drinkType%-10s name: $name%-40s amount: $amount%-3d size: $size%-3d${unit.toString.toLowerCase}. alc: $alcS%-6s priceDKK: $priceDKK%6.2f priceEUR: " +
      f"$priceEUR%6.2f priceSEK: $priceSEK%6.2f apDKK: $apDKK%4.2fml. apEUR: $apEUR%4.2fml. apSEK: $apSEK%4.2fml."
  }

  def toMinimalString() = {
    val alcS = if (alc < 0) "???" else f"$alc%2.1f%%"

    f"$drinkType%-10s name: $name%-40s amount: $amount%-3d size: $size%-3d${unit.toString.toLowerCase}. alc: $alcS%-6s priceSEK: $priceSEK%6.2f apSEK: $apSEK%4.2fml."
  }

  def toArray(): Array[Any] ={
    Array(url, name, drinkType, amount, size, alc, priceDKK, apDKK, priceEUR, apEUR, priceSEK, apSEK)
  }
}

object Drink {
  def apply(drinkType: DrinkType.Value, element: Element) = {

    val url = element.getElementsByTag("a").attr("href")
    val name = Extractor.extrName(element.getElementsByTag("h3").text)
    val amount = Extractor.extrAmount(element.getElementsByClass("sizeDescription").text)
    val size = Extractor.extrSize(element.getElementsByClass("sizeDescription").text)
    val unit = Extractor.extrUnit(element.getElementsByClass("sizeDescription").text)
    val alc = Extractor.extrAlc(element.getElementsByTag("h3").text)
    val priceDKK = Extractor.extrDKK(element.getElementsByClass("normalPrice").text)
    val priceEUR = Extractor.extrEUR(element.getElementsByClass("normalPrice").text)
    val priceSEK = Extractor.extrSEK(element.getElementsByClass("normalPrice").text)

    new Drink(url, drinkType, name, amount, size, unit, alc, priceDKK, priceEUR, priceSEK)
  }

}