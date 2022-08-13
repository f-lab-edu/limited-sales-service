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
   */
  void insertUser(final User user);

  /**
   * 이메일 중복 체크 select
   *
   * @param email 사용자 이메일
   * @return 이메일 중복 = true
   */
  boolean checkEmailDuplication(@Param("email") final String email);

  /**
   * 회원 탈퇴 update
   *
   * @param user
   */
  void deleteUser(final User user);

  /**
   * 패스워드 변경 update
   *
   * @param updateUser 변경할 패스워드 설정
   */
  void updatePassword(final User updateUser);

  /**
   * 내 정보 변경 update
   *
   * @param email 현재 사용자 이메일
   * @param cellphone 변경할 전화번호
   * @return
   */
  void updateUserInformation(
      @Param("email") final String email, @Param("cellphone") final String cellphone);

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
   * @param email
   * @return
   */
  User findByEmail(final String email);

  /**
   * 관리자로 권한 변경
   *
   * @param email
   */
  void updateUserRoleToAdmin(final String email);
}
