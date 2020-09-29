package Components;

//Class for checking string values

import Semesteroppgave.Dialogs;
import javafx.fxml.FXML;

public class Validator
{
    //Check if string can be converted to Integer
    public boolean intValue(String i)
    {
        if(i.length() == 1)
        {
            if(i.equals("0"))
            {
                return false;
            }
        }
        return !i.isEmpty() && i.matches("[0-9]+");
    }

    //Check if string can be converted to Double
    public boolean doubleValue(String d)
    {
        if(d.isEmpty())
        {
            return false;
        }
        if(d.matches("0+"))
        {
            return false;
        }
        if(d.contains(","))
        {
            return false;
        }
        if(d.contains("."))
        {
            String[] num = d.split("\\.");
            if(num.length > 2)
            {
                return false;
            }
            if(num[0].matches("[0-9]+") && num[1].matches("[0-9]+"))
            {
                return true;
            }
        }
        return !d.isEmpty() && d.matches("[0-9]+");
    }

    //Converter for editing Integers in TableView
    public static class IntegerConverter extends javafx.util.converter.IntegerStringConverter
    {
        private boolean valid;

        @FXML
        public Integer fromString(String s)
        {
            try
            {
                Integer newValue = super.fromString(s);
                valid = true;
                return newValue;
            }
            catch (NumberFormatException nfe)
            {
                Dialogs.Feilmelding("Må sette inn et gyldig heltall");
                valid = false;
                return 0;
            }
        }

        public boolean isValid()
        {
            return valid;
        }
    }

    //Converter for editing Doubles in TableView
    public static class DoubleConverter extends javafx.util.converter.DoubleStringConverter
    {
        private boolean valid;

        @FXML
        public Double fromString(String s)
        {
            try
            {
                Double newValue = super.fromString(s);
                valid = true;
                return newValue;
            }
            catch (NumberFormatException nfe)
            {
                Dialogs.Feilmelding("Ugyldige tegn i ny verdi");
                valid = false;
                return 0.0;
            }
        }

        public boolean isValid()
        {
            return valid;
        }
    }
}
