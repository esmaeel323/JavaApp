package Semesteroppgave;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    //Methods for navigation to the different product pages and admin log in
    @FXML
    private void PcBuild() throws IOException
    {
        App.setRoot("PcBuild");
    }
    @FXML
    private void Admin() throws IOException
    {
        App.setRoot("loginn");
    }

    @FXML
    private void Komponenter() throws IOException
    {
        App.setRoot("komponenter");
    }

    @FXML
    private void ferdigPC() throws IOException
    {
        App.setRoot("ferdigPC");
    }
}