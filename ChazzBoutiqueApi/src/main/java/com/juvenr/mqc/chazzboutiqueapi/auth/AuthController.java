package com.juvenr.mqc.chazzboutiqueapi.auth;

import com.juvenr.mqc.chazzboutiqueapi.auth.dto.LoginRequest;
import com.juvenr.mqc.chazzboutiqueapi.auth.dto.LoginResponse;
import com.mycompany.chazzboutiquenegocio.dtos.InicioSesionDTO;
import com.mycompany.chazzboutiquenegocio.dtos.UsuarioDTO;
import com.mycompany.chazzboutiquenegocio.excepciones.NegocioException;
import com.mycompany.chazzboutiquenegocio.interfacesObjetosNegocio.IUsuarioNegocio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUsuarioNegocio usuarioNegocio;

    public AuthController(IUsuarioNegocio usuarioNegocio) {
        this.usuarioNegocio = usuarioNegocio;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        UsuarioDTO usuario;
        try {
            usuario = usuarioNegocio.iniciarSesion(
                    new InicioSesionDTO(req.getNombreUsuario(), req.getContrasena())
            );
        } catch (NegocioException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }

        if (usuario.getActivo() == null || !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");
        }

        return new LoginResponse(usuario.getId(), usuario.getNombreUsuario(), usuario.getRol());
    }
}
