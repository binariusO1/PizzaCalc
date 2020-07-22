package com.binario.pizzacomparator;

public class PizzaGroup {
    private final static int RIGID_ITEM_HEIGHT = 189;
    private final static int PIZZA_ITEM_HEIGHT = 40;
    private final static int PIZZA_ADD_HEIGHT = 60;
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
    PizzaGroup(int number)
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
    String getStringItemNumber()
    {
        return Integer.toString(itemNumber);
    }
    int getItemNumber()
    {
        return itemNumber;
    }

    long getUniqueID()
    {
        return uniqueID;
    }
    int getItemHeight()
    {
        return itemHeight;
    }
    int getPizzaListHeight()
    {
        return pizzaListHeight;
    }

    void increaseItemHeight(int height)
    {
        itemHeight += height;
    }

    void expandItemHeight()
    {
        itemHeight += PIZZA_ITEM_HEIGHT;
        pizzaListHeight+=PIZZA_ITEM_HEIGHT;
    }
    void removeItemHeight()
    {
        itemHeight -= PIZZA_ITEM_HEIGHT;
        pizzaListHeight-=PIZZA_ITEM_HEIGHT;
    }
    void removeAddHeight()
    {
        itemHeight -= PIZZA_ADD_HEIGHT;
    }
    void addAddHeight()
    {
        itemHeight += PIZZA_ADD_HEIGHT;
    }
    void setItemNumber(int number)
    {
        itemNumber = number;
    }
    boolean isAddPizzaVisible() { return addPizzaVisible; }
    void setAddPizzaVisible(boolean state) { addPizzaVisible = state; }
}
