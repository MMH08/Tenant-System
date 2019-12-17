package com.initvent.tenantsystem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends ActionBarActivity implements OnClickListener{

	Button btnNewTask, btnNewInfo,btnSettings,btnMaster;
	ListView lvTask,lvInfo;
	TextView txt1;
	// Session Manager Class
	SessionManager session;
	public static int flagforlogincall=1;
	
	ArrayList<GetListView> arrList = new ArrayList<GetListView>();
	ArrayAdapterList adapter;

	ArrayList<GetListView> arrListinfo = new ArrayList<GetListView>();
	ArrayAdapterList adapterinfo;
	
	private ArrayAdapter<String> arradapter;
	List<String> osList=new ArrayList<String>(); 
	String spnSelcteditem="";
	static String building_id;
	static int datapopFlag=1;
	
	TextView chng_taskID;
	
	 String server;
	 String service;
	 String userid;
	 String pass;
	 String encode_pass;

	 ContentValues cv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnNewTask=(Button)findViewById(R.id.btnNewTask);
		btnSettings=(Button)findViewById(R.id.btnSettings);
		
		lvTask=(ListView)findViewById(R.id.lvTask);
		lvInfo=(ListView)findViewById(R.id.lvInfo);
		
		txt1=(TextView)findViewById(R.id.textView1);
		// Session class instance
        session = new SessionManager(getApplicationContext());

        //if(flagforlogincall==1)
       // {
	        //session.checkLogin();   //Every time force login
	        //flagforlogincall++;
        //}
		btnSettings.setVisibility(View.INVISIBLE);

		btnNewTask.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		
		lvTask.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				chng_taskID = (TextView) view.findViewById(R.id.txtadpId);

			    String memberID_val = chng_taskID.getText().toString();

			    Cursor cursor_Task = SplashActivity.mydb.rawQuery("SELECT * FROM  Task where id='"+memberID_val+"' and (taskEndtimestamp='' or taskEndtimestamp is null or taskEndtimestamp='null')",null);

			    if(cursor_Task.moveToNext())
			    {
					Intent modify_intent = new Intent(getApplicationContext(),TaskUpdateActivity.class);
					
					modify_intent.putExtra("chng_taskID", memberID_val);
					
					startActivity(modify_intent);			    	
			    }
			    else
			    {
			    	Toast.makeText(getBaseContext(),"Finished task should not update",Toast.LENGTH_LONG).show();
			    }
			}
		});	
		
		Cursor cursor_login = SplashActivity.mydb.rawQuery("SELECT * FROM  Login ",null);
		if (cursor_login != null) {
			cursor_login.moveToLast();
			try{
			if(isConnected()){
			cursor_login.moveToLast();
			 server=cursor_login.getString(1);
	   		 service=cursor_login.getString(2);
	   		 userid=cursor_login.getString(3);
	   		 pass=cursor_login.getString(4);
	   		 pass=Uri.encode(pass);
	   		 
	         String task_url = "http://"+server+"/"+service+"/TMSService.svc/TaskListForAndroid?user_Id="+ userid +"&user_Pass="+ pass;	
	         new Tasks().execute(task_url);

	         String info_url = "http://"+server+"/"+service+"/TMSService.svc/InfoForAndroid?user_Id="+ userid +"&user_Pass="+ pass;				          
	         new Building_Info().execute(info_url);

			}
			}
			catch(Exception e){
				
				
			}
			cursor_login.close();
		}
//		populateTasklist();   //
//		populateInfolist(); 		

	}
	// Menu Activities

	private boolean isConnected() {
		// TODO Auto-generated method stub
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
            return true;
        else
            return false;   
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:			
			Intent settingsIntent = new Intent(this, LoginActivity.class);
			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(settingsIntent);
			return true;			
		default:
			return super.onOptionsItemSelected(item);
       }

	}	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btnNewTask:
				Intent taskIntent = new Intent(this, TaskActivity.class);
				taskIntent.putExtra("chng_taskID", "");
				taskIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(taskIntent);
				break;
			case R.id.btnSettings:
				Intent settingsIntent = new Intent(this, LoginActivity.class);
				settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(settingsIntent);
				break;
			default:
				break;
		
		}
	}
	private void populateTasklist() {
		// TODO Auto-generated method stub
		//lvTask.cl
		arrList.clear();
		List<GetListView> taskList = new ArrayList<GetListView>();
		Cursor cursor_Task = SplashActivity.mydb.rawQuery("SELECT * FROM  Task ORDER BY Date(substr(taskDeadline,1,10))",null);
		 // looping through all rows and adding to list
		GetListView contact;
		 //if (cursor_Task.moveToFirst()) {
			 while (cursor_Task.moveToNext()) 
			 {
			 contact = new GetListView();
			 contact.setID(Integer.parseInt(cursor_Task.getString(0)));
			 contact.settaskHeading(cursor_Task.getString(1));
			 contact.settaskNote(cursor_Task.getString(2));
			 contact.setbuilding_id(cursor_Task.getString(13));
			 if(cursor_Task.getString(6).equals("null"))
				 contact.setdoneDate("");
			 else
				 contact.setdoneDate(cursor_Task.getString(6));
			 //contact.setImage(cursor_Task.getBlob(3));
			 // Adding contact to list
			 taskList.add(contact);
			 } 
		 //}
			for (GetListView cn : taskList) {
				String log = "ID: " + cn.getID() + " Heading: " + cn.gettaskHeading()
				+ ", Task Note: " + cn.gettaskNote()+ "Create Date: " + cn.getbuilding_id()+ "Finish Date: " + cn.getdoneDate();

				// Writing Contacts to log
				Log.d("Result: ", log);
				//add contacts data in arrayList
				arrList.add(cn);

				}
				adapter = new ArrayAdapterList(this, R.layout.screen_list_task,arrList);			
				lvTask.setAdapter(adapter);
	}
	
	private void populateInfolist() {
		// TODO Auto-generated method stub
		
		arrListinfo.clear();
		List<GetListView> taskInfoList = new ArrayList<GetListView>();
		Cursor cursor_Task = SplashActivity.mydb.rawQuery("select i.id,substr(i.infocreateDate,1,10) as infocreateDate,infoNote,buildingname,substr(i.infocreateDate,1,10) date from info i inner join Building b on i.building_id=b.building_id order by Date(date) desc",null);
		 // looping through all rows and adding to list
		GetListView contact;
		 //if (cursor_Task.moveToFirst()) {
			 while (cursor_Task.moveToNext()) 
			 {
			 contact = new GetListView();
			 contact.setID(Integer.parseInt(cursor_Task.getString(0)));
			 contact.settaskHeading(cursor_Task.getString(2));
			 contact.settaskNote(cursor_Task.getString(1));
			 contact.setbuilding_id(cursor_Task.getString(3));
			 // Adding contact to list
			 taskInfoList.add(contact);
			 } 
		 //}
			for (GetListView cn : taskInfoList) {
				String log = "ID: " + cn.getID() + " Note: " + cn.gettaskHeading()
				+ ", Create Date: " + cn.gettaskNote()+ "Building id: " + cn.getbuilding_id();

				// Writing Contacts to log
				Log.d("Result: ", log);
				//add contacts data in arrayList
				arrListinfo.add(cn);

				}
				adapterinfo = new ArrayAdapterList(this, R.layout.screen_list,arrListinfo);			
				lvInfo.setAdapter(adapterinfo);
	}
	
private class Tasks extends AsyncTask<String, Void, String> {
		
	    private ProgressDialog pDialog;
		    	
	 	 @Override
	    protected void onPreExecute() {
	          super.onPreExecute();
	          
	          pDialog = new ProgressDialog(MainActivity.this);
	          pDialog.setMessage("Getting Data ...");
	          pDialog.setIndeterminate(false);
	          pDialog.setCancelable(true);
	          pDialog.show();
	          
	  	}
			
			@Override
			protected String doInBackground(String... urls) {

				return GET(urls[0]);
			}

			private String GET(String url) {
				InputStream inputStream = null;
				String result = "";
				try 
				{

					// create HttpClient
					HttpClient httpclient = new DefaultHttpClient();

					// make GET request to the given URL
					HttpResponse httpResponse = httpclient
							.execute(new HttpGet(url));

					// receive response as inputStream
					inputStream = httpResponse.getEntity().getContent();

					// convert inputstream to string
					if (inputStream != null)
						result = convertInputStreamToString(inputStream);
					else
						result = "Did not work!";

				} 
				catch (Exception e) 
				{
					Log.d("InputStream", e.getLocalizedMessage());
				}
				//pDialog.dismiss();
		        //if (pDialog != null && pDialog.isShowing()) {
		        //    pDialog.dismiss();
		        //}
		        
				return result;
			}

			// onPostExecute displays the results of the AsyncTask.
			@Override
			protected void onPostExecute(String result) {		
				//pDialog.dismiss();
				
				try {
			        if ((this.pDialog != null) && this.pDialog.isShowing()) {
			            this.pDialog.dismiss();
			        }
			    } catch (final IllegalArgumentException e) {
			        // Handle or log or ignore
			    } catch (final Exception e) {
			        // Handle or log or ignore
			    } finally {
			        this.pDialog = null;
			    }  

				// Integer OutputData;
		        try 
				{

		        	JSONArray jsonarray = new JSONArray(result);
					int lengthJsonArr = jsonarray.length();
					
					SplashActivity.mydb.execSQL("DELETE FROM Task");
					SplashActivity.mydb.execSQL("DELETE FROM Photo");
					for (int i = 0; i < lengthJsonArr; i++) 
					{
						JSONObject jsonChildNode = jsonarray.getJSONObject(i);

						String heading = jsonChildNode.optString("heading").toString();
		 				String note = jsonChildNode.optString("note").toString();

		 				String createDate = jsonChildNode.optString("taskCreateDate").toString();
	 					if(!createDate.equals("null"))
	 					{
		 					int idx1 = createDate.indexOf("(");
		 				    int idx2 = createDate.indexOf(")") - 5;
		 				    String s = createDate.substring(idx1+1, idx2);
		 				    long l = Long.valueOf(s);
		 				    createDate=new SimpleDateFormat( "yyyy-MM-dd hh:mm a", Locale.getDefault()).format(l);
	 					}
	 					String finishDate = jsonChildNode.optString("endTimeStamp").toString();
	 					if(!finishDate.equals("null"))
	 					{
		 					int idx1 = finishDate.indexOf("(");
		 				    int idx2 = finishDate.indexOf(")") - 5;
		 				    String s = finishDate.substring(idx1+1, idx2);
		 				    long l = Long.valueOf(s);
		 				   finishDate=new SimpleDateFormat( "yyyy-MM-dd hh:mm a", Locale.getDefault()).format(l);
	 					}
						
	 					String deadline = jsonChildNode.optString("deadline").toString();
	 					if(!deadline.equals("null"))
	 					{
		 					int idx1 = deadline.indexOf("(");
		 				    int idx2 = deadline.indexOf(")") - 5;
		 				    String s = deadline.substring(idx1+1, idx2);
		 				    long l = Long.valueOf(s);
		 				   deadline=new SimpleDateFormat( "yyyy-MM-dd hh:mm a", Locale.getDefault()).format(l);
	 					}

	 					String tasktype_id = jsonChildNode.optString("tasktype_id").toString();
	 					String building_id = jsonChildNode.optString("building_id").toString();
		 				String tenant_id = jsonChildNode.optString("tenant_id").toString();
		 				String user_id = jsonChildNode.optString("user_id").toString();
		 				String task_id = jsonChildNode.optString("id").toString();
						
		 				String photo = jsonChildNode.optString("photoAttachmentFiles").toString();
		 				JSONArray jsonarrayPhoto = new JSONArray(photo);
		 				int lengthJsonArrPhoto = jsonarrayPhoto.length();
		 				for (int j = 0; j < lengthJsonArrPhoto; j++) 
						{
			 				JSONObject jsonChildNodePhoto = jsonarrayPhoto.getJSONObject(j);
			 				
				 				String img = jsonChildNodePhoto.optString("taskImg").toString();
				 				
				 				Bitmap decodedString=decodeBase64(img);
				 				byte[] data = getBitmapAsByteArray(decodedString);
				 				
				 				cv = new ContentValues();
				 				cv.put("task_id",task_id);				 				
				 				cv.put("picture", data);
				 				
				 				SplashActivity.mydb.insert("Photo", null, cv);

				 				//SplashActivity.mydb.execSQL("insert into Photo"
			    				//		+ " (picture,task_id) "
			    				//		+" VALUES ('"+ data + "','"+ task_id + "')");
						}

						
						SplashActivity.mydb.execSQL("insert into Task"
		    						+ " (taskHeading,taskNote,taskDeadline,taskSlip,taskCreateDate,taskEndtimestamp,tasktype_Id,building_Id,tenant_Id,user_Id,task_Id) "
		    						+" VALUES ('"+ heading + "','"+ note + "','"+deadline+"','2','"+ createDate + "','"+ finishDate + "','"+ tasktype_id + "','"+ building_id + "','"+ tenant_id + "','"+ user_id + "','"+ task_id + "')");
						
						
					}

					//Intent logintomain = new Intent(getApplicationContext(), MainActivity.class);
					//startActivity(logintomain);
					//finish();
					populateTasklist();   //

				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
					//Intent logintomain = new Intent(getApplicationContext(), MainActivity.class);
					//startActivity(logintomain);
					//finish();
				}

			}

			public Bitmap decodeBase64(String input)
			{
			    byte[] decodedBytes = Base64.decode(input, 0);
			    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
			}
			public byte[] getBitmapAsByteArray(Bitmap bitmap)
			{
			    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			    bitmap.compress(CompressFormat.PNG, 100, outputStream);
			    return outputStream.toByteArray();
			    
			}
		}

private class Building_Info extends AsyncTask<String, Void, String> {
	
    private ProgressDialog pDialog;
	    	
 	 @Override
    protected void onPreExecute() {
          super.onPreExecute();
          
          pDialog = new ProgressDialog(MainActivity.this);
          pDialog.setMessage("Getting Data ...");
          pDialog.setIndeterminate(false);
          pDialog.setCancelable(true);
          pDialog.show();
          
  	}
		
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		private String GET(String url) {
			InputStream inputStream = null;
			String result = "";
			try 
			{

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient
						.execute(new HttpGet(url));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

			} 
			catch (Exception e) 
			{
				Log.d("InputStream", e.getLocalizedMessage());
			}
			//pDialog.dismiss();
	        //if (pDialog != null && pDialog.isShowing()) {
	        //    pDialog.dismiss();
	        //}
	        
			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {		
			//pDialog.dismiss();
			
			try {
		        if ((this.pDialog != null) && this.pDialog.isShowing()) {
		            this.pDialog.dismiss();
		        }
		    } catch (final IllegalArgumentException e) {
		        // Handle or log or ignore
		    } catch (final Exception e) {
		        // Handle or log or ignore
		    } finally {
		        this.pDialog = null;
		    }  

			// Integer OutputData;
	        try 
			{

	        	JSONArray jsonarray = new JSONArray(result);
				int lengthJsonArr = jsonarray.length();
				
				SplashActivity.mydb.execSQL("DELETE FROM Info");
				for (int i = 0; i < lengthJsonArr; i++) 
				{
					JSONObject jsonChildNode = jsonarray.getJSONObject(i);
 					String createDate = jsonChildNode.optString("createDate").toString();

 					int idx1 = createDate.indexOf("(");
 				    int idx2 = createDate.indexOf(")") - 5;
 				    String s = createDate.substring(idx1+1, idx2);
 				    long l = Long.valueOf(s);
 				    createDate=new SimpleDateFormat( "yyyy-MM-dd hh:mm a", Locale.getDefault()).format(l);

	 				String note = jsonChildNode.optString("note").toString();
					String building_id = jsonChildNode.optString("building_id").toString();
					String info_id = jsonChildNode.optString("id").toString();
					
					SplashActivity.mydb.execSQL("insert into Info"
	    						+ " (infocreateDate,infoNote,building_id,info_id) VALUES ('"+ createDate + "','"+ note + "','"+ building_id + "','"+ info_id + "')");
					
				}

				//Intent logintomain = new Intent(getApplicationContext(), MainActivity.class);
				//startActivity(logintomain);
				//finish();
				populateInfolist(); 		
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
				//Intent logintomain = new Intent(getApplicationContext(), MainActivity.class);
				//startActivity(logintomain);
				//finish();
			}

		}
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}

}
