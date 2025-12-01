package dev.dimitrov.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import dev.dimitrov.obj.Alumno;
import dev.dimitrov.obj.Instituto;

public class Bbddoo3 {
    
    private static final Logger LOG = LoggerFactory.getLogger(Bbddoo3.class);
    private ObjectContainer db;

    public Bbddoo3(File ficheroBd, boolean overwrite) {

        if (overwrite && ficheroBd.exists()) {
            LOG.warn("Se borra la anterior DB");
            ficheroBd.delete();
        }

        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ficheroBd.getName());

        // Esto configura que borre todo en cascada sobre las intancias de Insituto
        db.ext().configure().objectClass(Instituto.class).cascadeOnDelete(true);
    }

    public void addInsti(Instituto insti) {
        db.store(insti);
    }

    public void guardarAlumno(Instituto i, Alumno al) {
        ObjectSet<Instituto> instis = db.queryByExample(i);

        Instituto instiDb = instis.get(0);

        instiDb.matricularAlumno(al);

        db.store(instiDb);
    }

    public void verTodo() {
        ObjectSet<Instituto> instis = db.queryByExample(new Instituto());
        if (!instis.isEmpty()) {
            System.out.println("----------------------------------");
            for (Instituto i : instis) {
                System.out.println("INSTITUTO: " + i.getNombreInsti());
                for (Alumno a : i.getMatriculados()) {
                    System.out.println(a);
                }
                System.out.println("----------------------------------");
            }
        }
    }

    public List<Alumno> getTodosAlumnos(String nombre) {
        List<Alumno> alumnos = null;
        ObjectSet<Instituto> instisRecuperados = db.queryByExample(new Instituto(nombre, null, null));

        if (instisRecuperados.size() > 0) {
            alumnos = new ArrayList<>();

            alumnos.addAll(instisRecuperados.getFirst().getMatriculados());
        } else {
            LOG.warn("No se recuperó ningun instituto con el nombre: " + nombre);
        }

        return alumnos;
    }

    public Instituto getInstituto(Instituto i) {
        Instituto instiRecuperado = null;

        ObjectSet<Instituto> instis = db.queryByExample(i);
        instiRecuperado = instis.next();

        return instiRecuperado;
    }

    public boolean borrarInstituto(Instituto institutoABorrar) {
        boolean status = false;
        db.delete(getInstituto(institutoABorrar));

        return status;
    }

    public void close() {
        db.close();
    }

    public boolean expulsarATodosAlumnos (String nombreInstituto){
        boolean status = false;
        Instituto i = new Instituto(nombreInstituto,null,null);
        i = getInstituto(i);
        try{
            List<Alumno> alumnos = i.getMatriculados();
            for(Alumno alumno: alumnos){
                i.expulsarAlumnos(alumno);
            }

            db.commit();
            status = true;
        }
        catch(Exception e){
            db.rollback();
            LOG.error("Ocurrió el siguiente error mientras se borraban alumnos: "+e.getMessage());
        }

        return status;
    }


    public Instituto consultaInstiMatriculado(String nomAlumno){
        Instituto target = null;
        ObjectSet<Instituto> instis = db.queryByExample(new Instituto(null,null,null));
        for(Instituto instituto: instis){
            List<Alumno> alumnos = instituto.getMatriculados();
            for(int i = 0; i<alumnos.size(); i++){
                if(alumnos.get(i).getNombre().equalsIgnoreCase(nomAlumno)){
                    target = instituto;
                    break;
                }
            }

            if (target != null){
                break;
            }
        }

        return target;
    }
}
