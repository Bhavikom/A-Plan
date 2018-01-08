/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.smacsoftwares.aplanapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.TableDataModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.helper.ItemTouchHelperAdapter;
import de.smacsoftwares.aplanapp.helper.ItemTouchHelperViewHolder;
import de.smacsoftwares.aplanapp.helper.OnStartDragListener;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {
    //private final List<String> mItems = new ArrayList<>();
    ArrayList<TableDataModelClass> arraylistMain = new ArrayList<>();
    String[] etValArr;
    Context con;
    private final OnStartDragListener mDragStartListener;
    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener,
                               ArrayList<TableDataModelClass> _arraylist) {
        mDragStartListener = dragStartListener;
        arraylistMain =_arraylist;
        etValArr = new String[arraylistMain.size()];
        this.con = context;
       // mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_table, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view,new CustomEtListener());
        return itemViewHolder;
    }
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position)
    {
        holder.textView.setText(arraylistMain.get(position).name);
        // Update the position when focus changes
        holder.myCustomEditTextListener.updatePosition(position,holder);
        // Setting the values accordingly
       // holder.edittextWidth.setText(etValArr[position]);

        holder.edittextWidth.setText(arraylistMain.get(position).width+"");
        // Start a drag whenever the handle view it touched
        holder.imgDragable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        if(GlobalClass.isEditMode){
            holder.edittextWidth.setEnabled(true);
            holder.checkBox.setEnabled(true);
            holder.imgDragable.setClickable(true);
        }
        else {
            holder.edittextWidth.setEnabled(false);
            holder.checkBox.setEnabled(false);
            holder.imgDragable.setClickable(false);
        }

        if (position % 2 == 0) {

            holder.linearLayout.setBackgroundColor(con.getResources().getColor(R.color.select_filter));

        } else {

            holder.linearLayout.setBackgroundColor(con.getResources().getColor(R.color.white_color));

        }
        /*if(arraylistMain.get(position).name.equalsIgnoreCase("") || arraylistMain.get(position).name == null){
            holder.linearLayout.setVisibility(View.GONE);
        }*/
        GlobalClass.setupTypeface(holder.linearLayout, GlobalClass.fontLight);

        final TableDataModelClass tableData = arraylistMain.get(position);
        holder.ref = position;
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setChecked(arraylistMain.get(position).selected);
        holder.checkBox.setTag(position);
        holder.checkBox.setOnCheckedChangeListener(myCheckChangList);

        /*holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                TableDataModelClass contact = (TableDataModelClass) cb.getTag();
                contact.setSelected(cb.isChecked());
                getSelectedData();
                //stList.get(pos).setSelected(cb.isChecked());

            }
        });*/
    }
    @Override
    public void onItemDismiss(int position) {
        arraylistMain.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arraylistMain, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    @Override
    public int getItemCount() {
        return arraylistMain.size();
    }
    @Override
    public long getItemId(int position)
    {
        return super.getItemId(position);
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder,GlobalClass.TextCallBackListener {
        public final TextView textView;
        public final EditText edittextWidth;
        LinearLayout linearLayout;
        ImageView imgDragable;
        CheckBox checkBox;
        // Instance of a Custom edit text listener
        public CustomEtListener myCustomEditTextListener;
        int ref;
            public ItemViewHolder(View itemView,CustomEtListener myLis) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textviewtableitem);
                edittextWidth = (EditText) itemView.findViewById(R.id.edittexttableitem);
                linearLayout=(LinearLayout)itemView.findViewById(R.id.linearitem);
                GlobalClass.setupTypeface(linearLayout, GlobalClass.fontRegular);
                imgDragable=(ImageView) itemView.findViewById(R.id.img_dragable);
                checkBox = (CheckBox) itemView.findViewById(R.id.switchnumber);
                myCustomEditTextListener = myLis;
                edittextWidth.addTextChangedListener(myCustomEditTextListener);
            }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
        @Override
        public void updateText(String val) {
        }
    }
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener()
    {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
        {
            getTableData((Integer) buttonView.getTag()).selected = isChecked;
            getSelectedData();
        }
    };
    public TableDataModelClass getTableData(int position)
    {
        return arraylistMain.get(position);
    }
    public ArrayList<TableDataModelClass> getSelectedData()
    {
        ArrayList<TableDataModelClass> arraylistSelected = new ArrayList<TableDataModelClass>();
        for (TableDataModelClass tableData : arraylistMain)
        {
            //if (tableData.selected)
                arraylistSelected.add(tableData);
        }
        return arraylistSelected;
    }
    private class CustomEtListener implements TextWatcher {
        private int position;
        GlobalClass.TextCallBackListener textCallBackListener;
        /**
         * Updates the position according to onBindViewHolder
         *
         * @param position - position of the focused item
         */
        public void updatePosition(int position,ItemViewHolder pa) {
            this.position = position;
            textCallBackListener = (GlobalClass.TextCallBackListener) pa;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // Change the value of array according to the position
            etValArr[position] = charSequence.toString();
            arraylistMain.get(position).width=charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

}

