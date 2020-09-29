package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Power extends Component
{
    private transient SimpleIntegerProperty voltage;
    private transient SimpleIntegerProperty watt;

    public Power(String name, Double price, int voltage, int watt)
    {
        super(name, price);

        this.voltage = new SimpleIntegerProperty(voltage);
        this.watt = new SimpleIntegerProperty(watt);
    }

    public int getVoltage()
    {
        return voltage.get();
    }

    public int getWatt()
    {
        return watt.get();
    }

    public void setVoltage(int voltage)
    {
        this.voltage.set(voltage);
    }

    public void setWatt(int watt)
    {
        this.watt.set(watt);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s;%s", getName(), getPrice(), getVoltage(), getWatt());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeInt(getVoltage());
        out.writeInt(getWatt());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        int voltage = in.readInt();
        int watt = in.readInt();

        this.voltage = new SimpleIntegerProperty(voltage);
        this.watt = new SimpleIntegerProperty(watt);
    }
}
