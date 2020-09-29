package Exceptions;

import java.io.IOException;

public class ComponentFormatException extends IOException {

    public ComponentFormatException(String msg){
        super(msg);
    }

}