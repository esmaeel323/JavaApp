package Filbehandling;

import Semesteroppgave.ProductRegister;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveRegistry implements FileSaver
{
    @Override
    public void save(ProductRegister currentRegister, Path fileLocation) throws IOException
    {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fileLocation)))
        {
            out.writeObject(currentRegister);
        }
    }
}
