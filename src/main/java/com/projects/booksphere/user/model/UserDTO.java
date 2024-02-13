package com.projects.booksphere.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        Long id,
        @Size(max = 45, message = "{user.nickname.size.max}")
        @NotBlank(message = "{user.nickname.notBlank}")
        String nickname,
        @Size(max = 45, message = "{user.email.size.max}")
        @NotBlank(message = "{user.email.notBlank}")
        String email,
        @Size(max = 45, message = "{user.password.size.max}")
        @NotBlank(message = "{user.password.notBlank}")
        String password,
        @Size(max = 45, message = "{user.firstname.size.max}")
        String firstName,
        @Size(max = 45, message = "{user.secondname.size.max}")
        String secondName,
        @Size(max = 2000, message = "{user.bio.size.max}")
        String bio
) {
}