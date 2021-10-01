package seoultech.gdsc.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoultech.gdsc.web.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
