package efub.assignment.community.post.dto.request;

import jakarta.validation.constraints.NotNull;

public record PostLikeDto (@NotNull Long postId, @NotNull Long memberId){}