package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

}
