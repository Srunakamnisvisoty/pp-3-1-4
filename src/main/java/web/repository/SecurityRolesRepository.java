package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.SecurityRoles;

@Repository
public interface SecurityRolesRepository extends JpaRepository<SecurityRoles, Long> {
    @Query("SELECT r FROM SecurityRoles r WHERE r.role = :role")
    SecurityRoles getByRoleName(String role);
}
