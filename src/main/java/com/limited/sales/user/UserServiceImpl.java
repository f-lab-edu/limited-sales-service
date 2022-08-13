package com.limited.sales.user;

import com.limited.sales.config.Constant;
import com.limited.sales.exception.sub.BadRequestException;
import com.limited.sales.exception.sub.NoValidUserException;
import com.limited.sales.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
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
    final User newUser =
        User.builder()
            .cellphone(user.getCellphone())
            .email(user.getEmail())
            .role(user.getRole())
            .status(user.getStatus())
            .password(bCryptPasswordEncoder.encode(user.getPassword()))
            .build();
    try {
      userMapper.insertUser(newUser);
    } catch (DuplicateKeyException e) {
      log.error(e.toString());
      e.printStackTrace();
      throw new DuplicateKeyException("이미 존재하는 계정입니다.");
    }
  }

  @Override
  public boolean checkPassword(final User currentUser, final String currentPassword) {
    return bCryptPasswordEncoder.matches(currentPassword, currentUser.getPassword());
  }

  @Override
  public void updatePassword(final User currentUser, final String updatePassword) {
    if (StringUtils.isBlank(updatePassword)) {
      throw new BadRequestException("변경할 비밀번호가 존재하지 않습니다.");
    }

    final User updateUser =
        User.builder().email(currentUser.getEmail()).password(updatePassword).build();

    userMapper.updatePassword(updateUser);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkEmailDuplication(final String email) {
    if (StringUtils.isBlank(email)) {
      throw new BadRequestException("이메일이 존재 하지 않습니다.");
    }
    return userMapper.checkEmailDuplication(email);
  }

  @Override
  public void deleteUser(final User user) {
    try {
      userMapper.deleteUser(user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateUserInformation(final User user, final String cellphone) {
    if (StringUtils.isBlank(cellphone)) {
      throw new BadRequestException("변경할 휴대폰 번호가 존재하지 않습니다.");
    }

    userMapper.updateUserInformation(user.getEmail(), cellphone);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasUser(final User user) {
    return userMapper.hasUser(user);
  }

  @Transactional(readOnly = true)
  @Override
  public User findByEmail(final String userEmail) {
    if (StringUtils.isBlank(userEmail)) {
      throw new BadRequestException("이메일이 존재하지 않습니다.");
    }

    return Optional.ofNullable(userMapper.findByEmail(userEmail))
        .orElseThrow(
            () -> {
              throw new NoValidUserException("계정이 존재하지 않습니다.");
            });
  }

  @Override
  public void updateUserRoleToAdmin(@NotNull final User user, @NotNull final String adminCode) {
    if (!Constant.ADMIN_CODE.equals(adminCode)) {
      throw new BadRequestException("관리자 코드가 일치하지 않습니다.");
    }

    userMapper.updateUserRoleToAdmin(user.getEmail());
  }
}
