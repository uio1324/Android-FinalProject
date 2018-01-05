package com.example.great.project.Model;

/**
 * Created by Danboard on 18-1-5.
 */

public class Student {
    private String sName;
    private String nickName;
    private String password;

    public Student(String sName, String nickName, String password) {
        this.sName = sName;
        this.nickName = nickName;
        this.password = password;
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


    private Integer maxTask = 5;
    private String studyTime = null;
    private String restTime = null;

    public void setMaxTask(Integer maxTask) {
        this.maxTask = maxTask;
    }
    public void setStudyTime(String startTime) {
        this.studyTime = startTime;
    }
    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public Integer getMaxTask() {
        return maxTask;
    }
    public String getStudyTime() {
        return studyTime;
    }
    public String getRestTime() {
        return restTime;
    }
}
