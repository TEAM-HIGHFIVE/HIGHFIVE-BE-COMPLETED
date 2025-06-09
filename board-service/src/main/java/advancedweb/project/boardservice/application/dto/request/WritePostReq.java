package advancedweb.project.boardservice.application.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record WritePostReq(
        @NotEmpty String title,
        @NotEmpty String content
) {}
