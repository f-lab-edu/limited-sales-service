package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 회원등록
     * @param user
     */
    void saveUser(@Param("user") User user);

    /**
     * 이메일 중복체크
     * @param userEmail
     * @return
     */
    int checkEmail(@Param("userEmail") String userEmail);

    /**
     * 회원정보 변경
     * @param user
     */
    void updateUser(@Param("user") User user);

    /**
     * 비밀번호 일치 여부 체크
     * @param user
     * @return
     */
    int checkPassword(@Param("user") User user);

    /**
     * 회원탈퇴
     * @param user
     */
    int deleteUser(@Param("user") User user);

    void updateUserRole(@Param("userEmail") String userEmail);

    /**
     * 사용자 정보 가져오기
     * @param userEmail
     * @return
     */
    User findByUserEmail(@Param("userEmail") String userEmail);

}
