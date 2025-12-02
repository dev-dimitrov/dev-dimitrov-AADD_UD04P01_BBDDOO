package dev.dimitrov.obj;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Instituto {
    private String nombreInsti;
    private Integer idCentro;
    private List<Alumno> matriculados;
    

    private static final Logger LOG = LoggerFactory.getLogger(Instituto.class);

    public Instituto(String nombreInsti, Integer idCentro, List<Alumno> matriculados) {
        this.nombreInsti = nombreInsti;
        this.idCentro = idCentro;
        this.matriculados = matriculados;
    }


    public Instituto(){
        matriculados = new ArrayList<>();
    }


    public Instituto(List<Alumno> a){
        matriculados = a;
    }

    public void matricularAlumno(Alumno al){
        boolean status = matriculados.add(al);
        LOG.info("Alumno "+al.toString()+(status ? "matriculado correctamente" : "No se pudo matricular"));
    }

    public void expulsarAlumnos(Alumno al){
        boolean status = matriculados.remove(al);
        LOG.info("Alumno "+al.toString()+(status ? "expulsado correctamente" : "No se pudo expulsar, existe acaso?"));
    }


    public String getNombreInsti() {
        return nombreInsti;
    }

    public void setNombreInsti(String nombreInsti) {
        this.nombreInsti = nombreInsti;
    }

    public Integer getIdCentro() {
        return idCentro;
    }

    public boolean cambiarIdInstituto(Integer idCentro) {
        this.idCentro = idCentro;
        return this.idCentro == idCentro; // lmao
    }
    

    public List<Alumno> getMatriculados() {
        return matriculados;
    }

    @Override
    public String toString() {
        return "Instituto [nombreInsti=" + nombreInsti + ", idCentro=" + idCentro + ", matriculados=" + matriculados
                + "]";
    }


    public void setIdCentro(Integer idCentro) {
        this.idCentro = idCentro;
    }


    public void setMatriculados(List<Alumno> matriculados) {
        this.matriculados = matriculados;
    }
}
