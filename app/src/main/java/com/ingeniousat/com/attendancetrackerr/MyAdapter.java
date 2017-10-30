package com.ingeniousat.com.attendancetrackerr;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by TONMOYPC on 10/25/2017.
 */
public class MyAdapter extends BaseAdapter {

    ArrayList<Employee> emplist;
    Activity activity;

    public MyAdapter(ArrayList<Employee> emplist, Activity activity) {
        super();
        this.emplist = new ArrayList<>(emplist);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return emplist.size();
    }

    @Override
    public Object getItem(int position) {
        return emplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder {
        TextView mName;
        TextView mIntime;
        TextView mOutime;
        TextView mWorking;
        TextView mDate;
        TextView mRemarks;
        TextView mStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_user_item, null);
            holder = new ViewHolder();
            //holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mIntime = (TextView) convertView.findViewById(R.id.inTime);
            holder.mOutime = (TextView) convertView.findViewById(R.id.outTime);
            holder.mWorking = (TextView) convertView.findViewById(R.id.workingHour);
            holder.mDate = (TextView) convertView.findViewById(R.id.dateValue);
            holder.mRemarks = (TextView) convertView.findViewById(R.id.remarks);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Employee item = emplist.get(position);

        String valueget = item.getStatus().toString();
        //holder.mName.setText(item.getName().toString());
        holder.mIntime.setText(item.getInTime().toString());
        holder.mOutime.setText(item.getOutTime().toString());
        holder.mDate.setText(item.getDate().toString());
        if(valueget.equals("green")){
            holder.mStatus.setText("good");
        }
        else if(valueget.equals("yellow")){
            holder.mStatus.setText("moderate");
        }
        else if(valueget.equals("red")){
            holder.mStatus.setText("late");
        }
        else{
            holder.mStatus.setText("null");
        }
        holder.mRemarks.setText(item.getRemarks().toString());

        return convertView;
    }

}