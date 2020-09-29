package Semesteroppgave;

import Components.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

//Dialogs to inform user about events

public class Dialogs {

    //Error messages
    public static void Feilmelding(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Feilmelding!");
        alert.setHeaderText("Feilmelding");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //Added new product
    public static void addedNewProductDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Legg til produkt");
        alert.setHeaderText("Nytt produkt er lagt til!");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //General success dialog
    public static void showSuccessDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //Show build info
    public static void showInformationDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasjon");
        alert.setHeaderText("Mer informasjon om produktet:");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    //Create a new component
    public static Component createNewComponent(String kategori)
    {
        Validator v = new Validator();
        Dialog<Component> dialog = new Dialog<>();
        dialog.setTitle("Legg til produkt");
        dialog.setHeaderText("Fyll inn informasjon");
        dialog.setResizable(true);

        GridPane gui = new GridPane();
        Label name = new Label("Navn: ");
        Label price = new Label("Pris: ");
        TextField nameValue = new TextField();
        TextField priceValue = new TextField();

        nameValue.setId("name");
        priceValue.setId("price");

        gui.add(name, 1, 1);
        gui.add(nameValue, 2, 1);
        gui.add(price, 1, 2);
        gui.add(priceValue, 2, 2);

        switch (kategori)
        {
            case("Prosessor"):
            {
                Label freq = new Label("Frekvens(GHz): ");
                Label core = new Label("Kjerner: ");
                TextField freqValue = new TextField();
                TextField coreValue = new TextField();

                freqValue.setId("freq");
                coreValue.setId("core");

                gui.add(freq, 1, 3);
                gui.add(freqValue, 2, 3);
                gui.add(core, 1, 4);
                gui.add(coreValue, 2, 4);
                break;
            }
            case("Minne"):
            {
                Label type = new Label("Type: ");
                Label size = new Label("Størrelse(GB): ");
                ComboBox<String> typeValue = new ComboBox<>();
                typeValue.getItems().addAll(FXCollections.observableArrayList("DDR3", "DDR4"));
                TextField sizeValue = new TextField();

                typeValue.setId("type");
                sizeValue.setId("size");

                gui.add(type, 1, 3);
                gui.add(typeValue, 2, 3);
                gui.add(size, 1, 4);
                gui.add(sizeValue, 2, 4);
                break;
            }
            case("Lagring"):
            {
                Label type = new Label("Type: ");
                Label size = new Label("Størrelse(GB): ");
                ComboBox<String> typeValue = new ComboBox<>();
                typeValue.getItems().addAll(FXCollections.observableArrayList("SSD", "HDD"));
                TextField sizeValue = new TextField();

                typeValue.setId("type");
                sizeValue.setId("size");

                gui.add(type, 1, 3);
                gui.add(typeValue, 2, 3);
                gui.add(size, 1, 4);
                gui.add(sizeValue, 2, 4);
                break;
            }
            case("Grafikk"):
            {
                Label size = new Label("Størrelse(GB): ");
                TextField sizeValue = new TextField();

                sizeValue.setId("size");

                gui.add(size, 1, 3);
                gui.add(sizeValue, 2, 3);
                break;
            }
            case("Strømforsyning"):
            {
                Label voltage = new Label("Volt: ");
                Label watt = new Label("Watt: ");
                TextField voltageValue = new TextField();
                TextField wattValue = new TextField();

                voltageValue.setId("voltage");
                wattValue.setId("watt");

                gui.add(voltage, 1, 3);
                gui.add(voltageValue, 2, 3);
                gui.add(watt, 1, 4);
                gui.add(wattValue, 2, 4);
                break;
            }
            case("Operativsystem"):
            {
                Label type = new Label("Type: ");
                ComboBox<String> typeValue = new ComboBox<>();
                typeValue.getItems().addAll(FXCollections.observableArrayList("32", "64"));

                typeValue.setId("type");

                gui.add(type, 1, 3);
                gui.add(typeValue, 2, 3);
                break;
            }
            case("Hovedkort"):
            {
                Label ports = new Label("Antall porter: ");
                TextField portsValue = new TextField();

                portsValue.setId("ports");

                gui.add(ports, 1, 3);
                gui.add(portsValue, 2, 3);
                break;
            }
        }

        ButtonType ok = new ButtonType("Legg til", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().setContent(gui);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        dialog.getDialogPane().lookupButton(ok).addEventFilter(ActionEvent.ACTION, event ->
        {
            if(!validateInput(gui, kategori))
            {
                event.consume();
            }
        });

        dialog.setResultConverter(button ->
        {
            if(button == ok)
            {
                String name1 = nameValue.getText();
                Double price1 = Double.parseDouble(priceValue.getText());

                switch(kategori)
                {

                    case("Prosessor"):
                    {
                        Double freq = Double.parseDouble(getString((TextField) gui.lookup("#freq")));
                        int cores = Integer.parseInt(getString((TextField) gui.lookup("#core")));

                        return new Processor(name1, price1, freq, cores);
                    }
                    case("Minne"):
                    {
                        String type = getType((ComboBox) gui.lookup("#type"));
                        int size = Integer.parseInt(getString((TextField) gui.lookup("#size")));

                        return new Memory(name1, price1, type, size);
                    }
                    case("Lagring"):
                    {
                        String type = getType((ComboBox) gui.lookup("#type"));
                        int size = Integer.parseInt(getString((TextField) gui.lookup("#size")));

                        return new HardDisk(name1, price1, type, size);
                    }
                    case("Grafikk"):
                    {
                        int size = Integer.parseInt(getString((TextField) gui.lookup("#size")));

                        return new GraphicCard(name1, price1, size);
                    }
                    case("Strømforsyning"):
                    {
                        int voltage = Integer.parseInt(getString((TextField) gui.lookup("#voltage")));
                        int watt = Integer.parseInt(getString((TextField) gui.lookup("#watt")));

                        return new Power(name1, price1, voltage, watt);
                    }
                    case("Operativsystem"):
                    {
                        int type  = Integer.parseInt(getType((ComboBox) gui.lookup("#type")));

                        return new OperatingSystem(name1, price1, type);
                    }
                    case("Hovedkort"):
                    {
                        int ports = Integer.parseInt(getString((TextField) gui.lookup("#ports")));

                        return new Motherboard(name1, price1, ports);
                    }
                }
            }
            return null;
        });

        Optional<Component> result = dialog.showAndWait();
        return result.orElse(null);
    }

    //Validate input for new component, to prevent Exceptions
    private static boolean validateInput(GridPane dataHolder, String kategori)
    {
        Validator v = new Validator();
        String name = getString((TextField) dataHolder.lookup("#name"));
        String price = getString((TextField) dataHolder.lookup("#price"));

        if(name.isEmpty())
        {
            Feilmelding("Navn kan ikke være tomt");
            return false;
        }
        else if(!v.doubleValue(price))
        {
            Feilmelding("Ugyldige tegn i pris");
            return false;
        }

        switch(kategori)
        {
            case("Prosessor"):
            {
                String freq = getString((TextField) dataHolder.lookup("#freq"));
                String core = getString((TextField) dataHolder.lookup("#core"));

                if(!v.doubleValue(freq))
                {
                    Feilmelding("Ugyldige tegn i frekvens");
                    return false;
                }
                else if(!v.intValue(core))
                {
                    Feilmelding("Ugyldige tegn i antall kjerner");
                    return false;
                }
                else
                {
                    return true;
                }
            }
            case("Minne"):
            case("Lagring"):
            {
                String type = getType((ComboBox) dataHolder.lookup("#type"));
                String size = getString((TextField) dataHolder.lookup("#size"));

                if(type.isEmpty())
                {
                    Feilmelding("Velg en type");
                    return false;
                }
                else if(!v.intValue(size))
                {
                    Feilmelding("Ugyldige tegn i størrelse");
                    return false;
                }
                else
                {
                    return true;
                }
            }
            case("Grafikk"):
            {
                String size = getString((TextField) dataHolder.lookup("#size"));
                if(!v.intValue(size))
                {
                    Feilmelding("Ugyldige tegn i størrelse");
                    return false;
                }
                else
                {
                    return true;
                }
            }
            case("Strømforsyning"):
            {
                String voltage = getString((TextField) dataHolder.lookup("#voltage"));
                String watt = getString((TextField) dataHolder.lookup("#watt"));

                if(!v.intValue(voltage))
                {
                    Feilmelding("Ugyldige tegn i volt");
                }
                else if(!v.intValue(watt))
                {
                    Feilmelding("Ugyldige tegn i watt");
                }
                else
                {
                    return true;
                }
            }
            case("Operativsystem"):
            {
                String type = getType((ComboBox) dataHolder.lookup("#type"));
                if(type.isEmpty())
                {
                    Feilmelding("Velg en type");
                    return false;
                }
                else
                {
                    return true;
                }
            }
            case("Hovedkort"):
            {
                String ports = getString((TextField) dataHolder.lookup("#ports"));
                if(!v.intValue(ports))
                {
                    Feilmelding("Ugyldige tegn i antall porter");
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }
        return false;
    }

    //Retrieve input from Nodes
    private static String getString(TextField field)
    {
        return field.getText();
    }

    private static String getType(ComboBox<String> box)
    {
        return box.getValue();
    }
}