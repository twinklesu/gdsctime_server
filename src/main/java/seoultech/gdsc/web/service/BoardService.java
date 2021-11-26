package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.BoardDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.BoardCategoryRepository;
import seoultech.gdsc.web.repository.BoardRepository;
import seoultech.gdsc.web.repository.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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
    public BoardService(BoardRepository boardRepository,
                        ModelMapper modelMapper,
                        UserRepository userRepository,
                        BoardCategoryRepository boardCategoryRepository,
                        EntityManager entityManager) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.boardCategoryRepository = boardCategoryRepository;
    }

    /*
    특정 카테고리의 모든 글 조회
     */
    public List<BoardDto.Response> getCategoryList(int categoryId) {
        List<Board> boards;
        if (categoryId == 7) {
            boards = boardRepository.findAllByIsHotOrderByCreatedAtDesc(true);
        } else {
            boards = boardRepository.findAllByBoardCategory_IdOrderByCreatedAtDesc(categoryId);
        }
        List<BoardDto.Response> responses = boards.stream().map(board -> {
            BoardDto.Response boardDto = modelMapper.map(board, BoardDto.Response.class);
            // "2021-10-04T07:02:29.000+00:00" -> yyMMdd
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            if (board.getIsSecret()) {
                boardDto.setNickname("익명");
            } else {
                boardDto.setNickname(board.getUser().getNickname());
            }
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
        res.setBoardCategoryId(board.getBoardCategory().getId());
        return res;
    }

    /*
    게시글 작성
     */
    public int writeNew(BoardDto.Request request, int id) {
        Board newBoard = modelMapper.map(request, Board.class);
        Optional<User> writer = userRepository.findById(id);
        if (writer.isPresent()){
            newBoard.setUser(writer.get());
        }
        boardCategoryRepository.findById(request.getCategoryId()).ifPresent(newBoard::setBoardCategory); // 카테고리 연결
        boardRepository.save(newBoard);
        // 방금 작성한 글 id 리턴해주기
        Optional<Board> postBoard = boardRepository.findTopIdByUser_IdOrderByCreatedAtDesc(id);
        if (postBoard.isPresent()){
            return postBoard.get().getId();
        }
        return -1;
    }

    /*
    각 게시판의 최신 글 조회
     */
    public List<BoardDto.RecentResponse> getRecentBoard(){
        List<Board> boards = boardRepository.findRecentBoard();
        List<BoardDto.RecentResponse> recentResponseList = boards.stream().map(board -> {
            BoardDto.RecentResponse boardDto = modelMapper.map(board, BoardDto.RecentResponse.class);
            LocalDateTime before24 = LocalDateTime.now().minusDays(1);
            Boolean isNew = board.getCreatedAt().isAfter(before24);
            boardDto.setIsNew(isNew);
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            boardDto.setBoardCategoryId(board.getBoardCategory().getId());
            return boardDto;
        }).collect(Collectors.toList());
        return recentResponseList;
    }

    /*
    핫 게시글 조회
     */
    public List<BoardDto.HotResponse> getHotBoard() {
        List<Board> boardList = boardRepository.findTop2ByIsHotOrderByCreatedAtDesc(true);
        List<BoardDto.HotResponse> responses = boardList.stream().map(board -> {
            BoardDto.HotResponse boardDto = modelMapper.map(board, BoardDto.HotResponse.class);
            // "2021-10-04T07:02:29.000+00:00" -> yyMMdd
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            return boardDto;
        }).collect(Collectors.toList());
        return responses;
    }

    /*
    카테고리 별 최신2개, 최신핫게2개
     */
    public List<BoardDto.HotResponse> getFilteredBoard(int category, Boolean isHot) {
        List<BoardDto.HotResponse> response;
        List<Board> boards;
        if (isHot) {
            // 핫게만
            boards = boardRepository.findTop2ByIsHotAndBoardCategory_IdOrderByCreatedAtDesc(isHot, category);
        } else {
            // 모든글에서
            boards = boardRepository.findTop2ByBoardCategory_IdOrderByCreatedAtDesc(category);
        }
        response = boards.stream().map(board -> {
            BoardDto.HotResponse boardDto = modelMapper.map(board, BoardDto.HotResponse.class);
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            return boardDto;
        }).collect(Collectors.toList());
        return response;
    }

    /*
    글 검색
     */
    public List<BoardDto.SearchResponse> searchCategory(int category, String word) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByKeyword(word, word);
        } else {
            boards = boardRepository.findAllByCategoryByKeyword(word, word, category);
        }
        List<BoardDto.SearchResponse> res = boards.stream().map(board -> {
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

    /*
    실시간 인기 게시물
     */
    public List<BoardDto.DetailResponse> getRealtime() {
        List<Board> boards = boardRepository.findRealtime();
        List<BoardDto.DetailResponse> responses = boards.stream().map(board -> {
            BoardDto.DetailResponse boardDto = modelMapper.map(board, BoardDto.DetailResponse.class);
            boardDto.setBoardCategoryId(board.getBoardCategory().getId());
            if (board.getIsSecret()) {
                boardDto.setNickname("익명");
                boardDto.setProfilePic("익명프사");
            } else {
                boardDto.setNickname(board.getUser().getNickname());
                boardDto.setProfilePic(board.getUser().getProfilePic());
            }
            return boardDto;

        }).collect(Collectors.toList());
        return responses;
    }
}
