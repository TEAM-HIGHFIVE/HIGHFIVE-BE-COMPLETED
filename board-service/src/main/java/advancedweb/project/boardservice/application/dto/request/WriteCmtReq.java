package advancedweb.project.boardservice.application.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record WriteCmtReq(
        @NotEmpty String content
) {}
