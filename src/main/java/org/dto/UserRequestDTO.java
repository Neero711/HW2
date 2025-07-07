package org.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    // C валидацией относительно реалистичных данных
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age should be greater than 0")
    @Max(value = 100, message = "Age should be less than 100")
    private Integer age;
}