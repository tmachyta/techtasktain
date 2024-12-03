package tain.task.service.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tain.task.model.Role;
import tain.task.repository.role.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void testGetRoleByRoleName() {

        Role.RoleName roleName = Role.RoleName.USER;

        Role role = new Role();
        role.setRoleName(roleName);

        when(roleRepository.findRoleByRoleName(roleName)).thenReturn(Optional.of(role));

        Role resultRole = roleService.getRoleByRoleName(roleName);

        assertEquals(role, resultRole);
    }

    @Test
    public void testGetRoleByRoleNameNotFound() {
        Role.RoleName roleName = Role.RoleName.ADMIN;

        when(roleRepository.findRoleByRoleName(roleName)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.getRoleByRoleName(roleName));
    }
}
