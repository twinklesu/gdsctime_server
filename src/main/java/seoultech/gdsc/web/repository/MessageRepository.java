package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.Message;
import seoultech.gdsc.web.entity.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByFromUser_Id(@Param(value = "userId") int userId);
    List<Message> findAllByToUser_Id(@Param(value = "userId") int userId);
    @Query(value = "select * from message where id in (select id from (select least(from_user_id, to_user_id) as user1, greatest(from_user_id, to_user_id) as user2, max(id) as id from message where from_user_id = ?1  or to_user_id = ?2 group by user1, user2) as subtable) order by created_at desc", nativeQuery = true)
    List<Message> findAllRecent(int fromId, int toId);

    @Query(value = "select * from message where to_user_id in (?1, ?2) and from_user_id in (?1, ?2) order by created_at desc", nativeQuery = true)
    List<Message> findDetailMessage(int userId, int otherId);
}
