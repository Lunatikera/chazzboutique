package com.juvenr.mqc.chazzboutiqueapi.auth.dto;

public class LoginResponse {

    private Long usuarioId;
    private String nombreUsuario;
    private String rol;

    public LoginResponse() {
    }

    public LoginResponse(Long usuarioId, String nombreUsuario, String rol) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
