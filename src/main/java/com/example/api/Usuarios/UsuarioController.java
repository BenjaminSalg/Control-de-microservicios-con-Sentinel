package com.example.api.Usuarios;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/user")
public class UsuarioController {
    @Autowired
    private  UsuarioService usuarioService;

    @GetMapping("/")
    public String getIndex() {
        return "";
    }

    @SentinelResource(value = "getAllUsuarios")
    @GetMapping("/all")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @SentinelResource(value = "getAllUsuariosAleatorio")
    @GetMapping("/allAleatorio")
    public List<Usuario> getAllUsuariosAleatorio() {
        return usuarioService.getAllUsuariosAleatorio();
    }

    @SentinelResource(value = "getUsuarioById")
    @GetMapping("/{id}")
    public ResponseEntity<String> getUsuarioById(@PathVariable String id) {
        Optional<Usuario> usuarioOptional = usuarioService.getUsuarioById(id);

        if (usuarioOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        //Pasa los datos de usuario a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String body = objectMapper.writeValueAsString(usuarioOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error interno.");
        }
    }

    @SentinelResource(value = "getUsuariosAleatorio")
    @GetMapping("/usuarioAleatorio")
    public ResponseEntity<String> getUsuarioAleatorio() {
        Optional<Usuario> usuarioOptional = usuarioService.getUsuarioAleatorio();

        if (usuarioOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        //Pasa los datos de usuario a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String body = objectMapper.writeValueAsString(usuarioOptional.get());

            return ResponseEntity.status(HttpStatus.OK).headers(this.getHeaders()).body(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error interno.");
        }
    }

    /**
     * Crea el encabezado para las respuestas con la fecha en formato GMT-3
     */
    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("GMT-3"));
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss"));
        headers.set(HttpHeaders.DATE, formattedDate);
        return headers;
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.createUsuario(usuario);
    }

    @PutMapping("/{id}")
    public Usuario updateUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable String id) {
        usuarioService.deleteUsuario(id);
    }

    /***
     * Crea 10mil usuarios
     */
    @PostMapping("/crearMuchos")
    public void crearMuchos(){
        int cantUsuarios = this.getAllUsuarios().size();
        for (int i=cantUsuarios; i<cantUsuarios+10000; i++){
            Usuario nuevoUser = new Usuario();
            nuevoUser.setNombre("Nombre usuario " +i);
            nuevoUser.setDescripcion("Descripcion usuario " +i);
            usuarioService.createUsuario(nuevoUser);
        }
    }

    @PostMapping("/borrarTodos")
    public void borrarTodos(){
        usuarioService.deleteAllUsuarios();
    }

    /**
     * Maneja las excepciones lanzadas por Sentinel
     */
    @ExceptionHandler(FlowException.class)
    public ResponseEntity<String> handleFlowException(FlowException ex) {
        //Obtiene tipo de umbral (QPS/Hilos)
        int grade = ex.getRule().getGrade();

        String msj = switch (grade) {
            case RuleConstant.FLOW_GRADE_QPS -> "Demasiadas solicitudes al mismo tiempo. Por favor, espere un momento.";
            case RuleConstant.FLOW_GRADE_THREAD -> "Demasiados hilos ejecutandose. Por favor, espere un momento.";
            default -> "Regla no encontrada, espere un momento.";
        };

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(msj);
    }
}
