package com.initvent.tenantsystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TaskActivity extends Activity implements OnClickListener{

	static String memberId="";
	ArrayList<GetListView> arrList = new ArrayList<GetListView>();
	ArrayListAdapterPhoto adapter;
	
	ArrayList<GetListView> attarrList = new ArrayList<GetListView>();
	ArrayAdapterList attadapter;	
	ListView lvTaskPhoto,lvTaskAttach;

	Button btnTaskAttachAdd,btnTaskPhotoAdd,btnTaskAdd,btnTaskSave,btnTaskDelete;
	EditText etTaskHeading,etTaskNote;
	Spinner spnBuilding,spnTenant;
	ImageView imageView1,imgicn;
	
	private Uri fileUri;
	ContentValues cv;
	Bitmap img1,rbitmap;

	String cur_date_time,starttimestamp="",endtimestamp="", UserId="", Pass="",taskDeadline="";
	
	final Context context = this;
	int ph_id=0,at_id=0;

	TextView chng_attachID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		
		lvTaskPhoto=(ListView)findViewById(R.id.lvTaskPhoto);
		
		btnTaskPhotoAdd=(Button)findViewById(R.id.btnTaskPhotoAdd);
		btnTaskSave=(Button)findViewById(R.id.btnTaskSave);
		btnTaskDelete=(Button)findViewById(R.id.btnTaskDelete);

		Display display=getWindowManager().getDefaultDisplay();
	    int width=display.getWidth();
	    btnTaskPhotoAdd.setWidth(width/2);
	    btnTaskSave.setWidth(width/2);
	    btnTaskDelete.setWidth(width/2);

		imageView1=(ImageView)findViewById(R.id.imageView1);
		imgicn=(ImageView)findViewById(R.id.imgIcon);

		etTaskHeading=(EditText)findViewById(R.id.etTaskHeading);
		etTaskNote=(EditText)findViewById(R.id.etTaskNote);

		btnTaskPhotoAdd.setOnClickListener(this);
		btnTaskSave.setOnClickListener(this);
		btnTaskDelete.setOnClickListener(this);
		
		Intent i = getIntent();
		memberId = i.getStringExtra("chng_taskID");//Only Id from table
		//TaskActivity lPhoto = new TaskActivity();
		//if(memberId.equals(""))
		//{
			btnTaskDelete.setVisibility(View.GONE);			
			//btnTaskPhotoAdd.setVisibility(View.GONE);			
			//imageView1.setVisibility(View.GONE);			
		/*}
		else
		{
			listPhoto(memberId);
			taskBasicinfo(memberId);
			btnTaskDelete.setVisibility(View.VISIBLE);
			btnTaskPhotoAdd.setVisibility(View.VISIBLE);
			imageView1.setVisibility(View.VISIBLE);
		}*/
		taskAdd();
		imageView1.setOnTouchListener(new ImageView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ImageView tempImageView = imageView1;


		        AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
		        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

		        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
		                (ViewGroup) findViewById(R.id.layout_root));
		        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		        image.setImageDrawable(tempImageView.getDrawable());
		        imageDialog.setView(layout);
		        imageDialog.setPositiveButton("Initvent",new DialogInterface.OnClickListener(){

		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		            }

		        });


		        imageDialog.create();
		        imageDialog.show();  
	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
		});
    	setListViewHeightBasedOnChildren(lvTaskPhoto);      //dynamic listview for photo
		lvTaskPhoto.setOnTouchListener(new ListView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
	            int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
			}
	    });
		
		lvTaskPhoto.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				TextView txtPhotoId = (TextView) view.findViewById(R.id.txtPhotoId);

			    ph_id = Integer.parseInt(txtPhotoId.getText().toString());
			    
	    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				// set title
				alertDialogBuilder.setTitle("This cutomer will be deleted");

				// set dialog message
				alertDialogBuilder
						.setMessage("Click Yes to Delete")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										
										SplashActivity.mydb.delete("Photo", "id" + "="
												+ ph_id, null);

										//dbcon.deleteData(member_id);
										this.returnHome();
									}

									private void returnHome() {
										Intent home_intent = new Intent(getApplicationContext(),
												TaskActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										home_intent.putExtra("chng_taskID", memberId);

										startActivity(home_intent);
										
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();



			}
		});		

	}
	 @Override
	 protected void onPause(){
	     super.onPause();
	    String deleted_id="";
	    deleted_id=lastRecord();
	    if(deleted_id.equals(""))
	    	deleted_id=null;
 		//SplashActivity.mydb.delete("Task", "id" + "=" + deleted_id, null);
	 }
	 @Override
	 protected void onResume(){
	     super.onResume();
	     //taskAdd();

	 }
	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	//	     Log.d(this.getClass().getName(), "back button pressed");
			    String deleted_id="";
			    deleted_id=lastRecord();
			    if(deleted_id.equals(""))
			    	deleted_id=null;
		 		SplashActivity.mydb.delete("Photo", "task_id" + "=" + deleted_id, null);
		 		SplashActivity.mydb.delete("Task", "id" + "=" + deleted_id, null);
	
		 }
		 
		 return super.onKeyDown(keyCode, event);
	 }
	 private String lastRecord()
	 {
		String last_record="";
	    Cursor deleted_task = SplashActivity.mydb.rawQuery("SELECT id FROM Task WHERE taskHeading=''",null);
		while(deleted_task.moveToNext())
		{
			last_record = deleted_task.getString(0);
		}
		return last_record;
	 }
	 private void listPhoto(String task_id2) {
		// TODO Auto-generated method stub
		arrList.clear();

		List<GetListView> taskList = new ArrayList<GetListView>();
		Cursor cursor_Task = SplashActivity.mydb.rawQuery("SELECT * FROM  Photo where task_id='"+ task_id2 +"' and attach_id is null ",null);
		 // looping through all rows and adding to list
		GetListView contact;
		 //if (cursor_Task.moveToFirst()) {
			 while (cursor_Task.moveToNext()) 
			 {
			 contact = new GetListView();
			 contact.setPhotoid(Integer.parseInt(cursor_Task.getString(0)));
			 contact.setImage(cursor_Task.getBlob(1));
			 // Adding contact to list
			 taskList.add(contact);
			 } 
		 //}
			for (GetListView cn : taskList) {
				String log = "ID:" + cn.getPhotoid() + " Photo: " + cn.getImage();

				// Writing Contacts to log
				Log.d("Result: ", log);
				//add contacts data in arrayList
				arrList.add(cn);

				}
				adapter = new ArrayListAdapterPhoto(this, R.layout.photo_list,arrList);			
				lvTaskPhoto.setAdapter(adapter);

	}
	/*
	private void taskBasicinfo(String memberId) {
		// TODO Auto-generated method stub
		String taskheading="",tasknote="";
		Cursor cursor_task_id = SplashActivity.mydb.rawQuery("SELECT * FROM  Task WHERE id='"+memberId+"'",null);
		while(cursor_task_id.moveToNext())
		{
			taskheading=cursor_task_id.getString(1);
			tasknote=cursor_task_id.getString(2);
			
			etTaskHeading.setText(taskheading);
			etTaskNote.setText(tasknote);
			
		}

	}*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btnTaskPhotoAdd:
				 try {				 
			            PackageManager packageManager = getPackageManager();
			            boolean doesHaveCamera = packageManager
			                    .hasSystemFeature(PackageManager.FEATURE_CAMERA);

			            if (doesHaveCamera) {
			                // start the image capture Intent
			                Intent intent = new Intent(
			                        MediaStore.ACTION_IMAGE_CAPTURE);
			                // Get our fileURI
			                //fileUri = getOutputMediaFile();

			                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			                startActivityForResult(intent, 100);
			            }
			        } catch (Exception ex) {
			            Toast.makeText(getApplicationContext(),
			                    "There was an error with the camera.",
			                    Toast.LENGTH_LONG).show();
			        }

				break;
			case R.id.btnTaskSave:
				taskUpdate();
				Intent tasttomain = new Intent(this, MainActivity.class);
				tasttomain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(tasttomain);
				break;
			case R.id.btnTaskDelete:
	    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

				// set title
				alertDialogBuilder.setTitle("This cutomer will be deleted");

				// set dialog message
				alertDialogBuilder
						.setMessage("Click Yes to Delete")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										
										SplashActivity.mydb.delete("Task", "id" + "="
												+ memberId, null);

										//dbcon.deleteData(member_id);
										this.returnHome();
									}

									private void returnHome() {
										Intent home_intent = new Intent(getApplicationContext(),
												MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(home_intent);
										
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				break;
			default:
				break;
		
		}

	}
	private void taskAdd() {
		// TODO Auto-generated method stub
		String taskheading,user_id,building_id,tenant_id,tasknote;
		taskheading = etTaskHeading.getText().toString().trim();
		tasknote = etTaskNote.getText().toString().trim();
		
		user_id="";
		Cursor cursor_userid = SplashActivity.mydb.rawQuery("select sysUser_id,user_id,userId from User u inner join Login l on u.user_id=l.userId",null);
		while(cursor_userid.moveToNext())
		{
			user_id = cursor_userid.getString(0);
		}

	

		building_id="";
		tenant_id="";
		Cursor cursor_buildingid = SplashActivity.mydb.rawQuery("SELECT * FROM  Tenant where tenant_id='"+user_id+"'",null);
		while(cursor_buildingid.moveToNext())
		{
			building_id = cursor_buildingid.getString(6);
			tenant_id = cursor_buildingid.getString(7);
		}
		
		String tasktype_id="";
		Cursor cursor_tasktypeid = SplashActivity.mydb.rawQuery("SELECT taskType_id  FROM  Building b inner join Customer c on b.customer_id=c.customer_id  where building_id='"+building_id+"'",null);
		while(cursor_tasktypeid.moveToNext())
		{
			tasktype_id = cursor_tasktypeid.getString(0);
		}
		
		String dvalue="";
		cur_date_time = new SimpleDateFormat( "yyyy-MM-dd hh:mm a", Locale.getDefault()).format(new Date());
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		Date resultdate = new Date(c.getTimeInMillis());
		taskDeadline=new SimpleDateFormat( "yyyy-MM-dd hh:mm a", Locale.getDefault()).format(resultdate);
		if(memberId.equals(""))
		{
	     	SplashActivity.mydb.execSQL( "insert into Task"
					+ " (taskHeading,taskNote,taskDeadline,taskSlip,taskStarttimestamp,taskEndtimestamp,taskDuration,taskDurationRounded,tasktype_Id,building_Id,tenant_Id,user_Id,taskCreateDate)"
	     			+ " VALUES ('"+taskheading+"','"+tasknote+"','"+taskDeadline+"','2','"+starttimestamp+"','"+endtimestamp+"','"+dvalue+"','"+dvalue+"','"+tasktype_id+"','"+building_id+"','"+tenant_id+"','"+user_id+"','"+cur_date_time+"')");
	
//	     	Toast.makeText(getBaseContext(),"Inserted successfully",Toast.LENGTH_LONG).show();		
		}
	}
	private void taskUpdate() 
	{
		String taskheading,tasknote;
		taskheading = etTaskHeading.getText().toString().trim();
		tasknote = etTaskNote.getText().toString().trim();
		
		if(!taskheading.equals(""))
		{	
			String update_id="";
			update_id=maxRecord();
			SplashActivity.mydb.execSQL("update Task set"
					+ " taskHeading='"+taskheading+"',taskNote='"+tasknote+"' WHERE id='"+update_id+"'");
	
	     	//Toast.makeText(getBaseContext(),"Insert successfully",Toast.LENGTH_LONG).show();
	
			if(isConnected()){
	     		new ExportTaskOnline().execute(); //task export to server
	     	}
	        else{
	        	Toast.makeText(
						getBaseContext(),
						"Oops!You are not Connected to INTERNET",
						Toast.LENGTH_LONG).show();
	        }	
		}
	}
	private String maxRecord()
	{
		String update_id="";
		Cursor update_task = SplashActivity.mydb.rawQuery("SELECT MAX(id) FROM Task ORDER BY id DESC LIMIT 1",null);
		while(update_task.moveToNext())
		{
			update_id = update_task.getString(0);
		}
		return update_id;
	}
	protected void onActivityResult(int requestCode, int resultCode,Intent intent)
    {
        if (requestCode == 100)
         {
            if (resultCode == RESULT_OK)
            {
                if (intent == null)
                {
                    // The picture was taken but not returned
                    Toast.makeText(
                            getApplicationContext(),
                            "The picture was taken and is located here: "
                                    + fileUri.toString(), Toast.LENGTH_LONG)
                            .show();
                }
                else
                {                    	
                    	try
                    	{               
	                    	 // The picture was returned
		                     Bundle extras = intent.getExtras();
		                     img1=(Bitmap) extras.get("data");
		                     rbitmap=getResizedBitmap(img1, 108, 108);
		                     ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);
		                     imageView1.setImageBitmap(rbitmap);
		                     //imageView1.setImageBitmap((Bitmap) extras.get("data"));
		                     String maxrec=maxRecord();
           					 addPhoto(maxrec);
           					 setListViewHeightBasedOnChildren(lvTaskPhoto); //dynamic listview

           					 //imageView1.setImageResource(R.id.imageView1);
                    	}
                    	catch(Exception e)
                    	{ 
                    		Toast.makeText(getApplicationContext(),"Please Take image: "
	                                    + fileUri.toString(), Toast.LENGTH_LONG).show();
                    	}
                    	
                    	
                    
                }
            }
        }
    }	
	private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		// TODO Auto-generated method stub
		int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(
	        bm, 0, 0, width, height, matrix, false);
	    bm.recycle();
	    return resizedBitmap;
	}
	private void addPhoto(String taskId) {
		// TODO Auto-generated method stub
			cv = new ContentValues();
			//String name = etName.getText().toString();
			//String description = etDescription.getText().toString();
			
			cv.put("task_id",taskId);
			//cv.put(MEMBER_Description, description);
			
			byte[] data = getBitmapAsByteArray(rbitmap);
			cv.put("picture", data);
			SplashActivity.mydb.insert("Photo", null, cv);
			
			//copyDatabase();
			Toast.makeText(getBaseContext(),"Inserted successfully",Toast.LENGTH_LONG).show();
		//}

			listPhoto(taskId);
	}
	public static byte[] getBitmapAsByteArray(Bitmap bitmap)
	{
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.PNG, 0, outputStream);
	    return outputStream.toByteArray();
	    
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {    //dynamic list size with scroll
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    int listnoFlag=1;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	        if(listnoFlag==3)
	        	break;
	        listnoFlag++;
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}
	
	public boolean isConnected()
	{
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
            return true;
        else
            return false;   
    }
	public class ExportTaskOnline extends AsyncTask<String, Void, Boolean> {
	   private ProgressDialog pDialog;
    	
		 @Override
	   protected void onPreExecute() {
	         super.onPreExecute();
	         
	         pDialog = new ProgressDialog(TaskActivity.this);
	         pDialog.setMessage("Getting Data ...");
	         pDialog.setIndeterminate(false);
	         pDialog.setCancelable(true);
	         //pDialog.show();
	         
	 	}
		@SuppressLint("NewApi")
		protected Boolean doInBackground(final String... args) {

			try {

				JSONObject parrent = new JSONObject();
				JSONArray Array = new JSONArray();

				Cursor Online = SplashActivity.mydb.rawQuery(
						"select * from Task ORDER BY id DESC LIMIT 1", null);

				JSONObject new_member = new JSONObject();
				
				while (Online.moveToNext()) {

					 Cursor curPhoto = SplashActivity.mydb.rawQuery("select * from Photo WHERE task_id='"+Online.getString(0)+"'",null);

					 JSONArray photoList = new JSONArray();
						
					 if (curPhoto != null) {
						 
						 while (curPhoto.moveToNext()) {
							 
							 JSONObject objPhoto = new JSONObject(); 
							 
							 byte[] picture = curPhoto.getBlob(curPhoto.getColumnIndex("picture"));
							 String image = Base64.encodeToString(picture, Base64.DEFAULT);
							 
							 objPhoto.put("taskImg",image);
							 //objPhoto.put("task_Id","C34C8AC4-28D7-4BAF-BE38-0844B45210A9");
					 
							 photoList.put(objPhoto);
						 
						 }
						 
					 }
					new_member.put("photoAttachmentFiles",photoList);
					new_member.put("heading", Uri.encode(Online.getString(1)));
					new_member.put("note", Uri.encode(Online.getString(2)));
					new_member.put("tasktype_id", Online.getString(9));
					new_member.put("building_id", Online.getString(10));
					new_member.put("tenant_id", Online.getString(11));
					new_member.put("user_id", Online.getString(12));

					SimpleDateFormat format = new SimpleDateFormat("Z");
					Date date = new Date();
					new_member.put("taskCreateDate", "/Date(" + String.valueOf(date.getTime()) + format.format(date) + ")/");

					new_member.put("slip", Online.getString(4));
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a"); 
					String ddate=Online.getString(3);
					if(ddate != null && !ddate.isEmpty() && !ddate.equals("null"))
					{
						Date sdate = dateFormat.parse(Online.getString(3));
						new_member.put("deadline", "/Date(" + String.valueOf(sdate.getTime()) + format.format(sdate) + ")/");
					}
					
					Array.put(new_member);
				}
				parrent.put("Tasks", Array);

				Cursor cursor_login = SplashActivity.mydb.rawQuery("SELECT *FROM  Login ",null);
				String server="",service="",userid="",pass="";
				
				if (cursor_login != null) {
					cursor_login.moveToLast();
					try{
						
						 server=cursor_login.getString(1);
			    		 service=cursor_login.getString(2);
			    		 userid=cursor_login.getString(3);
			    		 pass=cursor_login.getString(4);
			    		 pass=Uri.encode(pass);;
						//UserId= cursor_login.getString(3);
						//Pass=cursor_login.getString(4);
					
					}catch(Exception e){
						
						
					}
					//cursor_login.close();
					
				}
				parrent.put("userId",userid );
				parrent.put("userPass", pass);

				//parrent.put("userPass", Pass);
				//parrent.put("userId",UserId );
  			    
				//Cursor lin = SplashActivity.mydb.rawQuery("select * from Login",null);
				//lin.moveToLast();
				//String servername = lin.getString(1).trim();
				//String userid = lin.getString(2).trim();
	
				String URL = "http://"+server+"/"+service+"/TMSService.svc/TaskForAndroid";
				
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpPost httppostreq = new HttpPost(URL);
				StringEntity se = new StringEntity(parrent.toString());
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
				httppostreq.setEntity(se);
				HttpResponse httpresponse = httpclient.execute(httppostreq);
				
				 try {
	                 
					  String t= EntityUtils.toString(httpresponse.getEntity());
					  if(t.contentEquals("true")){
						  Toast.makeText(getApplicationContext(),"Export successful!"+t, Toast.LENGTH_LONG).show();
					  }
					  else{
						  
						  Toast.makeText(TaskActivity.this, "Export failed",Toast.LENGTH_SHORT).show();
					  }

					 
	              } catch (ClientProtocolException e) {
	                 
	                  e.printStackTrace();
	              } catch (IOException e) {
	                  
	                  e.printStackTrace();
	              }
				return true;

			}

			catch (Exception e) {
				Log.e("ActivityB", e.getMessage(), e);
				return true;
			}
		}

		protected void onPostExecute(final Boolean success) {
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

			if (success) {
				Toast.makeText(TaskActivity.this, "Export successful!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TaskActivity.this, "Export failed",
						Toast.LENGTH_SHORT).show();
				
				 //mydb.execSQL("DELETE FROM " + TABLE);
				
			}
		}
	}
}
