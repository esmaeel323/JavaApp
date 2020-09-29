package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Motherboard extends Component
{
    private transient SimpleIntegerProperty ports;

    public Motherboard(String name, Double price, int ports)
    {
        super(name, price);

        this.ports = new SimpleIntegerProperty(ports);
    }

    public int getPorts()
    {
        return ports.get();
    }

    public void setPorts(int ports)
    {
        this.ports.set(ports);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s", getName(), getPrice(), getPorts());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeInt(getPorts());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        int ports = in.readInt();

        this.ports = new SimpleIntegerProperty(ports);
    }
}
