package com.binario.pizzacalc;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//---------------------------------------------------------------------------------------------------------------------------------------------
//                                                          PIZZA ADAPTER
//---------------------------------------------------------------------------------------------------------------------------------------------
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
    private View.OnClickListener mClickListener;
    private OnItemButtonItemClickListener itemButtonListener;
    // Store a member variable for the contacts
    private List<Pizza> arrGroup;
    public boolean isClickable = true;
    private ConvertPxDp convert;
    private Context context;
    //----------------------------------------------------------------------------------------------------------------
    //                                                  CONSTRUCTOR
    //----------------------------------------------------------------------------------------------------------------
    // Pass in the contact array into the constructor
    public ItemAdapter()
    {
        arrGroup = new ArrayList<Pizza>();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          VIEW HOLDER
    //---------------------------------------------------------------------------------------------------------------------------------------------
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView diameterTextView;
        private TextView priceTextView;
        private TextView amountTextView;
        private Button ip_button_item;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            diameterTextView = (TextView) itemView.findViewById(R.id.ip_show_diam);
            priceTextView = (TextView) itemView.findViewById(R.id.ip_show_price);
            amountTextView = (TextView) itemView.findViewById(R.id.ip_show_amount);
            ip_button_item = (Button) itemView.findViewById(R.id.ip_button_item);

        }
        public Button getItemButton() {
            return ip_button_item;
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          onCreate VIEW HOLDER
    //---------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_pizza, parent, false);    //instead parent, false -> null

        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          onBind VIEW HOLDER
    //---------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public final void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        //viewHolder.setIsRecyclable(false);
        // Get the data model based on position
        //viewHolder.getAdapterPosition() instead position
        Pizza contact = this.arrGroup.get(position);
        //Log.d("----------------------------TAG", "contact pos:"+ "(" + (position) + ")");

        // Set item views based on your views and data model
        TextView textView1 = viewHolder.diameterTextView;
        textView1.setText(contact.getStringPizzaDiameter());
        TextView textView2 = viewHolder.priceTextView;
        textView2.setText(contact.getStringPizzaPrice());
        TextView textView3 = viewHolder.amountTextView;
        textView3.setText(contact.getStringPizzaAmount());

        //----- SETCLICK
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(view);
                //setCurrentNumber(arrGroup.get(position).getItemNumber());
                Log.d("----------------------------TAG", "fgetNumber: (" + (position) + ")");

                int color = Color.parseColor("#FF000000");

                //ViewGroup.LayoutParams params = viewHolder.linearLayoutMain.getLayoutParams();
                //viewHolder.linearLayoutMain.setBackgroundColor(color);
                //viewHolder.linearLayoutMain.setLayoutParams(params);
            }
        });
        if (itemButtonListener!= null) {
            viewHolder.getItemButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemButtonListener.onItemIsClick(v, position);
                }
            });
        }
        //Log.d("----------------------------TAG", "onBindViewHolder (" + ++TEMP_B + ")");
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                         FUNCTIONS
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //====================================================================
    //                         GET SET
    //====================================================================

    @Override
    public int getItemCount()
    {
        return this.arrGroup.size();
    }
    // Returns the total count of items in the list
    public int Size() {
        return this.arrGroup.size();
    }
    public void addPizza(int diameter, float price,int amount)
    {
        arrGroup.add( new Pizza(diameter,price,amount) );
        //notifyItemRangeChanged(0,arrGroup.size());
    }
    public void remove(int position)
    {
        //we have to re-count all items indexes
        arrGroup.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0,arrGroup.size());

    }
    public void removePizza(int position)
    {

    }
    public int getItemNumber(int position)
    {
        return position;
    }

    public int getItemHeigth(int position)
    {
        return 0;
    }

    public String getSumOfPricePerPiece()
    {
        float sum=0;
        for(int i = 0 ; i < arrGroup.size() ; i++)
        {
            sum+=arrGroup.get(i).getPricePerPiece();
        }
        sum/=arrGroup.size();
        return Float.toString(sum);
    }
    //====================================================================
    //                         INNER FUNCTIONS
    //====================================================================

    //====================================================================
    //                         BUTTONS
    //====================================================================

    //----- SETCLICK
    public void setClickListener(View.OnClickListener callback) {
        this.mClickListener = callback;
    }

    public void setItemButtonListener(ItemAdapter.OnItemButtonItemClickListener itemButtonListener) {
        this.itemButtonListener = itemButtonListener;
    }
    public interface OnItemButtonItemClickListener {
        void onItemIsClick(View button, int position);
    }
}
