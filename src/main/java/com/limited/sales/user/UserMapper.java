package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface UserMapper {
  /**
   * 회원가입 insert
   *
   * @param user
   * @return
   */
  int insertUser(final User user);

  /**
   * 이메일 중복 체크 select
   *
   * @param user
   * @return
   */
  boolean checkEmailDuplication(final User user);

  /**
   * 회원 탈퇴 update
   *
   * @param user
   * @return
   */
  int deleteUser(final User user);

  /**
   * 패스워드 변경 update
   *
   * @param user
   * @return
   */
  int changePassword(final User user);

  /**
   * 내 정보 변경 update
   *
   * @param userEmail
   * @param targetUserUserCellphone
   * @return
   */
  int changeUserInformation(
      @Param("userEmail") final String userEmail,
      @Param("targetUserUserCellphone") final String targetUserUserCellphone);

  /**
   * 아이디/패스워드 사용자 체크 select
   *
   * @param user
   * @return
   */
  @Transactional(readOnly = true)
  boolean hasUser(final User user);

  /**
   * 사용자 정보 가져오기
   *
   * @param userEmail
   * @return
   */
  User findByEmail(@Param("userEmail") final String userEmail);

  /**
   * 사용자 이메일 존재 여부
   *
   * @param userEmail
   * @return boolean
   */
  boolean existOfUserEmail(@Param("userEmail") final String userEmail);

  /**
   * 관리자로 권한 변경
   *
   * @param userEmail
   * @return
   */
  int changeUserRoleToAdmin(@Param("userEmail") final String userEmail);

  /**
   * 사용자 패스워드 체크
   *
   * @param userPassword
   * @return
   */
  boolean checkPassword(
      @Param("userEmail") final String userEmail, @Param("userPassword") final String userPassword);
}
