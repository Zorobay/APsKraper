package functionality.propertyextraction

import drinks.SizeUnit

object Extractor {

  private val amountPattern = "\\s*(\\d+).*".r
  private val sizePattern = ".*[aàá]\\s*(\\d+).*".r
  private val unitPattern = ".*\\d\\s*(\\w*).*".r
  private val alcPattern = "\\D*(\\d+,?\\d*)\\s*%.*".r
  private val namePattern = "\\s*(\\D+).*".r
  private val priceDKKPattern = "\\s*DKK\\s*(\\d+,?\\d*).*".r
  private val priceEURPattern = ".*EUR\\s*(\\d+,?\\d*).*".r
  private val priceSEKPattern = ".*SEK\\s*(\\d+,?\\d*).*".r

  def extrAmount(str: String): Int = {
    str match {
      case amountPattern(amount) => amount.toInt
      case _ => -1
    }
  }

  def extrSize(str: String): Int = {
    str match {
      case sizePattern(size) => size.toInt
      case _ => -1
    }
  }

  def extrUnit(str: String): SizeUnit.Value = {
    str match {
      case unitPattern(unit) => unit.toLowerCase() match {
        case "cl" => SizeUnit.CL
        case "l" => SizeUnit.L
        case _ => SizeUnit.UNKNOWN
      }
      case _ => SizeUnit.UNKNOWN
    }
  }

  def extrAlc(str: String): Double = {
    str match {
      case alcPattern(alc) => alc.replace(',', '.').toDouble
      case _ => -1.0
    }
  }

  def extrName(str: String): String = {
    str match {
      case namePattern(name) => {
        if (name.charAt(name.length() - 1) == ' ')
          name.substring(0, name.length() - 1)
        else
          name
      }
      case _ => "???"
    }
  }

  def extrDKK(str: String): Double = {
    str match {
      case priceDKKPattern(dkk) => dkk.replace(',', '.').toDouble
      case _ => -1.0
    }
  }

  def extrEUR(str: String): Double = {
    str match {
      case priceEURPattern(eur) => eur.replace(',', '.').toDouble
      case _ => -1.0
    }
  }

  def extrSEK(str: String): Double = {
    str match {
      case priceSEKPattern(sek) => sek.replace(',', '.').toDouble
      case _ => -1.0
    }
  }

}