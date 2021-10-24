package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.LikedDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.Comment;
import seoultech.gdsc.web.entity.Liked;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.BoardRepository;
import seoultech.gdsc.web.repository.CommentRepository;
import seoultech.gdsc.web.repository.LikedRepository;
import seoultech.gdsc.web.repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class LikedService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final LikedRepository likedRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public LikedService(UserRepository userRepository,
                        ModelMapper modelMapper,
                        LikedRepository likedRepository,
                        BoardRepository boardRepository,
                        CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.likedRepository = likedRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    /*
    좋아요 이미 눌렀는지 확인하고, 이미 눌렀을 경우 fail
    처음 누르는 경우 좋아요 추가하고, 게시글/댓글에 좋아요 수 올려주기
    게시글의 경우, 좋아요가 10이 되었을 경우 is_hot 마킹
     */
    public String saveLike(int id, LikedDto.Request req) {
        User user = userRepository.getById(id);
        // 이미 눌렀는지 확인하기
        Optional<Liked> alreadyLiked = likedRepository.findByUser_IdAndLikeCategoryAndRefId(id, req.getCategory(), req.getRefId());
        if (alreadyLiked.isPresent()) {
            if (req.getCategory() == 1) {
                return "이미 공감한 글입니다";
            } else {
                return "이미 공감한 댓글입니다";
            }
        }
        // 최초 공감
        Liked liked = modelMapper.map(req, Liked.class);
        liked.setUser(user);
        liked.setLikeCategory(req.getCategory());
        likedRepository.save(liked);

        if (req.getCategory() == 1) {
            // 게시글인 경우
            Optional<Board> board = boardRepository.findById(req.getRefId());
            board.ifPresent(board1 -> {
                        board1.setLikeNum(board1.getLikeNum()+1);
                        if (board1.getLikeNum() == 10) {
                            board1.setIsHot(true);
                        }
                        boardRepository.save(board1);
            });
        } else {
            // 댓글인 경우
            Optional<Comment> comment = commentRepository.findById(req.getRefId());
            comment.ifPresent(comment1 -> {
                comment1.setLikeNum(comment1.getLikeNum()+1);
                commentRepository.save(comment1);
            });
        }
        return "";
    }
}
