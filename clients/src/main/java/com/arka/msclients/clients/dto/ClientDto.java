package com.arka.msclients.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Email(message= "Invalid email format")
    @NotBlank(message= "Email cannot be empty")
    private String email;

    private String phone;
    private String address;
}
