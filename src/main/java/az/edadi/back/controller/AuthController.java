package az.edadi.back.controller;

import az.edadi.back.model.request.NewPasswordRequestModel;
import az.edadi.back.model.request.PasswordRecoverRequest;
import az.edadi.back.model.request.SignInRequestModel;
import az.edadi.back.model.request.SignUpRequestModel;
import az.edadi.back.model.response.JwtTokenResponseModel;
import az.edadi.back.service.AuthenticationService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@RequestBody JwtTokenResponseModel tokens) {
        return ResponseEntity.ok(authenticationService.refreshToken(tokens));
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<?> login(@RequestBody SignInRequestModel signInRequestModel) {
        return ResponseEntity.ok(authenticationService.login(signInRequestModel));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity addUser(@Valid @RequestBody final SignUpRequestModel signUpRequestModel) {
        authenticationService.register(signUpRequestModel);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "/recovery")
    public ResponseEntity sendToken(@RequestBody PasswordRecoverRequest emailOrUsername) throws MessagingException, IOException, TemplateException {
        return ResponseEntity.ok(new PasswordRecoverRequest(authenticationService.sendTokenByEmail(emailOrUsername.getUsernameOrEmail())));
    }

    @PutMapping(value = "/recovery")
    public ResponseEntity recoverPassword(@Valid @RequestBody NewPasswordRequestModel password) {
        return ResponseEntity.ok(authenticationService.resetPassword(password.getToken(), password.getPassword()));
    }


}
