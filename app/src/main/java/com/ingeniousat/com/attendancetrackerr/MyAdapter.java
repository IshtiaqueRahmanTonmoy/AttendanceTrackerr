package com.ingeniousat.com.attendancetrackerr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TONMOYPC on 10/25/2017.
 */
public class MyAdapter extends ArrayAdapter<Employee> {

    ArrayList<Employee> emplist;
    Activity activity;
    Context context;

    public MyAdapter(Context context, int resource, ArrayList<Employee> emplist) {
        super(context, resource,emplist);
        this.context = context;
        this.emplist = emplist;
    }

    @Override
    public int getCount() {
        return emplist.size();
    }

    @Override
    public Employee getItem(int position) {
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
        TextView mLocation;
        TextView mRemarks;
        TextView mStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_user_item, null);
            holder = new ViewHolder();
            //holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mIntime = (TextView) convertView.findViewById(R.id.inTime);
            holder.mOutime = (TextView) convertView.findViewById(R.id.outTime);
            holder.mWorking = (TextView) convertView.findViewById(R.id.workingHour);
            holder.mDate = (TextView) convertView.findViewById(R.id.dateValue);
            holder.mLocation = (TextView) convertView.findViewById(R.id.locationValue);
            holder.mRemarks = (TextView) convertView.findViewById(R.id.remarks);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Employee item = emplist.get(position);

        //holder.mName.setText(item.getName().toString());
        holder.mIntime.setText(item.getInTime().toString());
        holder.mOutime.setText(item.getOutTime().toString());
        holder.mWorking.setText(item.getTotaltime().toString());
        holder.mDate.setText(item.getDate().toString());
        holder.mLocation.setText(item.getLocation().toString());

        if(item.getStatus().toString().equals("good")){
            holder.mStatus.setText(item.getStatus().toString());
            holder.mStatus.setTextColor(Color.parseColor("#008000"));
        }
        else if(item.getStatus().toString().equals("moderate")){
            holder.mStatus.setText(item.getStatus().toString());
            holder.mStatus.setTextColor(Color.parseColor("#FFFF00"));
        }
        else if(item.getStatus().toString().equals("poor")){
            holder.mStatus.setText(item.getStatus().toString());
            holder.mStatus.setTextColor(Color.parseColor("#FF0000"));
        }
        //holder.mStatus.setText(item.getStatus().toString());
        holder.mRemarks.setText(item.getRemarks().toString());

        return convertView;
    }

}