package dev.dimitrov.util;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import dev.dimitrov.obj.Alumno;
import dev.dimitrov.obj.Instituto;

public class Bbddoo {
    private static final Logger LOG = LoggerFactory.getLogger(Bbddoo.class);
    private ObjectContainer db = null;
    private Instituto insti = null;
    public Bbddoo(File ficheroBd, boolean overwrite){

        if(overwrite && ficheroBd.exists()){
            LOG.warn("Se borra la anterior DB");
            ficheroBd.delete();
        }

        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ficheroBd.getName());
        db.ext().configure().objectClass(Instituto.class).cascadeOnDelete(true);
    }

    public void guardarAlumno(Alumno al) {
        if(insti != null){
            ObjectSet<Instituto> instis = db.queryByExample(insti);
            Instituto instiSeleccionado = instis.get(0);
            instiSeleccionado.matricularAlumno(al);
            db.store(instiSeleccionado);
        }
        else{
            LOG.error("No se ha podido guardar el alumno porque no hay ningun instituto en el que guardarlo");
        }
       
    }

    public List<Alumno> getTodosAlumnos() {
        ObjectSet<Instituto> insti = db.queryByExample(new Instituto(null,null,null));
        Instituto i = insti.get(0);
        return i.getMatriculados();
    }

    public boolean guardarInstituto(Instituto insti) {
        boolean status = false;
        
        // comprobacion de si hay 2 institutos (Solo se puuede guardar uno)
        if (this.insti == null){
            db.store(insti);
            this.insti = insti;
            status = true;
            LOG.info("Guardado el instituto");
        }
        else{
            LOG.warn("Ya existe un instituto almacenado");
        }
        
        
        return status;
    }

    public Instituto getInstituto() {
        if(insti == null){
            LOG.error("No esta guardado aun ningun instituto");
        }
        else{
            ObjectSet<Instituto> instituto = db.queryByExample(insti);
            insti = instituto.get(0);
        }

        return insti;
    }

    public boolean borrarInstituto() {
        boolean status = false;
        if(insti != null){
            Instituto instiDb = this.getInstituto();
            db.delete(instiDb);
            insti = null;
            status = true;
        }
        return status;
    }

    public boolean expulsarATodosAlumnos(){
        boolean status = false;
        if(insti != null){
            ObjectSet<Alumno> todos = db.queryByExample(new Alumno(null, null, null));
            try{
                for(Alumno a: todos){
                    db.delete(a);
                }
                db.commit();
                status = true;
                LOG.info("Se completó el borrado de todos los alumnos del instituto");
            }
            catch(Exception e){
                db.rollback();
                LOG.error("Hubo un problema durante el borrado de todos los alumnos, se han revertido todos los cambios");
            }
            
        }
        else{
            LOG.warn("No se puede expulsar a ningun alumno porque no hay un instituto guardado");
        }
        return status;
    }

    public void close(){
        db.close();
    }
}
