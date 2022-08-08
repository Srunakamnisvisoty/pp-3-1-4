package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.mapper.UserMapper;
import web.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UsersController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }


    @GetMapping()
    public String getById(Principal principal, Model model){
        model.addAttribute("user", UserMapper.toDto(userService.getByLogin(principal.getName())));
        return "/user/getById";
    }
}
