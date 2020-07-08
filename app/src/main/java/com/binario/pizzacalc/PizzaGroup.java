package com.binario.pizzacalc;

public class PizzaGroup {
    public static int RIGID_ITEM_HEIGHT = 91;
    public static int PIZZA_ITEM_HEIGHT = 40;
    public static int PIZZA_ADD_HEIGHT = 60;
    private static long uniqueIDCounter=0;

    private int itemNumber;
    private long uniqueID;
    private int itemHeight;
    private int pizzaListHeight;

    private boolean addPizzaVisible;
    //List of Pizzas

    //--unused yet

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          CONSTRUCTORS
    //---------------------------------------------------------------------------------------------------------------------------------------------
    public PizzaGroup(int number)
    {
        itemNumber = number;
        uniqueID = uniqueIDCounter++;
        itemHeight = RIGID_ITEM_HEIGHT;
        pizzaListHeight = 0;
        addPizzaVisible = true;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          PUBLIC FUNCTIONS
    //---------------------------------------------------------------------------------------------------------------------------------------------
    public String getStringItemNumber()
    {
        return Integer.toString(itemNumber);
    }
    public int getItemNumber()
    {
        return itemNumber;
    }

    public long getUniqueID()
    {
        return uniqueID;
    }
    public int getItemHeight()
    {
        return itemHeight;
    }
    public int getPizzaListHeight()
    {
        return pizzaListHeight;
    }

    public void setItemHeight(int height)
    {
        itemHeight = height;
    }

    public void expandItemHeight()
    {
        itemHeight += PIZZA_ITEM_HEIGHT;
        pizzaListHeight+=PIZZA_ITEM_HEIGHT;
    }
    public void removeItemHeight()
    {
        itemHeight -= PIZZA_ITEM_HEIGHT;
        pizzaListHeight-=PIZZA_ITEM_HEIGHT;
    }
    public void removeAddHeight()
    {
        itemHeight -= PIZZA_ADD_HEIGHT;
    }
    public void setItemNumber(int number)
    {
        itemNumber = number;
    }
    public boolean isAddPizzaVisible() { return addPizzaVisible; }
    public void setAddPizzaVisible(boolean state) { addPizzaVisible = state; }
}
