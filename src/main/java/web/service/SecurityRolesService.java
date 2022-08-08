package web.service;

import web.model.SecurityRoles;

import java.util.List;

public interface SecurityRolesService {
    List<SecurityRoles> getRoles();
    SecurityRoles getByRoleName(String roleName);
}
