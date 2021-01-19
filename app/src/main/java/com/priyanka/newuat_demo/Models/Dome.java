package com.priyanka.newuat_demo.Models;

public class Dome {
    String DomName;
    String DomValue;

    public Dome(String domName, String domValue) {
        DomName = domName;
        DomValue = domValue;
    }

    public String getDomName() {
        return DomName;
    }

    public void setDomName(String domName) {
        DomName = domName;
    }

    public String getDomValue() {
        return DomValue;
    }

    public void setDomValue(String domValue) {
        DomValue = domValue;
    }
}
