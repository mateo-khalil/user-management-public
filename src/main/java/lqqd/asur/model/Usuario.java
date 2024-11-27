package lqqd.asur.model;

import jakarta.persistence.*;
import lqqd.asur.model.usuario.Estado;
import lqqd.asur.model.usuario.Role;
import lqqd.asur.model.usuario.TipoDocumento;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "usuario")
    private List<Pago> pagos;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    private Long documento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private String domicilio;

    @Column(nullable = false)
    private String telefono1;

    @Column(nullable = true)
    private String telefono2;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Guardar el nombre del enum en la base de datos como String
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING) // Guardar el nombre del enum en la base de datos como String
    @Column(nullable = false)
    private Estado estado;

    @Column(nullable = true)
    private String categoriaSocio;

    @Column(nullable = true)
    private Boolean dificultadAuditiva;

    @Column(nullable = true)
    private Boolean manejaLenguajeSenias;

    @Column(nullable = true)
    private Boolean participaSubcomision;

    @Column(nullable = true)
    private String descripcionSubcomision;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String contrasenia) {
        this.password = contrasenia;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getCategoriaSocio() {
        return categoriaSocio;
    }

    public void setCategoriaSocio(String categoriaSocio) {
        this.categoriaSocio = categoriaSocio;
    }

    public Boolean getDificultadAuditiva() {
        return dificultadAuditiva;
    }

    public void setDificultadAuditiva(Boolean dificultadAuditiva) {
        this.dificultadAuditiva = dificultadAuditiva;
    }

    public Boolean getManejaLenguajeSenias() {
        return manejaLenguajeSenias;
    }

    public void setManejaLenguajeSenias(Boolean manejaLenguajeSenias) {
        this.manejaLenguajeSenias = manejaLenguajeSenias;
    }

    public Boolean getParticipaSubcomision() {
        return participaSubcomision;
    }

    public void setParticipaSubcomision(Boolean participaSubcomision) {
        this.participaSubcomision = participaSubcomision;
    }

    public String getDescripcionSubcomision() {
        return descripcionSubcomision;
    }

    public void setDescripcionSubcomision(String descripcionSubcomision) {
        this.descripcionSubcomision = descripcionSubcomision;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public boolean isSocio() {
        return categoriaSocio != null;
    }

    @Override
    public String toString() {
        return String.format(
                """
                        ========================================
                        Usuario:       %s %s
                        ========================================
                        ID:            %d
                        Nombres:       %s
                        Apellidos:     %s
                        Documento:     %s (%s)
                        Email:         %s
                        Rol:           %s
                        Estado:        %s
                        ========================================
                        """,
                nombres, apellidos, id, nombres, apellidos, documento, tipoDocumento, email, role, estado
        );
    }
}
