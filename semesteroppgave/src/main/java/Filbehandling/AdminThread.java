package Filbehandling;
import Semesteroppgave.AdminRegister;
import Semesteroppgave.Dialogs;
import Semesteroppgave.ProductRegister;
import javafx.concurrent.Task;
import java.io.IOException;
import java.nio.file.Paths;

//Class for handling Threads

public class AdminThread  extends Task{
    private ProductRegister productRegister;
    private AdminRegister adminRegister;

    public AdminThread(ProductRegister productRegister, AdminRegister admins)
    {
        this.productRegister= productRegister;
        adminRegister = admins;
    }

    //Retrieve admin data in separate Thread
    public Object call()
    {
        try
        {
            Thread.sleep(2000);
            GetRegistry gr = new GetRegistry();
            GetAdmins ga = new GetAdmins();
            gr.open(productRegister, Paths.get("Products.jobj"));
            ga.open(adminRegister, Paths.get("Admins.jobj"));
        }
        catch (IOException | InterruptedException e)
        {
            Dialogs.Feilmelding("Avviket sier: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


}