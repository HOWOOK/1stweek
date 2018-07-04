package com.example.q.practice3;

public class Tab1Contacts_item {
    private String photoUri;
    private String name;
    private String phone;

    public String getPhotoUri(){return photoUri;}
    public String getName(){return name;}
    public String getPhone(){return phone;}
    public Tab1Contacts_item(String photoUri,String name, String phone){
        this.photoUri = photoUri;
        this.name=name;
        this.phone=phone;
    }
}

