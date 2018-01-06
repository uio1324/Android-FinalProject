package com.example.great.project.Model;

/**
 * Created by Danboard on 18-1-5.
 */

public class Student {
    private String sName;
    private String nickName;
    private String password;

    public Student(){}
    public Student(String sName, String nickName, String password) {
        this.sName = sName;
        this.nickName = nickName;
        this.password = password;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setPassWord(String password) {
        this.password = password;
    }

    public String getSName() {return sName;}
    public String getNickName() {
        return nickName;
    }
    public String getPassword() {
        return password;
    }

}
