package drinks

object DrinkType extends Enumeration {
  val BEER, CIDER, WINE, SPIRITS, LIQUOR = Value;
}

object SizeUnit extends Enumeration {
  val CL, L, UNKNOWN = Value;
}