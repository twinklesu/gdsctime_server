package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoultech.gdsc.web.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
