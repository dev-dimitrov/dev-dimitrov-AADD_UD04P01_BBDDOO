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

public class Bbddoo2 {
    private static final Logger LOG = LoggerFactory.getLogger(Bbddoo.class);
    private ObjectContainer db = null;
    private Instituto insti = null;

    public Bbddoo2(File ficheroBd, boolean overwrite){

        if(overwrite && ficheroBd.exists()){
            LOG.warn("Se borra la anterior DB");
            ficheroBd.delete();
        }

        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ficheroBd.getName());
        db.ext().configure().objectClass(Instituto.class).cascadeOnDelete(true);
    }

    public void guardarAlumno(Alumno al, Instituto i) {
        ObjectSet<Instituto> instis = db.queryByExample(i);
        Instituto modificado = null;
        if(!instis.isEmpty() && instis.size() == 1){
            modificado = instis.getFirst();

            modificado.matricularAlumno(al);
            db.store(modificado);
            LOG.info("Matriculado el alumno: "+al+" en el instituto con ID: "+modificado.getIdCentro());
        }
    }

    
    // Devuelve los alumnos de todos los institutos
    public List<Alumno> getTodosAlumnos() {
        List<Alumno> todosAlumnos = new ArrayList<>();
        ObjectSet<Instituto> todosInstis = db.queryByExample(new Instituto(null));

        for(Instituto i: todosInstis){
            todosAlumnos.addAll(i.getMatriculados());
        }

        LOG.info("recuperados un total de "+todosAlumnos+" alumno/s");
        return todosAlumnos;
    }

    public void addInstituto(Instituto insti) {
        db.store(insti);

        LOG.info("Añadido el instituto: con id: "+insti.getIdCentro());
    }

    // Devuelve la primera ocurrencia de ese instituto
    public Instituto getInstituto(Instituto i) {
        ObjectSet<Instituto> instis = db.queryByExample(i);
        return instis.getFirst();
    }

    public void borrarInstituto(Instituto insti) {
        ObjectSet<Instituto> instis = db.queryByExample(insti);
        int cont = 0;
        for(Instituto i: instis){
            db.delete(i);
            cont ++;
        }
        LOG.info("Se borraron un total de "+cont+" institutos de la DB");
    }

    public void expulsarATodosAlumnos(Instituto i){
        Instituto institutoRecogido = this.getInstituto(i);

        institutoRecogido.getMatriculados().clear();
 
        db.store(institutoRecogido);
        LOG.info("Borrados todos los institutos del id: "+i.getIdCentro());
    }
    
    public void verTodo(){
        ObjectSet<Instituto> instis = db.queryByExample(new Instituto());
        if (!instis.isEmpty()){
            System.out.println("----------------------------------");
            for(Instituto i: instis){
                System.out.println("INSTITUTO: "+i.getNombreInsti());
                for (Alumno a: i.getMatriculados()){
                    System.out.println(a);
                }
                System.out.println("----------------------------------");
            }   
        }
        

    }

    public void close(){
        db.close();
    }
}
