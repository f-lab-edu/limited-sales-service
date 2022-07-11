package com.limited.sales.user.vo;

import com.limited.sales.user.vo.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/*
UserAdapter 클래스의 내부

userdetails.User 클래스를 상속받는다.
UserAdapter의 멤버는 오로지 User 객체만이 존재한다.
생성자 내부에서 userdetails.User 클래스의 생성자를 호출하여 username, password, role을 세팅한다.

userdetails.User 클래스를 상속받는 이유 ?
PrincipalDetailsService에서 Return하는 객체는 UserDetails 타입이여야 한다.
따라서 UserDetails를 구현하는 User 클래스를 상속 받는 방식으로 사용한다.
 */

@Getter
public class UserAdapter extends org.springframework.security.core.userdetails.User {

    private User user;

    public UserAdapter(User user) {
        super(user.getUserEmail(), user.getUserPassword(), List.of(new SimpleGrantedAuthority(user.getUserRole())));
        this.user = user;
    }

}
