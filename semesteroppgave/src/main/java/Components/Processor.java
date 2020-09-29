package Components;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Processor extends Component
{
    private transient SimpleDoubleProperty frequency;
    private transient SimpleIntegerProperty cores;

    public Processor(String name, Double price, Double freq, int cores)
    {
        super(name, price);

        frequency = new SimpleDoubleProperty(freq);
        this.cores = new SimpleIntegerProperty(cores);
    }

    public double getFrequency()
    {
        return frequency.get();
    }

    public int getCores()
    {
        return cores.get();
    }

    public void setCoreCount(int coreCount)
    {
        cores.set(coreCount);
    }

    public void setFrequency(double frequency)
    {
        this.frequency.set(frequency);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s;%s", getName(), getPrice(), getFrequency(), getCores());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeDouble(getFrequency());
        out.writeInt(getCores());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        Double freq = in.readDouble();
        int core = in.readInt();

        frequency = new SimpleDoubleProperty(freq);
        cores = new SimpleIntegerProperty(core);
    }
}

