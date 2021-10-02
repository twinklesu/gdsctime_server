package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
