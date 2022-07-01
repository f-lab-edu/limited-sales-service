package com.limited.sales.user;

import com.limited.sales.config.annotation.UserAuthority;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/user",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/email")
    public ResponseEntity<String> emailOverlapCheck(@RequestBody(required = false) final User user){
        userService.emailOverlapCheck(user);
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody final User user){
        final int result = userService.signUp(user);
        if(result > 0){
            return ResponseEntity.ok(null);
        } else {
            throw new RuntimeException("회원가입 도중 에러가 발생했습니다");
        }
    }

    @PatchMapping
    @UserAuthority
    @PreAuthorize(value="hasRole('ROLE_USER')")
    public ResponseEntity<String> userInfoUpdate(@RequestBody final User user){
        userService.changeMyInformation(user);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/password")
    @UserAuthority
    @PreAuthorize(value="hasRole('ROLE_USER')")
    public ResponseEntity<String> userPasswordChange(@RequestBody final User user){
        userService.changePassword(user);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    @UserAuthority
    @PreAuthorize(value="hasRole('ROLE_USER')")
    public ResponseEntity<String> userDelete(@RequestBody final User user){
        userService.leave(user);
        return ResponseEntity.ok(null);
    }

}
