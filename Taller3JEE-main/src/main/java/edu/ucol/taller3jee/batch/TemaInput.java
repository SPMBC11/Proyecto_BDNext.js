package edu.ucol.taller3jee.batch;

import edu.ucol.taller3jee.entity.Tema;
import edu.ucol.taller3jee.entity.TipoTema;

public class TemaInput {
    private Long id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private Integer numeroOrden;
    private String objetivos;
    private Integer duracionEstimada;

    public Tema convertirAEntidad() {
        Tema tema = new Tema();
        tema.setTitulo(this.titulo);
        tema.setDescripcion(this.descripcion);
        tema.setTipo(TipoTema.valueOf(this.tipo.toUpperCase()));
        tema.setNumeroOrden(this.numeroOrden);
        tema.setObjetivos(this.objetivos);
        tema.setDuracionEstimada(this.duracionEstimada);
        return tema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }
}
