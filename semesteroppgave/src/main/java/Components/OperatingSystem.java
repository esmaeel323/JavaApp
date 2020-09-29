package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OperatingSystem extends Component
{
    private transient SimpleIntegerProperty bit;
    public OperatingSystem(String name, Double price, int bit)
    {
        super(name, price);

        this.bit = new SimpleIntegerProperty(bit);
    }

    public int getBit()
    {
        return bit.get();
    }

    public void setBit(int bit)
    {
        this.bit.set(bit);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s", getName(), getPrice(), getBit());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeInt(getBit());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        int bit = in.readInt();

        this.bit = new SimpleIntegerProperty(bit);
    }
}
