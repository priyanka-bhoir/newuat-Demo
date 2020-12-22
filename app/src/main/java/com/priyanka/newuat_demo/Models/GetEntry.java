package com.priyanka.newuat_demo.Models;

public class GetEntry {
    String data;
    String links;
    String meta;

    public GetEntry(String data, String links, String meta) {
        this.data = data;
        this.links = links;
        this.meta = meta;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
