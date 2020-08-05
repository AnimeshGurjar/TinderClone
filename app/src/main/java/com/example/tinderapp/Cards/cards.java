package com.example.tinderapp.Cards;

public class cards {
    private String userid;
    private String name;
    private  String profileimageurl;
    public cards(String userid,String name,String profileimageurl)
    {
        this.userid=userid;
        this.name=name;
        this.profileimageurl=profileimageurl;
    }
    public String getUserid()
    {
        return userid;
    }
    public void setUserid(String userid)
    {
        this.userid=userid;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }

    public String getprofileimageurl()
    {
        return profileimageurl;
    }
    public void setprofileimageurl(String profileimageurl)
    {
        this.profileimageurl=profileimageurl;
    }
}