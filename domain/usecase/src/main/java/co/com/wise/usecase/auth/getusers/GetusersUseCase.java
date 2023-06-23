package co.com.wise.usecase.auth.getusers;

import co.com.wise.model.users.Users;
import co.com.wise.model.users.gateways.UsersRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class GetusersUseCase implements Supplier<Flux<Users>> {

    private final UsersRepository usersRepository;
    @Override
    public Flux<Users> get() {
        return usersRepository.findAllUsers();
    }
}
