package com.tradegenie.android.tradegenieapp.Models;

public class NavItem {
    public String mTitle;
    public String mSubtitle;
    public Boolean IsMessageArrived;
    public int mIcon;
    public boolean isSelected;

    public Boolean getMessageArrived() {
        return IsMessageArrived;
    }

    public void setMessageArrived(Boolean messageArrived) {
        IsMessageArrived = messageArrived;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmSubtitle() {
        return mSubtitle;
    }

    public void setmSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public NavItem(String title, String subtitle, int icon, boolean isSelected, boolean IsMessageArrived) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
        this.isSelected = isSelected;
        this.IsMessageArrived = IsMessageArrived;

    }
}
