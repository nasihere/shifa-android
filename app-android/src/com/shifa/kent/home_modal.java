package com.shifa.kent;

public class home_modal {

    public int Id;
    public String IconFile;
    public String StatusItem;
    public String Name;

    public void setStatus(String Status)
    {
    	this.StatusItem = Status;
    	
    }
    public String getStatus()
    {
    	return this.StatusItem;
    	
    }
    public home_modal(int id, String iconFile, String name, String StatusItem) {

        Id = id;
        IconFile = iconFile;
        Name = name;
        this.StatusItem = StatusItem; 
    }
    
}