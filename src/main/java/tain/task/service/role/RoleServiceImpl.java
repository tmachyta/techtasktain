package tain.task.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tain.task.model.Role;
import tain.task.repository.role.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(Role.RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(() ->
                new RuntimeException("can't find role by roleName: " + roleName));
    }
}
