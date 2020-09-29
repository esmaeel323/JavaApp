package Filbehandling;

import Components.*;
import Semesteroppgave.AdminRegister;
import Semesteroppgave.Dialogs;
import Semesteroppgave.ProductRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

//Retrieve stored admins

public class GetAdmins
{
    public void open(AdminRegister currentRegister, Path fileLocation) throws IOException
    {
        try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fileLocation)))
        {
            //Retrieve the stored registry
            AdminRegister storedRegister = (AdminRegister) in.readObject();

            //Remove everything in current instance
            currentRegister.removeAll();

            //Add stored Admins
            for(Admin a:storedRegister.getUsers())
            {
                currentRegister.addUser(a);
            }
        }
        catch (ClassNotFoundException e)
        {
            Dialogs.Feilmelding("Kunne ikke laste inn data, kontakt systemadministrator");
        }
    }
}
