package com.voting.survey_host.entity;

public final class DBConstants {

    // SURVEY QUERIES
    public static final String createSurveyQuery = "INSERT INTO survey (host_username, title) VALUES (?,?)";
    public static final String createQuestionQuery = "INSERT INTO question (survey_id, question) VALUES (?,?)";
    public static final String createChoiceQuery = "INSERT INTO choice (question_id, choice_text) VALUES (?,?)";
    public static final String getSurveysByHostQuery = "SELECT survey_id, title from survey where host_username=?";
    public static final String getQuestionsBySurveyQuery = "SELECT question_id, question from question where survey_id=?";
    public static final String getChoicesByQuestionQuery = "SELECT choice_id, choice from choices where question_id=?";
    public static final String getSurveyById =
            "select s1.survey_id, s1.host_username, s1.title, q1.question_id , q1.question, c1.choice_id, c1.choice \n" +
            "from survey s1 LEFT OUTER JOIN \n" +
            "question q1 ON s1.survey_id = q1.survey_id LEFT OUTER JOIN \n" +
            "choice c1 ON c1.question_id = q1.question_id \n" +
            "where s1.survey_id = ? order by q1.question_id, c1.choice_id;";

}
