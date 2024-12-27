package github.com.rexfilius.tea.modules.security.repository;

import github.com.rexfilius.tea.modules.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);


}
