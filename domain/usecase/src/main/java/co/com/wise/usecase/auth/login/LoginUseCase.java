package co.com.wise.usecase.auth.login;

import co.com.wise.model.users.Login;
import co.com.wise.model.users.Token;
import co.com.wise.model.users.gateways.UsersRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class LoginUseCase implements Function<Login, Mono<Token>> {
    private final UsersRepository usersRepository;
    @Override
    public Mono<Token> apply(Login login) {
        return usersRepository.login(login);
    }
}
