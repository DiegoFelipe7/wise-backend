package co.com.wise.api;

import co.com.wise.model.users.Login;
import co.com.wise.model.users.Token;
import co.com.wise.model.users.Users;
import co.com.wise.usecase.auth.getusers.GetusersUseCase;
import co.com.wise.usecase.auth.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
    private final LoginUseCase loginUseCase;
    private final GetusersUseCase getusersUseCase;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Login.class)
                .flatMap(ele -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(loginUseCase.apply(ele), Users.class));
    }

    public Mono<ServerResponse> users(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getusersUseCase.get(), Users.class);
    }

}
