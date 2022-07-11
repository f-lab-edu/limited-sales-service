package com.limited.sales.user;

import com.limited.sales.user.vo.User;

import javax.validation.constraints.NotNull;

public interface UserService{

    void signUp(final User user);

    int emailOverlapCheck(final User user);

    int leave(final User user);

    int changePassword(final User user);

    int changeMyInformation(final User user, final User targetUser);

    int userCheck(final User user);

    User findByUser(final @NotNull String userEmail);

    void changeUserRoleAdmin(final @NotNull String adminCode, final @NotNull User user);
}
