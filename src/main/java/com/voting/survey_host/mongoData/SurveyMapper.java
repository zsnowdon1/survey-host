package com.voting.survey_host.mongoData;

import com.voting.survey_host.entity.ChoiceDTO;
import com.voting.survey_host.entity.QuestionDTO;
import com.voting.survey_host.entity.SurveyDTO;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class SurveyMapper {

    public static Survey toEntitySurvey(SurveyDTO surveyDTO) {
        Survey survey = new Survey();
        survey.setTitle(surveyDTO.getTitle());
        survey.setUsername(surveyDTO.getHostUsername());
        survey.setSurveyId(surveyDTO.getSurveyId());
        if(!isNull(surveyDTO.getQuestions()))
            survey.setQuestions(toEntityQuestionList(surveyDTO.getQuestions()));
        return survey;
    }

    public static SurveyDTO toDTOSurvey(Survey survey) {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setSurveyId(survey.getSurveyId());
        surveyDTO.setTitle(survey.getTitle());
        surveyDTO.setHostUsername(survey.getUsername());
        if(!isNull(survey.getQuestions()))
            surveyDTO.setQuestions(toDTOQuestionList(survey.getQuestions()));
        return surveyDTO;
    }

    public static Question toEntityQuestion(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setQuestionText(questionDTO.getQuestionText());
        if(!isNull(questionDTO.getChoices()))
            question.setChoices(toEntityChoiceList(questionDTO.getChoices()));
        return question;
    }

    public static QuestionDTO toDTOQuestion(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestionText(question.getQuestionText());
        if(!isNull(question.getChoices()))
            questionDTO.setChoices(toDTOChoiceList(question.getChoices()));
        return questionDTO;
    }

    public static Choice toEntityChoice(ChoiceDTO choiceDTO) {
        Choice choice = new Choice();
        choice.setChoiceText(choiceDTO.getChoiceText());
        choice.setChoiceId(choiceDTO.getChoiceId() != null ? choiceDTO.getChoiceId() : null);
        return choice;
    }

    public static ChoiceDTO toDTOChoice(Choice choice) {
        ChoiceDTO choiceDTO = new ChoiceDTO();
        choiceDTO.setChoiceText(choice.getChoiceText());
        choiceDTO.setChoiceId(choice.getChoiceId());
        return choiceDTO;
    }

    private static List<Question> toEntityQuestionList(List<QuestionDTO> questionDTOList) {
        return questionDTOList.stream()
                .map(SurveyMapper::toEntityQuestion)
                .collect(Collectors.toList());
    }

    private static List<QuestionDTO> toDTOQuestionList(List<Question> questionList) {
        return questionList.stream()
                .map(SurveyMapper::toDTOQuestion)
                .collect(Collectors.toList());
    }

    private static List<Choice> toEntityChoiceList(List<ChoiceDTO> choiceDTOList) {
        return choiceDTOList.stream()
                .map(SurveyMapper::toEntityChoice)
                .collect(Collectors.toList());
    }

    private static List<ChoiceDTO> toDTOChoiceList(List<Choice> choiceList) {
        return choiceList.stream()
                .map(SurveyMapper::toDTOChoice)
                .collect(Collectors.toList());
    }
}
