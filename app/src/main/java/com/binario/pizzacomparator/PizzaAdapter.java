package com.binario.pizzacomparator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binario.pizzacomparator.helper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//---------------------------------------------------------------------------------------------------------------------------------------------
//                                                          PIZZA ADAPTER
//---------------------------------------------------------------------------------------------------------------------------------------------
public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder> implements ItemTouchHelperAdapter
{
    private final static int MAX_GROUP = 10;
    private final static int MAX_ITEM_PIZZA = 8;

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
    private Context context;

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
        private TextView scoreTextView2;
        private TextView scoreTextView3;
        private Button expand_button;
        public Button delete_button;
        private ConstraintLayout item_group;
        private LinearLayout ig_layout_add_pizza;
        //pizzas list
        private RelativeLayout ig_rl_pizza_list;
        private RecyclerView recyclerItemView;

        private TextView ig_measure;    //cm-inch
        private TextView ig_measure2;    //cm-inch

        private TextView ig_currency_1;
        private TextView ig_currency_2;

        private TextView ig_text_pizza;
        private TextView ig_text_about;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //nameTextView = (TextView) itemView.findViewById(R.id.ig_position_number);
            scoreTextView = (TextView) itemView.findViewById(R.id.ig_score);
            scoreTextView2 = (TextView) itemView.findViewById(R.id.ig_score_2);
            scoreTextView3 = (TextView) itemView.findViewById(R.id.ig_score_3);
            expand_button = (Button) itemView.findViewById(R.id.ig_expand_button_pizza);
            //delete_button = (Button) itemView.findViewById(R.id.ig_button_delete);
            item_group = (ConstraintLayout) itemView.findViewById(R.id.ig_layout_main);
            //pizzas list
            ig_rl_pizza_list = (RelativeLayout) itemView.findViewById((R.id.ig_rl_pizza_list));
            recyclerItemView = (RecyclerView) itemView.findViewById(R.id.ig_recyclerView);
            ig_layout_add_pizza = (LinearLayout) itemView.findViewById(R.id.ig_layout_add_pizza);

            ig_measure = (TextView) itemView.findViewById(R.id.ig_measure);
            ig_measure2 = (TextView) itemView.findViewById(R.id.ig_measure2);
            ig_currency_1 = (TextView) itemView.findViewById(R.id.ig_currency_1);
            ig_currency_2 = (TextView) itemView.findViewById(R.id.ig_currency_2);
            ig_text_pizza = (TextView) itemView.findViewById(R.id.ig_text_pizza);
            ig_text_about = (TextView) itemView.findViewById(R.id.ig_text_about);
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
       // TextView textView = viewHolder.nameTextView;
        //textView.setText(contact.getStringItemNumber());

        TextView textView = viewHolder.scoreTextView;
        textView.setText(arrItems.get(position).getSumOfTotalArea());

        TextView textView2 = viewHolder.scoreTextView2;
        textView2.setText(arrItems.get(position).getSumOfPricePerOne());

        TextView textView3 = viewHolder.scoreTextView3;
        textView3.setText(arrItems.get(position).getSumOfTotalSum());

        //cm-inch
        TextView textView4 = viewHolder.ig_measure;
        TextView textView5 = viewHolder.ig_measure2;
        if(MainActivity.settings_units==1){
            String temp = " " + viewHolder.itemView.getResources().getString(R.string.sett_layout_1_button_1_text);
            textView4.setText(temp);
            textView5.setText(temp);
        }
        else{
            String temp = " " + viewHolder.itemView.getResources().getString(R.string.sett_layout_1_button_2_text);
            textView4.setText(temp);
            textView5.setText(temp);
        }
        TextView textView6 = viewHolder.ig_currency_1;
        textView6.setText(MainActivity.current_currency);
        TextView textView7 = viewHolder.ig_currency_2;
        textView7.setText(MainActivity.current_currency);

        if(arrItems.get(position).getItemCount()==0)
        {
            TextView textView8 = viewHolder.ig_text_pizza;
            String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_pizza_add_another);
            textView8.setText(temp);
        }
        else
        {
            TextView textView8 = viewHolder.ig_text_pizza;
            String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_pizza_add);
            textView8.setText(temp);
        }

        //best offer
        TextView textView9 = viewHolder.ig_text_about;
        if(isBestOfferPosition(position) && isAtLeastTwo())
        {
            if(isBiggestPosition(position) && isAtLeastTwoBestOffer())
            {
                viewHolder.itemView.setBackgroundResource(R.drawable.item_group_background_choose);
                String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_more_same);
                textView9.setText(temp);
                textView9.setTypeface(null, Typeface.BOLD);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
                textView2.setTypeface(null, Typeface.BOLD);
                textView2.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
            }
            else if(isAtLeastTwoBestOffer())
            {
                //when $ per area is EQUAL
                viewHolder.itemView.setBackgroundResource(R.drawable.item_group_background);
                String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_none);
                textView9.setText(temp);
                textView9.setTypeface(null, Typeface.NORMAL);
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setTextColor(viewHolder.itemView.getResources().getColor(R.color.colorGray));
                textView2.setTypeface(null, Typeface.BOLD);
                textView2.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
            }
            else
            {
                viewHolder.itemView.setBackgroundResource(R.drawable.item_group_background_choose);
                String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_best_offer);
                textView9.setText(temp);
                textView9.setTypeface(null, Typeface.BOLD);
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setTextColor(viewHolder.itemView.getResources().getColor(R.color.colorGray));
                textView2.setTypeface(null, Typeface.BOLD);
                textView2.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
                if(isBiggestPosition(position))
                {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
                }
            }
        }
        else
        {
            viewHolder.itemView.setBackgroundResource(R.drawable.item_group_background);
            if(isBiggestPosition(position) && isAtLeastTwo())
            {
                String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_more_expensive);
                textView9.setText(temp);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
                textView2.setTypeface(null, Typeface.NORMAL);
                textView2.setTextColor(viewHolder.itemView.getResources().getColor(R.color.colorGray));
            }
            else
            {
                String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_none);
                textView9.setText(temp);
                textView9.setTypeface(null, Typeface.NORMAL);
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setTextColor(viewHolder.itemView.getResources().getColor(R.color.colorGray));
                textView2.setTypeface(null, Typeface.NORMAL);
                textView2.setTextColor(viewHolder.itemView.getResources().getColor(R.color.colorGray));
            }
        }
        if(isSmallestPrice(position) && isAtLeastTwo())
        {
            //textView3.setTypeface(null, Typeface.BOLD);
            if(isBestOfferPosition(position))
            {
            String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_less_best);
            textView9.setText(temp);
            }
            else
            {
                String temp = viewHolder.itemView.getResources().getString(R.string.ig_text_about_less_cheaper);
                textView9.setText(temp);
            }

            textView3.setTextColor(viewHolder.itemView.getResources().getColor(android.R.color.holo_green_light));
        }
        else
        {
            //textView3.setTypeface(null, Typeface.NORMAL);
            textView3.setTextColor(viewHolder.itemView.getResources().getColor(R.color.colorGray));
        }

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
                popupEditItem(position,pos,v);
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
    //                         GET-SET
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
        public void refreshView()
        {
            this.notifyItemRangeChanged(0,arrGroup.size());
            getRecyclerView(0);
        }
        public void add()
        {
            arrGroup.add( new PizzaGroup(arrGroup.size()+1) );
            arrItems.add( new ItemAdapter());
            this.notifyItemInserted(arrGroup.size() - 1);
            this.notifyItemChanged(arrGroup.size() - 1);
            getRecyclerView(0);
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
            getRecyclerView(0);
        }
        public void addPizza(int position,int diameter, float price,int amount)
        {
            if (arrItems.get(position).Size() < MAX_ITEM_PIZZA)
            {
                arrItems.get(position).addPizza(diameter,price,amount);
                arrItems.get(position).notifyItemInserted(arrItems.get(position).Size() - 1);
                arrItems.get(position).notifyItemChanged(arrItems.get(position).Size() - 1);
                if(arrItems.get(position).Size()== MAX_ITEM_PIZZA)
                {
                    arrGroup.get(position).removeAddHeight();
                    arrGroup.get(position).setAddPizzaVisible(false);
                }
            }
            else
            {
                String temp = this.recyclerView.getResources().getString(R.string.toast_max_group_number);
                Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
            }

            arrGroup.get(position).expandItemHeight();
            Log.d("----------------------------TAG", "get.Height: (" + (  arrGroup.get(position).getItemHeight() ) + ")");
            this.notifyItemRangeChanged(0,arrGroup.size());
            getRecyclerView(0);
        }
        public void removePizza(int position, int posItemPizza)
        {
            if(arrItems.get(position).Size()==MAX_ITEM_PIZZA)
            {
                arrGroup.get(position).addAddHeight();
                arrGroup.get(position).setAddPizzaVisible(true);
            }
            arrItems.get(position).removePizza(posItemPizza);

            arrGroup.get(position).removeItemHeight();
            this.notifyItemRangeChanged(0,arrGroup.size());
            getRecyclerView(0);
        }
        public int setCurrentNumber(int position)
        {
            return position;
        }
        public int getCurrentNumber(PizzaGroup item)
        {
            return item.getItemNumber();
        }

    //====================================================================
    //                         RECYCLER VIEW FUNCTIONS
    //====================================================================

    public int getRecyclerHeight(int corect)
    {
        int temp_sum=0;
        for (int i = 0; i < arrGroup.size(); i++) {
            temp_sum += convert.dpToPx(arrGroup.get(i).getItemHeight());
            temp_sum += convert.dpToPx(corect);
        }

        //Log.d("----------------------------TAG", "temp_sum: (" + ( convert.pxToDp(temp_sum)) + ")");
        if(temp_sum<=0)
            return 1;
        return temp_sum-1;
    }
    public void setRecyclerView(RecyclerView rv, ConstraintLayout f)
    {
        recyclerView = rv;
        fragment = f;
        getRecyclerView(0);
        //Toast.makeText(this.recyclerView.getContext(), "setRecyclerView " + bottomHeightBar, Toast.LENGTH_SHORT).show();
    }
    public void setRecyclerFragmentHeight(int height)
    {
        heightFragment = height;
    }
    private void getRecyclerView(int corect)
    {
        bottomHeightBar=MainActivity.BOTTOM_BAR_HEIGHT;
        int bhpx = convert.dpToPx(bottomHeightBar);
        ViewGroup.LayoutParams recycler_height = recyclerView.getLayoutParams();
        recyclerView.setLayoutParams(recycler_height);
        recycler_height.height = this.getRecyclerHeight(corect);

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
    //                         OTHER
    //===================================================================
    ////
    private boolean isBestOfferPosition(int position)
    {
        int max = 0;
        for (int i = 0; i < arrGroup.size(); i++) {
            if(arrItems.get(i).getIntSumOfPricePerOne() > max)
            {
                max = arrItems.get(i).getIntSumOfPricePerOne();
            }
        };
        if(arrItems.get(position).getIntSumOfPricePerOne() == max)
            return true;
        else
            return false;
    }
    private boolean isAtLeastTwoBestOffer()
    {
        int sum = 0;
        int max = 0;
        for (int i = 0; i < arrGroup.size(); i++)
        {
            if(arrItems.get(i).getIntSumOfPricePerOne()>max)
            {
                max = arrItems.get(i).getIntSumOfPricePerOne();
            }
        }
        for (int i = 0; i < arrGroup.size(); i++)
        {
            if(arrItems.get(i).getIntSumOfPricePerOne() == max)
            {
                sum++;
                if(sum==2)
                    return true;
            }
        }
        return false;
    }
    private boolean isBiggestPosition(int position)
    {
        int max = 0;
        for (int i = 0; i < arrGroup.size(); i++) {
            if(arrItems.get(i).getIntSumOfTotalArea() > max)
            {
                max = arrItems.get(i).getIntSumOfTotalArea();
            }
        }
        if(arrItems.get(position).getIntSumOfTotalArea() == max)
            return true;
        else
            return false;
    }
    private boolean isSmallestPrice(int position)
    {
        float min = Float.MAX_VALUE;
        for (int i = 0; i < arrGroup.size(); i++) {
            if(arrItems.get(i).getIntSumOfTotalSum() < min)
            {
                min = arrItems.get(i).getIntSumOfTotalSum();
            }
        }
        if(arrItems.get(position).getIntSumOfTotalSum() == min)
            return true;
        else
            return false;
    }
    private boolean isAtLeastTwo()
    {
        int sum = 0;
        for (int i = 0; i < arrGroup.size(); i++)
        {
            if(arrItems.get(i).getItemCount()>0)
            {
                sum++;
                if(sum==2)
                    return true;
            }
        }
        return false;
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
        lp.width = fragment.getWidth();
        alertDialog.getWindow().setAttributes(lp);
        //alertDialog.getWindow().setLayout(fragment.getWidth(), MainActivity.converter.dpToPx(140));

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
                getRecyclerView(0);
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
                    getRecyclerView(0);
                    alertDialog.dismiss();
                    Log.d("----------------------------TAG", "true: (" + (0) + ")");
                }
                return true;
            }
        });
    }
    public void popupEditItem(final int position, final int item_pos, final View view)
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
        lp.x = location[0]-convert.dpToPx(16);
        lp.y = location[1]-convert.dpToPx(34);

        Log.d("----------------------------TAG", " locatio(0): (" + ( location[0] ) + ")" + " locatio(1): (" + ( location[1] ) + ")");
        lp.dimAmount = 0.0f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //alertDialog.getWindow().setLayout(fragment.getWidth(),400);

        Button pop_ip_op_delete = (Button) dialogView.findViewById(R.id.pop_ip_op_delete);
        Button pop_ip_op_edit = (Button) dialogView.findViewById(R.id.pop_ip_op_edit);
        Button pop_ip_op_button_item = (Button) dialogView.findViewById(R.id.pop_ip_op_button_item);

        isClickable = false;

        pop_ip_op_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("----------------------------TAG", "pop_ip_op_delete: (" + (item_pos) + ")");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removePizza(position,item_pos);
                        alertDialog.dismiss();
                    }
                }, 120);
                isClickable = true;
            }
        });

        pop_ip_op_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("----------------------------TAG", "pop_ip_op_delete: (" + (item_pos) + ")");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int d = arrItems.get(position).getItem(item_pos).getDiameter();
                        float p = arrItems.get(position).getItem(item_pos).getPrice();
                        int a = arrItems.get(position).getItem(item_pos).getAmount();

                        popupEditValues(view,position,item_pos,d,p,a);
                        alertDialog.dismiss();
                    }
                }, 120);
                isClickable = true;
            }
        });

        pop_ip_op_button_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("----------------------------TAG", "pop_ip_op_delete: (" + (item_pos) + ")");
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
    public void popupAddValues(View view, final int pos)
    {
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_add_values, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //alertDialog.setContentView(view);
        Button add_pizza_button = (Button) dialogView.findViewById(R.id.av_add_button);
        TextView pop_add_units = (TextView) dialogView.findViewById(R.id.pop_add_units);
        if(MainActivity.settings_units==1){
            String temp = "["+dialogView.getResources().getString(R.string.sett_layout_1_button_1_text)+"]";
            pop_add_units.setText(temp);
        }
        else{
            String temp = "["+dialogView.getResources().getString(R.string.sett_layout_1_button_2_text)+"]";
            pop_add_units.setText(temp);
        }
        String temp = MainActivity.current_currency;
        TextView pop_add_currency = (TextView) dialogView.findViewById(R.id.pop_add_currency);
        pop_add_currency.setText(temp);
        EditText av_editText_price = (EditText) dialogView.findViewById((R.id.av_editText_price));
        av_editText_price.setHint(temp);

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          ADD PIZZA BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------

        add_pizza_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText av_editText_diameter = (EditText) dialogView.findViewById((R.id.av_editText_diameter));
                EditText av_editText_price = (EditText) dialogView.findViewById((R.id.av_editText_price));
                EditText av_editText_amount = (EditText) dialogView.findViewById((R.id.av_editText_amount));
                int diameter = 0;
                float price = 0f;
                int amount = 1;
                String string_temp_1 = av_editText_diameter.getText().toString();
                if(!string_temp_1.isEmpty()){
                    diameter = Integer.parseInt(string_temp_1);
                }
                String string_temp_2 = av_editText_price.getText().toString();
                if(!string_temp_2.isEmpty()){
                    price = Float.parseFloat(string_temp_2);
                }
                String string_temp_3 = av_editText_amount.getText().toString();
                if(!string_temp_3.isEmpty()){
                    amount = Integer.parseInt(string_temp_3);
                }
                if( diameter != 0f && price != 0f && amount != 0)
                {
                    addPizza(pos,diameter,price,amount);
                    alertDialog.cancel();
                }
                else
                {
                    String temp = v.getResources().getString(R.string.toast_UNABLE_TO_ADD_PIZZA_TEXT);
                    Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void popupEditValues(View view, final int position, final int item_pos, final int di, final float pr, final int am)
    {
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_add_values, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //alertDialog.setContentView(view);
        Button edit_pizza_button = (Button) dialogView.findViewById(R.id.av_add_button);
        String string = view.getResources().getString(R.string.pop_add_button_text_edit);
        edit_pizza_button.setText(string);
        TextView title = (TextView)dialogView.findViewById(R.id.pop_add_text_add);
        string = view.getResources().getString(R.string.pop_add_title_edit);
        title.setText(string);

        final EditText av_editText_diameter = (EditText) dialogView.findViewById((R.id.av_editText_diameter));
        final EditText av_editText_price = (EditText) dialogView.findViewById((R.id.av_editText_price));
        final EditText av_editText_amount = (EditText) dialogView.findViewById((R.id.av_editText_amount));
        String temp = Integer.toString(di);
        av_editText_diameter.setHint(temp);
        temp = Float.toString(pr);
        av_editText_price.setHint(temp);
        temp = Integer.toString(am);
        av_editText_amount.setHint(temp);

        TextView pop_add_units = (TextView) dialogView.findViewById(R.id.pop_add_units);
        if(MainActivity.settings_units==1){
            temp = "["+dialogView.getResources().getString(R.string.sett_layout_1_button_1_text)+"]";
            pop_add_units.setText(temp);
        }
        else{
            temp = "["+dialogView.getResources().getString(R.string.sett_layout_1_button_2_text)+"]";
            pop_add_units.setText(temp);
        }
        TextView pop_add_currency = (TextView) dialogView.findViewById(R.id.pop_add_currency);
        pop_add_currency.setText(MainActivity.current_currency);
        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          ADD PIZZA BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------

        edit_pizza_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int diameter = di;
                float price = pr;
                int amount = am;

                String string_temp_1 = av_editText_diameter.getText().toString();
                if(!string_temp_1.isEmpty()){
                    diameter = Integer.parseInt(string_temp_1);
                }
                String string_temp_2 = av_editText_price.getText().toString();
                if(!string_temp_2.isEmpty()){
                    price = Float.parseFloat(string_temp_2);
                }
                String string_temp_3 = av_editText_amount.getText().toString();
                if(!string_temp_3.isEmpty()){
                    amount = Integer.parseInt(string_temp_3);
                }
                if( diameter != 0 && price != 0f && amount != 0)
                {
                    arrItems.get(position).getItem(item_pos).editValues(diameter,price,amount);
                    refreshView();
                    //Toast.makeText(ThirdFragment.this.getContext(), "add pizza to pos: " + pos, Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
                else
                {
                    String temp = v.getResources().getString(R.string.toast_UNABLE_TO_ADD_PIZZA_TEXT);
                    Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
