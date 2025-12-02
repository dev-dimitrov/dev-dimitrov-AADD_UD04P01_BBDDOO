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

public class Bb4 {
    private ObjectContainer db;
    private static final Logger LOG = LoggerFactory.getLogger(Bb4.class);

    public Bb4(File ficheroBd, boolean overwrite) {
        if (overwrite && ficheroBd.exists()) {
            LOG.warn("Se borra la anterior DB");
            ficheroBd.delete();
        }

        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), ficheroBd.getName());

        // Esto configura que borre todo en cascada sobre las intancias de Instituto
        db.ext().configure().objectClass(Instituto.class).cascadeOnDelete(true);
    }

    public void matricularAlumno(Instituto i, Alumno al){

    }

    public void expulsarAlumno(Alumno al){}

    public void  cambiarIdInstituto(int viejoId,int nuevoId){
        Instituto modificar = this.getInstituto(new Instituto(null, viejoId, null));
        modificar.setIdCentro(nuevoId);
        
        db.store(modificar);

    }

    public void guardarAlumno(Alumno al){

    }

    public List<Alumno> getTodosAlumnos(String nombreInstituto){
        List<Alumno> alumnos = null;


        return alumnos;
    }

    public boolean guardarInstituto(Instituto insti){
        boolean status = true;
        try{
            db.store(insti);
            db.commit();
        }
        catch(Exception e){
            status = false;
            db.rollback(); // Esto no tiene mucho sentido pero vale
        }
        

        return status;
    }

    public Instituto getInstituto(Instituto institutoBuscado){
        ObjectSet<Instituto> instis = db.queryByExample(institutoBuscado);
        return instis.getFirst();
    }

    // Se borran todos los institutos que sean iguales de forma transaccional
    public boolean borrarInstituto(Instituto institutoABorrar){
        boolean status = true;
        ObjectSet<Instituto> instis = db.queryByExample(institutoABorrar);

        try{
            for(Instituto i: instis){
                db.delete(i);
            }

            db.commit();
            LOG.info("Se eliminaron "+instis.size()+" ocurrencias de ese instituto satisfactoriamente");
        }
        catch(Exception ex){
            LOG.error("Ocurrió un error mientras se borraban todos las ocurrencias de ese instituto");
            db.rollback();
            status = false;
        }
        return status; 
    }

    public boolean expulsarATodosAlumnos(String nombreInstituto){
        return false;
    }


    // Devuelve todos los institutos en los que esté matriculado un alumno con el nombre pasado por parámetro
    public List<Instituto> consultaInstiMatriculado(String nomAlumno){
        nomAlumno = nomAlumno.toUpperCase();
        int cont = 0;
        List<Instituto> encontrados = new ArrayList<>();
        ObjectSet<Instituto> instis = db.queryByExample(new Instituto(null,null,null));
        for(Instituto i: instis){
            List<Alumno> alum = i.getMatriculados();
            boolean encontrado = false;
            for(int j = 0;j<alum.size() && !encontrado; j++){
                if(alum.get(j).getNombre().equalsIgnoreCase(nomAlumno)){
                    encontrados.add(i);
                    encontrado = true;
                    cont ++;
                }
            }
        }
        LOG.debug("Se encontraron "+cont+" institutos con al menos una persona que se llame "+nomAlumno);
        return encontrados;
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
