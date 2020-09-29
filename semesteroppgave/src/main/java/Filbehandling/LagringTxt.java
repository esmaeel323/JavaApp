package Filbehandling;
import Components.*;
import Exceptions.BuildFormatException;
import Exceptions.ComponentFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Write purchases to a receipt file

public class LagringTxt implements Lagring {
    public static String DELIMITER = ";";

    //Receipt for individual components
    public static void skriveUtKvittering(List<Component> Components, Double totalPrice, Path path) throws IOException
    {
        StringBuffer str = new StringBuffer();
        str.append("\n");
        str.append("KVITTERING       ");
        str.append("\n");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        str.append("Dato:      " + date.toString());
        str.append("\n");
        str.append("\n");
        str.append("      **************     ");
        str.append("\n");

        for(Component p : Components) {
            str.append(ComponentFormatter(p));
            str.append("\n");
        }

        str.append("      **************     ");
        str.append("\n");
        str.append("\n");
        str.append("Total Pris er:       "+ DELIMITER + totalPrice.toString());
        str.append("\n");
        str.append("\n");
        str.append("VELKOMMEN TILBAKE !        ");

        skriveInnFil(path, str.toString());

    }

    //Receipt for custom build
    public static void skriveInn(List<Component> Components, Path path) throws IOException {

        Double total = 0.0;
        StringBuffer str = new StringBuffer();
        for(Component p : Components) {
            total += p.getPrice()*Integer.parseInt(p.getNumber());
            str.append(ComponentFormatter(p));
            str.append("\n");
        }
        str.append("Total: " + total + "kr");
        skriveInnFil(path, str.toString());
    }

    //Receipt for ready made builds
    public static void skriveInnBuild(List<Build> builds, Path path) throws IOException {

        Double total = 0.0;
        StringBuffer str = new StringBuffer();
        for(Build b : builds)
        {
            total += b.getPrice()*Integer.parseInt(b.getNumber());
            str.append(BuildFormatter(b));
            str.append("\n");
        }
        str.append("Total: " + total + "kr");
        skriveInnFil(path, str.toString());
    }

    //Write receipt to file
    public static void skriveInnFil(Path path, String str) throws IOException {
        Files.write(path, str.getBytes());
    }

    //Format products to receipt files
    public static String ComponentFormatter(Component c) throws ComponentFormatException
    {
        String type = c.getClass().getSimpleName();
        String ut = "";

        switch (type)
        {
            case("Processor"):
            {
                Processor p = (Processor) c;
                ut = p.getName() + DELIMITER + p.getPrice() + DELIMITER + p.getNumber() + DELIMITER + p.getFrequency() + DELIMITER + p.getCores();
                break;
            }
            case("GraphicCard"):
            {
                GraphicCard g = (GraphicCard) c;
                ut = g.getName() + DELIMITER + g.getPrice() + DELIMITER + g.getNumber() + DELIMITER + g.getSize();
                break;
            }
            case("HardDisk"):
            {
                HardDisk h = (HardDisk) c;
                ut = h.getName() + DELIMITER + h.getPrice() + DELIMITER + h.getNumber() + DELIMITER + h.getType() + DELIMITER + h.getSize();
                break;
            }
            case("Memory"):
            {
                Memory m = (Memory) c;
                ut = m.getName() + DELIMITER + m.getPrice() + DELIMITER + m.getNumber() + DELIMITER + m.getType() + DELIMITER + m.getSize();
                break;
            }
            case("Motherboard"):
            {
                Motherboard m = (Motherboard) c;
                ut = m.getName() + DELIMITER + m.getPrice() + DELIMITER + m.getNumber() + DELIMITER + m.getPorts();
                break;
            }
            case("OperatingSystem"):
            {
                OperatingSystem os = (OperatingSystem) c;
                ut = os.getName() + DELIMITER + os.getPrice() + DELIMITER + os.getNumber() + DELIMITER + os.getBit();
                break;
            }
            case("Power"):
            {
                Power p = (Power) c;
                ut = p.getName() + DELIMITER + p.getPrice() + DELIMITER + p.getNumber() + DELIMITER + p.getVoltage() + DELIMITER + p.getWatt();
                break;
            }
        }

        if (ut.isEmpty()){
            throw new ComponentFormatException("Ugyldig produkt format");
        }
        return ut;
    }

    public static String BuildFormatter(Build b) throws BuildFormatException
    {
        return "Navn: " + b.getName() + " - Pr. stk: " + b.getPrice() + "kr - Antall: " + b.getNumber();
    }
}