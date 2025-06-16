package advancedweb.project.userservice.application.usecase;

import advancedweb.project.userservice.application.dto.request.LoginReq;
import advancedweb.project.userservice.application.dto.response.AuthRes;
import advancedweb.project.userservice.domain.entity.User;
import advancedweb.project.userservice.domain.service.UserService;
import advancedweb.project.userservice.global.config.security.TokenProvider;
import advancedweb.project.userservice.global.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static advancedweb.project.userservice.global.exception.code.status.AuthErrorStatus._EMPTY_USERNAME_PASSWORD;
import static advancedweb.project.userservice.global.exception.code.status.GlobalErrorStatus._LOGIN_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserAuthUseCaseTest {

    private UserService userService;
    private TokenProvider tokenProvider;
    private UserAuthUseCase userAuthUseCase;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        tokenProvider = mock(TokenProvider.class);
        userAuthUseCase = new UserAuthUseCase(userService, tokenProvider);
    }

    @Test
    void login_O() {
        // given
        LoginReq loginReq = new LoginReq("qwer1234", "asdf1234");
        User mockUser = User.builder()
                .userNo("U123")
                .username("qwer1234")
                .password("asdf1234")
                .build();

        when(userService.findByLoginReq(loginReq)).thenReturn(mockUser);
        when(tokenProvider.createAccessToken("U123")).thenReturn("access-token");

        // when
        AuthRes response = userAuthUseCase.login(loginReq);

        // then
        assertThat(response.token()).isEqualTo("access-token");

        verify(userService, times(1)).findByLoginReq(loginReq);
        verify(tokenProvider, times(1)).createAccessToken("U123");
    }

    @Test
    void login_X_wrong_password() {
        // given
        LoginReq invalidLoginReq = new LoginReq("qwer1234", "wrong-password");

        // when
        when(userService.findByLoginReq(invalidLoginReq))
                .thenThrow(new RestApiException(_LOGIN_ERROR));

        // then
        assertThrows(RestApiException.class, () -> {
            userAuthUseCase.login(invalidLoginReq);
        });

        verify(userService, times(1)).findByLoginReq(invalidLoginReq);
        verify(tokenProvider, never()).createAccessToken(any());
    }

    @Test
    void login_X_empty_username_password() {
        // given
        LoginReq emptyEmailReq = new LoginReq("", "asdf1234");
        LoginReq emptyPasswordReq = new LoginReq("qwer1234", "");

        when(userService.findByLoginReq(emptyEmailReq))
                .thenThrow(new RestApiException(_EMPTY_USERNAME_PASSWORD));
        when(userService.findByLoginReq(emptyPasswordReq))
                .thenThrow(new RestApiException(_EMPTY_USERNAME_PASSWORD));

        // then
        assertThrows(RestApiException.class, () -> userAuthUseCase.login(emptyEmailReq));
        assertThrows(RestApiException.class, () -> userAuthUseCase.login(emptyPasswordReq));
    }
}