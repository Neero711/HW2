package org.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private Integer age;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}