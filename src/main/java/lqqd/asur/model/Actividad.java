package lqqd.asur.model;

import jakarta.persistence.*;
import lqqd.asur.model.actividad.Estado;
import lqqd.asur.model.actividad.FormaPago;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "tipo_actividad_id", nullable = false)
    private TipoActividad tipoActividad;

    @Column(nullable = false)
    private LocalDateTime fechaInicioConHora;

    @Column(nullable = false)
    private LocalDateTime fechaFinConHora;

    @ManyToOne
    @JoinColumn(name = "espacio_id", nullable = false)
    private Espacio lugar;

    @Column(nullable = false)
    private Boolean requiereInscripcion;

    @Column
    private LocalDateTime fechaAperturaInscripcion;

    @Column(nullable = false)
    private Double costo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaPago formaPago;

    @Column
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.ACTIVA;


    // ==============================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoActividad getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(TipoActividad tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public LocalDateTime getFechaInicioConHora() {
        return fechaInicioConHora;
    }

    public void setFechaInicioConHora(LocalDateTime fechaInicioConHora) {
        this.fechaInicioConHora = fechaInicioConHora;
    }

    public LocalDateTime getFechaFinConHora() {
        return fechaFinConHora;
    }

    public void setFechaFinConHora(LocalDateTime fechaFinConHora) {
        this.fechaFinConHora = fechaFinConHora;
    }

    public Espacio getLugar() {
        return lugar;
    }

    public void setLugar(Espacio lugar) {
        this.lugar = lugar;
    }

    public Boolean getRequiereInscripcion() {
        return requiereInscripcion;
    }

    public void setRequiereInscripcion(Boolean requiereInscripcion) {
        this.requiereInscripcion = requiereInscripcion;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public LocalDateTime getFechaAperturaInscripcion() {
        return fechaAperturaInscripcion;
    }

    public void setFechaAperturaInscripcion(LocalDateTime fechaAperturaInscripcion) {
        this.fechaAperturaInscripcion = fechaAperturaInscripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Calcula y devuelve la duración en horas de la actividad.
     *
     * @return La duración en horas, o null si las fechas no están definidas.
     */
    public Integer calcularDuracionHoras() {
        if (fechaInicioConHora != null && fechaFinConHora != null) {
            return (int) Duration.between(fechaInicioConHora, fechaFinConHora).toHours();
        }
        return null;
    }
}
