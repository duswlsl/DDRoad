package com.seoul.ddroad.diary;

public class SingerItem {


    String mobile;
    int resId;

    public SingerItem(String mobile, int resId) {
        this.mobile = mobile;
        this.resId = resId;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
