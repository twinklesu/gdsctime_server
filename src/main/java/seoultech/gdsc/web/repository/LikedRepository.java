package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.Liked;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Integer> {
    List<Liked> findAllByUser_Id(@Param(value = "userId") int userId);
    Optional<Liked> findByUser_IdAndLikeCategoryAndRefId(@Param(value="userId") int userId,
                                                         @Param(value="LikeCategory") int category,
                                                         @Param(value="refId") int refId);
}
