package Filbehandling;

import Components.*;
import Semesteroppgave.Dialogs;
import Semesteroppgave.ProductRegister;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

//Retrieve stored products

public class GetRegistry implements FileOpener
{
    @Override
    public void open(ProductRegister currentRegister, Path fileLocation) throws IOException
    {
        try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fileLocation)))
        {
            //Retrieve the stored registry
            ProductRegister storedRegister = (ProductRegister) in.readObject();

            //Remove everything in current instance
            currentRegister.removeAll();

            //Place stored components in to the current registry.
            for(Component c:storedRegister.getRegister())
            {
                currentRegister.addProduct(c);
            }

            for(Processor p:storedRegister.getProcessors())
            {
                currentRegister.addProcessor(p);
            }

            for(GraphicCard g :storedRegister.getGraphicCards())
            {
                currentRegister.addGraphics(g);
            }

            for(HardDisk h :storedRegister.getStorageUnits())
            {
                currentRegister.addStorage(h);
            }

            for(Memory m :storedRegister.getMemoryChips())
            {
                currentRegister.addMemory(m);
            }

            for(Motherboard m : storedRegister.getMotherboards())
            {
                currentRegister.addMotherboard(m);
            }

            for(OperatingSystem os : storedRegister.getAllOS())
            {
                currentRegister.addOS(os);
            }

            for(Power p : storedRegister.getPowerSupplies())
            {
                currentRegister.addPower(p);
            }

            for(Build b : storedRegister.getBuilds())
            {
                currentRegister.addBuild(b);
            }
        }
        catch (ClassNotFoundException e)
        {
            Dialogs.Feilmelding("Kunne ikke laste inn data, kontakt systemadministrator");
        }
    }
}
