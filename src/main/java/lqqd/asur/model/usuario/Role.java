package lqqd.asur.model.usuario;

public enum Role {
    ADMINISTRADOR,
    AUXILIAR_ADMINISTRATIVO,
    SOCIO,
    NO_SOCIO;

    public boolean isAdminOrAuxiliar() {
        return this == ADMINISTRADOR || this == AUXILIAR_ADMINISTRATIVO;
    }

    public boolean isSocioOrNoSocio() {
        return this == SOCIO || this == NO_SOCIO;
    }
}