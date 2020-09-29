package Filbehandling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileStringWriter
{
    public static void writeString(Path path, String str) throws IOException
    {
        Files.write(path, str.getBytes());
    }
}
