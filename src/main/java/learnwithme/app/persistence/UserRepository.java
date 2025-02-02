package learnwithme.app.persistence;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import learnwithme.app.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	AppUser findByName(String name);
}



