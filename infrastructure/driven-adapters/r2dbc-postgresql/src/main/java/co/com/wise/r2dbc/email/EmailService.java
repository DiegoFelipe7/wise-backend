package co.com.wise.r2dbc.email;

import co.com.wise.r2dbc.exception.CustomException;
import co.com.wise.r2dbc.exception.TypeStateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Value("${evox.email.sender}")
    private  String emailSender;
    private final JavaMailSender mailSender;
    private final Scheduler scheduler;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.scheduler = Schedulers.boundedElastic(); // Crea un scheduler elástico para las operaciones asíncronas
    }

    public Mono<Void> sendEmailWelcome(String fullName, String email, String token) {
        return Mono.fromRunnable(() -> {
            try {
                Context context = new Context();
                context.setVariable( "token",token);
                context.setVariable( "fullName", fullName);
                String htmlContent = templateEngine.process("welcome", context);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(emailSender);
                helper.setSubject("Bienvenido a evox");
                helper.setTo(email);
                helper.setText(htmlContent, true);
                mailSender.send(message);
            } catch (Exception e) {
                System.out.println(e);
                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, e.getMessage(), TypeStateResponse.Error));
            }
        }).subscribeOn(scheduler).then();
    }

    public Mono<Void> sendEmailRecoverPassword(String fullName,String email, String token) {
        return Mono.fromRunnable(() -> {
            try {
                Context context = new Context();
                context.setVariable("token", token); // Establece las variables necesarias para la plantilla
                context.setVariable("fullName" , fullName);
                String htmlContent = templateEngine.process("recoverPassword", context);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(emailSender);
                helper.setTo(email);
                helper.setSubject("Restablecimiento de contraseña");
                helper.setText(htmlContent, true);
                mailSender.send(message);
            } catch (Exception e) {
                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, e.getMessage(), TypeStateResponse.Error));
            }
        }).subscribeOn(scheduler).then();
    }
}
