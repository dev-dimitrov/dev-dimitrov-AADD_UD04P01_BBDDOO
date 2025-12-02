package dev.dimitrov;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.dimitrov.obj.Alumno;
import dev.dimitrov.obj.Instituto;
import dev.dimitrov.util.Bb4;

public class Main3 {
    public static void main(String[] args) {
        Bb4 db = new Bb4(new File("ejemplo2.db4o"), true);

        Instituto i1 = new Instituto("GTB",2, new ArrayList<Alumno>(List.of(
            new Alumno("Paco"),
            new Alumno("Jose"),
            new Alumno("Luis"),
            new Alumno("Mateo"),
            new Alumno("Juan"),
            new Alumno("Oriol")
        )));

        Instituto i2 = new Instituto("ATENEA",3, new ArrayList<Alumno>(List.of(
            new Alumno("Fran"),
            new Alumno("Jose"),
            new Alumno("Carlos"),
            new Alumno("David"),
            new Alumno("Mortadelo"),
            new Alumno("Oriol")
        )));

        db.guardarInstituto(i1);
        db.guardarInstituto(i2);
        db.verTodo();
        List<Instituto> is  = db.consultaInstiMatriculado("oriol");

        for(Instituto i: is){
            System.out.println(i.getNombreInsti());
        }

        db.close();
    }
}
