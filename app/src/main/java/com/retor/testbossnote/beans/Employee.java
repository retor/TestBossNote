package com.retor.testbossnote.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by retor on 14.10.2015.
 */
public class Employee implements Parcelable {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String picture_url;
    private int age;
    private int salary;

    public Employee() {
    }

    public Employee(String name, String surname, String patronymic, String picture_url) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.picture_url = picture_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", picture_url='" + picture_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.patronymic);
        dest.writeString(this.picture_url);
    }

    protected Employee(Parcel in) {
        this.name = in.readString();
        this.surname = in.readString();
        this.patronymic = in.readString();
        this.picture_url = in.readString();
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        public Employee createFromParcel(Parcel source) {
            return new Employee(source);
        }

        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
}
