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

import de.kevindaniels.bib_stundenplan.data.ExamsItem;
import de.kevindaniels.bib_stundenplan.R;
import de.kevindaniels.bib_stundenplan.fragments.FragmentTimeTable;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsAdapter.ViewHolder> {

    private ArrayList<ExamsItem> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public ExamsAdapter(Context context, ArrayList<ExamsItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = mInflater.inflate(R.layout.layout_exams_item, parent, false);
        return new ViewHolder(v);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        ExamsItem item = mData.get(i);
        holder.examsTopic.setText(item.getExamsTopic());
        holder.examsTime.setText(item.getExamsTime());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView examsTopic;
        TextView examsTime;

        public ViewHolder(View itemView) {
            super(itemView);

            examsTopic = (TextView) itemView.findViewById(R.id.exams_plan);
            examsTime = (TextView) itemView.findViewById(R.id.exams_time);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
