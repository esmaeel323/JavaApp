package Components;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GraphicCard extends Component
{
    private transient SimpleIntegerProperty size;

    public GraphicCard(String name, Double price, int size)
    {
        super(name, price);

        this.size = new SimpleIntegerProperty(size);
    }

    public int getSize()
    {
        return size.get();
    }

    public void setSize(int size)
    {
        this.size.set(size);
    }

    @Override
    public String toString()
    {
        return String.format("%s;%s;%s", getName(), getPrice(), getSize());
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeInt(getSize());
    }

    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException
    {
        in.defaultReadObject();
        int size = in.readInt();

        this.size = new SimpleIntegerProperty(size);
    }
}
