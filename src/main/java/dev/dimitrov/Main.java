package dev.dimitrov;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.dimitrov.obj.Alumno;
import dev.dimitrov.obj.Instituto;
import dev.dimitrov.util.Bbddoo2;

public class Main {
    public static void main(String[] args) {
        Bbddoo2 d = new Bbddoo2(new File("insti.db4o"), true);
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

        d.addInstituto(i1);
        d.addInstituto(i2);
        d.verTodo();
        d.guardarAlumno(new Alumno("Alvaro"), i2);
        d.verTodo();
        d.expulsarATodosAlumnos(i2);
        d.verTodo();
        d.guardarAlumno(new Alumno("JoseLuis"), i2);
        d.verTodo();
        d.borrarInstituto(i2);
        d.verTodo();
        d.close();
    }
}