package com.limited.sales.user;

import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.DuplicatedIdException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public void signUp(final User user) {
    if (hasUser(user)) {
      throw new DuplicatedIdException("이미 존재하는 계정입니다.");
    }

    final User newUser =
        User.builder()
            .cellphone(user.getCellphone())
            .email(user.getEmail())
            .role(user.getRole())
            .status(user.getStatus())
            .password(bCryptPasswordEncoder.encode(user.getPassword()))
            .build();

    userMapper.insertUser(newUser);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkPassword(final User currentUser, final String currentPassword) {
    Optional.ofNullable(currentPassword)
        .filter(v -> v.length() != 0)
        .orElseThrow(
            () -> {
              throw new BadRequestException("현재 패스워드를 입력하지 않았습니다.");
            });

    return bCryptPasswordEncoder.matches(currentPassword, currentUser.getPassword());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkEmailDuplication(final User user) {
    Optional.ofNullable(user)
        .map(User::getEmail)
        .filter(v -> v.length() != 0)
        .orElseThrow(
            () -> {
              throw new BadRequestException("이메일이 존재 하지 않습니다.");
            });

    return userMapper.checkEmailDuplication(user);
  }

  @Override
  public int deleteUser(final User user) {
    Optional.ofNullable(user)
        .map(this::hasUser)
        .orElseThrow(
            () -> {
              throw new NoValidUserException("계정이 존재하지 않습니다.");
            });

    return userMapper.deleteUser(user);
  }

  @Override
  public int changePassword(final User currentUser, Map<String, String> changeData) {
    final Optional<String> maybeData = Optional.ofNullable(changeData.get("newPassword"));
    maybeData
        .filter(v -> v.length() != 0)
        .orElseThrow(
            () -> {
              throw new BadRequestException("변경할 패스워드가 없거나 잘못 입력했습니다.");
            });

    final User changeUser =
        User.builder()
            .email(currentUser.getEmail())
            .password(changeData.get("newPassword"))
            .build();

    if (!userMapper.existOfUserEmail(changeUser.getEmail())) {
      throw new NoValidUserException("계정이 존재하지 않습니다.");
    }
    return userMapper.changePassword(changeUser);
  }

  @Override
  public int changeUserInformation(final User user, final User targetUser) {
    Optional.ofNullable(user)
        .map(User::getEmail)
        .filter(v -> v.length() != 0)
        .orElseThrow(
            () -> {
              throw new BadRequestException("이메일이 존재하지 않습니다.");
            });

    Optional.ofNullable(targetUser)
        .map(User::getCellphone)
        .filter(v -> v.length() != 0)
        .orElseThrow(
            () -> {
              throw new BadRequestException("전화번호가 존재하지 않습니다.");
            });

    return userMapper.changeUserInformation(user.getEmail(), targetUser.getCellphone());
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasUser(final User user) {
    return userMapper.hasUser(user);
  }

  @Transactional(readOnly = true)
  @Override
  public User findByEmail(final String userEmail) {
    final User foundByEmail = userMapper.findByEmail(userEmail);

    Optional.ofNullable(foundByEmail)
        .orElseThrow(
            () -> {
              throw new NoValidUserException("계정이 존재하지 않습니다.");
            });

    return foundByEmail;
  }

  @Override
  public void changeUserRoleToAdmin(@NotNull final String adminCode, @NotNull final User user) {
    final User byUser = userMapper.findByEmail(user.getEmail());
    Optional.ofNullable(adminCode)
        .filter(v -> v.length() != 0)
        .filter(Constant.ADMIN_CODE::equals)
        .orElseThrow(
            () -> {
              throw new BadRequestException("관리자 코드가 없거나 잘못 입력했습니다.");
            });

    Optional.ofNullable(byUser)
        .orElseThrow(
            () -> {
              throw new NoValidUserException("계정이 존재하지 않습니다.");
            });

    userMapper.changeUserRoleToAdmin(byUser.getEmail());
  }
}
