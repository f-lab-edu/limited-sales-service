package com.limited.sales.user;

import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.DuplicatedIdException;
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
    final boolean isValidExistUser = hasUser(user);
    if (isValidExistUser) {
      throw new DuplicatedIdException("이미 존재하는 계정입니다.");
    }

    final User newUser =
        User.builder()
            .userCellphone(user.getUserCellphone())
            .userEmail(user.getUserEmail())
            .userRole(user.getUserRole())
            .useYn(user.getUseYn())
            .userPassword(bCryptPasswordEncoder.encode(user.getUserPassword()))
            .build();

    userMapper.insertUser(newUser);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkPassword(@NotNull final String userPassword) {
    final String userPasswordEncode = bCryptPasswordEncoder.encode(userPassword);
    return userMapper.checkPassword(userPasswordEncode);
  }

  @Override
  @Transactional(readOnly = true)
  public int checkEmailDuplication(final User user) {
    final String userEmail = user.getUserEmail();

    if (userEmail == null || "".equals(userEmail)) {
      throw new SignException("이메일이 존재 하지 않습니다.");
    }

    return userMapper.checkEmailDuplication(user);
  }

  @Override
  public int deleteUser(final User user) {
    final boolean isValidExistUser = hasUser(user);
    if (isValidExistUser) {
      throw new NoValidUserException("계정이 존재하지 않습니다.");
    }
    return userMapper.deleteUser(user);
  }

  @Override
  public int changePassword(final User user) {
    final User foundByEmail = userMapper.findByEmail(user.getUserEmail());
    if (foundByEmail == null) {
      throw new NoValidUserException("계정이 존재하지 않습니다.");
    }
    return userMapper.changePassword(foundByEmail);
  }

  @Override
  public int changeUserInformation(final User user, final User targetUser) {
    final Optional<String> optUserEmail = Optional.ofNullable(user.getUserEmail());
    final Optional<String> optTargetUserUserCellphone =
        Optional.ofNullable(targetUser.getUserCellphone());

    optUserEmail.orElseThrow(
        () -> {
          throw new NoValidUserException("이메일이 파라미터에 값이 없습니다.");
        });
    optTargetUserUserCellphone.orElseThrow(
        () -> {
          throw new NoValidUserException("전화번호 파라미터에 값이 없습니다.");
        });

    return userMapper.changeUserInformation(optUserEmail.get(), optTargetUserUserCellphone.get());
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasUser(final User user) {
    return userMapper.hasUser(user);
  }

  @Transactional(readOnly = true)
  @Override
  public User findByEmail(@NotNull final String userEmail) {
    final User foundByEmail = userMapper.findByEmail(userEmail);
    if (foundByEmail == null) {
      throw new NoValidUserException("계정이 존재하지 않습니다.");
    }

    return foundByEmail;
  }

  @Override
  public void changeUserRoleToAdmin(@NotNull final String adminCode, @NotNull final User user) {
    final User byUser = userMapper.findByEmail(user.getUserEmail());
    if (byUser == null) {
      throw new NoValidUserException("계정이 존재하지 않습니다.");
    }
    if (Constant.ADMIN_CODE.equals(adminCode)) {
      userMapper.changeUserRoleToAdmin(byUser.getUserEmail());
    } else {
      throw new RuntimeException("관리자 코드가 없거나 잘못 입력했습니다.");
    }
  }
}
