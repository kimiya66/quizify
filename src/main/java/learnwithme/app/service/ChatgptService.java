package learnwithme.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import learnwithme.app.model.ChatGpt;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class ChatgptService {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${openai.api.uri}")
	private String apiUrl; 
	
	@Value("${openai.api.key}")
	private String apiKey; 
	
	
	
public String runBart() {
	
	ProcessBuilder processbuilder = new ProcessBuilder("python","C:\\PythonFiles\\pythonProject\\QuestionAndANswer.py");
	try {
		Process process = processbuilder.start();
		StringBuilder stringbuilder = new StringBuilder(); 
		String line1; 
		  OutputStream outputStream = process.getOutputStream();  
		  BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\PythonFiles\\pythonProject\\TextFile.txt"));
		  while((line1=bufferedReader.readLine())!=null) {
			  stringbuilder.append(line1);
			  line1 = line1+"\n";
			  outputStream.write(line1.getBytes());
			}
		//  stringbuilder.append("\n");
		//  outputStream.write("\n".getBytes());
		  outputStream.flush();
          outputStream.close();
          bufferedReader.close();
//		  BufferedWriter bufwriter = new BufferedWriter(new OutputStreamWriter(outputStream));
//		  bufwriter.("Hello from Java\n".getBytes());
//          outputStream.flush();
//          outputStream.close(); 
		
		bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=""; 
		StringBuilder output = new StringBuilder(); 
		while((line=bufferedReader.readLine())!=null) {
			output.append(line);
		}
		
		bufferedReader.close();
		return output.toString();
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return "";
		
		
	}

public String extractText() throws IOException {
	
	String line1; 
	 
	StringBuilder stringbuilder = new StringBuilder(); 
	  BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\PythonFiles\\pythonProject\\TextFile.txt"));
	  while((line1=bufferedReader.readLine())!=null) {
		  stringbuilder.append(line1);
		 
		
		}
	  return stringbuilder.toString(); 
	
}


	
    public String extractTextFromImage(File imageFile) throws Exception {
    	
    	//System.setProperty("TESSDATA_PREFIX", "C:\\Users\\kimia\\.m2\\repository\\net\\sourceforge\\tess4j\\tess4j\\5.13.0\\tess4j-5.13.0.jar");
    	String tessdataPath = extractTessData();
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessdataPath); // Set the path to tessdata
        tesseract.setLanguage("eng"); // Set the language

        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    private String extractTessData() throws Exception {
        // Create a temporary directory
        Path tempDir = Files.createTempDirectory("tessdata");
        
        // List of files you expect in the tessdata directory
        String[] languageFiles = {"eng.traineddata"}; // Add any other languages you need

        // Extract each file from the JAR to the temporary directory
        for (String fileName : languageFiles) {
            try (InputStream inputStream = getClass().getResourceAsStream("/tessdata/" + fileName);
                 OutputStream outputStream = new FileOutputStream(tempDir.resolve(fileName).toFile())) {
                if (inputStream == null) {
                    throw new RuntimeException("File not found: " + fileName);
                }
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }

        return tempDir.toString();
    }
    
    
    public String callOpenAI(String prompt) throws IOException {
    	String promptInline="create questions and answers from the following text, also Start each question with Question: and follow up with an answer starting it with Answer:.Here is the text: ";
        HttpHeaders headers = new HttpHeaders();
        String Text = this.extractText();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        
        String requestBody = "{"
                + "\"model\": \"gpt-4\", "  // Specify the latest model here
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" +  promptInline + Text +  "\"}]"
                + "}";
     
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        // Extracting the data from the JSON response
        ChatGpt openAIResponse = objectMapper.readValue(response.getBody(), ChatGpt.class);

        // Assuming you want the content from the first choice
        return openAIResponse.getChoices().get(0).getMessage().getContent();
        
    }

	


}
