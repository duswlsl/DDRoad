package com.seoul.ddroad.map;

public class APIData {
    Data cafe[] = new Data[1000];
    Data hospital[] = new Data[1000];
    Data hotel[] = new Data[1000];
    Data salon[] = new Data[1000];
    Data trail[] = new Data[1000];

    public static int cafeTotal = 0;
    public static int salonTotal = 0;
    public static int hotelTotal = 0;
    public static int trailTotal = 0;
    public static int hospitalTotal = 0;

    APIData() {
        for (int i = 0; i < 1000; i++) {
            cafe[i] = new Data();
            hospital[i] = new Data();
            hotel[i] = new Data();
            salon[i] = new Data();
            trail[i] = new Data();
        }
    }
}