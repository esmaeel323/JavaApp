package Semesteroppgave;

import Components.Build;
import Components.Component;
import Components.Validator;
import Filbehandling.GetRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputMethodEvent;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class FerdigPCController
{
    //Product register and total price formating
    ProductRegister register = new ProductRegister();
    private Double total = 0.0;
    DecimalFormat df = new DecimalFormat("0.00");

    //Filter TextFields
    @FXML private Label Pris;
    @FXML private TextField fromPrice, toPrice, txtFilter;

    //Product TableView and columns
    @FXML private TableView<Build> PCTableView;
    @FXML private TableColumn<Build, String> nameColumn = new TableColumn<>("Navn");
    @FXML private TableColumn<Build, Double> priceColumn = new TableColumn<>("Pris");
    @FXML private TableColumn<Build, String> numberColumn = new TableColumn<>("Antall");

    //Cart TableView and columns
    @FXML private TableView<Build> cartTable;
    @FXML private TableColumn<Build, String> cartNameColumn = new TableColumn<>("Navn");
    @FXML private TableColumn<Build, Double> cartPriceColumn = new TableColumn<>("Pris");
    @FXML private TableColumn<Build, String> cartNumberColumn = new TableColumn<>("Antall");

    //For the receipt page
    static ObservableList<Build> kvitData = FXCollections.observableArrayList();

    //Filter CPU
    @FXML private CheckBox frekunder2, frek2to4, frekover4 = new CheckBox();

    //Filter RAM
    @FXML private CheckBox ramunder8, ram8to16, ramover16 = new CheckBox();

    //Filter GPU
    @FXML private CheckBox grafunder4, graf4to8, grafover8 = new CheckBox();

    //Filter storage size
    @FXML private CheckBox lagr100gb, lagr500to999, lagrovertusen = new CheckBox();

    //Filter storage type
    @FXML private CheckBox typeSSD, typeHDD = new CheckBox();

    public void initialize() throws IOException
    {
        GetRegistry gr = new GetRegistry();
        gr.open(register, Paths.get("./Products.jobj"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<Build, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Build, Double>("price"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<Build, String>("number"));

        cartNameColumn.setCellValueFactory(new PropertyValueFactory<Build, String>("name"));
        cartPriceColumn.setCellValueFactory(new PropertyValueFactory<Build, Double>("price"));
        cartNumberColumn.setCellValueFactory(new PropertyValueFactory<Build, String>("number"));

        //Edit number wanted
        PCTableView.setEditable(true);
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        PCTableView.getItems().setAll(register.getBuilds());
    }

    //Filter products
    @FXML
    private void filter()
    {
        //To be added
        ObservableList<Build> filteredBuilds = FXCollections.observableArrayList();
        filteredBuilds.addAll(register.getBuilds());

        //To be removed
        ObservableList<Build> toBeRemoved = FXCollections.observableArrayList();

        Validator v = new Validator();
        String bottomPrice = fromPrice.getText();
        String topPrice = toPrice.getText();
        Double from = 0.0;
        Double to = 99999999.9;

        //Check if price range is set and values are valid
        if(!bottomPrice.isEmpty())
        {
            if(!v.intValue(bottomPrice) && !v.doubleValue(bottomPrice))
            {
                Dialogs.Feilmelding("Ugyldige tegn i fra-prisen");
            }
            else
            {
                from = Double.parseDouble(bottomPrice);
            }
        }

        if(!topPrice.isEmpty())
        {
            if(!v.intValue(topPrice) && !v.doubleValue(topPrice))
            {
                Dialogs.Feilmelding("Ugyldige tegn i til-prisen");
            }
            else
            {
                to = Double.parseDouble(topPrice);
            }
        }

        //Filter based on price
        //Only bottom price is set
        if(topPrice.isEmpty() && !bottomPrice.isEmpty())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getPrice() < from)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Only top price is set
        if(!topPrice.isEmpty() && bottomPrice.isEmpty())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getPrice() > to)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Price range is set
        if(!topPrice.isEmpty() && !bottomPrice.isEmpty())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getPrice() < from || b.getPrice() > to)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Filter based on storage type
        //Only SSD wanted
        if(typeSSD.isSelected() && !typeHDD.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(!b.getH().getType().equals("SSD"))
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Only HDD wanted
        if(typeHDD.isSelected() && !typeSSD.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(!b.getH().getType().equals("HDD"))
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Filter builds based on CPU GHz
        //Only one is selected
        //Less than 2GHz wanted
        if(frekunder2.isSelected() && !frek2to4.isSelected() && !frekover4.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if((b.getP().getFrequency() > 2))
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //More than 4GHz wanted
        if(!frekunder2.isSelected() && !frek2to4.isSelected() && frekover4.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getP().getFrequency() < 4)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Between 2 and 4 GHz wanted
        if(!frekunder2.isSelected() && frek2to4.isSelected() && !frekover4.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getP().getFrequency() > 4 || b.getP().getFrequency() < 2)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Two selected
        //Don't want more than 4 GHz
        if(frekunder2.isSelected() && frek2to4.isSelected() && !frekover4.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getP().getFrequency() > 4)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Don't want less than 2 GHz
        if(!frekunder2.isSelected() && frek2to4.isSelected() && frekover4.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getP().getFrequency() < 2)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Want either less than 2 GHz or greater than 4 GHz
        if(frekunder2.isSelected() && !frek2to4.isSelected() && frekover4.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getP().getFrequency() > 2 && b.getP().getFrequency() < 4)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Filter builds based on memory size
        //Only one is selected
        if(ramunder8.isSelected() && !ram8to16.isSelected() && !ramover16.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getM().getSize() > 8)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!ramunder8.isSelected() && ram8to16.isSelected() && !ramover16.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getM().getSize() < 8 || b.getM().getSize() > 16)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!ramunder8.isSelected() && !ram8to16.isSelected() && ramover16.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getM().getSize() < 16)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Two selected
        if(ramunder8.isSelected() && ram8to16.isSelected() && !ramover16.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getM().getSize() > 16)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!ramunder8.isSelected() && ram8to16.isSelected() && ramover16.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getM().getSize() < 8)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(ramunder8.isSelected() && !ram8to16.isSelected() && ramover16.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getM().getSize() > 8 && b.getM().getSize() < 16)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Filter builds based on GPU size
        //Only one is selected
        if(grafunder4.isSelected() && !graf4to8.isSelected() && !grafover8.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getG().getSize() > 4)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!grafunder4.isSelected() && graf4to8.isSelected() && !grafover8.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getG().getSize() < 4 || b.getG().getSize() > 8)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!grafunder4.isSelected() && !graf4to8.isSelected() && grafover8.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getG().getSize() < 8)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Two selected
        if(grafunder4.isSelected() && graf4to8.isSelected() && !grafover8.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getG().getSize() > 8)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!grafunder4.isSelected() && graf4to8.isSelected() && grafover8.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getG().getSize() < 4)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(grafunder4.isSelected() && !graf4to8.isSelected() && grafover8.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getG().getSize() > 4 && b.getG().getSize() < 8)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Filter builds based on storage size
        //Only one is selected
        if(lagr100gb.isSelected() && !lagr500to999.isSelected() && !lagrovertusen.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getH().getSize() > 100)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!lagr100gb.isSelected() && !lagr500to999.isSelected() && lagrovertusen.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getH().getSize() < 1000)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!lagr100gb.isSelected() && lagr500to999.isSelected() && !lagrovertusen.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getH().getSize() < 500 || b.getH().getSize() > 999)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Two selected
        if(lagr100gb.isSelected() && lagr500to999.isSelected() && !lagrovertusen.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getH().getSize() > 999 || (b.getH().getSize() > 100 && b.getH().getSize() < 500))
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(!lagr100gb.isSelected() && lagr500to999.isSelected() && lagrovertusen.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getH().getSize() < 500)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        if(lagr100gb.isSelected() && !lagr500to999.isSelected() && lagrovertusen.isSelected())
        {
            for(Build b : filteredBuilds)
            {
                if(b.getH().getSize() > 100 && b.getH().getSize() < 1000)
                {
                    toBeRemoved.add(b);
                }
            }
        }

        //Check filterText
        if(!txtFilter.getText().isEmpty())
        {
            for(Build b : filteredBuilds)
            {
                if(!b.getName().toLowerCase().contains(txtFilter.getText()))
                {
                    toBeRemoved.add(b);
                }
            }
        }

        filteredBuilds.removeAll(toBeRemoved);
        PCTableView.setItems(filteredBuilds);
    }

    @FXML
    public void filterText()
    {
        filter();
    }

    @FXML
    public void showReceipt(ActionEvent actionEvent) throws IOException
    {
        if(cartTable.getItems().size() < 1)
        {
            Dialogs.Feilmelding("Handlekurven er tom.\nLegg til en vare for å fortsette.");
        }
        else
        {
            kvitData = cartTable.getItems();
            App.setRoot("BuildReceipt");
        }
    }

    //Add product to cart
    @FXML
    public void addComponentToCart(ActionEvent actionEvent)
    {
        int selectedIndex = PCTableView.getSelectionModel().getSelectedIndex();
        Build b = PCTableView.getSelectionModel().getSelectedItem();

        if (selectedIndex < 0) {
            // Nothing selected.
            Dialogs.Feilmelding("Vennligst velg et produkt å legge til i handlekurven");
            return;
        }

        boolean exists = false;
        try
        {

            for (Build check : cartTable.getItems())
            {
                if (check.equals(b))
                {
                    exists = true;
                }
            }
            if (exists)
            {
                cartTable.refresh();
            }
            else
            {
                cartTable.getItems().add(b);
            }
            totalPris();
        }
        catch (NumberFormatException nfe)
        {
            Dialogs.Feilmelding("Antall må være et heltall");
        }
    }

    //Remove product from cart
    @FXML
    public void deleteComponentFromCart(ActionEvent actionEvent)
    {
        int selectedIndex = cartTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0)
        {
            //Remove part
            cartTable.getItems().remove(selectedIndex);
            // Update total price
            totalPris();

        }
        else
        {
            // Nothing selected.
            Dialogs.Feilmelding("Velg et produkt å fjerne fra handlekurven");
        }
    }

    //Get current total price
    private void totalPris()
    {
        total = 0.0;
        for (Build b : cartTable.getItems())
        {
            total += b.getPrice()*Integer.parseInt(b.getNumber());
        }
        Pris.setText(df.format(total) + " Kr");
    }

    @FXML
    public void editAntall(TableColumn.CellEditEvent editedCell)
    {
        Validator v = new Validator();
        String number = editedCell.getNewValue().toString();
        if(v.intValue(number))
        {
            if(Integer.parseInt(number)>0)
            {
                PCTableView.getSelectionModel().getSelectedItem().setNumber(number);
            }
        }
        else
        {
            Dialogs.Feilmelding("Antall må være et heltall større enn 0");
        }
    }

    //Show details regarding selected build
    @FXML
    public void showBuildInfo(ActionEvent actionEvent)
    {
        if(PCTableView.getSelectionModel().isEmpty())
        {
            Dialogs.Feilmelding("Velg en maskin");
        }
        else
        {
            Dialogs.showInformationDialog(PCTableView.getSelectionModel().getSelectedItem().toString());
        }
    }

    //Go home
    @FXML
    public void tilbake(ActionEvent actionEvent) throws IOException
    {
        App.setRoot("Home");
    }

}
