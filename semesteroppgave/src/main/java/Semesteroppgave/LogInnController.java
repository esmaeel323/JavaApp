package Semesteroppgave;

import Filbehandling.GetAdmins;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Paths;

public class LogInnController
{
    @FXML TextField txtbruker;
    @FXML PasswordField txtpassord;
    @FXML Label feilmelding;

    //All administrators in the system
    AdminRegister adminRegister = new AdminRegister();


    @FXML
    public void initialize()
    {
        //Retrieve users upon opening window
        getUsers();
    }

    @FXML
    private void tilbake() throws IOException
    {
        App.setRoot("Home");
    }

    //Check username and password against all users
    @FXML
    private void Logginn()throws IOException
    {
        String bruker = txtbruker.getText();
        String passord = txtpassord.getText();
        if(adminRegister.check(bruker, passord))
        {
            App.setRoot("admin");
        }
    }

    //Retrieve user data
    private void getUsers()
    {
        try
        {
            GetAdmins ga = new GetAdmins();
            ga.open(adminRegister, Paths.get("Admins.jobj"));
        }
        catch (IOException e)
        {
            Dialogs.Feilmelding("Kunne ikke hente data \n" + e.getMessage());
        }
    }
}