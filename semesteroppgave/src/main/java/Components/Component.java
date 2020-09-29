package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Component implements Serializable
{
    private static final long serialVersionUID = 1;

    private transient SimpleStringProperty name;
    private transient SimpleDoubleProperty price;
    private transient SimpleStringProperty number;

    public Component(String n, Double p)
    {
        if(n.isEmpty())
        {
            throw new IllegalArgumentException("Navn kan ikke være tomt");
        }

        name = new SimpleStringProperty(n);
        price = new SimpleDoubleProperty(p);
    }

    public void setName(String name)
    {
        if(name.isEmpty())
        {
            throw new IllegalArgumentException("Navn kan ikke være tomt");
        }
        this.name.set(name);
    }

    public String getName()
    {
        return name.get();
    }

    public void setPrice(Double price)
    {
        this.price.set(price);
    }

    public Double getPrice()
    {
        return price.get();
    }

    public String getNumber(){
        return number.get();
    }

    public void setNumber(String nc)
    {
        this.number.set(nc);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s", name.getValue(), price.get(), number.get());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeUTF(getName());
        out.writeDouble(getPrice());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        String name = in.readUTF();
        Double price = in.readDouble();

        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.number = new SimpleStringProperty("1");
    }
}
