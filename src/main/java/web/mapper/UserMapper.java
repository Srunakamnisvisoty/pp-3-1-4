package web.mapper;

import web.dto.UserDto;
import web.model.Role;
import web.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private UserMapper() {
    }

    public static User toModel(UserDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setSecurityRolesList(dto.getSecurityRolesList());
        return user;
    }

    public static List<UserDto> toDto(List<User> userList) {
        return userList.stream().map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        List<String> lstRoles = new ArrayList<>();
        for(Role role : user.getRoleList()){
            lstRoles.add(role.getRole());
        }
        dto.setSecurityRolesList(user.getSecurityRolesList());
        dto.setRoleList(lstRoles);
        dto.setRoles(user.getRoleListString());
        return dto;
    }
}
