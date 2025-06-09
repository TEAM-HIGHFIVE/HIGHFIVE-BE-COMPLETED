package advancedweb.project.userservice.application.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginReq(
        @NotEmpty String username,
        @NotEmpty String password
) {}
