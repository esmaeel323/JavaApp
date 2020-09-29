package Semesteroppgave;

import Components.*;
import Filbehandling.AdminThread;
import Filbehandling.SaveAdmins;
import Filbehandling.SaveRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import java.io.*;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class AdminController
{
    //Combo boxes for creating and filtering
    @FXML private ComboBox<String> comTypes = new ComboBox<>();
    @FXML private ComboBox<String> filterCom = new ComboBox<>();

    //Combo boxes for build creation
    @FXML private ComboBox<String> buildCPU, buildRAM, buildStorage, buildGPU, buildPower, buildOS, buildMother = new ComboBox<>();

    //Gridpane for registering new components
    @FXML private GridPane productData = new GridPane();

    //Tableview for Components, Builds and Admins
    @FXML private TableView<Component> productTable;
    @FXML private TableView<Build> buildTable;
    @FXML private TableView<Admin> adminTable;

    //Admin columns
    @FXML private TableColumn<Admin, String> usernameColumn;
    @FXML private TableColumn<Admin, String> passwordColumn;

    //Component columns
    @FXML private TableColumn<Component, String> nameColumn;
    @FXML private TableColumn<Component, Double> priceColumn;

    //Build columns
    @FXML private TableColumn<Build, String> buildNameColumn;
    @FXML private TableColumn<Build, Double> buildPriceColumn;

    //Custom Component columns
    @FXML private TableColumn<Component, Double> freqColumn = new TableColumn<>("Frekvens");
    @FXML private TableColumn<Component, String> memoryTypeColumn = new TableColumn<>("Type");
    @FXML private TableColumn<Component, String> storageTypeColumn = new TableColumn<>("Type");
    @FXML private TableColumn<Component, Integer> memorySizeColumn = new TableColumn<>("Størrelse(GB)");
    @FXML private TableColumn<Component, Integer> storageSizeColumn = new TableColumn<>("Størrelse(GB)");
    @FXML private TableColumn<Component, Integer> graphicSizeColumn = new TableColumn<>("Størrelse(GB)");
    @FXML private TableColumn<Component, Integer> portsColumn = new TableColumn<>("USB-porter");
    @FXML private TableColumn<Component, Integer> bitColumn = new TableColumn<>("Bit");
    @FXML private TableColumn<Component, Integer> voltageColumn = new TableColumn<>("Volt");
    @FXML private TableColumn<Component, Integer> wattColumn = new TableColumn<>("Watt");
    @FXML private TableColumn<Component, Integer> coreColumn = new TableColumn<>("Kjerner");

    //User textfields and label for build cost
    @FXML private TextField txtUsername, txtPassword, sok, txtBuildName, txtBuildPrice;
    @FXML private Label buildCost;

    //Storage unit for all products and admins
    private ProductRegister productRegister = new ProductRegister();
    private AdminRegister adminRegister = new AdminRegister();

    //TableView converters for Double and Integer values, and validator
    private Validator.IntegerConverter stringToInt = new Validator.IntegerConverter();
    private Validator.DoubleConverter stringToDouble = new Validator.DoubleConverter();
    private Validator v = new Validator();

    //Thread to upload data
    private AdminThread task;

    //Thread gui
    @FXML TabPane AdminGUI;

    @FXML
    public void initialize()
    {
        //Retrieve and display stored products
        task = new AdminThread(productRegister, adminRegister);
        task.setOnSucceeded(this:: threadDone);
        task.setOnFailed(this::threadFailed);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        AdminGUI.setDisable(true);
        AdminGUI.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        //Combo boxes for creating and filtering
        ObservableList<String> create = FXCollections.observableArrayList("Prosessor", "Minne", "Lagring", "Grafikk", "Strømforsyning", "Operativsystem", "Hovedkort");
        ObservableList<String> options = FXCollections.observableArrayList("Alle", "Prosessor", "Minne", "Lagring", "Grafikk", "Strømforsyning", "Operativsystem", "Hovedkort");

        comTypes.getItems().addAll(create);
        filterCom.getItems().addAll(options);
        filterCom.setValue("Alle");

        //Component columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<Component, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Component, Double>("price"));

        //Build columns
        buildNameColumn.setCellValueFactory(new PropertyValueFactory<Build, String>("name"));
        buildPriceColumn.setCellValueFactory(new PropertyValueFactory<Build, Double>("price"));

        //Admin columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Admin, String>("password"));

        //Custom columns for components
        freqColumn.setCellValueFactory(new PropertyValueFactory<Component, Double>("frequency"));
        memoryTypeColumn.setCellValueFactory(new PropertyValueFactory<Component, String>("type"));
        storageTypeColumn.setCellValueFactory(new PropertyValueFactory<Component, String>("type"));
        memorySizeColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("size"));
        storageSizeColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("size"));
        graphicSizeColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("size"));
        coreColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("cores"));
        portsColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("ports"));
        bitColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("bit"));
        voltageColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("voltage"));
        wattColumn.setCellValueFactory(new PropertyValueFactory<Component, Integer>("watt"));

        //Makes it possible to edit a component in the list.
        //Common traits
        productTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToDouble));

        //Make it possible to edit Admins
        adminTable.setEditable(true);
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        //Individual traits
        freqColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToDouble));
        memorySizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));
        storageSizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));
        graphicSizeColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));
        coreColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));
        portsColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));
        bitColumn.setCellFactory(ComboBoxTableCell.forTableColumn(32, 64));
        memoryTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn("DDR3", "DDR4"));
        storageTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn("HDD", "SSD"));
        bitColumn.setCellFactory(ComboBoxTableCell.forTableColumn(32, 64));
        voltageColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));
        wattColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringToInt));

        //Commit changes on edit
        freqColumn.setOnEditCommit(event ->
        {
            Processor p = (Processor) event.getRowValue();
            if(v.doubleValue(event.getNewValue().toString()))
            {
                p.setFrequency(event.getNewValue());
            }
            else
            {
                p.setFrequency(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        memorySizeColumn.setOnEditCommit(event ->
        {
            Memory c = (Memory) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                c.setSize(event.getNewValue());
            }
            else
            {
                c.setSize(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        storageSizeColumn.setOnEditCommit(event ->
        {
            Memory c = (Memory) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                c.setSize(event.getNewValue());
            }
            else
            {
                c.setSize(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        graphicSizeColumn.setOnEditCommit(event ->
        {
            GraphicCard c = (GraphicCard) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                c.setSize(event.getNewValue());
            }
            else
            {
                c.setSize(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        coreColumn.setOnEditCommit(event ->
        {
            Processor p = (Processor) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                p.setCoreCount(event.getNewValue());
            }
            else
            {
                p.setCoreCount(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        portsColumn.setOnEditCommit(event ->
        {
            Motherboard p = (Motherboard) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                p.setPorts(event.getNewValue());
            }
            else
            {
                p.setPorts(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        bitColumn.setOnEditCommit(event ->
        {
            OperatingSystem p = (OperatingSystem) event.getRowValue();
            p.setBit(event.getNewValue());
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        memoryTypeColumn.setOnEditCommit(event ->
        {
            Memory m = (Memory) event.getRowValue();
            m.setType(event.getNewValue());
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        storageTypeColumn.setOnEditCommit(event ->
        {
            HardDisk m = (HardDisk) event.getRowValue();
            m.setType(event.getNewValue());
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        voltageColumn.setOnEditCommit(event ->
        {
            Power p = (Power) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                p.setVoltage(event.getNewValue());
            }
            else
            {
                p.setVoltage(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });

        wattColumn.setOnEditCommit(event ->
        {
            Power p = (Power) event.getRowValue();
            if(v.intValue(event.getNewValue().toString()))
            {
                p.setWatt(event.getNewValue());
            }
            else
            {
                p.setWatt(event.getOldValue());
            }
            productTable.refresh();
            saveProducts();
            updateBuildCombo();
        });
    }

    //Thread handling
    private void threadFailed(Event event)
    {
        AdminGUI.setDisable(false);
        updateComponentTable();
        updateAdminTable();
        updateBuildCombo();
        updateBuildTable();
    }

    private void threadDone(Event event)
    {
        AdminGUI.setDisable(false);
        updateComponentTable();
        updateAdminTable();
        updateBuildCombo();
        updateBuildTable();
    }

    //Adding a product to the list
    @FXML
    void addProduct(ActionEvent event)
    {
        String kategori = comTypes.getSelectionModel().getSelectedItem();
        Component c;
        if(kategori == null)
        {
            Dialogs.Feilmelding("Velg en kategori å legge til");
        }
        else
        {
            c = Dialogs.createNewComponent(kategori);
            if (c == null)
            {
                Dialogs.Feilmelding("Produkt ble ikke registrert");
            }
            else
            {
                switch (kategori)
                {
                    case ("Prosessor"):
                    {
                        productRegister.addProcessor((Processor) c);
                        break;
                    }
                    case ("Minne"):
                    {
                        productRegister.addMemory((Memory) c);
                        break;
                    }
                    case ("Lagring"):
                    {
                        productRegister.addStorage((HardDisk) c);
                        break;
                    }
                    case ("Grafikk"):
                    {
                        productRegister.addGraphics((GraphicCard) c);
                        break;
                    }
                    case ("Strømforsyning"):
                    {
                        productRegister.addPower((Power) c);
                        break;
                    }
                    case ("Operativsystem"):
                    {
                        productRegister.addOS((OperatingSystem) c);
                        break;
                    }
                    case ("Hovedkort"):
                    {
                        productRegister.addMotherboard((Motherboard) c);
                        break;
                    }
                }
                Dialogs.addedNewProductDialog(c.getName() + "er lagt til");
                productRegister.addProduct(c);
                saveProducts();
                updateComponentTable();
                updateBuildCombo();
                filter();
            }
        }
    }

    //Remove products from the list
    @FXML
    public void removeProduct(ActionEvent event)
    {
        Component c = productTable.getSelectionModel().getSelectedItem();
        if(c != null)
        {
            String type = c.getClass().getSimpleName();
            Dialogs.showSuccessDialog(c.getName() + " er slettet");
            productRegister.removeProduct(c, type);
        }
        else
        {
            Dialogs.Feilmelding("Velg et produkt å slette");
        }

        saveProducts();
        updateComponentTable();
        updateBuildCombo();
    }

    //Add new admin
    @FXML
    public void addUser(ActionEvent actionEvent)
    {
        String name = txtUsername.getText();
        String pass = txtPassword.getText();

        if(name.isEmpty())
        {
            Dialogs.Feilmelding("Brukernavn kan ikke være tomt!");
        }
        else if(pass.isEmpty())
        {
            Dialogs.Feilmelding("Passord kan ikke være tomt!");
        }
        else if(pass.length() < 5)
        {
            Dialogs.Feilmelding("Passord må ha minst 5 tegn!");
        }
        else
        {
            Dialogs.showSuccessDialog(name + " er lagt til");
            adminRegister.addUser(new Admin(name, pass));
            saveAdmins();
            updateAdminTable();
            txtUsername.clear();
            txtPassword.clear();
        }
    }

    //Remove admin
    @FXML
    public void removeUser(ActionEvent actionEvent)
    {
        Admin a = adminTable.getSelectionModel().getSelectedItem();
        if(a == null)
        {
            Dialogs.Feilmelding("Velg en bruker å slette");
        }
        else
        {
            Dialogs.showSuccessDialog(a.getUsername() + " har blitt fjernet");
            adminRegister.removeUser(a);
            saveAdmins();
            updateAdminTable();
        }
    }

    //Add new build
    @FXML
    public void addBuild(ActionEvent actionEvent)
    {
        String name = txtBuildName.getText();
        String price = txtBuildPrice.getText();
        Validator v = new Validator();

        if(name.isEmpty())
        {
            Dialogs.Feilmelding("Navn kan ikke være tomt");
        }
        else if(!v.doubleValue(price))
        {
            Dialogs.Feilmelding("Ugyldige tegn i pris");
        }
        else if(buildCPU.getSelectionModel().isEmpty() || buildRAM.getSelectionModel().isEmpty() || buildGPU.getSelectionModel().isEmpty() || buildOS.getSelectionModel().isEmpty() || buildStorage.getSelectionModel().isEmpty() || buildMother.getSelectionModel().isEmpty() || buildPower.getSelectionModel().isEmpty())
        {
            Dialogs.Feilmelding("Må velge en av hver komponent til maskinen");
        }
        else
        {
            Processor p = productRegister.getProcessors().get(buildCPU.getSelectionModel().getSelectedIndex());
            Memory m = productRegister.getMemoryChips().get(buildRAM.getSelectionModel().getSelectedIndex());
            GraphicCard g = productRegister.getGraphicCards().get(buildGPU.getSelectionModel().getSelectedIndex());
            HardDisk h = productRegister.getStorageUnits().get(buildStorage.getSelectionModel().getSelectedIndex());
            OperatingSystem o = productRegister.getAllOS().get(buildOS.getSelectionModel().getSelectedIndex());
            Motherboard mo = productRegister.getMotherboards().get(buildMother.getSelectionModel().getSelectedIndex());
            Power po = productRegister.getPowerSupplies().get(buildPower.getSelectionModel().getSelectedIndex());

            Build b = new Build(name, p, g, h, m, mo, o, po, Double.parseDouble(price));
            productRegister.addBuild(b);
            Dialogs.addedNewProductDialog(b.getName() + " er lagt til");
            saveProducts();
            updateBuildTable();
        }
    }

    //Calculate cost new build
    @FXML
    public void cost(ActionEvent actionEvent)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        Double total = 0.00;
        if(buildCPU.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getProcessors().get(buildCPU.getSelectionModel().getSelectedIndex()).getPrice();
        }

        if(buildGPU.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getGraphicCards().get(buildGPU.getSelectionModel().getSelectedIndex()).getPrice();
        }

        if(buildRAM.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getMemoryChips().get(buildRAM.getSelectionModel().getSelectedIndex()).getPrice();
        }

        if(buildStorage.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getStorageUnits().get(buildStorage.getSelectionModel().getSelectedIndex()).getPrice();
        }

        if(buildMother.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getMotherboards().get(buildMother.getSelectionModel().getSelectedIndex()).getPrice();
        }

        if(buildOS.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getAllOS().get(buildOS.getSelectionModel().getSelectedIndex()).getPrice();
        }

        if(buildPower.getSelectionModel().getSelectedIndex() > -1)
        {
            total += productRegister.getPowerSupplies().get(buildPower.getSelectionModel().getSelectedIndex()).getPrice();
        }

        buildCost.setText(df.format(total));
    }

    //Remove build
    @FXML
    public void removeBuild(ActionEvent actionEvent)
    {
        Build b = buildTable.getSelectionModel().getSelectedItem();
        if(b == null)
        {
            Dialogs.Feilmelding("Velg en maskin å slette");
        }
        else
        {
            Dialogs.showSuccessDialog(b.getName() + " har blitt fjernet");
            productRegister.removeBuild(b);
            saveProducts();
            updateBuildTable();
        }
    }

    //Show details for a build
    @FXML
    public void showBuildInfo(ActionEvent actionEvent)
    {
        if(buildTable.getSelectionModel().isEmpty())
        {
            Dialogs.Feilmelding("Velg en maskin");
        }
        else
        {
            Dialogs.showInformationDialog(buildTable.getSelectionModel().getSelectedItem().toString());
        }
    }

    //Edit common component information
    @FXML
    public void editProductName(TableColumn.CellEditEvent editedCell)
    {
        String newName = editedCell.getNewValue().toString();
        if(newName.isEmpty())
        {
            productTable.getSelectionModel().getSelectedItem().setName(editedCell.getOldValue().toString());
            Dialogs.Feilmelding("Navn kan ikke være tomt");
            productTable.refresh();
        }
        else
        {
            productTable.getSelectionModel().getSelectedItem().setName(newName);
            productTable.refresh();
        }
        saveProducts();
        updateBuildCombo();
    }

    @FXML
    public void editProductPrice(TableColumn.CellEditEvent<Component, Double> editedCell)
    {
        try
        {
            if(stringToDouble.isValid())
            {
                editedCell.getRowValue().setPrice(editedCell.getNewValue());
                productTable.refresh();
            }
        }
        catch (NumberFormatException nfe)
        {
            Dialogs.Feilmelding("Ugyldige tegn i pris");
            productTable.refresh();
        }
        saveProducts();
        updateBuildCombo();
    }

    //Edit admins username
    public void editUsername(TableColumn.CellEditEvent<Admin, String> adminStringCellEditEvent)
    {
        if(adminStringCellEditEvent.getNewValue().isEmpty())
        {
            Dialogs.Feilmelding("Brukernavn kan ikke være tomt");
            adminStringCellEditEvent.getRowValue().setUsername(adminStringCellEditEvent.getOldValue());
        }
        else
        {
            adminStringCellEditEvent.getRowValue().setUsername(adminStringCellEditEvent.getNewValue());
        }
        adminTable.refresh();
        saveAdmins();
    }

    //Edit admins password
    public void editPassword(TableColumn.CellEditEvent<Admin, String> adminStringCellEditEvent)
    {
        if(adminStringCellEditEvent.getNewValue().isEmpty())
        {
            Dialogs.Feilmelding("Passord kan ikke være tomt");
            adminStringCellEditEvent.getRowValue().setPassword(adminStringCellEditEvent.getOldValue());
        }
        else if(adminStringCellEditEvent.getNewValue().length() < 5)
        {
            Dialogs.Feilmelding("Passord må ha minst 5 tegn");
            adminStringCellEditEvent.getRowValue().setPassword(adminStringCellEditEvent.getOldValue());
        }
        else
        {
            adminStringCellEditEvent.getRowValue().setPassword(adminStringCellEditEvent.getNewValue());
        }
        adminTable.refresh();
        saveAdmins();
    }

    //Filter methods
    @FXML
    private void filterTxt()
    {
        filter();
    }

    @FXML
    private void filterProducts()
    {
        filter();
    }

    private void filter()
    {
        //Retrieve filter data
        String krav = sok.getText();
        String filter = filterCom.getSelectionModel().getSelectedItem();

        //Prepare table for component categories
        productTable.getColumns().clear();
        productTable.getColumns().addAll(nameColumn, priceColumn);

        //Check category filter data
        if(!filter.isEmpty())
        {
            switch (filter)
            {
                case ("Prosessor"):
                {
                    productTable.getColumns().addAll(freqColumn, coreColumn);

                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getProcessors());
                        for(Processor p : productRegister.getProcessors())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getProcessors());
                    }
                    break;
                }
                case ("Minne"):
                {
                    productTable.getColumns().addAll(memoryTypeColumn, memorySizeColumn);
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getMemoryChips());
                        for(Memory p : productRegister.getMemoryChips())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getMemoryChips());
                    }
                    break;

                }
                case ("Lagring"):
                {
                    productTable.getColumns().addAll(storageTypeColumn, storageSizeColumn);
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getStorageUnits());
                        for(HardDisk p : productRegister.getStorageUnits())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getStorageUnits());
                    }
                    break;
                }
                case ("Grafikk"):
                {
                    productTable.getColumns().addAll(graphicSizeColumn);
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getGraphicCards());
                        for(GraphicCard p : productRegister.getGraphicCards())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getGraphicCards());
                    }
                    break;
                }
                case ("Strømforsyning"):
                {
                    productTable.getColumns().addAll(voltageColumn, wattColumn);
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getPowerSupplies());
                        for(Power p : productRegister.getPowerSupplies())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getPowerSupplies());
                    }
                    break;
                }
                case ("Operativsystem"):
                {
                    productTable.getColumns().addAll(bitColumn);
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getAllOS());
                        for(OperatingSystem p : productRegister.getAllOS())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getAllOS());
                    }
                    break;
                }
                case ("Hovedkort"):
                {
                    productTable.getColumns().addAll(portsColumn);
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getMotherboards());
                        for(Motherboard p : productRegister.getMotherboards())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getMotherboards());
                    }
                    break;
                }
                case ("Alle"):
                {
                    //Check text filter data
                    if(!krav.isEmpty())
                    {
                        productTable.getItems().setAll(productRegister.getRegister());
                        for(Component p : productRegister.getRegister())
                        {
                            //Remove unmatched
                            if(!p.toString().toLowerCase().contains(krav))
                            {
                                productTable.getItems().remove(p);
                            }
                        }
                    }
                    else
                    {
                        productTable.getItems().setAll(productRegister.getRegister());
                    }
                    break;
                }
            }
        }
    }

    //Go back to home
    @FXML
    void tilbake(ActionEvent event) throws IOException
    {
        saveProducts();
        saveAdmins();
        App.setRoot("Home");
    }

    //Refresh combo boxes for creating builds
    private void updateBuildCombo()
    {
        buildCPU.getItems().clear();
        buildOS.getItems().clear();
        buildPower.getItems().clear();
        buildMother.getItems().clear();
        buildStorage.getItems().clear();
        buildGPU.getItems().clear();
        buildRAM.getItems().clear();

        for(Processor p:productRegister.getProcessors())
        {
            buildCPU.getItems().add(p.getName() + " - " + p.getPrice() + "kr - " + p.getFrequency() + " GHz");
        }

        for(Memory m:productRegister.getMemoryChips())
        {
            buildRAM.getItems().add(m.getName() + " - " + m.getPrice() + "kr - " + m.getSize() + " GB");
        }

        for(GraphicCard g:productRegister.getGraphicCards())
        {
            buildGPU.getItems().add(g.getName() + " - " + g.getPrice() + "kr - " + g.getSize() + " GB");
        }

        for(HardDisk h:productRegister.getStorageUnits())
        {
            buildStorage.getItems().add(h.getName() + " - " + h.getPrice() + "kr - " + h.getType() + " - " + h.getSize() + " GB");
        }

        for(Motherboard m:productRegister.getMotherboards())
        {
            buildMother.getItems().add(m.getName() + " - " + m.getPrice() + "kr - " + m.getPorts() + "USB-port(s)");
        }

        for(Power po:productRegister.getPowerSupplies())
        {
            buildPower.getItems().add(po.getName() + " - " + po.getPrice() + "kr - " + po.getWatt() + "W - " + po.getVoltage() + "V");
        }

        for(OperatingSystem o:productRegister.getAllOS())
        {
            buildOS.getItems().add(o.getName() + " - " + o.getPrice() + "kr - " + o.getBit() + " bit");
        }
    }

    //Refresh the component table
    private void updateComponentTable()
    {
        productTable.getColumns().clear();
        productTable.getColumns().addAll(nameColumn, priceColumn);
        productTable.getItems().setAll(productRegister.getRegister());
    }

    //Referesh the build table
    private void updateBuildTable()
    {
        buildTable.getItems().setAll(productRegister.getBuilds());
    }

    //Refresh the admin table
    void updateAdminTable()
    {
        adminTable.getItems().setAll(adminRegister.getUsers());
    }

    //Save registries to file
    private void saveProducts()
    {
        try
        {
            SaveRegistry sr = new SaveRegistry();
            sr.save(productRegister, Paths.get("./Products.jobj"));
        }
        catch (FileNotFoundException e)
        {
            Dialogs.Feilmelding("Product fil ikke funnet, kontakt System administrator");
        }
        catch (IOException e)
        {
            Dialogs.Feilmelding(e.getMessage());
        }
    }

    private void saveAdmins()
    {
        try
        {
            SaveAdmins sa = new SaveAdmins();
            sa.save(adminRegister, Paths.get("./Admins.jobj"));
        }
        catch (FileNotFoundException e)
        {
            Dialogs.Feilmelding("Bruker-fil ikke funnet, kontakt System administrator");
        }
        catch (IOException e)
        {
            Dialogs.Feilmelding(e.getMessage());
        }
    }
}
