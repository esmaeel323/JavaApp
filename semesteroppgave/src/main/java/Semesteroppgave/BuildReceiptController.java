package Semesteroppgave;

import Components.Build;
import Exceptions.ComponentFormatException;
import Filbehandling.LagringTxt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BuildReceiptController
{

    @FXML
    Button tilbake, kvittering;

    //Show user final cart
    @FXML
    ListView list;

    //Receipt data
    ObservableList<Build> kvit = FXCollections.observableArrayList();

    @FXML
    Label dato;


    public void initialize()
    {
        kvit.addAll(FerdigPCController.kvitData);
        for(Build b : kvit)
        {
            String leggTil = "Navn: " + b.getName() + " - Pr. stk: " + b.getPrice() + "kr - Antall: " + b.getNumber() + "\n";
            list.getItems().add(leggTil);
        }

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        dato.setText(df.format(calobj.getTime()));
    }

    @FXML
    private void Tilbake() throws IOException
    {
        kvit.clear();
        list.getItems().clear();
        App.setRoot("Home");
    }

    //Save receipt
    @FXML
    private void Kvittering()
    {
        ArrayList<Build> list = new ArrayList(kvit);

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
                LagringTxt.skriveInnBuild(list, path);
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