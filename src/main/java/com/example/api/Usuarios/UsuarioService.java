package com.example.api.Usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    public Optional<Usuario> getUsuarioById(String id) {
        return repository.findById(id);
    }

    public Optional<Usuario> getUsuarioByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public Usuario createUsuario(Usuario ejemplo) {
        return repository.save(ejemplo);
    }

    public Usuario updateUsuario(String id, Usuario ejemplo) {
        if (repository.existsById(id)) {
            ejemplo.setId(id);
            return repository.save(ejemplo);
        } else {
            return null;
        }
    }

    public void deleteUsuario(String id) {
        repository.deleteById(id);
    }

    public void deleteAllUsuarios() {
        repository.deleteAll();
    }

    public Optional<Usuario> getUsuarioAleatorio() {
        Random random = new Random();
        int numero = random.nextInt(9999);
        return this.getUsuarioByNombre("Nombre usuario " +numero);
    }

    public List<Usuario> getAllUsuariosAleatorio() {
        List<Usuario> listaUsuarios = repository.findAll();
        Collections.shuffle(listaUsuarios);
        return listaUsuarios;
    }
}
