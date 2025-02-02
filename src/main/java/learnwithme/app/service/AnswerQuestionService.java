package learnwithme.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import learnwithme.app.model.Question;

@Service
public class AnswerQuestionService {
	
	public List<Question> questionAndAnswers(String string){
		
	List<Question> questionAnswerMap = new ArrayList<>();
	        // Split the input string by "Answer:" and "Question:"
	 String[] arrayString = string.split("Question:");
	 for(String lstring : arrayString) {
		 if(!lstring.isEmpty()) {
		 questionAnswerMap.add(new Question(lstring.substring(0,lstring.indexOf("Answer:")),lstring.substring(lstring.indexOf("Answer:")+"Answer:".length())));
		 }
	 }
	 return questionAnswerMap;

		
		
	}
	


}
