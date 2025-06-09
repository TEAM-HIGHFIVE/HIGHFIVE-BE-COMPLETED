package advancedweb.project.userservice.ui.controller;

import advancedweb.project.userservice.application.dto.request.LoginReq;
import advancedweb.project.userservice.application.dto.request.SignUpReq;
import advancedweb.project.userservice.application.dto.response.AuthRes;
import advancedweb.project.userservice.application.usecase.UserAuthUseCase;
import advancedweb.project.userservice.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {

    private final UserAuthUseCase userAuthUseCase;

    /**
     *  회원가입 API
     */
    @PostMapping("/sign-up")
    public BaseResponse<AuthRes> signUp(@RequestBody @Valid SignUpReq request) {
        return BaseResponse.onSuccess(userAuthUseCase.signUp(request));
    }

    /**
     *  로그인 API
     */
    @PostMapping("/login")
    public BaseResponse<AuthRes> login(@RequestBody @Valid LoginReq request) {
        return BaseResponse.onSuccess(userAuthUseCase.login(request));
    }
}
