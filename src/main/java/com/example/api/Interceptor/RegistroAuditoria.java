package com.example.api.Interceptor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RegistrosAuditoria")
@Data
public class RegistroAuditoria {
    @Id
    private String id;
    private String metodo;
    private String controlador;
    private String ipAddress;
    private String fecha;
}
