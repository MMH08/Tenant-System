package com.initvent.tenantsystem;

public class GetListView {
	// private variables
	int _id;
	String _taskHeading;
	String _building_id; 
	String _taskNote;
	String _doneDate;
	
	int _photoid;
	byte[] _image;
	
	// Empty constructor
	public GetListView() {

	}

	// constructor
	public GetListView(int keyId, String taskHeading, String building_id, String taskNote, String doneDate) {
	this._id = keyId;
	this._taskHeading = taskHeading;
	this._building_id = building_id;
	this._taskNote = taskNote;
	this._doneDate = doneDate;

	}

	//constructor
	public GetListView(int keyId, byte[] image) {
	this._photoid = keyId;
	this._image = image;

	}

	// getting ID
	public int getID() {
	return this._id;
	}

	// setting id
	public void setID(int keyId) {
	this._id = keyId;
	}

	// getting task heading
	public String gettaskHeading() {
	return this._taskHeading;
	}

	// setting task heading
	public void settaskHeading(String taskHeading) {
	this._taskHeading = taskHeading;
	}
	
	// getting task heading
	public String getbuilding_id() {
	return this._building_id;
	}

	// setting task heading
	public void setbuilding_id(String building_id) {
	this._building_id = building_id;
	}
	
	// getting task heading
	public String gettaskNote() {
	return this._taskNote;
	}

	// setting task heading
	public void settaskNote(String taskNote) {
	this._taskNote = taskNote;
	}
	// getting task heading
	public String getdoneDate() {
	return this._doneDate;
	}

	// setting task heading
	public void setdoneDate(String doneDate) {
	this._doneDate = doneDate;
	}
	
	
	
	
	// getting phone number
	public int getPhotoid() {
	return this._photoid;
	}

	// setting phone number
	public void setPhotoid(int photoId) {
	this._photoid = photoId;
	}

	// getting phone number
	public byte[] getImage() {
	return this._image;
	}

	// setting phone number
	public void setImage(byte[] image) {
	this._image = image;
	}

}
