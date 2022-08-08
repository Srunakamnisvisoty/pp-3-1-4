package web.dto;

import lombok.Data;
import web.model.SecurityRoles;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class UserDto {
    private long id;
    @NotBlank(message = "Cant be is empty")
    @Size(min = 2, max = 30)
    private String login;
    @NotBlank(message = "Cant be is empty")
    @Size(min = 8, max = 30)
    private String password;
    @Email
    @NotBlank(message = "Cant be is empty")
    private String email;
    @NotBlank(message = "Cant be is empty")
    @Positive
    @Max(150)
    private int age;
    private List<String> roleList;
    private String roles;
    private List<SecurityRoles> securityRolesList;
}
