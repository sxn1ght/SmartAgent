package com.ksu.database;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SxNight on 2015/5/10.
 */
public class KeywordJsonBean {
    @SerializedName("Category")
    private String CategoryStr;

    @SerializedName("Keyword")
    private String KeywordStr;

    @SerializedName("TimeCategory")
    private String TimeCategory;

    public String getCategoryStr() {
        return CategoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        CategoryStr = categoryStr;
    }

    public String getKeywordStr() {
        return KeywordStr;
    }

    public void setKeywordStr(String keywordStr) {
        KeywordStr = keywordStr;
    }

    public String getTimeCategory() {
        return TimeCategory;
    }

    public void setTimeCategory(String timeCategory) {
        TimeCategory = timeCategory;
    }
}
