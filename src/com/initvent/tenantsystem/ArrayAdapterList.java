package com.initvent.tenantsystem;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrayAdapterList extends ArrayAdapter<GetListView>{

	Context context;
	int layoutResourceId;
	ArrayList<GetListView> data=new ArrayList<GetListView>();
	
	public ArrayAdapterList(Context context, int layoutResourceId, ArrayList<GetListView> data) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub

		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TextHolder holder = null;
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TextHolder();
			holder.txtId = (TextView)row.findViewById(R.id.txtadpId);
			holder.txtHeading = (TextView)row.findViewById(R.id.txtadpHeading);
			holder.txtNote = (TextView)row.findViewById(R.id.txtadpBuildingid);
			holder.txtCreateDate = (TextView)row.findViewById(R.id.txtadpNote);
			holder.txtdoneDate = (TextView)row.findViewById(R.id.txtdoneDate);
			row.setTag(holder);
		}
		else
		{
			holder = (TextHolder)row.getTag();
		}
		
		GetListView getdata = data.get(position);
		holder.txtId.setText(""+getdata._id);
		holder.txtHeading.setText(""+getdata ._taskHeading);
		holder.txtNote.setText(""+getdata ._taskNote);
		holder.txtCreateDate.setText(""+getdata ._building_id);
		holder.txtdoneDate.setText(""+getdata ._doneDate);
		return row;
	}
	static class TextHolder
	{
		//ImageView imgIcon;
		//TextView txtTitle;
		TextView txtId;
		TextView txtHeading;
		TextView txtNote;
		TextView txtCreateDate;
		TextView txtdoneDate;
	}

}
