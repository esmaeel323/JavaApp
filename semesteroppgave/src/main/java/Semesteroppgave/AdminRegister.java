package Semesteroppgave;

import Components.Admin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminRegister implements Serializable
{
    private static final long serialVersionUID = 1;
    private transient ObservableList<Admin> register = FXCollections.observableArrayList();

    public void addUser(Admin a)
    {
        register.add(a);
    }

    public ObservableList<Admin> getUsers()
    {
        return register;
    }

    public void removeUser(Admin a)
    {
        register.remove(a);
    }

    public void removeAll()
    {
        register.removeAll();
    }

    //Check in regards to log in with error messages
    public boolean check(String user, String pass)
    {
        boolean foundUser = false;
        boolean correctPass = false;
        if(user.isEmpty() || pass.isEmpty())
        {
            Dialogs.Feilmelding("Brukernavn og passord kan ikke være tomt!");
            return false;
        }
        for(Admin a:register)
        {
            if(a.getUsername().equals(user))
            {
                foundUser = true;
                correctPass = a.check(pass);
            }
        }

        if(!foundUser || !correctPass)
        {
            Dialogs.Feilmelding("Feil i brukernavn eller passord");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException
    {
        s.defaultWriteObject();
        s.writeObject(new ArrayList<>(register));
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        List<Admin> list = (List<Admin>) s.readObject();

        register = FXCollections.observableArrayList();
        register.addAll(list);
    }
}
