package GUI

import javafx.collections.{FXCollections, ObservableList}

import drinks.Drink
import functionality.Scraper

object data{
  def getColumnData: ObservableList[Drink] = {
    val data: ObservableList[Drink] = FXCollections.observableArrayList();

    val ciders = Scraper.scrapeCider(Scraper.rostockURL)

    for(i <- ciders){
      data.add(i)
    }
    data
  }
}
