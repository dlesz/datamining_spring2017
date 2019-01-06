package Enums;

/**
 * Created by dlesz on 25/04/2017.
 */
public class ProgrammingLanguages {

    public static int ProgrammingLanguages(String s) {
       if (s.equals("android")) return 0;
       if (s.equals("bash")) return 1;
       if (s.equals("batch")) return 2;
       if (s.equals("c")) return 3;
       if (s.equals("c#") || s.equals( "c sharp") || s.equals("sharp")|| s.equals("c##")) return 4;
       if (s.equals("css")) return 6;
       if (s.equals("erlang")) return 7;
       if (s.equals("f#")) return 8;
       if (s.equals("gml") || s.equals("gml.")) return 9;
       if (s.equals("golang")) return 10;
       if (s.equals("haskell")) return 11;
       if (s.equals("haxe")) return 12;
       if (s.equals("html") || s.equals("html5")) return 13;
       if (s.equals("java")) return 14;
       if (s.equals("javascript") || s.equals("js") || s.equals("jvascript")) return 15;
       if (s.equals("kotlin")) return 16;
       if (s.equals("lua")) return 17;
       if (s.equals("matlab")) return 18;
       if (s.equals("pascal")) return 19;
       if (s.equals("php")) return 20;
       if (s.equals("powershell")) return 21;
       if (s.equals("processing")) return 22;
       if (s.equals("processing arduino")) return 23;
       if (s.equals("prolog")) return 24;
       if (s.equals("puredata")) return 25;
       if (s.equals("python")) return 26;
       if (s.equals("r")) return 27;
       if (s.equals("ruby")) return 28;
       if (s.equals("scala")) return 29;
       if (s.equals("sql")) return 30;
       if (s.equals("swift")) return 31;
       if (s.equals("vbnet")) return 32;
       if (s.equals("vba") || s.equals("vb")) return 33;
       if (s.equals("vvvv")) return 34;
       if (s.equals("c++")) return 35;
       if (s.equals("arduino"))return 36;
       if (s.equals("")) return -1; // if empty string or null
       if (s.equals("34")) return -1; // a quick fix --> would not work with huge data set.
       else return -999;
    }
}