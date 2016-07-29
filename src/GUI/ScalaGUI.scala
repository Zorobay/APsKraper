package GUI

import functionality.Scraper
import functionality.drinks.Drink

import scala.collection.mutable.ArrayBuffer
import scala.swing._
import scala.swing.event.{ButtonClicked, ValueChanged}

object Gui extends SimpleSwingApplication{

  setSystemLookAndFeel()

  val content = new BoxPanel(Orientation.Vertical)

  val top = new MainFrame{
    title = "APsKraper"
    content.contents.append(ControlPanel.getControlPanel, Table.getTable, TextArea.getTextArea)
    contents = content
    size = new Dimension(1000,500)
    centerOnScreen
  }

  top.visible = true
  def getContent: Component = {
    content
  }

  def setSystemLookAndFeel() {
    import javax.swing.UIManager
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  }

  def updateTable(element: Component): Unit ={
    top.contents = new BoxPanel(Orientation.Vertical){contents.append(ControlPanel.getControlPanel, element, TextArea.getTextArea)}
  }
}

object Data{
  def cider(loc: String) = Scraper.scrapeCider(loc).map(x => x.toArray).toArray
  def beer(loc: String) = Scraper.scrapeBeer(loc).map(x => x.toArray).toArray
  def spirits(loc: String) = Scraper.scrapeSpirits(loc).map(x => x.toArray).toArray
}

object Table{
  var cider = false;
  var beer = false;
  var spirits = false;

  def toggleCider: Unit ={
    cider = !cider
  }

  def toggleBeer: Unit ={
    beer = !beer
  }

  def toggleSpirits: Unit ={
    spirits = !spirits
  }

  private def createTable(): BoxPanel ={

    lazy val ui = new BoxPanel(Orientation.Vertical){
      val headers = Array("url", "name", "drinkType", "amount", "size", "ABV %", "price DKK", "apDKK", "price EUR", "apEUR", "price SEK", "apSEK")

      var data: ArrayBuffer[Array[Any]] = ArrayBuffer.empty
      if(beer)
        data ++ (Scraper.scrapeBeer(Scraper.rostock).map(x => x.toArray))
      if(cider)
        data ++ (Scraper.scrapeCider(Scraper.rostock).map(x => x.toArray))
      if(spirits)
        data ++ (Scraper.scrapeSpirits(Scraper.rostock).map(x => x.toArray))

      val table = new UpdateableTable(data.toArray, headers)

      contents += new ScrollPane(table)
    }

    ui
  }

  def addData(data: Array[Array[Drink]]): Unit ={
    Gui.top.contents
  }

  def getTable = createTable()

  def updateTable = {
    Gui.updateTable(getTable)
  }
}

object ControlPanel{
  lazy val ui = new FlowPanel(){
    val beer = new CheckBox("Beer")
    val cider = new CheckBox("Cider")
    val spirits = new CheckBox("Spirits")
    val timeoutLabel = new Label("timeout")
    val timeoutBox = new TextField("ms (0 = inf)")
    val timeout = new BoxPanel(Orientation.Vertical){
      contents += timeoutLabel
      contents += timeoutBox
    }

    listenTo(beer, cider, spirits, timeoutBox)
    reactions += {
      case ButtonClicked(`beer`) => {
        Table.toggleBeer
        Table.updateTable
      }
      case ButtonClicked(`cider`) => {
        Table.toggleCider
        Table.updateTable
      }
      case ButtonClicked(`spirits`) => {
        Table.toggleSpirits
        Table.updateTable
      }
      case ValueChanged(`timeoutBox`) => {
        try{
          if(timeoutBox.text == "")
            Scraper.timeout = 0
          else
            Scraper.timeout = timeoutBox.text.toInt
        }catch{
          case c: NumberFormatException => TextArea.append("Wrong format of timeout! Only use numbers.")
        }
      }
    }
    contents.append(beer, cider, spirits, timeout)
  }

  def getControlPanel = ui
}

object TextArea{
  val textArea = new TextArea

  def getTextArea: TextArea ={
    textArea
  }

  def append(str: String): Unit ={
    textArea.append(str)
  }

  def setText(str: String): Unit ={
    textArea.text = str
  }
}