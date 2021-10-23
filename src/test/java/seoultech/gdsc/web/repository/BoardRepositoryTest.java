package seoultech.gdsc.web.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.WebApplicationTests;
import seoultech.gdsc.web.entity.Board;

import java.util.List;

@SpringBootTest
@Transactional
public class BoardRepositoryTest extends WebApplicationTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void findAllByCategoryTest() throws JsonProcessingException {
        System.out.println("#######findAllByCategoryTest########");
        List<Board> boardList = boardRepository.findAllByBoardCategory_Id(1);
        System.out.println(objectMapper.writeValueAsString(boardList));
        System.out.println("#########End#############");
    }

}


/*
* [{"id":2,
* "user_id":{"id":92,"user_id":"test_id","password":"$2a$10$cwQie0y8MaAvJ1b53YIWHuNAhRz2C53juXQzf.W/glSEJkMlcIThe","email":"test@test.com","hp":"test_hp","name":"test_name","nickname":"test_nick","major":"test_major","profile_pic":null,"is_auth":null,"created_at":null,"updated_at":null},
* "title":"에브리타임",
* "content":"GDSC 클론 코딩 정말 재밌어~",
* "category":1,
* "image_url":null,
* "is_secret":true,
* "like_num":0,
* "comment_num":0,
* "created_at":"2021-10-04T07:02:29.000+00:00",
* "updated_at":"2021-10-04T07:02:29.000+00:00"
* },
* {"id":3,"user_id":{"id":92,"user_id":"test_id","password":"$2a$10$cwQie0y8MaAvJ1b53YIWHuNAhRz2C53juXQzf.W/glSEJkMlcIThe","email":"test@test.com","hp":"test_hp","name":"test_name","nickname":"test_nick","major":"test_major","profile_pic":null,"is_auth":null,"created_at":null,"updated_at":null},"title":"재밌는 풀스택 코딩","content":"프론트는 리액트, 백은 스프링","category":1,"image_url":null,"is_secret":true,"like_num":0,"comment_num":0,"created_at":"2021-10-04T07:03:04.000+00:00","updated_at":"2021-10-04T07:03:04.000+00:00"}]
*/
