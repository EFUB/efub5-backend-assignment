package efub.assignment.community.post.controller;

import efub.assignment.community.global.exception.BlogException;
import efub.assignment.community.global.exception.ExceptionCode;
import efub.assignment.community.global.exception.GlobalExceptionHandler;
import efub.assignment.community.member.entity.Member;
import efub.assignment.community.member.repository.MembersRepository;
import efub.assignment.community.post.domain.Post;
import efub.assignment.community.post.dto.response.PostResponse;
import efub.assignment.community.post.repository.PostRepository;
import efub.assignment.community.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostControllerTest {


    @Autowired MockMvc mockMvc;
    @Autowired MembersRepository membersRepository;
    @Autowired PostRepository postRepository;

    @BeforeEach
    void seed(){
        Member member = new Member("e@e.com", "testpw","efub", "대학교", "2323");
        membersRepository.save(member);
    }

    @Test
    @DisplayName("GET /posts/{id} > 200 & 응답 필드 검증")
    void getPost_200() throws Exception{
        //given
        Member member = membersRepository.findAll().get(0);
        Post post = new Post("제목", "내용은다섯글자이상", member);
        post = postRepository.save(post);

        // when then
        mockMvc.perform(get("/posts/{id}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"));
    }

    @Test
    @DisplayName("GET /posts/{id} > 404 & 존재하지 않는 게시물 조회")
    void getPost_404() throws Exception {
        // given - 존재하지 않는 게시물 ID
        Long nonExistentPostId = 999L;

        // when then
        mockMvc.perform(get("/posts/{id}", nonExistentPostId))
                .andExpect(status().isNotFound());
    }


}