package web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.SecurityRoles;
import web.repository.SecurityRolesRepository;
import web.service.SecurityRolesService;

import java.util.List;

@Service
public class SecurityRolesServiceImpl implements SecurityRolesService {
    private SecurityRolesRepository securityRolesRepository;

    @Autowired
    public void setSecurityRolesRepository(SecurityRolesRepository securityRolesRepository) {
        this.securityRolesRepository = securityRolesRepository;
    }

    @Transactional
    @Override
    public List<SecurityRoles> getRoles() {
        return securityRolesRepository.findAll();
    }

    @Transactional
    @Override
    public SecurityRoles getByRoleName(String roleName) {
        return securityRolesRepository.getByRoleName(roleName);
    }
}
