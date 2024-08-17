package com.voting.survey_host.entity;

public final class DBConstants {

    public static final String createSurveyQuery = "INSERT INTO survey (host_username, title) VALUES (?,?)";
    public static final String createQuestionQuery = "INSERT INTO question (survey_id, question) VALUES (?,?)";
    public static final String createChoiceQuery = "INSERT INTO choice (question_id, choice) VALUES (?,?)";
    public static final String getSurveysByHostQuery = "SELECT survey_id, title FROM survey WHERE host_username=?";
    public static final String getQuestionByIdQuery = "SELECT survey_id, question FROM question WHERE question_id=?";
    public static final String getQuestionsBySurveyQuery = "SELECT question_id, question FROM question WHERE survey_id=?";
    public static final String getChoicesByQuestionQuery = "SELECT choice_id, choice FROM choice WHERE question_id=?";
    public static final String deleteChoiceQuery = "DELETE FROM choice WHERE choice_id=?";
    public static final String deleteQuestionQuery = "DELETE FROM question WHERE question_id=?";
    public static final String deleteChoiceByQuestionQuery = "DELETE FROM choice WHERE question_id=?";
    public static final String getSurveyById =
            "select s1.survey_id, s1.host_username, s1.title, q1.question_id , q1.question, c1.choice_id, c1.choice \n" +
            "from survey s1 LEFT OUTER JOIN \n" +
            "question q1 ON s1.survey_id = q1.survey_id LEFT OUTER JOIN \n" +
            "choice c1 ON c1.question_id = q1.question_id \n" +
            "where s1.survey_id = ? order by q1.question_id, c1.choice_id;";

}
