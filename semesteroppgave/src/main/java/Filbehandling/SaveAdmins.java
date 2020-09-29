package Filbehandling;

import Semesteroppgave.AdminRegister;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveAdmins
{
    public void save(AdminRegister currentRegister, Path fileLocation) throws IOException
    {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fileLocation)))
        {
            out.writeObject(currentRegister);
        }
    }
}
