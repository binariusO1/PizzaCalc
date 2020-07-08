package com.binario.pizzacalc;

public class Pizza {
    private final static double PI = 3.1415;
    private int diameter;
    private float price;
    private int amount;
    private float pricePerPiece;    //$/cm
    private long uniqueID;
    private static long uniqueIDCounter=0;
    private int itemHeight;
    public static int PIZZA_ITEM_HEIGHT = 40;

    public Pizza(int diam, float pr,int a )
    {
        uniqueID = uniqueIDCounter++;
        diameter = diam;
        price = pr;
        amount =a;
        pricePerPiece = 0;
        itemHeight = PIZZA_ITEM_HEIGHT;
    }
    public String getStringPizzaDiameter()
    {
        return Integer.toString(diameter);
    }
    public String getStringPizzaPrice()
    {
        return Float.toString(price);
    }
    public String getStringPizzaAmount()
    {
        return Integer.toString(amount);
    }

    public String getStringPricePerPiece()
    {
        return Float.toString(pricePerPiece);
    }

    public float getPricePerPiece()
    {
        float area = (float)PI*diameter*diameter/4;
        pricePerPiece = price/area;
        return pricePerPiece;
    }
    public int getItemHeight()
    {
        return itemHeight;
    }
}
