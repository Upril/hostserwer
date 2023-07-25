package com.serwertetowy.auth;

import com.serwertetowy.config.Role;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    public record AuthenticationResponse(String token){}
    public record RegisterRequest(String firstname, String lastname, String email, @NotBlank(message = "Password is mandatory") @Length(min = 6, max = 40, message = "The password must have between 6 and 40 characters.") String password, @NotNull(message = "Role is mandatory") Role role){}
    public record AuthenticationRequest(@NotBlank(message = "Email is mandatory") @Email(message = "Email not valid") String email, @NotBlank(message = "Password is mandatory") @Length(min = 6, max = 40, message = "The password must have between 6 and 40 characters.") String password){}
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate (@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String name = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(name,msg);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String,String> handleConstraintExceptions(ConstraintViolationException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String name = String.valueOf(error.getPropertyPath());
            String msg =  error.getMessage();
            errors.put(name,msg);
        });
        return errors;
    }
}
