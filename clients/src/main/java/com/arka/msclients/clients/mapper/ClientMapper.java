package com.arka.msclients.clients.mapper;

import java.time.LocalDateTime;

import com.arka.msclients.clients.dto.ClientDto;
import com.arka.msclients.clients.model.Client;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static Client toEntity(ClientDto dto){
        if(dto == null) {
            return null;
        }
        Client client = new Client();
        client.setId(dto.getId());
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setUpdatedAt(LocalDateTime.now());
        if(dto.getId() == null) {
            client.setCreatedAt(LocalDateTime.now());
        }
        return client;
    }

    public static ClientDto toDto(Client client){
        if(client == null) {
            return null;
        }
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        return dto;
    }






}
