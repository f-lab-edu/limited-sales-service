package com.limited.sales.user;

import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;

public interface UserService {

  void signUp(final User user);

  int checkEmailDuplication(final User user);

  int deleteUser(final User user);

  int changePassword(final User user);

  int changeUserInformation(final User user, final User targetUser);

  boolean hasUser(final User user);

  User findByEmail(final @NotNull String userEmail);

  void changeUserRoleToAdmin(final @NotNull String adminCode, final @NotNull User user);

  boolean checkPassword(final @NotNull String userPassword);
}
