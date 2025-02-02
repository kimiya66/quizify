package learnwithme.app.service;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import learnwithme.app.model.AppUser;
import learnwithme.app.model.Question;
import learnwithme.app.persistence.UploadedFileRepository;
import learnwithme.app.persistence.UserRepository;


@Controller
public class LearnController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ChatgptService chatgptService;
	
	 @Autowired
	  private AnswerQuestionService answerquestionService;
	
	 @Autowired
	  private FileService fileService;
	 
	 @Autowired 
	 private UserService userservice; 
	 
	 @Autowired
	private UserRepository userrepo;
	
	@Value("${openweather.api.key}")
	private String apiKey;
	
	@Value("${openweather.api.url}")
	private String apiUrl; 
	
	
	
	
	
	
	@GetMapping("/learn")
	public String weather( Model model) throws Exception {

		String string = chatgptService.extractTextFromImage(new File("C:\\Spring-Project\\Imgaes\\CodesnippetAndText.PNG"));
     
        model.addAttribute("text",string.replaceAll(".*[;{}()].*",""));
      return "displayText";
       // summary = summary.substring(summary.indexOf(':'),summary.indexOf('}')-1);
     
	}
	
	@GetMapping("/question")
	public @ResponseBody List<Question> question( Model model) throws Exception {

		//String string = chatgptService.extractTextFromImage(new File("C:\\Spring-Project\\Imgaes\\Screenshot.PNG"));
      
      //  String summary = chatgptService.runBart();
         
      return  answerquestionService.questionAndAnswers(chatgptService.callOpenAI(""));
       // summary = summary.substring(summary.indexOf(':'),summary.indexOf('}')-1);
    
         
      
	}
	
	
	  @GetMapping("/upload")
    public String showUploadForm() {
        return "upload"; // A Thymeleaf template for the upload form
    }
	  
	  @GetMapping("/welcomepage")
	    public String showUserPage(Model model) {
		  model.addAttribute("appuser", new AppUser()); // Ensure this matches the expected name
	        return "welcomepage"; // A Thymeleaf template for the upload form
	    }
	  
	  @PostMapping("/appuser")
	    public String createUser(@ModelAttribute AppUser appuser, Model model) {
	        try {
	        	userservice.saveUser(appuser.getName());
	            model.addAttribute("message", "Name saved successfully!");
	        } catch (IOException e) {
	            model.addAttribute("message", "\"Name saved successfully!" + e.getMessage());
	        }
	        return "upload"; // Redirect back to upload form or another page
	    }
	
	
	
//	@PostMapping("/upload")
//    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
//        }
//
//        // Define the path to save the uploaded file
//        String filePath = "C:\\Spring-Project\\Imgaes\\" + file.getOriginalFilename();
//
//        try {
//            // Save the file locally
//            file.transferTo(new File(filePath));
//            return ResponseEntity.ok("File uploaded successfully: " + filePath);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
//        }
//    }
	  
	  @PostMapping("/upload")
	    public String uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("username") String username,Model model) {
		  AppUser appuser = userrepo.findByName(username); // Retrieve the current user
	        try {
	            fileService.saveFile(file, appuser);
	            model.addAttribute("message", "File uploaded successfully!");
	        } catch (IOException e) {
	            model.addAttribute("message", "File upload failed: " + e.getMessage());
	        }
	        return "upload"; // Redirect back to upload form or another page
	    }
	  
	  
	  @GetMapping("/project")
		public String project( Model model) throws Exception {

			//String string = chatgptService.extractTextFromImage(new File("C:\\Spring-Project\\Imgaes\\Screenshot.PNG"));
	     
	        
	      return   "projectPage";
	       // summary = summary.substring(summary.indexOf(':'),summary.indexOf('}')-1);
	     
		}
	


}
