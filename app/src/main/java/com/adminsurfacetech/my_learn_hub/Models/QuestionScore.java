package com.adminsurfacetech.my_learn_hub.Models;

public class QuestionScore {
    private String Question_Score;
    private String UserId;
    private String Score;
    private String CategoryId;
    private String CategoryName;
    private String Userimg;

    public QuestionScore() {
    }

    public QuestionScore(String question_Score, String userId, String score, String categoryId, String categoryName, String userimg) {
        Question_Score = question_Score;
        UserId = userId;
        Score = score;
        CategoryId = categoryId;
        CategoryName = categoryName;
        Userimg = userimg;
    }

    public String getQuestion_Score() {
        return Question_Score;
    }

    public void setQuestion_Score(String question_Score) {
        Question_Score = question_Score;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getUserimg() {
        return Userimg;
    }

    public void setUserimg(String userimg) {
        Userimg = userimg;
    }
}
