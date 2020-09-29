package Exceptions;

import java.io.IOException;

public class BuildFormatException extends IOException
{
    public BuildFormatException(String msg){
        super(msg);
    }
}
