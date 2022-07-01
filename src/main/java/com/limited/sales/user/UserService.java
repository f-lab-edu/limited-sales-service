package com.limited.sales.user;

import com.limited.sales.exception.NoValidUserException;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService{
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int signUp(final User user){
        final int check = userCheck(user);
        if(check > 0) throw new RuntimeException("이미 존재하는 계정입니다.");
        User signUser = User.builder()
                .userCellphone(user.getUserCellphone())
                .userEmail(user.getUserEmail())
                .userRole(user.getUserRole())
                .useYn(user.getUseYn())
                .userPassword(bCryptPasswordEncoder.encode(user.getUserPassword()))
                .build();
        return userMapper.insertUser(signUser);
    }

    @Transactional(readOnly = true)
    public int emailOverlapCheck(final User user) {
        if(user.getUserEmail() == null || "".equals(user.getUserEmail())){
            throw new RuntimeException("이메일이 존재 하지 않습니다.");
        }
        return userMapper.emailOverlapCheck(user);
    }

    public int leave(final User user) {
        final int check = userCheck(user);
        if(check == 0){
            throw new NoValidUserException("계정이 존재하지 않습니다.");
        }
        return userMapper.leave(user);
    }

    public int changePassword(final User user) {
        final int check = userCheck(user);
        if(check == 0){
            throw new NoValidUserException("계정이 존재하지 않습니다.");
        }
        return userMapper.changePassword(user);
    }

    public int changeMyInformation(final User user) {
        /*final int check = userCheck(user);
        if(check == 0){
            throw new NoValidUserException("계정이 존재하지 않습니다.");
        }*/
        return userMapper.changeMyInformation(user);
    }

    @Transactional(readOnly = true)
    public int userCheck(final User user) {
        return userMapper.userCheck(user);
    }

    /**
     * 어느 위치에서 오류가 났는지 정확히 찾기 힘들 것 같음.
     * 리팩토링이 필요할 듯.
     * @param userEmail
     * @return
     */
    //@Transactional(readOnly = true)
    public User findByUser(final @NotNull String userEmail){
        User byUser = userMapper.findByUser(userEmail);
        log.info("{}", byUser);
        Optional<User> user = Optional.of(userMapper.findByUser(userEmail));
        return user.orElseThrow(() -> new NoValidUserException("계정이 존재하지 않습니다."));
    }

}
