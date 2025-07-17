package com.example.pregnancytrackerignite.data.models;

import com.squareup.moshi.Json;

import java.util.List;

public class DetailsBlogsClass {
    @Json(name = "Details")
    private List<DetailsBlogModel> blogs = null;

    public List<DetailsBlogModel> getBlogs() {
        return blogs;
    }
}