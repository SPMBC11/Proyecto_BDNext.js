package edu.ucol.taller3jee.dto;

public class CrearEstudianteDTO {
    private String nombre;
    private String email;
    private String matricula;

    public CrearEstudianteDTO() {
    }

    public CrearEstudianteDTO(String nombre, String email, String matricula) {
        this.nombre = nombre;
        this.email = email;
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
