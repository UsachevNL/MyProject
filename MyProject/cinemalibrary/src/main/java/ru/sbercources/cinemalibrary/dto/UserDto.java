package ru.sbercources.cinemalibrary.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends GenericDto{
    @NotBlank(message = "Поле не должно быть пустым")
    private RoleDto role;
    @NotBlank(message = "Поле не должно быть пустым")
    private String firstName;
    @NotBlank(message = "Поле не должно быть пустым")
    private String lastName;
    @NotBlank(message = "Поле не должно быть пустым")
    private String login;
    @NotBlank(message = "Поле не должно быть пустым")
    private String password;
    @NotBlank(message = "Поле не должно быть пустым")
    private String middleName;
    @NotBlank(message = "Поле не должно быть пустым")
    private String email;
    @NotBlank(message = "Поле не должно быть пустым")
    private String phone;
    @NotBlank(message = "Поле не должно быть пустым")
    private String addres;
    @NotBlank(message = "Поле не должно быть пустым")
    private  String birthDate;
}
