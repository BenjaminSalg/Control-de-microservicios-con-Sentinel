package com.example.api.Usuarios;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Usuarios")
@Data
public class Usuario {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
}