package lqqd.asur.model.usuario;

public enum Estado {
    SIN_VALIDAR, // Usuarios que han solicitado su registro, pero sus datos aún están pendientes de validación
    VALIDADO, // Usuarios cuyos datos han sido validados por un Auxiliar Administrativo y por lo tanto se encuentran Activos
    INACTIVO // Usuarios que han sido dados de baja en forma lógica
    ;

    public boolean isActivo() {
        return this == VALIDADO;
    }
}
