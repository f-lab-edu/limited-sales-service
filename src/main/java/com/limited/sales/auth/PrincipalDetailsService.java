package com.limited.sales.auth;

import com.limited.sales.user.vo.User;
import com.limited.sales.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080/login 요청이 들어오면 이곳이 동작을 한다.
@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.debug("PrincipalDetailsService.loadUserByUsername");
        User userEntity = userMapper.findByUserEmail(userEmail);
        return new PrincipalDetails(userEntity);
    }
}
