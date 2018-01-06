package com.example.great.project.Model;

/**
 * Created by Danboard on 18-1-6.
 */

public class StuSet {
    private String sName;
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

    public String getSName() {return sName;}
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