package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        value = "/user",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/email")
    public ResponseEntity<String> emailOverlapCheck(@RequestBody final User user){
        userService.emailOverlapCheck(user);
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody final User user){
        userService.signUp(user);
        return ResponseEntity.ok(null);
    }

    @PatchMapping
    @PreAuthorize(value="hasRole('ROLE_USER')")
    public ResponseEntity<String> userInfoUpdate(@RequestBody final User user){
        userService.changeMyInformation(user);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/password")
    @PreAuthorize(value="hasRole('ROLE_USER')")
    public ResponseEntity<String> userPasswordChange(@RequestBody final User user){
        userService.changePassword(user);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    @PreAuthorize(value="hasRole('ROLE_USER')")
    public ResponseEntity<String> userDelete(@RequestBody final User user){
        userService.leave(user);
        return ResponseEntity.ok(null);
    }

}
