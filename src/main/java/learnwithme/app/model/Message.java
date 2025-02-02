package learnwithme.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
	 @JsonProperty("content")
	String content;
	 
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	} 
	
	

}
