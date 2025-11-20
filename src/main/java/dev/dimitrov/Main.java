package dev.dimitrov;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dev.dimitrov.obj.Alumno;
import dev.dimitrov.obj.Instituto;
import dev.dimitrov.util.Bbddoo;

public class Main {
    public static void main(String[] args) {
        Bbddoo d = new Bbddoo(new File("insti.db4o"), false);

        List<Alumno> n = d.getTodosAlumnos();

        System.out.println(n);

        d.close();
    }
}