package com.mygallery.helper;

public class Album {

	public String id;
	public String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCoverID() {
		return coverID;
	}
	public void setCoverID(long coverID) {
		this.coverID = coverID;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long coverID;
	public int count=1;

}
