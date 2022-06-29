package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import com.limited.sales.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("home")
    public @ResponseBody String home() {return "home";}

    @GetMapping("user/abc")
    public @ResponseBody String user() {
        log.debug("{}", "user진입");
        return "user";
    }

    @PostMapping("join")
    public @ResponseBody String join(@RequestBody User user) {
        userService.save(user);
        return "회원가입완료";
    }

}
