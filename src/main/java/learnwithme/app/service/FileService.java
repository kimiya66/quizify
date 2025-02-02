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

@Service
public class FileService {

    @Autowired
    private UploadedFileRepository fileRepository;

    private final String uploadDir = "uploads/";

    public UploadedFile saveFile(MultipartFile file, AppUser appuser) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(fileName);
        uploadedFile.setFilePath(filePath.toString());
        uploadedFile.setAppUser(appuser); // Assuming you have a current user context

        return fileRepository.save(uploadedFile);
    }
}
