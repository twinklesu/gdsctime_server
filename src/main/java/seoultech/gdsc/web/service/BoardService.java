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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    private final EntityManager entityManager;

    @Autowired
    public BoardService(BoardRepository boardRepository,
                        ModelMapper modelMapper,
                        UserRepository userRepository,
                        BoardCategoryRepository boardCategoryRepository,
                        EntityManager entityManager) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.boardCategoryRepository = boardCategoryRepository;
        this.entityManager = entityManager;
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

    /*
    각 게시판의 최신 글 조회
     */
    public List<BoardDto.RecentResponse> getRecentBoard(){
        // jpql은 entity field에 맞춰 작성
        String jpql = "select board from Board board where board.id in " +
                "(select max(board.id) from board group by board.boardCategory)" +
                "order by board.boardCategory.id asc";
        Query query = entityManager.createQuery(jpql);
        List<Board> boards = query.getResultList();
        List<BoardDto.RecentResponse> recentResponseList = boards.stream().map(board -> {
            BoardDto.RecentResponse boardDto = modelMapper.map(board, BoardDto.RecentResponse.class);
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            boardDto.setBoardCategoryId(board.getBoardCategory().getId());
            return boardDto;
        }).collect(Collectors.toList());
        return recentResponseList;
    }

    /*
    핫 게시글 조회
     */
//    public List<BoardDto.HotResponse> getHotBoard() {
//
//    }

    /*
    전체 글 검색
     */
    public List<BoardDto.SearchResponse> searchAll(String word) {
        List<BoardDto.SearchResponse> res = boardRepository.findAllByContentContaining(word).stream().map(board -> {
            BoardDto.SearchResponse boardDto = modelMapper.map(board, BoardDto.SearchResponse.class);
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            if (board.getIsSecret()) {
                boardDto.setNickName("익명");
            } else {
                boardDto.setNickName(board.getUser().getNickname());
            }
            return boardDto;
        }).collect(Collectors.toList());
        return res;
    }


}
