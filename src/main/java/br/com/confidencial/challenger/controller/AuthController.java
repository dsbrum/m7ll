package br.com.confidencial.challenger.controller;


import br.com.confidencial.challenger.domain.auth.usuario.AuthData;
import br.com.confidencial.challenger.domain.auth.usuario.Usuario;
import br.com.confidencial.challenger.infra.security.TokenDataJMT;
import br.com.confidencial.challenger.infra.security.TokenService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/login")
public class AuthController {

    Counter authUserSuccess;
    Counter authUserErrors;

    public AuthController(MeterRegistry registry) {
        authUserSuccess = Counter.builder("auth_user_success")
                .description("usuarios autenticados")
                .register(registry);

        authUserErrors = Counter.builder("auth_user_error")
                .description("erros de login")
                .register(registry);
    }
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid AuthData dados) {
        try{
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
            var authentication = manager.authenticate(authenticationToken);

            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
            authUserSuccess.increment();
            return ResponseEntity.ok(new TokenDataJMT(tokenJWT));
        }catch (Exception e){
            authUserErrors.increment();
            return ResponseEntity.badRequest().build();
        }

    }
}