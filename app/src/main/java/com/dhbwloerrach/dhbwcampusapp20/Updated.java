package com.dhbwloerrach.dhbwcampusapp20;

public class Updated
{

    public static final int Mensa=0, News=1, Guthaben=2, Role=3;
    private MensaPlan mensa;
    private int role;
    private boolean values[]={false,false,false, false};

    public Updated()
    {
    }

    public void InsertMensaPlan(MensaPlan mensaplan)
    {
        if(mensaplan!=null) {
            mensa = mensaplan;
            values[Updated.Mensa] = true;
        }
    }

    public MensaPlan GetMensaPlan()
    {
        return values[Updated.Mensa] ? mensa:null;
    }

    public void InsertRole(int role)
    {
        if(role!=-1) {
            this.role = role;
            values[Updated.Role] = true;
        }
    }

    public int GetRole()
    {
        return values[Updated.Role] ? role:0;
    }

    public boolean IsUpdated(int area)
    {
            return area<values.length && area>=0 && values[area];
    }

    public interface Refreshable {
        void Refresh(Updated areas);
    }

}