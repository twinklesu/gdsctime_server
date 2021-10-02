package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByUserId(String userId);
    boolean existsUserByEmail(String email);
    boolean existsUserByNickname(String nickname);
    boolean existsUserByHp(String hp);

    User getUserByUserId(String userId);

}
