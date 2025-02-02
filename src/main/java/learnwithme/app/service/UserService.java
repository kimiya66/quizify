package learnwithme.app.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import learnwithme.app.model.UploadedFile;
import learnwithme.app.model.AppUser;
import learnwithme.app.persistence.UploadedFileRepository;
import learnwithme.app.persistence.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

  
    public AppUser saveUser(String string) throws IOException {
    	AppUser appuser = new AppUser(); 
    	appuser.setName(string);
        return userRepository.save(appuser);
    }
}
