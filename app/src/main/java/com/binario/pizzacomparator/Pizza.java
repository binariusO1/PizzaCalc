package com.binario.pizzacomparator;

public class Pizza {
    private final static double PI = 3.1415;
    private final static int PIZZA_ITEM_HEIGHT = 40;

    private int diameter;
    private float price;
    private int amount;
    private float pricePerPiece;    //$/cm
    private long uniqueID;
    private static long uniqueIDCounter=0;
    private int itemHeight;

    Pizza(int diam, float pr, int a)
    {
        uniqueID = uniqueIDCounter++;
        diameter = diam;
        price = pr;
        amount =a;
        pricePerPiece = 0;
        itemHeight = PIZZA_ITEM_HEIGHT;
    }
    String getStringPizzaDiameter()
    {
        return Integer.toString(diameter);
    }
    String getStringPizzaPrice()
    {
        return Float.toString(price);
    }
    String getStringPizzaAmount()
    {
        return Integer.toString(amount);
    }

    public String getStringPricePerPiece()
    {
        return Float.toString(pricePerPiece);
    }

    int getDiameter()
    {return diameter;}
    float getPrice()
    {return price;}
    int getAmount()
    {return amount;}
    float getPricePerPiece()
    {
        float area = (float)PI*diameter*diameter/4;
        pricePerPiece = price/area;
        return pricePerPiece;
    }
    int getArea()
    {
        return (int)((float)PI*diameter*diameter/4)*amount;
    }
    int getPricePerOne()
    {
        float area = (float)PI*diameter*diameter/4*100;
        return (int)(area/price);
    }
    void editValues(int d, float p, int a)
    {
        diameter = d;
        price = p;
        amount = a;
    }
    int getItemHeight()
    {
        return itemHeight;
    }
}
