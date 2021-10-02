package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoultech.gdsc.web.entity.Liked;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Integer> {
}
