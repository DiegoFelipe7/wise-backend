package co.com.wise.model.users.gateways;

import co.com.wise.model.users.Login;
import co.com.wise.model.users.Token;
import reactor.core.publisher.Mono;

public interface UsersRepository {
    Mono<Token> login(Login login);

}
