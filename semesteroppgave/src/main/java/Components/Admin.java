package Components;

import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Admin implements Serializable
{
    private static final long serialVersionUID = 1;

    private transient SimpleStringProperty username;
    private transient SimpleStringProperty password;

    public Admin (String user, String pass)
    {
        username = new SimpleStringProperty(user);
        password = new SimpleStringProperty(pass);
    }

    public String getPassword()
    {
        return password.get();
    }

    public String getUsername()
    {
        return username.get();
    }

    public void setPassword(String password)
    {
        this.password.set(password);
    }

    public void setUsername(String username)
    {
        this.username.set(username);
    }

    //Check password
    public boolean check(String pass)
    {
        if(pass.equals(password.get()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
        out.writeUTF(getUsername());
        out.writeUTF(getPassword());
    }

    private void readObject(ObjectInputStream in) throws IOException
    {
        String user = in.readUTF();
        String pass = in.readUTF();
        username = new SimpleStringProperty(user);
        password = new SimpleStringProperty(pass);
    }
}

