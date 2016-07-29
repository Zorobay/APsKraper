package GUI

import scala.swing.Table

/**
  * Created by Zorobay on 28-Jul-16.
  */
class UpdateableTable(rowData: Array[Array[Any]], headers: Seq[_]) extends Table(rowData, headers) {

  def updateData(data: Array[Array[Any]]): Table = {
    new Table(data, headers)
  }

  def updateHeaders(headers: Seq[_]): Table = {
    new Table(rowData, headers)
  }

  def empty: Table = {
    new Table()
  }
}

