package com.projects.booksphere.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(max = 45, message = "{user.username.size.max}")
    @NotBlank(message = "{user.username.notBlank}")
    private String username;
    @Size(max = 45, message = "{user.email.size.max}")
    @NotBlank(message = "{user.email.notBlank}")
    private String email;
    @Size(max = 45, message = "{user.password.size.max}")
    @NotBlank(message = "{user.password.notBlank}")
    private String password;
    @Size(max = 45, message = "{user.firstname.size.max}")
    @NotBlank(message = "{user.firstname.notBlank}")
    private String firstName;
    @Size(max = 45, message = "{user.secondname.size.max}")
    @NotBlank(message = "{user.secondname.notBlank}")
    private String secondName;
}