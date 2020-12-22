package com.priyanka.newuat_demo.Models;

public class module_pojo {
    String id;
    String name;
    String singular;
    String plural;


    public module_pojo(String name, String singular, String plural) {
        this.name = name;
        this.singular = singular;
        this.plural = plural;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }
}
