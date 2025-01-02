//package com.voting.survey_host.resultSetExtractor;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.ResultSetExtractor;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class SurveyResultSetExtractor implements ResultSetExtractor<Survey> {
//    @Override
//    public Survey extractData(ResultSet rs) throws SQLException, DataAccessException {
//        Survey survey = null;
//        Map<Long, Question> questionMap = new HashMap<>();
//        while(rs.next()) {
//            if(survey == null) {
//                survey = new Survey();
//                survey.setSurveyId(rs.getLong("survey_id"));
//                survey.setTitle(rs.getString("title"));
//                survey.setHostUsername(rs.getString("host_username"));
//                survey.setQuestionList(new ArrayList<>());
//            }
//
//            long questionId = rs.getLong("question_id");
//            Question question = questionMap.get(questionId);
//
//            if(question == null && questionId != 0) {
//                question = new Question();
//                question.setQuestionId(questionId);
//                question.setQuestion(rs.getString("question"));
//                question.setSurveyId(survey.getSurveyId());
//                question.setChoices(new ArrayList<>());
//                survey.getQuestionList().add(question);
//                questionMap.put(questionId, question);
//            }
//
//            long choiceId = rs.getLong("choice_id");
//            if(choiceId != 0) {
//                Choice choice = new Choice();
//                choice.setChoiceId(rs.getLong("choice_id"));
//                choice.setQuestionId(questionId);
//                choice.setChoice(rs.getString("choice"));
//                question.getChoices().add(choice);
//            }
//        }
//        return survey;
//    }
//}
