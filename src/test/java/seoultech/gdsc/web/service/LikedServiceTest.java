package seoultech.gdsc.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seoultech.gdsc.web.dto.LikedDto;

@SpringBootTest
@Transactional
public class LikedServiceTest {

    @Autowired
    private LikedService likedService;

    @Test
    @Transactional
    public void saveLikeTest() {
        LikedDto.Request req = new LikedDto.Request(2, 3);

        String result = likedService.saveLike(1, req);
        System.out.println("success:" + result);

        result = likedService.saveLike(1, req);
        System.out.println("board fail test: " + result);
    }
}
