package com.limited.sales.user;

import com.limited.sales.auth.PrincipalDetails;
import com.limited.sales.jwt.JwtProperties;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private static final String ADMIN_CODE = "ADMIN";

    private final UserService userService;

    /**
     * 회원가입
     * @param user
     * @return
     */
    @PostMapping("user")
    public String join(@RequestBody User user) {
        userService.saveUser(user);
        return "회원가입완료";
    }


    /**
     * 이메일 중복 체크
     * @param userEmail
     * @return
     */
    @GetMapping("user/email")
    public String userEmailChk(@RequestBody String userEmail) {
        int cntEmail = userService.checkEmail(userEmail);
        if(cntEmail > 0) {
            return "이미 존재하는 이메일입니다.";
        }else {

            return "사용 가능한 이메일입니다.";
        }
    }

    /**
     * 회원정보 수정
     * @param user
     * @return
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PatchMapping("user")
    public String userInfoChange(Authentication authentication, @RequestBody User user) {
        String userEmail = getUserInfo(authentication).getUserEmail();
        user.setUserEmail(userEmail);
        userService.updateUser(user);
        return "사용자 정보가 변경되었습니다.";
    }

    /**
     * 회원 탈퇴
     * @param authentication
     * @return
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("user")
    public String userInfoDelete(Authentication authentication) {
        User user = getUserInfo(authentication);
        if(userService.checkPassword(user)) {
            userService.deleteUser(user);
            return "회원탈퇴가 완료되었습니다."; }
        else {return "잘못된 비밀번호 입니다.";}
    }


    /**
     * 관리자 계정 전환
     * @param authentication
     * @param adminCode
     * @return
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PatchMapping("user/admin")
    public String userChangeAdmin(Authentication authentication, @RequestBody Map<String, String> adminCode) {
        if(adminCode.get("adminCode").equals(ADMIN_CODE)) {
            String userEmail  = getUserInfo(authentication).getUserEmail();
            userService.updateUserRole(userEmail);
            return "관리자 계정으로 전환이 완료되었습니다.";
        }else {
            return "잘못된 인증 코드 입니다.";
        }
    }

    /**
     * 시큐리티에 저장된 현재 로그인 된 유저정보 가져오기
     * @param authentication
     * @return
     */
    private User getUserInfo(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return user;
    }

    /**
     * accessToken 재발급 - 로그인 연장
     * @param authentication
     * @param userEmail
     * @return
     */
    @GetMapping("/re-issue")
    public @ResponseBody ResponseEntity<String> reIssue(Authentication authentication, @RequestBody String userEmail, HttpServletResponse response) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String accessToken = userService.reIssueAccessToken(principalDetails, userEmail);
        //accessToken을 발급해준다. response에 Authorization으로 실어준다.
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken); //한칸 반드시 띄워줘야함.
        return ResponseEntity.ok(null);
    }


    @GetMapping("/logout")
    public  @ResponseBody ResponseEntity<String> logout(@RequestBody User user, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        userService.logout(user.getUserEmail(), accessToken);
        return ResponseEntity.ok(null);
    }


}
