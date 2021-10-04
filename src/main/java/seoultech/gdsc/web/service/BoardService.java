package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import seoultech.gdsc.web.dto.BoardDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.User;
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

    @Autowired
    public BoardService(BoardRepository boardRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;

    }

    public List<BoardDto.Response> getCategoryList(int categoryId) {
        List<Board> boards = boardRepository.findAllByCategory(categoryId);
        List<BoardDto.Response> responses = boards.stream().map(board -> {
            BoardDto.Response boardDto = modelMapper.map(board, BoardDto.Response.class);
            // "2021-10-04T07:02:29.000+00:00" -> yymmdd
            boardDto.setCreatedAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
            boardDto.setBoardId(board.getId());
            if (board.getIsSecret()) {
                boardDto.setNickname("익명");
                boardDto.setProfilePic("https://익명사진url나중에해야지...");
            } else {
                boardDto.setNickname(board.getUser().getNickname()); // 컬럼 명 바꿔야겠는데 ㅋㅋㅋㅋㅋ
                boardDto.setNickname(board.getUser().getProfilePic());
            }
            return boardDto;
        }).collect(Collectors.toList());
        return responses;
    }

    public BoardDto.Response getBoardDetail(int id) {
        Board board = boardRepository.getById(id);
        BoardDto.Response res = modelMapper.map(board, BoardDto.Response.class);
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

    public BoardDto.Response writeNew(BoardDto.Request request, int id) {
        Board newBoard = modelMapper.map(request, Board.class);
        Optional<User> writer = userRepository.findById(id);
        if (writer.isPresent()){
            newBoard.setUser(writer.get());
        }
        Board writtenBoard = boardRepository.save(newBoard);

        // return response
        BoardDto.Response res = modelMapper.map(writtenBoard, BoardDto.Response.class);
        if (newBoard.getIsSecret()) {
            res.setNickname("익명");
            res.setProfilePic("https://익명프사주소");
        } else {
            writer.ifPresent(user -> {
                res.setNickname(user.getNickname());
                res.setProfilePic(user.getProfilePic());
            });
        res.setCreatedAt(writtenBoard.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMdd")));
        }
        return res;
    }
}
