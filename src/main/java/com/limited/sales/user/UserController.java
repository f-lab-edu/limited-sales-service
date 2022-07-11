package com.limited.sales.user;

import com.google.gson.Gson;
import com.limited.sales.principal.PrincipalDetails;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping(
        value = "/user",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
//@ResponseStatus(HttpStatus.OK)
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

    @GetMapping
    @PreAuthorize(value="hasRole({'ROLE_USER', 'ROLE_ADMIN'})")
    public ResponseEntity<String> findUser(@RequestBody final User user){
        final String findUserEmail = user.getUserEmail();
        final User byUser = userService.findByUser(findUserEmail);
        final Gson gson = new Gson();

        final String toJson = gson.toJson(byUser, User.class);
        return ResponseEntity.ok(toJson);
    }

    @PatchMapping
    @PreAuthorize(value="hasRole({'ROLE_USER', 'ROLE_ADMIN'})")
    public ResponseEntity<String> userInfoUpdate(final Authentication authentication, @RequestBody final User targetUser){
        final User user = getUser(authentication);
        userService.changeMyInformation(user, targetUser);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/password")
    @PreAuthorize(value="hasRole({'ROLE_USER', 'ROLE_ADMIN'})")
    public ResponseEntity<String> userPasswordChange(@RequestBody final User user){
        userService.changePassword(user);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    @PreAuthorize(value="hasRole({'ROLE_USER', 'ROLE_ADMIN'})")
    public ResponseEntity<String> userDelete(final Authentication authentication){
        final User user = getUser(authentication);
        userService.leave(user);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/admin/{code}")
    @PreAuthorize(value="hasRole({'ROLE_USER', 'ROLE_ADMIN'})")
    public ResponseEntity<String> admin(final Authentication authentication,
                                        @PathVariable("code") final String adminCode){
        final User user = getUser(authentication);
        userService.changeUserRoleAdmin(adminCode, user);
        return ResponseEntity.ok(null);
    }

    private User getUser(@NotNull final Authentication authentication) {
        final PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getUser();
    }

}
