package org.bookrental.bookrentalsystem.Data.Mapper;

import org.bookrental.bookrentalsystem.Data.Entity.User;
import org.bookrental.bookrentalsystem.Data.Request.UserRequest;
import org.bookrental.bookrentalsystem.Data.Response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUserType(request.getUserType());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .build();
    }
}
