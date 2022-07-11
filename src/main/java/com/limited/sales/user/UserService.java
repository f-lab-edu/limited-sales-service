package com.limited.sales.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.limited.sales.error.exception.user.DuplicatedIdException;
import com.limited.sales.jwt.JwtProperties;
import com.limited.sales.jwt.JwtProvider;
import com.limited.sales.redis.RedisService;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisService redisService;

    /*
        builder의 장점
        1. 필요한 데이터만 설정이 가능하다.
        2. 코드의 가독성이 높아진다.
        3. 불변성을 확보할 수 있다.
        4. 더욱 유연한 코드 작성에 용이하다.
     */

    /**
     * 회원가입
     * @param user
     */
    public void saveUser(final User user) {
        if(userMapper.checkEmail(user.getUserEmail())!=1) {
            User joinUser = User.builder()
                    .userPassword(bCryptPasswordEncoder.encode(user.getUserPassword()))
                    .userRole("ROLE_USER") //기본 권한
                    .useYn("Y") //기본 사용자 사용여부
                    .build();
            userMapper.saveUser(joinUser);
        } else {
            throw new DuplicatedIdException("이미 존재하는 이메일 입니다.");
        }
    }

    /**
     * 이메일 중복 체크
     * @param userEmail
     * @return
     */
    public int checkEmail(String userEmail) {
        int abc = userMapper.checkEmail(userEmail);
        log.debug("==================== test ={}", abc);
        return userMapper.checkEmail(userEmail);
    }

    /**
     * 회원 정보 수정
     * @param user
     */
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    /**
     * 비밀번호 일치여부 확인
     * @param user
     */
    public boolean checkPassword(User user) {

        User findUser = userMapper.findByUserEmail(user.getUserEmail());
        String encodePassword = findUser.getUserPassword();

        String rawPassword = user.getUserPassword();
        if(bCryptPasswordEncoder.matches(rawPassword, encodePassword)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 회원 탈퇴
     * @param user
     */
    public int deleteUser(User user) {
        User deleteUser = User.builder().useYn("N").build();
        return userMapper.deleteUser(deleteUser);
    }

    /**
     * 관리자 계정 전환
     * @param userEmail
     */
    public void updateUserRole(String userEmail) {
        userMapper.updateUserRole(userEmail);
    }

    /**
     * 사용자 정보 가져오기
     * @param userEmail
     */
    public User findByUserEmail(String userEmail) {
        User user = userMapper.findByUserEmail(userEmail);
        return user;
    }


    /**
     * access 토큰 재발급
     * @param user
     * @param userEmail
     * @return
     */
    public String reIssueAccessToken(User user, String userEmail) {
        userMapper.findByUserEmail(userEmail);
        JwtProvider jwtProvider = new JwtProvider();
        String accessToken = jwtProvider.getToken(user, "accessToken");
        return accessToken;
    }

    /**
     * 로그아웃 처리
     * @param userEmail
     * @param accessToken
     */
    public void logout(String userEmail, String accessToken) {
        // 로그아웃하면서, 보유한 access token 을 레디스에 저장한다.
        log.debug("=================== logout -> accessToken : {}", accessToken);
        //현재 accessToken의 만료시간
        String jwtToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");
        Date expiresAt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getExpiresAt();
        //남은 만료시간
        long expiredAccessTokenTime = expiresAt.getTime() - new Date().getTime();
        log.debug("=================== logout -> expiredAccessTokenTime : {}", expiredAccessTokenTime);
        redisService.setValue(JwtProperties.BLACKLIST_TOKEN_PREFIX + accessToken.replace(JwtProperties.TOKEN_PREFIX, ""), userEmail, Duration.ofMillis(expiredAccessTokenTime));
        log.debug("=================== logout -> key : {}", JwtProperties.BLACKLIST_TOKEN_PREFIX + accessToken.replace(JwtProperties.TOKEN_PREFIX, ""));
        // refresh token 은 삭제한다.
        redisService.deleteValue(userEmail);
    }



}
