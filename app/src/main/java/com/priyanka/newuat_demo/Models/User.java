package com.priyanka.newuat_demo.Models;

public class User {

    String name;
    String id;
    String created_at;
    String updated_at;
    String deleted_at;
    String assigned_user_id;
    String first_name;
    String last_name;
    String user_type;
    String designation;
    String department;
    String phone;
    String address;

    public User(String name, String id, String created_at, String updated_at, String deleted_at, String assigned_user_id, String first_name, String last_name, String user_type, String designation, String department, String phone, String address) {
        this.name = name;
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.assigned_user_id = assigned_user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_type = user_type;
        this.designation = designation;
        this.department = department;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getAssigned_user_id() {
        return assigned_user_id;
    }

    public void setAssigned_user_id(String assigned_user_id) {
        this.assigned_user_id = assigned_user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
