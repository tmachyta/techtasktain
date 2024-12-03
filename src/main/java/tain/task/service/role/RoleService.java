package tain.task.service.role;

import tain.task.model.Role;
import tain.task.model.Role.RoleName;

public interface RoleService {
    Role getRoleByRoleName(RoleName roleName);
}
