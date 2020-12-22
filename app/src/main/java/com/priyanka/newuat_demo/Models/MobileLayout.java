package com.priyanka.newuat_demo.Models;

public class MobileLayout {
    String id;
    String modulename;
    String modulelabel;
    String layoutdefs;
    String fields;


    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getModulelabel() {
        return modulelabel;
    }

    public void setModulelabel(String modulelabel) {
        this.modulelabel = modulelabel;
    }

    public String getLayoutdefs() {
        return layoutdefs;
    }

    public void setLayoutdefs(String layoutdefs) {
        this.layoutdefs = layoutdefs;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public MobileLayout(String modulename, String modulelabel, String layoutdefs, String fields) {
        this.modulename = modulename;
        this.modulelabel = modulelabel;
        this.layoutdefs = layoutdefs;
        this.fields = fields;
    }
}
