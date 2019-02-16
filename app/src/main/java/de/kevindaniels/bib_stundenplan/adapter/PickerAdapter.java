package de.kevindaniels.bib_stundenplan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.kevindaniels.bib_stundenplan.data.PickerItem;
import de.kevindaniels.bib_stundenplan.R;
import de.kevindaniels.bib_stundenplan.fragments.FragmentTimeTable;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.ViewHolder> {

    private ArrayList<PickerItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int mSelectedPos = RecyclerView.NO_POSITION;

    // data is passed into the constructor
    public PickerAdapter(Context context, ArrayList<PickerItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = mInflater.inflate(R.layout.layout_plan_picker, parent, false);
        return new ViewHolder(v);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        PickerItem item = mData.get(i);
        holder.dateMonth.setText(item.getDateMonth());
        holder.dateDay.setText(item.getDateDay());

        holder.dateMonth.setSelected(mSelectedPos == i);
        holder.dateDay.setSelected(mSelectedPos == i);
        holder.dateBG.setSelected(mSelectedPos == i);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateMonth;
        TextView dateDay;
        ConstraintLayout dateBG;

        public ViewHolder(View itemView) {
            super(itemView);

            dateMonth = (TextView) itemView.findViewById(R.id.date_item_month);
            dateDay = (TextView) itemView.findViewById(R.id.date_item_day);
            dateBG = (ConstraintLayout) itemView.findViewById(R.id.date_item_bg);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onPickerItemClick(view, getAdapterPosition());
            }

            notifyItemChanged(mSelectedPos);
            mSelectedPos = getLayoutPosition();
            notifyItemChanged(mSelectedPos);
        }
    }

    // convenience method for getting data at click position
    PickerItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onPickerItemClick(View view, int position);
    }

}
