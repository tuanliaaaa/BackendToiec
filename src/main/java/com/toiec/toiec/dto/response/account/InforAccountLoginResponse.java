package com.toiec.toiec.dto.response.account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InforAccountLoginResponse {
    private int iduser;
    private String username;
    private String status;
    private String name; // Thêm trường name
    private Set<RoleResponse> roles;

    public InforAccountLoginResponse(List<Object[]> results) {
        this.setIduser((Integer) results.get(0)[0]);
        this.setUsername((String) results.get(0)[1]);
        this.setStatus((String) results.get(0)[2]);
        this.setName((String) results.get(0)[3]); // Ánh xạ cột name

        Set<RoleResponse> roles = results.stream()
                .map(result -> {
                    RoleResponse roleResponse = new RoleResponse();
                    roleResponse.setIdRole((Integer) result[4]);
                    roleResponse.setRoleName((String) result[5]);
                    return roleResponse;
                })
                .collect(Collectors.toSet());
        this.setRoles(roles);
    }
}

