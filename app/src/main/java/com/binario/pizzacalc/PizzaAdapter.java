package com.binario.pizzacalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binario.pizzacalc.helper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//---------------------------------------------------------------------------------------------------------------------------------------------
//                                                          PIZZA ADAPTER
//---------------------------------------------------------------------------------------------------------------------------------------------
public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder> implements ItemTouchHelperAdapter
{
    private OnExpandButtonItemClickListener expandButtonListener;
    private OnDeleteButtonItemClickListener deleteButtonListener;
    // Store a member variable for the contacts
    private List<PizzaGroup> arrGroup;
    private List<ItemAdapter> arrItems;
    public boolean isClickable = true;
    private boolean isAddPizzaVisible = true;
    private ConvertPxDp convert;

    private RecyclerView recyclerView;
    private ConstraintLayout fragment;
    private int heightFragment;
    private int bottomHeightBar;

    Context context;
    //----------------------------------------------------------------------------------------------------------------
    //                                                  CONSTRUCTOR
    //----------------------------------------------------------------------------------------------------------------
    // Pass in the contact array into the constructor
    public PizzaAdapter(int numGroup)
    {
        arrGroup = new ArrayList<PizzaGroup>();
        for (int i = 1; i <= numGroup; i++) {
            arrGroup.add(new PizzaGroup(i));
        }
        //this is necessary to stop 'blinking' items in list
        this.setHasStableIds(false);
        convert = new ConvertPxDp();

        arrItems = new ArrayList<ItemAdapter>();
        for (int i = 1; i <= numGroup; i++) {
            arrItems.add(new ItemAdapter());
        }
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
        private TextView nameTextView;
        private TextView scoreTextView;
        private Button expand_button;
        public Button delete_button;
        private ConstraintLayout item_group;
        private LinearLayout ig_layout_add_pizza;
        //pizzas list
        private RelativeLayout ig_rl_pizza_list;
        private RecyclerView recyclerItemView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.ig_position_number);
            scoreTextView = (TextView) itemView.findViewById(R.id.ig_score);
            expand_button = (Button) itemView.findViewById(R.id.ig_expand_button_pizza);
            //delete_button = (Button) itemView.findViewById(R.id.ig_button_delete);
            item_group = (ConstraintLayout) itemView.findViewById(R.id.ig_layout_main);
            //pizzas list
            ig_rl_pizza_list = (RelativeLayout) itemView.findViewById((R.id.ig_rl_pizza_list));
            recyclerItemView = (RecyclerView) itemView.findViewById(R.id.ig_recyclerView);
            ig_layout_add_pizza = (LinearLayout) itemView.findViewById(R.id.ig_layout_add_pizza);
        }

        public Button getExpandButton() {
            return expand_button;
        }
        public Button getDeleteButton() {
            return delete_button;
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          onCreate VIEW HOLDER
    //---------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_group, parent, false);    //instead parent, false -> null

        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(view);
        heightFragment = fragment.getHeight();
        //Log.d("----------------------------TAG", "fragment.getHeight(1): (" + (fragment.getHeight()) + ")");
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
        PizzaGroup contact = this.arrGroup.get(position);
        //Log.d("----------------------------TAG", "contact pos:"+ "(" + (position) + ")");

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getStringItemNumber());

        TextView textView2 = viewHolder.scoreTextView;
        textView2.setText(arrItems.get(position).getSumOfPricePerPiece());


        //Set item height
        ConstraintLayout itemView = viewHolder.item_group;
        //arrGroup.get(position).setItemHeight(itemView.getHeight());
        ViewGroup.LayoutParams itemViewH = itemView.getLayoutParams();
        int itemH = convert.dpToPx(arrGroup.get(position).getItemHeight());
        itemViewH.height = itemH;
        //Log.d("----------------------------TAG", "HEIGHT(" + (position) + ")" + " " + "HEIGHT: " + ( itemH ));

        //ViewGroup.LayoutParams recycler_height = viewHolder.recyclerItemView.getLayoutParams();
       // viewHolder.recyclerItemView.setLayoutParams(recycler_height);
        RecyclerView rv = viewHolder.recyclerItemView;
        //ViewGroup.LayoutParams recycler_height=rv.getLayoutParams();
        //recycler_height.height=160;
        //rv.setLayoutParams(recycler_height);

        context = viewHolder.itemView.getContext();
        rv.setLayoutManager(new LinearLayoutManager(context ));
        rv.setAdapter(arrItems.get(position));

        ViewGroup.LayoutParams ig_rl_pizza_list_height = viewHolder.ig_rl_pizza_list.getLayoutParams();
        ig_rl_pizza_list_height.height = convert.dpToPx(arrGroup.get(position).getPizzaListHeight());
        viewHolder.ig_rl_pizza_list.setLayoutParams(ig_rl_pizza_list_height);
/*
        viewHolder.arrayAdapter = new ArrayAdapter<Pizza>(viewHolder.itemView.getContext(), R.layout.item_pizza, R.id.ip_show_diam , arrGroup.get(position).arrPizzas);
        viewHolder.ig_pizza_list.setAdapter(viewHolder.arrayAdapter);

        ViewGroup.LayoutParams ig_rl_pizza_list_height = viewHolder.ig_rl_pizza_list.getLayoutParams();
        ig_rl_pizza_list_height.height = convert.dpToPx(arrGroup.get(position).getPizzaListHeight());
        viewHolder.ig_rl_pizza_list.setLayoutParams(ig_rl_pizza_list_height);
*/

        if(arrGroup.get(position).isAddPizzaVisible())   viewHolder.ig_layout_add_pizza.setVisibility(LinearLayout.VISIBLE);
        else                                             viewHolder.ig_layout_add_pizza.setVisibility(LinearLayout.GONE);


        if (expandButtonListener!= null) {
            viewHolder.getExpandButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandButtonListener.onExpandIsClick(v, position);
                }
            });
        }

        if (deleteButtonListener!= null) {
            viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isClickable){
                        deleteButtonListener.onDeleteIsClick(v, position);
                    }

                }
            });
        }
        //click on item - unused
        arrItems.get(position).setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int pos = adapter.getCurrentNumber();
                //Log.d("----------------------------TAG", "Click on item arrItems: (" + (position) + ")");
            }
        });

        arrItems.get(position).setItemButtonListener(new ItemAdapter.OnItemButtonItemClickListener() {
            @Override
            public void onItemIsClick(View v,int pos) {
                Log.d("----------------------------TAG", "Click on item: (" + (pos) + ")");
                popupEditItem(pos,v);
            }
        });

    }
    //hiding this code - fix bug with recycler scroll
        /*
            {
            @Override
            public long getItemId(int position) {
                //return position;
                long temp_uniqueID = arrGroup.get(position).getUniqueID();
                //long temp_uniqueID = arrGroup.get(position).itemNumber-1;
                Log.d("----------------------------TAG", "ID: (" + (temp_uniqueID) + ")");
                return (temp_uniqueID);
            }
            @Override
            public int getItemViewType(int position) {
                int temp_position = arrGroup.get(position).getItemNumber()-1;
                Log.d("----------------------------TAG", "TYPE: (" + (temp_position) + ")");
                return (temp_position);
            }
        }
        */

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
        public void add()
        {
            arrGroup.add( new PizzaGroup(arrGroup.size()+1) );
            arrItems.add( new ItemAdapter());
            this.notifyItemInserted(arrGroup.size() - 1);
            this.notifyItemChanged(arrGroup.size() - 1);
            getRecyclerView();
        }
        public void remove(int position)
        {
            //we have to re-count all items indexes
            arrGroup.remove(position);
            arrItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(0,arrGroup.size());

            //Log.d("----------------------------TAG", "POS: (" + (position) + ")");
            Iterator<PizzaGroup> iter = arrGroup.iterator();
            int i =0;
            while (iter.hasNext()) {
                iter.next().setItemNumber(i+1);

                i++;
            }
            getRecyclerView();
        }
        public void addPizza(int position,int diameter, float price,int amount)
        {
            if (arrItems.get(position).Size() < 3) //10
            {
                arrItems.get(position).addPizza(diameter,price,amount);
                arrItems.get(position).notifyItemInserted(arrItems.get(position).Size() - 1);
                arrItems.get(position).notifyItemChanged(arrItems.get(position).Size() - 1);
                Log.d("----------------------------TAG", "SIZE: (" + (arrItems.get(position).Size()) + ")");
                if(arrItems.get(position).Size()==3)
                {
                    arrGroup.get(position).removeAddHeight();
                    arrGroup.get(position).setAddPizzaVisible(false);
                }
            }
            else
            {
                Toast.makeText(context, "MAX_PIZZA_ITEMS", Toast.LENGTH_SHORT).show();
            }

            arrGroup.get(position).expandItemHeight();
            //arrGroup.get(position).addPizza(diameter,price);
            //this.notifyItemInserted(arrGroup.size() - 1);
            //this.notifyItemChanged(arrGroup.size() - 1);
            this.notifyItemRangeChanged(0,arrGroup.size());
            getRecyclerView();
        }
        public void removePizza(int position)
        {

        }
        public int setCurrentNumber(int position)
        {
            return position;
        }
        public int getCurrentNumber(PizzaGroup item)
        {
            return item.getItemNumber();
        }

        public int getItemHeigth(int position)
        {
            return arrGroup.get(position).RIGID_ITEM_HEIGHT;
        }

    //====================================================================
    //                         RECYCLER VIEW FUNCTIONS
    //====================================================================

    public int getRecyclerHeight()
    {
        int temp_sum=0;
        for (int i = 0; i < arrGroup.size(); i++) {
            temp_sum += convert.dpToPx(arrGroup.get(i).getItemHeight());
        }
        //Log.d("----------------------------TAG", "temp_sum: (" + ( convert.pxToDp(temp_sum)) + ")");
        if(temp_sum<=0)
            return 1;
        return temp_sum-1;
    }
    public void setRecyclerView(RecyclerView rv, int bh, ConstraintLayout f)
    {
        recyclerView = rv;
        bottomHeightBar = bh;
        fragment = f;


    }
    private void getRecyclerView()
    {

        int bhpx = convert.dpToPx(bottomHeightBar);
        //heightFragment = fragment.getHeight();
        ViewGroup.LayoutParams recycler_height = recyclerView.getLayoutParams();
        recyclerView.setLayoutParams(recycler_height);
        recycler_height.height = this.getRecyclerHeight();
        Log.d("----------------------------TAG", "recycler_height1: (" + (recycler_height.height) + ")");
        //Log.d("----------------------------TAG", "hf: (" + (heightFragment) + ")");
        if (recycler_height.height >= heightFragment - bhpx) {
            recycler_height.height = heightFragment - bhpx;
        }
        Log.d("----------------------------TAG", "heightFragment - bh: (" + (heightFragment)+" " + ( bhpx) + ")");
        Log.d("----------------------------TAG", "recycler_height2: (" + (recycler_height.height) + ")");
        recyclerView.setLayoutParams(recycler_height);

    }

    //====================================================================
    //                         BUTTONS
    //====================================================================


    public void setExpandButtonListener(OnExpandButtonItemClickListener expandButtonListener) {
        this.expandButtonListener = expandButtonListener;
    }
    public void setDeleteButtonListener(OnDeleteButtonItemClickListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
    }

    public interface OnExpandButtonItemClickListener {
        void onExpandIsClick(View button, int position);
    }
    public interface OnDeleteButtonItemClickListener {
        void onDeleteIsClick(View button, int position);
    }

    //====================================================================
    //                         TOUCH FUNCTIONS
    //====================================================================
    @Override
    public void onItemDismiss(int position) {
        popupYesNo(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(arrGroup, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(arrGroup, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    //====================================================================
    //                         POPUPS
    //====================================================================
    public void popupYesNo(final int pos)
    {
        ViewGroup viewGroup = this.recyclerView.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_yes_no, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.getWindow().setLayout(fragment.getWidth(),400);

        Button pop_yn_yes_button = (Button) dialogView.findViewById(R.id.pop_yn_yes_button);
        Button pop_yn_no_button = (Button) dialogView.findViewById(R.id.pop_yn_no_button);
        isClickable = false;
        pop_yn_yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(pos);
                alertDialog.cancel();
                isClickable = true;
                }
        });
        pop_yn_no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(pos);
                notifyItemRangeChanged(0,arrGroup.size());
                notifyDataSetChanged();
                getRecyclerView();
                alertDialog.cancel();
                isClickable = true;
            }
        });
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    notifyItemChanged(pos);
                    notifyItemRangeChanged(0,arrGroup.size());
                    notifyDataSetChanged();
                    getRecyclerView();
                    alertDialog.dismiss();
                    Log.d("----------------------------TAG", "true: (" + (0) + ")");
                }
                return true;
            }
        });
    }
    public void popupEditItem(final int item_pos, View view)
    {
        //int item_pos = arrItems.get(pos).
        ViewGroup viewGroup = this.recyclerView.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.popup_item_pizza_options, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyThemeOverlayAlertDialog_itemPizzaOption);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);


        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();

        //----------------------------------------------------
        //  anchor section
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        int[] location = new int[2];
        view.getLocationInWindow(location);
        lp.x = location[0]-convert.dpToPx(15);
        lp.y = location[1]-convert.dpToPx(33);

        Log.d("----------------------------TAG", " locatio(0): (" + ( location[0] ) + ")" + " locatio(1): (" + ( location[1] ) + ")");
        lp.dimAmount = 0.0f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //alertDialog.getWindow().setLayout(fragment.getWidth(),400);

        Button pop_ip_op_edit = (Button) dialogView.findViewById(R.id.pop_ip_op_edit);
        Button pop_ip_op_delete = (Button) dialogView.findViewById(R.id.pop_ip_op_delete);
        Button pop_ip_op_button_item = (Button) dialogView.findViewById(R.id.pop_ip_op_button_item);
        isClickable = false;
        pop_ip_op_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----------------------------TAG", "pop_ip_op_edit: (" + (item_pos) + ")");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 120);
                isClickable = true;
            }
        });
        pop_ip_op_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----------------------------TAG", "pop_ip_op_delete: (" + (item_pos) + ")");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 120);
                isClickable = true;
            }
        });
        pop_ip_op_button_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----------------------------TAG", "pop_ip_op_delete: (" + (item_pos) + ")");
                        alertDialog.dismiss();
                isClickable = true;
            }
        });
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.d("----------------------------TAG", "pop_ip_op_return: (" + (item_pos) + ")");
                            alertDialog.dismiss();
                }
                return true;
            }
        });
    }

}
