package dev.dimitrov;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.dimitrov.obj.Alumno;
import dev.dimitrov.obj.Instituto;
import dev.dimitrov.util.Bbddoo3;

public class Main2 {
    public static void main(String[] args) {
        Bbddoo3 d = new Bbddoo3(new File("ejemplo.db4o"), true);
        Instituto i1 = new Instituto("GTB",2, new ArrayList<Alumno>(List.of(
            new Alumno("Paco"),
            new Alumno("Jose"),
            new Alumno("Luis"),
            new Alumno("Mateo"),
            new Alumno("Juan"),
            new Alumno("Pepe")
        )));

        Instituto i2 = new Instituto("ATENEA",2, new ArrayList<Alumno>(List.of(
            new Alumno("Fran"),
            new Alumno("Jose"),
            new Alumno("Carlos"),
            new Alumno("David"),
            new Alumno("Mortadelo"),
            new Alumno("Oriol")
        )));

        d.addInsti(i1);
        d.addInsti(i2);
        d.guardarAlumno(i2, new Alumno("Carlos archidona"));
        
        List<Alumno> a=  d.getTodosAlumnos("ATENEA");

/*         for(Alumno i: a){
            System.out.println(a);
        } */

        // System.out.println(d.getInstituto(i1).getNombreInsti());
        d.verTodo();
        // d.borrarInstituto(i2);
        System.out.println(d.consultaInstiMatriculado("carlos archidona").getNombreInsti());
        d.verTodo();
        d.close();
    }
}
