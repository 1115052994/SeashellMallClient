package com.liemi.seashellmallclient.data.event;

/**
 * Created by Bingo on 2018/7/17.
 */

public class RefreshChatUnreadNumEvent {
    public int unreadNum;       //-1=使用上次的值
    public boolean isHaveAddFriendUnread; //是否包含请求的未读

    public RefreshChatUnreadNumEvent(int unreadNum) {
        this.unreadNum = unreadNum;
        isHaveAddFriendUnread=true;
    }

    public RefreshChatUnreadNumEvent(int unreadNum, boolean isHaveAddFriendUnread) {
        this.unreadNum = unreadNum;
        this.isHaveAddFriendUnread = isHaveAddFriendUnread;
    }
}
