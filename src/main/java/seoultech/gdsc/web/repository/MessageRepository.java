package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoultech.gdsc.web.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
