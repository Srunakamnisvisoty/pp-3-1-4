package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import web.dto.UserDto;
import web.mapper.UserMapper;
import web.model.Role;
import web.model.SecurityRoles;
import web.model.User;
import web.service.SecurityRolesService;
import web.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Valid
@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiAdminController {
    private UserService userService;
    private SecurityRolesService securityRolesService;

    @Autowired
    public void setUserService(UserService userService, SecurityRolesService securityRolesService){
        this.userService = userService;
        this.securityRolesService = securityRolesService;
    }

    @PostMapping("/new")
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        User user = UserMapper.toModel(userDto);
        for (SecurityRoles rle : user.getSecurityRolesList()) {
            SecurityRoles sRole = securityRolesService.getByRoleName(rle.getRole());
            Role role = new Role();
            role.setRole(sRole.getRole());
            role.setRoles(sRole);
            user.addRole(role);
        }
        return ResponseEntity.ok(UserMapper.toDto(userService.add(user)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("securityRoleList", securityRolesService.getRoles());
        return ResponseEntity.ok(UserMapper.toDto(userService.getById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") long id, @RequestBody @Valid UserDto userDto) {
        boolean encPass = false;
        User user = UserMapper.toModel(userDto);
        User userEdit = userService.getById(id);
        userEdit.setSecurityRolesList(user.getSecurityRolesList());
        userEdit.setEmail(user.getEmail());
        userEdit.setLogin(user.getLogin());
        userEdit.setAge(user.getAge());
        if (!StringUtils.isEmptyOrWhitespace(user.getPassword())) {
            userEdit.setPassword(user.getPassword());
            encPass = true;
        }
        user = userEdit;
        List<Role> editRoleList = new ArrayList<>(user.getRoleList());
        for (Role oldRole : editRoleList) {
            if (!user.getSecurityRolesList().contains(oldRole.getRoles())) {
                user.removeRole(user.getRoleList().get(user.getRoleList().indexOf(oldRole)));
            }
        }
        for (SecurityRoles rle : user.getSecurityRolesList()) {
            if (!user.getRoleListString().contains(rle.getRole())) {
                Role role = new Role();
                role.setRole(rle.getRole());
                role.setRoles(rle);
                user.addRole(role);
            }
        }
        return ResponseEntity.ok(UserMapper.toDto(userService.edit(user, encPass)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
