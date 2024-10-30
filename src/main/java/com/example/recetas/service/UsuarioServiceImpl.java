package com.example.recetas.service;

import com.example.recetas.model.Response;
import com.example.recetas.model.Usuario;
import com.example.recetas.repository.UsuarioRepository;
import com.example.recetas.util.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTUtil jwtTokenUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<Response> crearUsuario(Usuario usuario) throws Exception {
        Response response;
        try {
            if (!isValidEmail(usuario.getCorreo())) {
                response = new Response("error", null, "Correo no válido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!isValidPassword(usuario.getContrasena())) {
                response = new Response("error", null,
                        "Contraseña no válida. Debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una minúscula, un número y un carácter especial.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!isValidRole(usuario.getRol())) {
                response = new Response("error", null, "Rol no válido. Solo se permite 'admin' o 'cliente'.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Hashear la contraseña antes de guardarla
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuario.setFechaRegistro(LocalDate.now());

            // Guardar usuario
            Usuario newUser = usuarioRepository.save(usuario);
            response = new Response("success", newUser, "");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response = new Response("error", null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Response> login(String correo, String contrasena, HttpServletResponse httpResponse)
            throws Exception {
        Response response;
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            response = new Response("error", null, "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            response = new Response("error", null, "Contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Generar el token JWT
        String token = jwtTokenUtil.generateToken(usuario.getCorreo());

        // Crear la cookie para almacenar el token JWT
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(false); // Asegura que no sea accesible por JavaScript
        jwtCookie.setSecure(false); // Asegura que solo se envíe en HTTPS (activar en producción)
        jwtCookie.setPath("/"); // Establece el alcance de la cookie
        jwtCookie.setMaxAge(10 * 60 * 60); // Expira en 10 horas

        // Agregar la cookie en la respuesta HTTP
        httpResponse.addCookie(jwtCookie);

        // Respuesta de éxito
        response = new Response("success", null, "Inicio de sesión exitoso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Mantén esto en true para producción (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(0); // Establece la edad a 0 para eliminar la cookie

        response.addCookie(cookie);
    }

    @Override
    public ResponseEntity<Response> actualizarRol(Long idUsuario, String nuevoRol) throws Exception {
        Response response;
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            response = new Response("error", null, "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Usuario usuario = usuarioOpt.get();
        if (!isValidRole(nuevoRol)) {
            response = new Response("error", null, "Rol no valido");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        usuario.setRol(nuevoRol);
        Usuario updatedUser = usuarioRepository.save(usuario);
        response = new Response("success", updatedUser, "");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @Override
    public ResponseEntity<Response> eliminarUsuario(Long idUsuario) throws Exception {
        Response response;
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            response = new Response("error", null, "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        usuarioRepository.deleteById(idUsuario);
        response = new Response("success", new String("Usuario eliminado con exito"), "");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Funciones de validación
    public boolean isValidRole(String role) {
        String lowerCaseRole = role.toLowerCase();
        return lowerCaseRole.equals("admin") || lowerCaseRole.equals("cliente");
    }

    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}
