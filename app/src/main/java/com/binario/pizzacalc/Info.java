package com.binario.pizzacalc;

public class Info {
    public static boolean state;
    public Info ()
    {
        state = false;
    }

    public static void setState(boolean state) {
        if (state==true)
            state=false;
        else
            state=true;
    }

    public static boolean getState() {
        return state;
    }
}
