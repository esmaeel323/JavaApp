package Semesteroppgave;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import Components.*;
import Filbehandling.GetRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class PcBuildController {

    //Nodes for selecting the different parts of the custom build
    @FXML
    private ComboBox Processorer, Sjermkorter, Minne, Hardisk, Hovedkort, Strom, OS;
    @FXML
    Label Pris;

    //Lists for cart, receipt and current total
    protected static ObservableList<String> kjoplist = FXCollections.observableArrayList();
    protected static ObservableList<Component> kvitData = FXCollections.observableArrayList();
    private static ArrayList<Double> Totalpris = new ArrayList<>();

    //Formating for the current total
    private Double total = 0.00;
    DecimalFormat df = new DecimalFormat("0.00");

    //Get all products
    ProductRegister register = new ProductRegister();
    @FXML

    public void initialize() throws IOException {
        GetRegistry gr = new GetRegistry();
        gr.open(register, Paths.get("./Products.jobj"));

        for(Processor p:register.getProcessors())
        {
            Processorer.getItems().add(String.format("%-10s  %-10s  %-10s  %-10s", p.getName(), p.getFrequency()+" GHz", p.getCores()+" Cores", p.getPrice()+"kr"));
        }

        for (GraphicCard g:register.getGraphicCards())
        {
            Sjermkorter.getItems().add(String.format("%-10s   %-10s  %s", g.getName(), g.getSize()+" GHz", g.getPrice()+"kr"));
        }

        for (Memory m:register.getMemoryChips())
        {
            Minne.getItems().add(String.format("%-10s  %-10s  %-10s  %s", m.getName(), m.getSize()+" GB", m.getType(), m.getPrice()+"kr"));
        }

        for (HardDisk h:register.getStorageUnits()){
            Hardisk.getItems().add(String.format("%-10s  %-10s  %-10s  %s", h.getName(), h.getSize()+" GB", h.getType(), h.getPrice()+ "kr"));

        }

        for (Motherboard n:register.getMotherboards()){
            Hovedkort.getItems().add(String.format("%-10s  %-10s   %s", n.getName(), n.getPorts()+" USB", n.getPrice()+ "kr"));

        }

        for (Power p:register.getPowerSupplies()){
            Strom.getItems().add(String.format("%-10s  %-10s  %-10s  %s", p.getName(), p.getVoltage()+" watt", p.getWatt()+" V", p.getPrice()+ "kr"));

        }

        for (OperatingSystem o:register.getAllOS()){
            OS.getItems().add(String.format("%-10s  %-10s  %s", o.getName(), o.getBit()+" Bit", o.getPrice()+ "kr"));

        }

        Pris.setText(df.format(total) + "  Kr");

        Totalpris.add(0.0);
        Totalpris.add(0.0);
        Totalpris.add(0.0);
        Totalpris.add(0.0);
        Totalpris.add(0.0);
        Totalpris.add(0.0);
        Totalpris.add(0.0);

        kvitData.add(new Component("Dummy", 0.0));
        kvitData.add(new Component("Dummy", 0.0));
        kvitData.add(new Component("Dummy", 0.0));
        kvitData.add(new Component("Dummy", 0.0));
        kvitData.add(new Component("Dummy", 0.0));
        kvitData.add(new Component("Dummy", 0.0));
        kvitData.add(new Component("Dummy", 0.0));
    }

    //Methods for handling the build, current total and data to be sent for the receipt
    @FXML
    private  void ProssPris(ActionEvent event)
    {
        int selected = Processorer.getSelectionModel().getSelectedIndex();
        kvitData.set(0, register.getProcessors().get(selected));
        String pross = Processorer.getSelectionModel().getSelectedItem().toString();
        String str[] = pross.split (" ");
        String st = str[str.length-1].replaceAll("[^\\d.]", "");
        double d = Double.valueOf(st);
        Totalpris.set(0,d);
        totalPrice();
    }

    @FXML
    private  void SjermPris(ActionEvent event)
    {
        int selected = Sjermkorter.getSelectionModel().getSelectedIndex();
        kvitData.set(1, register.getGraphicCards().get(selected));
        String sjerm = Sjermkorter.getSelectionModel().getSelectedItem().toString();
        String str[] = sjerm.split (" ");
        String st = str[str.length-1].replaceAll("[^\\d.]", "");
        double d = Double.valueOf(st);
        Totalpris.set(1,d);
        totalPrice();
    }

    @FXML
    private  void MinnPris(ActionEvent event)
    {
        int selected = Minne.getSelectionModel().getSelectedIndex();
        kvitData.set(2, register.getMemoryChips().get(selected));
        String minne = Minne.getSelectionModel().getSelectedItem().toString();
        String str[] = minne.split (" ");
        String st = str[str.length-1].replaceAll("[^\\d.]", "");
        double d = Double.valueOf(st);
        Totalpris.set(2,d);
        totalPrice();

    }

    @FXML
    private  void HardiskPris(ActionEvent event)
    {
        int selected = Hardisk.getSelectionModel().getSelectedIndex();
        kvitData.set(3, register.getStorageUnits().get(selected));
        String hardisk = Hardisk.getSelectionModel().getSelectedItem().toString();
        String str[] = hardisk.split (" ");
        String st = str[str.length-1].replaceAll("[^\\d.]", "");
        double d = Double.valueOf(st);
        Totalpris.set(3,d);
        totalPrice();

    }

    @FXML
    private  void HovedkortkPris(ActionEvent event)
    {
        int selected = Hovedkort.getSelectionModel().getSelectedIndex();
        kvitData.set(4, register.getMotherboards().get(selected));
        String kort= Hovedkort.getSelectionModel().getSelectedItem().toString();
        String str[] = kort.split (" ");
        String st = str[str.length-1].replaceAll("[^\\d.]", "");
        double d = Double.valueOf(st);
        Totalpris.set(4,d);
        totalPrice();

    }

    @FXML
    private  void StromPris(ActionEvent event)
    {
        int selected = Strom.getSelectionModel().getSelectedIndex();
        kvitData.set(5, register.getPowerSupplies().get(selected));
        String strom = Strom.getSelectionModel().getSelectedItem().toString();
        String str[] = strom.split (" ");
        String st = str[str.length-1].replaceAll("[^\\d.]", "");
        double d = Double.valueOf(st);
        Totalpris.set(5,d);
        totalPrice();

    }

    @FXML
    private  void OSPris(ActionEvent event)
    {
        kvitData.set(6, register.getAllOS().get(OS.getSelectionModel().getSelectedIndex()));
        double d = Double.valueOf(kvitData.get(6).getPrice());
        Totalpris.set(6,d);
        totalPrice();
    }

    //Update current total
    public void totalPrice()
    {
        double sum = 0.00;
        for(Double d : Totalpris)
            sum += d;
        Pris.setText(df.format(sum) + "  Kr");

    }

    //When you're finished
    @FXML
    private void tilbake() throws IOException {
        Totalpris.clear();
        App.setRoot("Home");
    }

    //Go to see the receipt/cart
    @FXML
    private void Kjop() throws IOException {

        if (Processorer.getSelectionModel().isEmpty()|| Sjermkorter.getSelectionModel().isEmpty()|| Minne.getSelectionModel().isEmpty()||
                Hardisk.getSelectionModel().isEmpty()|| Hovedkort.getSelectionModel().isEmpty()|| Strom.getSelectionModel().isEmpty()|| OS.getSelectionModel().isEmpty()){

            Dialogs.Feilmelding("Vennligt Fyll listen");

        }

        else {

            String pross = Processorer.getSelectionModel().getSelectedItem().toString();
            String sjerm = Sjermkorter.getSelectionModel().getSelectedItem().toString();
            String minne = Minne.getSelectionModel().getSelectedItem().toString();
            String harddisk = Hardisk.getSelectionModel().getSelectedItem().toString();
            String kort = Hovedkort.getSelectionModel().getSelectedItem().toString();
            String strom = Strom.getSelectionModel().getSelectedItem().toString();
            String os = OS.getSelectionModel().getSelectedItem().toString();
            kjoplist.addAll(pross, sjerm, minne, harddisk, kort, strom, os);

            Totalpris.clear();
            App.setRoot("kvittering");
        }
    }
}