package com.arka.msclients.clients.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table("clients")
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
