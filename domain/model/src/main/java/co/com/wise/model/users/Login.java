package co.com.wise.model.users;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Login {
    private String email;
    private String password;
    private String ipAddress;
    private String country;
    private String browser;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
