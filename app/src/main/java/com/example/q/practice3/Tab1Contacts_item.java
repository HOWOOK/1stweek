package com.example.q.practice3;

public class Tab1Contacts_item {
    //private String icon;
    private String photoUri;
    private String name;
    private String phone;

    //public String getIcon(){return icon;}
    public String getPhotoUri(){return photoUri;}
    public String getName(){return name;}
    public String getPhone(){return phone;}
    public Tab1Contacts_item(String photoUri,String name, String phone){
        //this.icon=icon;
        this.photoUri = photoUri;
        this.name=name;
        this.phone=phone;
    }
}

