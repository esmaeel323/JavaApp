package Filbehandling;

import Semesteroppgave.ProductRegister;

import java.io.IOException;
import java.nio.file.Path;

public interface FileOpener {

    void open(ProductRegister register, Path fileLocation) throws IOException;

}
