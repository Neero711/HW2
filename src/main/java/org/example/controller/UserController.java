package org.example.controller;

import org.example.dto.UserDTO;
import org.example.dto.UserRequestDTO;
import org.example.service.UserService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.net.URI;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Управление пользователями", description = "API  по управлению пользователями")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список пользователей")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getAllUsers() {
        List<EntityModel<UserDTO>> userModels = userService.getAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(UserController.class).slash(user.getId()).withRel("user"),
                        linkTo(UserController.class).withRel("users")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()));
    }


    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по айди")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUserById(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id) {
        UserDTO user = userService.getUserById(id);

        EntityModel<UserDTO> model = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"),
                linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Cоздание нового пользователя", description = "Создает и возвращает новго пользователя")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<EntityModel<UserDTO>> createUser(
            @Parameter(description = "", required = true)
            @Valid @RequestBody UserRequestDTO user) {
        UserDTO createdUser = userService.createUser(user);

        EntityModel<UserDTO> model = EntityModel.of(createdUser,
                linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));

        URI location = linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).toUri();

        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "Обноить пользователя", description = "Обновляет и возвращаяет обновленного пользователя")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(
            @Parameter(description = "ID пользователя на обновление", required = true)
            @PathVariable Long id,
            @Parameter(description = "Обновленный пользователь", required = true)
            @Valid @RequestBody UserRequestDTO request) {
        UserDTO updatedUser = userService.updateUser(id, request);

        EntityModel<UserDTO> model = EntityModel.of(updatedUser,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователЯ по айди")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя под удаление", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
