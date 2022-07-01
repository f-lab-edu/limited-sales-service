package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Mapper
public interface UserMapper {
    /**
     * 회원가입
     * insert
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 이메일 중복 체크
     * select
     * @param user
     * @return
     */
    int emailOverlapCheck(User user);

    /**
     * 회원 탈퇴
     * update
     * @param user
     * @return
     */
    int leave(User user);

    /**
     * 패스워드 변경
     * update
     * @param user
     * @return
     */
    int changePassword(User user);

    /**
     * 내 정보 변경
     * update
     * @param user
     * @return
     */
    int changeMyInformation(User user);

    /**
     * 아이디/패스워드 사용자 체크
     * select
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    int userCheck(User user);

    /**
     * 사용자 정보 가져오기
     * @param userEmail
     * @return
     */
    User findByUser(@Param("userEmail") String userEmail);


}
