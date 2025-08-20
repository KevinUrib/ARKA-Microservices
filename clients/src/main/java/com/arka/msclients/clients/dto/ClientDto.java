package com.arka.msclients.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
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
