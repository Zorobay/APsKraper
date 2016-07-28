package GUI;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import scala.reflect.internal.util.TableDef;

public class GUI extends Application {

    public static void initiate(String[] args) {

        launch(args);
    }

    private TableView table = new TableView();

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("APsKraper");

        // set up grid pane
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setGridLinesVisible(true);
        pane.setPadding(new Insets(25, 25, 25, 25));

        //Set up table columns
        table.setEditable(false);
        TableColumn URL_col = new TableColumn("URL");
        URL_col.setCellValueFactory(
                new PropertyValueFactory<drinks.Drink, String>()
        );
        TableColumn drinkType_col = new TableColumn("Drink Type");
        TableColumn name_col = new TableColumn("Name");
        TableColumn amount_col = new TableColumn("Amount");
        TableColumn size_col = new TableColumn("Size");
        TableColumn alc_col = new TableColumn("ABV");
        TableColumn AP_col = new TableColumn("Alcohol per *");
        TableColumn SEK_col = new TableColumn("SEK");
        TableColumn DKK_col = new TableColumn("DKK");
        TableColumn EUR_col = new TableColumn("EUR");

        AP_col.getColumns().addAll(SEK_col,DKK_col,EUR_col);
        table.getColumns().addAll(URL_col, drinkType_col, name_col, amount_col, size_col, alc_col, AP_col);
        pane.getChildren().add(table);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle colorpicker events
    private final EventHandler<ActionEvent> colorPickerClicked = event ->{
        System.out.println("hej");
    };
}