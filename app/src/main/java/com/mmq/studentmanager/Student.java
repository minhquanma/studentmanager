package com.mmq.studentmanager;

import java.io.Serializable;


/**
 * Created by Jon on 3/11/2017.
 */

public class Student implements Serializable {

    // Personal information.
    private String key;
    private String name;
    private String home;
    private String country;
    private int year;
    private String phone;
    private String avatar;
    private boolean gender;

    // Educations
    private double listeningPts;
    private double speakingPts;
    private double readingPts;
    private double writingPts;

    private double averageScore;
    private int courseDate;

    // Information
    private String createdDate;

    public Student() {
        /* - Blank default constructor essential for Firebase
         * - In case creating a null object */
    }

    public Student(String name, String home, String country, int year, String phone, String avatar, boolean gender) {
        this.name = name;
        this.home = home;
        this.country = country;
        this.year = year;
        this.phone = phone;
        this.avatar = avatar;
        this.gender = gender;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public double getListeningPts() {
        return listeningPts;
    }

    public void setListeningPts(double listeningPts) {
        this.listeningPts = listeningPts;
    }

    public double getSpeakingPts() {
        return speakingPts;
    }

    public void setSpeakingPts(double speakingPts) {
        this.speakingPts = speakingPts;
    }

    public double getReadingPts() {
        return readingPts;
    }

    public void setReadingPts(double readingPts) {
        this.readingPts = readingPts;
    }

    public double getWritingPts() {
        return writingPts;
    }

    public void setWritingPts(double writingPts) {
        this.writingPts = writingPts;
    }

    public int getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(int courseDate) {
        this.courseDate = courseDate;
    }

    public boolean isGender() {
        return gender;
    }
    public double averageScore() {
        double score = (this.listeningPts +
                this.readingPts +
                this.speakingPts +
                this.writingPts) / 4;
        return score;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public String stringGender() {
        return isGender() ? "Male" : "Female";
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }
    public String getHome() {
        return home;
    }
    public String getCountry() {
        return country;
    }
    public int getYear() {
        return year;
    }
    public String getPhone() {
        return phone;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHome(String home) {
        this.home = home;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
