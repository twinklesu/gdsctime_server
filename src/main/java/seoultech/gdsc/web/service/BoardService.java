package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import seoultech.gdsc.web.dto.BoardDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.BoardCategory;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.BoardCategoryRepository;
import seoultech.gdsc.web.repository.BoardRepository;
import seoultech.gdsc.web.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper, UserRepository userRepository, BoardCategoryRepository boardCategoryRepository) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.boardCategoryRepository = boardCategoryRepository;
    }

    /*
    특정 카테고리의 모든 글 조회
     */
    public List<BoardDto.Response> getCategoryList(int categoryId) {
        List<Board> boards = boardRepository.findAllByBoardCategory_Id(categoryId);
        List<BoardDto.Response> responses = boards.stream().map(board -> {
            BoardDto.Response boardDto = modelMapper.map(board, BoardDto.Response.class);
            // "2021-10-04T07:02:29.000+00:00" -> yyMMdd
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            boardDto.setBoardCategoryId(board.getBoardCategory().getId());
            return boardDto;
        }).collect(Collectors.toList());
        return responses;
    }

    /*
    특정 게시물 조회
     */
    public BoardDto.DetailResponse getBoardDetail(int id) {
        Board board = boardRepository.getById(id);
        BoardDto.DetailResponse res = modelMapper.map(board, BoardDto.DetailResponse.class);
        res.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
        if (board.getIsSecret()) {
            res.setNickname("익명");
            res.setProfilePic("https://익명사진url나중에해야지...");
        } else {
            res.setNickname(board.getUser().getNickname());
            res.setProfilePic(board.getUser().getProfilePic());
        }
        return res;
    }

    /*
    게시글 작성
     */
    public void writeNew(BoardDto.Request request, int id) {
        Board newBoard = modelMapper.map(request, Board.class);
        Optional<User> writer = userRepository.findById(id);
        if (writer.isPresent()){
            newBoard.setUser(writer.get());
        }
        boardCategoryRepository.findById(request.getCategoryId()).ifPresent(newBoard::setBoardCategory); // 카테고리 연결
        boardRepository.save(newBoard);
    }
}
