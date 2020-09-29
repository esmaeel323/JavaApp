package Semesteroppgave;

import Components.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRegister implements Serializable
{
    private static final long serialVersionUID = 1;

    //Observable lists for each type of product + one for all components
    private transient ObservableList<Component> register = FXCollections.observableArrayList();
    private transient ObservableList<Processor> processorList = FXCollections.observableArrayList();
    private transient ObservableList<GraphicCard> graphicList = FXCollections.observableArrayList();
    private transient ObservableList<HardDisk> storageList = FXCollections.observableArrayList();
    private transient ObservableList<Memory> memoryList = FXCollections.observableArrayList();
    private transient ObservableList<Motherboard> motherboardList = FXCollections.observableArrayList();
    private transient ObservableList<OperatingSystem> OSList = FXCollections.observableArrayList();
    private transient ObservableList<Power> powerList = FXCollections.observableArrayList();
    private transient ObservableList<Build> buildList = FXCollections.observableArrayList();

    //Add product methods
    public void addProduct (Component c)
    {
        register.add(c);
    }
    public void addProcessor (Processor c)
    {
        processorList.add(c);
    }
    public void addGraphics (GraphicCard g)
    {
        graphicList.add(g);
    }
    public void addStorage (HardDisk h)
    {
        storageList.add(h);
    }
    public void addMemory(Memory m)
    {
        memoryList.add(m);
    }
    public void addMotherboard(Motherboard m)
    {
        motherboardList.add(m);
    }
    public void addOS(OperatingSystem os)
    {
        OSList.add(os);
    }
    public void addPower(Power p)
    {
        powerList.add(p);
    }
    public void addBuild(Build b)
    {
        buildList.add(b);
    }

    //Get products pr. category
    public ObservableList<Component> getRegister()
    {
        return register;
    }
    public ObservableList<Processor> getProcessors()
    {
        return processorList;
    }
    public ObservableList<GraphicCard> getGraphicCards()
    {
        return graphicList;
    }
    public ObservableList<HardDisk> getStorageUnits()
    {
        return storageList;
    }
    public ObservableList<Memory> getMemoryChips()
    {
        return memoryList;
    }
    public ObservableList<Motherboard> getMotherboards()
    {
        return motherboardList;
    }
    public ObservableList<OperatingSystem> getAllOS()
    {
        return OSList;
    }
    public ObservableList<Power> getPowerSupplies()
    {
        return powerList;
    }
    public ObservableList<Build> getBuilds()
    {
        return buildList;
    }

    //Remove a component from both registers
    public void removeProduct(Component c, String type)
    {
        register.remove(c);
        switch (type)
        {
            case("Processor"):
            {
                processorList.remove(c);
                break;
            }
            case("GraphicCard"):
            {
                graphicList.remove(c);
            }
            case("HardDisk"):
            {
                storageList.remove(c);
            }
            case("Memory"):
            {
                memoryList.remove(c);
            }
            case("Motherboard"):
            {
                motherboardList.remove(c);
            }
            case("OperatingSystem"):
            {
                OSList.remove(c);
            }
            case("Power"):
            {
                powerList.remove(c);
            }
        }
    }

    public void removeBuild(Build b)
    {
        buildList.remove(b);
    }

    //Remove all for retrieval of saved data
    public void removeAll()
    {
        register.clear();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Component c : register)
        {
            sb.append(c.toString());
            sb.append(System.lineSeparator());
        }
        for(Processor p : processorList)
        {
            sb.append(p.toString());
            sb.append(System.lineSeparator());
        }
        for(GraphicCard g : graphicList)
        {
            sb.append(g.toString());
            sb.append(System.lineSeparator());
        }
        for(HardDisk h : storageList)
        {
            sb.append(h.toString());
            sb.append(System.lineSeparator());
        }
        for(Memory m : memoryList)
        {
            sb.append(m.toString());
            sb.append(System.lineSeparator());
        }
        for(Motherboard m : motherboardList)
        {
            sb.append(m.toString());
            sb.append(System.lineSeparator());
        }
        for(OperatingSystem os : OSList)
        {
            sb.append(os.toString());
            sb.append(System.lineSeparator());
        }
        for(Power p : powerList)
        {
            sb.append(p.toString());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    //File save/load methods
    private void writeObject(ObjectOutputStream s) throws IOException
    {
        s.defaultWriteObject();
        s.writeObject(new ArrayList<>(register));
        s.writeObject(new ArrayList<>(processorList));
        s.writeObject(new ArrayList<>(graphicList));
        s.writeObject(new ArrayList<>(storageList));
        s.writeObject(new ArrayList<>(memoryList));
        s.writeObject(new ArrayList<>(motherboardList));
        s.writeObject(new ArrayList<>(OSList));
        s.writeObject(new ArrayList<>(powerList));
        s.writeObject(new ArrayList<>(buildList));
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        List<Component> list = (List<Component>) s.readObject();
        List<Processor> p = (List<Processor>) s.readObject();
        List<GraphicCard> g = (List<GraphicCard>) s.readObject();
        List<HardDisk> h = (List<HardDisk>) s.readObject();
        List<Memory> m = (List<Memory>) s.readObject();
        List<Motherboard> mb = (List<Motherboard>) s.readObject();
        List<OperatingSystem> os = (List<OperatingSystem>) s.readObject();
        List<Power> po = (List<Power>) s.readObject();
        List<Build> b = (List<Build>) s.readObject();

        register = FXCollections.observableArrayList();
        register.addAll(list);

        processorList = FXCollections.observableArrayList();
        processorList.addAll(p);

        graphicList = FXCollections.observableArrayList();
        graphicList.addAll(g);

        storageList = FXCollections.observableArrayList();
        storageList.addAll(h);

        memoryList = FXCollections.observableArrayList();
        memoryList.addAll(m);

        motherboardList = FXCollections.observableArrayList();
        motherboardList.addAll(mb);

        OSList = FXCollections.observableArrayList();
        OSList.addAll(os);

        powerList = FXCollections.observableArrayList();
        powerList.addAll(po);

        buildList = FXCollections.observableArrayList();
        buildList.addAll(b);
    }
}
