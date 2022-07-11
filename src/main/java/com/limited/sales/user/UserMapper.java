package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface UserMapper {
    /**
     * 회원가입
     * insert
     * @param user
     * @return
     */
    int insertUser(final User user);

    /**
     * 이메일 중복 체크
     * select
     * @param user
     * @return
     */
    int emailOverlapCheck(final User user);

    /**
     * 회원 탈퇴
     * update
     * @param user
     * @return
     */
    int leave(final User user);

    /**
     * 패스워드 변경
     * update
     * @param user
     * @return
     */
    int changePassword(final User user);

    /**
     * 내 정보 변경
     * update
     * @param userEmail
     * @param targetUserUserCellphone
     * @return
     */
    int changeMyInformation(@Param("userEmail") final String userEmail,
                            @Param("targetUserUserCellphone") final String targetUserUserCellphone);

    /**
     * 아이디/패스워드 사용자 체크
     * select
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    int userCheck(final User user);

    /**
     * 사용자 정보 가져오기
     * @param userEmail
     * @return
     */
    User findByUser(@Param("userEmail") final String userEmail);


    /**
     * 관리자로 권한 변경
     * @param adminCode
     * @param user
     * @return
     */
    int changeUserRoleAdmin(@Param("adminCode") final String adminCode, @Param("userEmail") final String userEmail);


}
