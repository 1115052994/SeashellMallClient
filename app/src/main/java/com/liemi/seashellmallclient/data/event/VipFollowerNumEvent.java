package com.liemi.seashellmallclient.data.event;

public class VipFollowerNumEvent {
    public int num;
    public int position;
    public VipFollowerNumEvent(int position, int num){
        this.position = position;
        this.num = num;
    }
}
