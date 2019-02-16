package de.kevindaniels.bib_stundenplan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.kevindaniels.bib_stundenplan.R;
import de.kevindaniels.bib_stundenplan.data.TableDayItem;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private ArrayList<TableDayItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public TableAdapter(Context context, ArrayList<TableDayItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = mInflater.inflate(R.layout.layout_plan_table, parent, false);
        return new ViewHolder(v);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        TableDayItem item = mData.get(i);
        holder.subject1.setText(item.getPlanSubject1());
        holder.time1.setText(item.getPlanTime1());
        holder.subject2.setText(item.getPlanSubject2());
        holder.time2.setText(item.getPlanTime2());
        holder.subject3.setText(item.getPlanSubject3());
        holder.time3.setText(item.getPlanTime3());
        holder.subject4.setText(item.getPlanSubject4());
        holder.time4.setText(item.getPlanTime4());
        holder.subject5.setText(item.getPlanSubject5());
        holder.time5.setText(item.getPlanTime5());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subject1;
        TextView time1;
        TextView subject2;
        TextView time2;
        TextView subject3;
        TextView time3;
        TextView subject4;
        TextView time4;
        TextView subject5;
        TextView time5;

        public ViewHolder(View itemView) {
            super(itemView);

            subject1 = (TextView) itemView.findViewById(R.id.date_plan_r1_f1);
            time1 = (TextView) itemView.findViewById(R.id.date_plan_r1_f2);
            subject2 = (TextView) itemView.findViewById(R.id.date_plan_r2_f1);
            time2 = (TextView) itemView.findViewById(R.id.date_plan_r2_f2);
            subject3 = (TextView) itemView.findViewById(R.id.date_plan_r3_f1);
            time3 = (TextView) itemView.findViewById(R.id.date_plan_r3_f2);
            subject4 = (TextView) itemView.findViewById(R.id.date_plan_r4_f1);
            time4 = (TextView) itemView.findViewById(R.id.date_plan_r4_f2);
            subject5 = (TextView) itemView.findViewById(R.id.date_plan_r5_f1);
            time5 = (TextView) itemView.findViewById(R.id.date_plan_r5_f2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    TableDayItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
