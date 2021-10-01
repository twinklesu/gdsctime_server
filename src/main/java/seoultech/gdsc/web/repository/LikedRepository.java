package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoultech.gdsc.web.entity.Liked;

public interface LikedRepository extends JpaRepository<Liked, Integer> {
}
