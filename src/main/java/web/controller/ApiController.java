package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.dto.UserDto;
import web.mapper.UserMapper;
import web.service.UserService;

import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> allUsers(){
        return ResponseEntity.ok(UserMapper.toDto(userService.userList()));
    }

    @GetMapping(value = "/users/me")
    public ResponseEntity<UserDto> me(Principal principal){
        return ResponseEntity.ok(UserMapper.toDto(userService.getByLogin(principal.getName())));
    }
}