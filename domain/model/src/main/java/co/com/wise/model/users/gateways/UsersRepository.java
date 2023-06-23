package co.com.wise.model.users.gateways;

import co.com.wise.model.users.Login;
import co.com.wise.model.users.Token;
import co.com.wise.model.users.Users;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsersRepository {
    Mono<Token> login(Login login);
    Flux<Users> findAllUsers();
}
