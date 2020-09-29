package Semesteroppgave;

import Components.Component;
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


public class KvitteringController
{

    @FXML
    Button tilbake, kvittering;

    @FXML
    ListView list;

    ObservableList<Component> kvit = FXCollections.observableArrayList();

    @FXML
    Label dato;


    public void initialize()
    {
        list.setItems(PcBuildController.kjoplist);
        kvit.addAll(PcBuildController.kvitData);

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        dato.setText(df.format(calobj.getTime()));
    }

    //Go home
    @FXML
    private void Tilbake() throws IOException {
        kvit.clear();
        list.getItems().clear();
        App.setRoot("Home");
    }

    //Save receipt to file
    @FXML
    private void Kvittering()
    {
        ArrayList<Component> list = new ArrayList(kvit);

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
                LagringTxt.skriveInn(list, path);
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