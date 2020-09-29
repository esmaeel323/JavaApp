package Filbehandling;

import Semesteroppgave.ProductRegister;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSaver
{
    void save(ProductRegister register, Path FileLocation) throws IOException;
}
