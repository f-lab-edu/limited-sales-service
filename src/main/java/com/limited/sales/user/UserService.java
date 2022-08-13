package com.limited.sales.user;

import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;

public interface UserService {

  void signUp(final User user);

  boolean checkEmailDuplication(final String email);

  void deleteUser(final User user);

  void updatePassword(final User currentUser, final String updatePassword);

  boolean checkPassword(final @NotNull User currentUser, final @NotNull String currentPassword);

  void updateUserInformation(final User user, final String cellphone);

  boolean hasUser(final User user);

  User findByEmail(final @NotNull String userEmail);

  void updateUserRoleToAdmin(@NotNull final User user, @NotNull final String adminCode);
}
