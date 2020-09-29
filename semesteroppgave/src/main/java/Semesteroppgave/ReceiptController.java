package Semesteroppgave;

import Components.Component;
import Exceptions.ComponentFormatException;
import Filbehandling.LagringTxt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ReceiptController
{


    public Label Date;
    @FXML
    private TableView<Component> receiptable;

    @FXML
    private TextField totalPrice;

    @FXML
    private Button kvittering;

    // Reference to main
    private App mainmethod;
    private Stage receiptStage;

    public void initialize()
    {

    }

    //Set scene
    public void setReceiptStage(Stage receiptStage) {
        this.receiptStage = receiptStage;
    }

    //Set date
    public void setDateAnTime(String dt)
    {
        Date.setText(dt);
    }

    public void setTotalPrice(String tp)
    {
        totalPrice.setText(tp);
    }

    //Retrieve receipt data
    public TableView getReceiptable() {
        return receiptable;
    }

    //Handle receipt data
    public void createReceipTable()
    {
        receiptable.getColumns().clear();
        receiptable.getItems().clear();
        receiptable.refresh();

        TableColumn<Component, String> number = new TableColumn<>("Komponenter");
        number.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Component, String> type = new TableColumn<>("Komponenter");
        type.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Component, String> price = new TableColumn<>("Pris");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        receiptable.getColumns().addAll(number, type, price);

    }

    //Save the receipt to file
    @FXML
    void printReceipt(ActionEvent event)
    {
        ArrayList<Component> list = new ArrayList();
        int csize = receiptable.getItems().size();

        for (int i = 0; i < csize; i++)
        {
            Component c = (Component) receiptable.getItems().get(i);
            list.add(c);
        }

        FileChooser saver = new FileChooser();
        Stage mainStage = new Stage();
        saver.setTitle("Lagre inn i fil");
        FileChooser.ExtensionFilter extFiltertxt = new FileChooser.ExtensionFilter("txt files (*.txt)", "*.txt", "*.txt");
        FileChooser.ExtensionFilter extFilterjobj = new FileChooser.ExtensionFilter("jobj files (*.jobj)", "*.jobj", "*.jobj");
        saver.getExtensionFilters().addAll(extFiltertxt, extFilterjobj);
        File selectedFile = saver.showSaveDialog(mainStage);
        if (selectedFile != null)
        {
            try
            {
                String paths = selectedFile.getAbsolutePath();
                var path = Paths.get(paths);
                Double pris = Double.parseDouble(totalPrice.getText());
                LagringTxt.skriveUtKvittering(list, pris, path);
            }
            catch (ComponentFormatException e)
            {
                Dialogs.Feilmelding(e.getMessage());
            }
            catch (IOException e)
            {
                Dialogs.Feilmelding(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}