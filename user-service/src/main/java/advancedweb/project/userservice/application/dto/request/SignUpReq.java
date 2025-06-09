package advancedweb.project.userservice.application.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record SignUpReq(
        @NotEmpty String username,
        @NotEmpty String password
) {}
