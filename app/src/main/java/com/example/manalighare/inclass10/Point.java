package com.example.manalighare.inclass10;

public class Point {
    String latitude;
    String longitude;

    public Point(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Point{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
