package Semesteroppgave;

import Components.Component;
import Components.Validator;
import Filbehandling.GetRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class KomponenterController
{
    //Nodes for shopping
    @FXML
    private TableView<Component> CartTable;
    @FXML
    private TableView ComponentTable;
    @FXML
    private Label totalSum;
    @FXML
    private Button kvittering;

    //Stage for the receipt method
    private Stage stage;

    // Reference to main
    private App mainmethod;

    //Formating total price
    @FXML
    private Double total = 0.0;
    DecimalFormat df = new DecimalFormat("0.00");

    //Component columns
    @FXML private TableColumn<Component, String> nameColumn = new TableColumn<>("Navn");;
    @FXML private TableColumn<Component, Double> priceColumn = new TableColumn<>("Pris");;
    @FXML private TableColumn<Component, Double> freqColumn = new TableColumn<>("Frekvens");
    @FXML private TableColumn<Component, String> typeColumn = new TableColumn<>("Type");
    @FXML private TableColumn<Component, Integer> sizeColumn = new TableColumn<>("Størrelse(GB)");
    @FXML private TableColumn<Component, Integer> portsColumn = new TableColumn<>("USB-Porter");
    @FXML private TableColumn<Component, Integer> bitColumn = new TableColumn<>("Bit");
    @FXML private TableColumn<Component, Integer> voltageColumn = new TableColumn<>("Volt");
    @FXML private TableColumn<Component, Integer> wattColumn = new TableColumn<>("Watt");
    @FXML private TableColumn<Component, Integer> coreColumn = new TableColumn<>("Antall kjerner");
    @FXML private TableColumn<Component, String> numberColumn = new TableColumn<>("Antall");

    // Init main method.
    ProductRegister register = new ProductRegister();

    public void setMainMethod(App main) {
        this.mainmethod = main;
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() throws IOException {
        //Get products
        GetRegistry gr = new GetRegistry();
        gr.open(register, Paths.get("./Products.jobj"));

        //Component columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<Component, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Component, Double>("price"));
        freqColumn.setCellValueFactory(new PropertyValueFactory<Component, Double>("frequency"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Component, String>("type"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("size"));
        coreColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("cores"));
        portsColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("ports"));
        bitColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("bit"));
        voltageColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("voltage"));
        wattColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("watt"));

        //Edit number wanted pr. product
        ComponentTable.setEditable(true);
        numberColumn.setCellFactory(TextFieldTableCell.<Component>forTableColumn());
        numberColumn.setCellValueFactory(new PropertyValueFactory("number"));

        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        numberColumn.setOnEditCommit((TableColumn.CellEditEvent<Component, String> event) -> {
            TablePosition<Component, String> pos = event.getTablePosition();

            String newVal = event.getNewValue();

            int row = pos.getRow();
            Component comp = event.getTableView().getItems().get(row);

            comp.setNumber(newVal);
        });

        createCart();
    }

    //Create shopping cart
    public void createCart()
    {
        TableColumn<Component, String> name = new TableColumn<>("PCKomponent");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Component, Double> price = new TableColumn<>("Price  Kr");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Component, String> number = new TableColumn<>("Antall");
        number.setCellValueFactory(new PropertyValueFactory<>("number"));

        CartTable.getColumns().addAll(name, price, number);
    }

    //Add product to cart
    public void addComponentToCart(ActionEvent actionEvent)
    {
        int selectedIndex = ComponentTable.getSelectionModel().getSelectedIndex();
        Component c = (Component) ComponentTable.getSelectionModel().getSelectedItem();
        if (selectedIndex < 0) {
            // Nothing selected.
            Dialogs.Feilmelding("Vennligst velg et produkt å legge i handlekurven");
            return;
        }
        Validator vald = new Validator();
        boolean valid = vald.intValue(c.getNumber());
        if(valid) {
            CartTable.getItems().add(c);
            totalPris();
        }else{
            Dialogs.Feilmelding("Antall må være et heltall større enn null");
        }
    }

    //Remove product from cart
    public void deleteComponentFromCart(ActionEvent actionEvent) {
        int selectedIndex = CartTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            //Remove part
            CartTable.getItems().remove(selectedIndex);
            // Update total price
            totalPris();

        } else {
            // Nothing selected.
            Dialogs.Feilmelding("Velg en komponent å fjerne fra handlekurven");
        }
    }

    //Show the different categories of components in the register and add appropriate columns
    public void showProcessorlist(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn,nameColumn, priceColumn, freqColumn, coreColumn);
        ComponentTable.getItems().setAll(register.getProcessors());

    }

    public void showGraphicCardList(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn,nameColumn, priceColumn, sizeColumn);
        ComponentTable.getItems().setAll(register.getGraphicCards());

    }

    public void showharddisklist(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn, nameColumn, priceColumn, typeColumn, sizeColumn);
        ComponentTable.getItems().setAll(register.getStorageUnits());

    }

    public void showmotherboardlist(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn,nameColumn, priceColumn, portsColumn);
        ComponentTable.getItems().setAll(register.getMotherboards());

    }

    public void showOSlist(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn,nameColumn, priceColumn, bitColumn);
        ComponentTable.getItems().setAll(register.getAllOS());

    }

    public void showpowerlist(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn,nameColumn, priceColumn, voltageColumn, wattColumn);
        ComponentTable.getItems().setAll(register.getPowerSupplies());

    }

    public void showmemorylist(ActionEvent actionEvent) {
        ComponentTable.getColumns().clear();
        ComponentTable.getColumns().addAll(numberColumn,nameColumn, priceColumn, typeColumn, sizeColumn);
        ComponentTable.getItems().setAll(register.getMemoryChips());

    }

    //Finish shopping
    public void showReceipt(ActionEvent actionEvent) throws IOException {

        if(CartTable.getItems().size()==0){
            Dialogs.Feilmelding("Handlekurven er tom.\nLegg til en vare for å fortsette.");
            return;
        }

        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("receipt.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage recstage = new Stage();
        recstage.setTitle("Bekreftelse");
        recstage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        recstage.setScene(scene);


        ReceiptController reccontroller = loader.getController();
        reccontroller.setReceiptStage(recstage);

        //getting current date and time using Date class
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        reccontroller.setDateAnTime(df.format(calobj.getTime()));
        String stotal = Double.toString(total);
        reccontroller.setTotalPrice(stotal);

        reccontroller.createReceipTable();
        int csize = CartTable.getItems().size();


        for (int i = 0; i < csize; i++) {
            Component c = CartTable.getItems().get(i);
            reccontroller.getReceiptable().getItems().add(c);
        }
        recstage.showAndWait();
    }

    //Show current total
    private void totalPris()
    {
        total = 0.0;
        for (Component c : CartTable.getItems())
        {
            total += c.getPrice()*Integer.parseInt(c.getNumber());
        }
        totalSum.setText(df.format(total) + " Kr");
    }

    //ET go home
    @FXML
    void tilbake() throws IOException
    {
        App.setRoot("Home");
    }
}