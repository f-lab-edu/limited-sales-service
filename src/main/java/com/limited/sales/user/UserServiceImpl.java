package com.limited.sales.user;

import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.exception.sub.SignException;
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
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void signUp(final User user) {
        final int count = userCheck(user);
        if (count > 0) throw new SignException("이미 존재하는 계정입니다.");

        final User signUser = User.builder()
                .userCellphone(user.getUserCellphone())
                .userEmail(user.getUserEmail())
                .userRole(user.getUserRole())
                .useYn(user.getUseYn())
                .userPassword(bCryptPasswordEncoder.encode(user.getUserPassword()))
                .build();

        userMapper.insertUser(signUser);
    }

    @Override
    @Transactional(readOnly = true)
    public int emailOverlapCheck(final User user) {
        final String userEmail = user.getUserEmail();

        if (userEmail == null || "".equals(userEmail)) throw new SignException("이메일이 존재 하지 않습니다.");

        return userMapper.emailOverlapCheck(user);
    }

    @Override
    public int leave(final User user) {
        final int count = userCheck(user);
        if (count == 0) throw new NoValidUserException("계정이 존재하지 않습니다.");
        return userMapper.leave(user);
    }

    @Override
    public int changePassword(final User user) {
        final User byUser = userMapper.findByUser(user.getUserEmail());
        if (byUser == null) throw new NoValidUserException("계정이 존재하지 않습니다.");
        return userMapper.changePassword(byUser);
    }

    @Override
    public int changeMyInformation(final User user, final User targetUser) {
        final Optional<String> optUserEmail = Optional.ofNullable(user.getUserEmail());
        final Optional<String> optTargetUserUserCellphone = Optional.ofNullable(targetUser.getUserCellphone());

        optUserEmail.orElseThrow(() -> { throw new NoValidUserException("이메일이 파라미터에 값이 없습니다."); });
        optTargetUserUserCellphone.orElseThrow(() -> { throw new NoValidUserException("전화번호 파라미터에 값이 없습니다."); });

        return userMapper.changeMyInformation(optUserEmail.get(), optTargetUserUserCellphone.get());
    }

    @Transactional(readOnly = true)
    @Override
    public int userCheck(final User user) {
        return userMapper.userCheck(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUser(@NotNull final String userEmail) {
        final User byUser = userMapper.findByUser(userEmail);
        if (byUser == null) throw new NoValidUserException("계정이 존재하지 않습니다.");

        return byUser;
    }

    @Override
    public void changeUserRoleAdmin(@NotNull final String adminCode, @NotNull final User user) {
        final User byUser = userMapper.findByUser(user.getUserEmail());
        if (byUser == null) throw new NoValidUserException("계정이 존재하지 않습니다.");

        userMapper.changeUserRoleAdmin(adminCode, byUser.getUserEmail());
    }
}
