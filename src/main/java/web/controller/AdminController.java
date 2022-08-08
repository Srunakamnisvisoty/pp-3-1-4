package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.service.SecurityRolesService;


@Validated
@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private SecurityRolesService securityRolesService;

    @Autowired
    public void setUserService(SecurityRolesService securityRolesService) {
        this.securityRolesService = securityRolesService;
    }

    @GetMapping()
    public String adminHome(Model model){
        model.addAttribute("securityRoleList", securityRolesService.getRoles());
        return "admin/admin";
    }
}
