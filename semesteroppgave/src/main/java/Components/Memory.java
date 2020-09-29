package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Memory extends Component
{

    private transient SimpleStringProperty type;
    private transient SimpleIntegerProperty size;

    public Memory(String name, Double price, String type, int size)
    {
        super(name, price);

        this.type = new SimpleStringProperty(type);
        this.size = new SimpleIntegerProperty(size);
    }

     public String getType()
     {
         return type.get();
     }

     public int getSize()
     {
         return size.get();
     }

    public void setType(String type)
    {
        this.type.set(type);
    }

    public void setSize(int size)
    {
        this.size.set(size);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s;%s", getName(), getPrice(), getType(), getSize());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeUTF(getType());
        out.writeInt(getSize());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        String type = in.readUTF();
        int size = in.readInt();

        this.type = new SimpleStringProperty(type);
        this.size = new SimpleIntegerProperty(size);
    }
}
