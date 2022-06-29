package com.limited.sales.user;

import com.limited.sales.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void save(@Param("user") User user);

    User findByUserEmail(@Param("userEmail") String userEmail);

}
