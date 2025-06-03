package advancedweb.project.userservice.ui.controller;

import advancedweb.project.userservice.application.dto.request.LoginReq;
import advancedweb.project.userservice.application.dto.request.SignUpReq;
import advancedweb.project.userservice.application.dto.response.AuthRes;
import advancedweb.project.userservice.application.usecase.UserAuthUseCase;
import advancedweb.project.userservice.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {

    private final UserAuthUseCase userAuthUseCase;

    /**
     *  회원가입 API
     */
    @PostMapping("/sign-up")
    public BaseResponse<AuthRes> signUp(@RequestBody SignUpReq request) {
        return BaseResponse.onSuccess(userAuthUseCase.signUp(request));
    }

    /**
     *  로그인 API
     */
    @PostMapping("/login")
    public BaseResponse<AuthRes> login(@RequestBody LoginReq request) {
        return BaseResponse.onSuccess(userAuthUseCase.login(request));
    }
}
