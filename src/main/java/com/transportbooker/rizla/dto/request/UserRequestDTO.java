package com.transportbooker.rizla.dto.request;

import com.transportbooker.rizla.models.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserRequestDTO {
    private Long id;
    private String username;
    private String password;
    private String name;
    /**
     * userTypes are PASSENGER,EXEC_PASSENGER OR DRIVER
     */
    private String userType;

    public CustomUser toCustomUser() {
        Set<String> roles = new HashSet<>();
        switch (userType.toUpperCase()) {
            case "PASSENGER":
                roles.add("ROLE_PASSENGER");
                break;
                case "DRIVER":
                    roles.add("ROLE_DRIVER");
                    break;
                    case "EXEC_PASSENGER":
                        roles.add("ROLE_EXECPASSENGER");
                        break;
                        default:
                            roles.add("ROLE_PASSENGER");
                            break;
        }

        return CustomUser.builder()
                .id(id)
                .username(username)
                .password(password)
                .name(name)
                .roles(roles)
                .build();
    }
}
