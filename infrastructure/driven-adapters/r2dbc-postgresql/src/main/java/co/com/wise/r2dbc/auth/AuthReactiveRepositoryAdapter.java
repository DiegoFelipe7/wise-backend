package co.com.wise.r2dbc.auth;

import co.com.wise.model.users.Login;
import co.com.wise.model.users.Token;
import co.com.wise.model.users.Users;
import co.com.wise.model.users.gateways.UsersRepository;
import co.com.wise.r2dbc.helper.ReactiveAdapterOperations;
import co.com.wise.r2dbc.security.jwt.JwtProvider;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class AuthReactiveRepositoryAdapter extends ReactiveAdapterOperations<Users, UsersEntity, Integer, AuthReactiveRepository>
        implements UsersRepository {
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthReactiveRepositoryAdapter(AuthReactiveRepository repository, ObjectMapper mapper, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Users.UsersBuilder.class).build());
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Token> login(Login login) {
        return repository.findByEmailIgnoreCase(login.getEmail())
                .switchIfEmpty(Mono.error(new NullPointerException("Malo")))
                .filter(user -> passwordEncoder.matches(login.getPassword(), user.getPassword()))
                .flatMap(user -> {
                    if (Boolean.TRUE.equals(user.getStatus())) {
                        return repository.findByEmailIgnoreCase(login.getEmail())
                                .map(ele -> new Token(jwtProvider.generateToken(user)));
                    }
                    return Mono.error(new NullPointerException("error"));
                })
                .switchIfEmpty(Mono.error(new NullPointerException("error")));

    }

    @Override
    public Flux<Users> findAllUsers() {
        return repository.findAll()
                .map(ele->mapper.mapBuilder(ele,Users.class));
    }

}
