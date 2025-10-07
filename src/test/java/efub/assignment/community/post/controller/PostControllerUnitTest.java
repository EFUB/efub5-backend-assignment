package efub.assignment.community.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.post.dto.request.PostLikeDto;
import efub.assignment.community.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // dto 객체 JSON 직렬화용 (역직렬화도 가능)

    @MockBean
    private PostService postService;

    @Test
    void 게시글_좋아요() throws Exception {
        // given
        boolean resultBoolean = true;
        PostLikeDto dto = new PostLikeDto(1L, 1L);
        String requestBody = objectMapper.writeValueAsString(dto);

        // postService.toggleHeart() 호출 시 true 나오도록 설정
        given(postService.toggleHeart(any(PostLikeDto.class)))
                .willReturn(resultBoolean);

        //when & then
        mockMvc.perform(post("/posts/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}