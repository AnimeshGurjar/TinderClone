package com.example.tinderapp.Matches;

public class MatchesObject {

    private String userid;
    private String name;
    private String profileImageUrl;
    public MatchesObject(String userid, String name, String profileImageUrl)
    {
        this.userid=userid;
        this.name=name;
        this.profileImageUrl=profileImageUrl;
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
    public String getProfileImageUrl() { return profileImageUrl; }
    public void serProfileImageUrl(String profileImageUrl) { this.profileImageUrl=profileImageUrl; }
}
