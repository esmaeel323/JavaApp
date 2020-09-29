package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Build implements Serializable
{
    private static final long serialVersionUID = 1;

    private transient SimpleStringProperty name;
    private Processor p;
    private GraphicCard g;
    private HardDisk h;
    private Memory m;
    private Motherboard mb;
    private OperatingSystem o;
    private Power po;
    private transient SimpleDoubleProperty price;
    private transient SimpleStringProperty number;

    public Build(String n, Processor processor, GraphicCard gc, HardDisk hd, Memory ram, Motherboard motherboard, OperatingSystem os, Power power, Double pr)
    {
        name = new SimpleStringProperty(n);
        p = processor;
        g  = gc;
        h = hd;
        m = ram;
        mb = motherboard;
        o = os;
        po = power;
        price = new SimpleDoubleProperty(pr);
    }

    public String getName()
    {
        return name.get();
    }
    public void setName(String n)
    {
        name.set(n);
    }

    public Processor getP()
    {
        return p;
    }
    public void setP(Processor p)
    {
        this.p = p;
    }

    public GraphicCard getG()
    {
        return g;
    }
    public void setG(GraphicCard g)
    {
        this.g = g;
    }

    public HardDisk getH()
    {
        return h;
    }
    public void setH(HardDisk h)
    {
        this.h = h;
    }

    public Memory getM()
    {
        return m;
    }
    public void setM(Memory m)
    {
        this.m = m;
    }

    public Motherboard getMb()
    {
        return mb;
    }
    public void setMb(Motherboard mb)
    {
        this.mb = mb;
    }

    public OperatingSystem getO()
    {
        return o;
    }
    public void setO(OperatingSystem o)
    {
        this.o = o;
    }

    public Power getPo()
    {
        return po;
    }
    public void setPo(Power po)
    {
        this.po = po;
    }

    public Double getPrice()
    {
        return price.get();
    }
    public void setPrice(Double price)
    {
        this.price.set(price);
    }

    public String getNumber()
    {
        return number.get();
    }
    public void setNumber(String number)
    {
        this.number.set(number);
    }

    @Override
    public String toString()
    {
        return "Prosessor: " + p.getName() + ", " + p.getFrequency() + "GHz, " + p.getCores() + " kjerner\n" +
                "Minne: " + m.getName() + ", " + m.getType() + ", " + m.getSize() + "GB\n" +
                "Grafikk: " + g.getName() + ", " + m.getSize() + "GB\n" +
                "Lagring: " + h.getName() + ", " + h.getType() + ", " + h.getSize() + "GB\n" +
                "Operativsystem: " + o.getName() + ", " + o.getBit() + "-bit\n" +
                "USB-porter: " + mb.getPorts();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeUTF(getName());
        out.writeDouble(getPrice());
        out.writeObject(p);
        out.writeObject(g);
        out.writeObject(h);
        out.writeObject(m);
        out.writeObject(mb);
        out.writeObject(o);
        out.writeObject(po);
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        String name = in.readUTF();
        Double price = in.readDouble();

        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        p = (Processor) in.readObject();
        g = (GraphicCard) in.readObject();
        h = (HardDisk) in.readObject();
        m = (Memory) in.readObject();
        mb = (Motherboard) in.readObject();
        o = (OperatingSystem) in.readObject();
        po = (Power) in.readObject();
        this.number = new SimpleStringProperty("1");
    }
}
