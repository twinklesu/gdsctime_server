package seoultech.gdsc.web.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seoultech.gdsc.web.dto.CommentDto;
import seoultech.gdsc.web.entity.Board;
import seoultech.gdsc.web.entity.Comment;
import seoultech.gdsc.web.entity.User;
import seoultech.gdsc.web.repository.BoardRepository;
import seoultech.gdsc.web.repository.CommentRepository;
import seoultech.gdsc.web.repository.UserRepository;

import javax.swing.text.html.Option;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, UserRepository userRepository, BoardRepository boardRepository){
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    /*
    ๋๊ธ ์กฐํ
     */
    public List<CommentDto.Response> getComments(int boardId) {
        List<Comment> comments = commentRepository.findAllByBoard_Id(boardId);
        List<CommentDto.Response> responses = comments.stream().map(comment -> {
            CommentDto.Response commentDto = modelMapper.map(comment, CommentDto.Response.class);
            // "2021-10-04T07:02:29.000+00:00" -> yyMMddhhmm
            commentDto.setCreatedAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyMMddhhmm")));
            if (comment.getUser() != null) {
                userRepository.findById(comment.getUser().getId()).ifPresent(user -> {
                    commentDto.setUserId(user.getId());
                });
                if (comment.getIsSecret()) {
                    commentDto.setNickname("์ต๋ช");
                    commentDto.setProfilePic("https://์ต๋ช์ฌ์งurl");
                } else {
                    commentDto.setNickname(comment.getUser().getNickname());
                    commentDto.setProfilePic(comment.getUser().getProfilePic());
                }
            } else {
                commentDto.setNickname("์ต๋ช");
                commentDto.setProfilePic("https://์ต๋ช์ฌ์งurl");
            }


            return commentDto;
        }).collect(Collectors.toList());
        return responses;
    }

    /*
    ๋๊ธ ์์ฑ
    ๋๊ธ ์์ฑ์ board์ ๋๊ธ ๊ฐฏ์ ์ฌ๋?ค์ค์ผํจ
     */
    public void writeNew(CommentDto.Request request, int id) {
        Comment newComment = modelMapper.map(request, Comment.class);
        userRepository.findById(id).ifPresent(newComment::setUser); // ์์ฑ์ ์ฐ๊ฒฐ
        Board thisBoard = boardRepository.getById(request.getBoardId()); // ๊ฒ์๊ธ ๊ฐ์?ธ์ค๊ธฐ
        newComment.setBoard(thisBoard);
        thisBoard.setCommentNum(thisBoard.getCommentNum()+1);
        boardRepository.save(thisBoard); // ๊ฒ์๊ธ ๋๊ธ ์ ์ฌ๋ฆผ
        commentRepository.save(newComment); // ๋๊ธ ์?์ฅ
    }
}
