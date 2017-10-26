package com.ingeniousat.com.attendancetrackerr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by TONMOYPC on 10/25/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Employee> emplist;
    WeakReference<Context> mContextWeakReference;
    Context context;

    public MyAdapter(ArrayList<Employee> emplist, Context context) {
        this.emplist = new ArrayList<>(emplist);
        this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = mContextWeakReference.get();

        if (context != null) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_item, parent, false);

            return new MyViewHolder(itemView, context);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Context context = mContextWeakReference.get();

        if (context == null) {
            return;
        }

        Employee employee = emplist.get(position);

        //Toast.makeText(context, ""+employee.getDate(), Toast.LENGTH_SHORT).show();
        holder.TvName.setText(employee.getName());
        holder.TvInTime.setText(employee.getInTime());
        holder.TvOutTime.setText(employee.getOutTime());
        holder.TvRemarks.setText(employee.getRemarks());
        holder.TvDate.setText(employee.getDate());
    }

    @Override
    public int getItemCount() {
        return emplist.size();
    }

    //holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView TvName,TvInTime, TvOutTime, TvRemarks, TvDate;
        public LinearLayout ll;

        public MyViewHolder(View itemView, final Context context) {

            super(itemView);
            TvName = (TextView) itemView.findViewById(R.id.name);
            TvInTime = (TextView) itemView.findViewById(R.id.inTime);
            TvOutTime = (TextView) itemView.findViewById(R.id.outTime);
            TvRemarks = (TextView) itemView.findViewById(R.id.remarks);
            TvDate = (TextView) itemView.findViewById(R.id.dateValue);
        }
    }
}