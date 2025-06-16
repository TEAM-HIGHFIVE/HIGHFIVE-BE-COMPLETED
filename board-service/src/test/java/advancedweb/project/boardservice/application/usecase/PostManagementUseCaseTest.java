package advancedweb.project.boardservice.application.usecase;

import advancedweb.project.boardservice.application.dto.request.WritePostReq;
import advancedweb.project.boardservice.application.dto.response.CommentRes;
import advancedweb.project.boardservice.application.dto.response.PostDetailRes;
import advancedweb.project.boardservice.application.dto.response.PostSummaryRes;
import advancedweb.project.boardservice.domain.entity.Comment;
import advancedweb.project.boardservice.domain.entity.Post;
import advancedweb.project.boardservice.domain.service.PostService;
import advancedweb.project.boardservice.global.cache.RecentBoardCacheUpdater;
import advancedweb.project.boardservice.global.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostManagementUseCaseTest {

    private PostService postService;
    private CommentManagementUseCase commentManagementUseCase;
    private RecentBoardCacheUpdater recentBoardCacheUpdater;
    private PostManagementUseCase useCase;

    @BeforeEach
    void setUp() {
        postService = mock(PostService.class);
        commentManagementUseCase = mock(CommentManagementUseCase.class);
        recentBoardCacheUpdater = mock(RecentBoardCacheUpdater.class);
        useCase = new PostManagementUseCase(postService, commentManagementUseCase, recentBoardCacheUpdater);
    }

    @Test
    void 모든_게시물_조회() {
        PostSummaryRes summary = mock(PostSummaryRes.class);
        Page<PostSummaryRes> page = new PageImpl<>(List.of(summary));
        when(postService.readAll(any())).thenReturn(page);

        Page<PostSummaryRes> result = useCase.readAll(0);

        assertEquals(1, result.getTotalElements());
        verify(postService).readAll(any());
    }

    @Test
    void 최근_조회한_게시물_조회() {
        Post post = mock(Post.class);
        when(recentBoardCacheUpdater.readRecentPosts()).thenReturn(List.of(post));
        PostSummaryRes summary = mock(PostSummaryRes.class);
        mockStatic(PostSummaryRes.class).when(() -> PostSummaryRes.create(post)).thenReturn(summary);

        List<PostSummaryRes> result = useCase.readRecentPost();

        assertEquals(1, result.size());
        verify(recentBoardCacheUpdater).readRecentPosts();
    }

    @Test
    void 게시물_상세_조회() {
        // given
        String postNo = "P1";
        String userNo = "u1";

        Post post = Post.create("Test Title", "Test Content", userNo);

        CommentRes comment = CommentRes.create(Comment.create("Test Comment", "u2", postNo), userNo);

        when(postService.read(postNo)).thenReturn(post);
        when(commentManagementUseCase.read(postNo, userNo)).thenReturn(List.of(comment));

        // when
        PostDetailRes result = useCase.read(postNo, userNo);

        // then
        assertEquals(1, result.comments().size());
        assertEquals("Test Title", result.title());
        verify(recentBoardCacheUpdater).cacheRecentPost(post);
    }

    @Test
    void 게시물_작성() {
        // given
        String userNo = "u1";
        WritePostReq request = new WritePostReq("Test Title", "Test Content");

        Post post = Post.create("Test Title", "Test Content", userNo);

        when(postService.save(userNo, request)).thenReturn(post);

        // when
        PostDetailRes result = useCase.write(userNo, request);

        // then
        assertEquals("Test Title", result.title());
        assertTrue(result.isMine());
        verify(recentBoardCacheUpdater).cacheRecentPost(post);
    }

    @Test
    void 게시물_삭제() {
        String postNo = "p1";
        String userNo = "u1";
        when(postService.isMine(postNo, userNo)).thenReturn(true);

        useCase.delete(postNo, userNo);

        verify(postService).delete(postNo);
    }

    @Test
    void 작성자가_아닌_유저의_게시물_삭제() {
        String postNo = "p1";
        String userNo = "u2";
        when(postService.isMine(postNo, userNo)).thenReturn(false);

        assertThrows(RestApiException.class, () -> useCase.delete(postNo, userNo));
        verify(postService, never()).delete(postNo);
    }
}
