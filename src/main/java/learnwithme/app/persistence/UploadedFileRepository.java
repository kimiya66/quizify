package learnwithme.app.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import learnwithme.app.model.UploadedFile;
import learnwithme.app.model.AppUser;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findByAppUser(AppUser appuser);
}
