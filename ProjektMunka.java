package projektmunka;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

interface Compere {

    boolean function(double x, double y);
}

class CSVfile {

    Integer Ev;
    Double B;
    Double K;
    Double KperB;

    public CSVfile(Integer Ev, Double B, Double K, Double KperB) {
        this.Ev = Ev;
        this.B = B;
        this.K = K;
        this.KperB = KperB;
    }

    public Integer getEv() {
        return Ev;
    }

    public Double getB() {
        return B;
    }

    public Double getK() {
        return K;
    }

    public Double getKperB() {
        return KperB;
    }

    @Override
    public String toString() {
        return Ev + ", " + B + ", " + K + ", " + KperB;
    }

}

public class ProjektMunka {

    public static void main(String[] args) {
        
        File kulkereskedelmiTermekforgalom = new File("C:\\Users\\Peti\\OneDrive\\Asztali gép\\Iskola\\Prog 2\\Projekt1\\stadat-ara0007-1.1.1.7-hu2.csv");
        
        ArrayList<CSVfile> lista = new ArrayList<>();
        fileRead(lista, kulkereskedelmiTermekforgalom);
        
        fileName(kulkereskedelmiTermekforgalom);
        fileColumnName(kulkereskedelmiTermekforgalom);
        lista.forEach(System.out::println);
        sorValaszto();
        fileColumnName(kulkereskedelmiTermekforgalom, 1);
        lista.forEach(n -> System.out.print(n.Ev + ", "));
        System.out.println("");
        fileColumnName(kulkereskedelmiTermekforgalom, 2);
        lista.forEach(n -> System.out.print(n.B + ", "));
        System.out.println("");
        fileColumnName(kulkereskedelmiTermekforgalom, 3);
        lista.forEach(n -> System.out.print(n.K + ", "));
        System.out.println("");
        fileColumnName(kulkereskedelmiTermekforgalom, 4);
        lista.forEach(n -> System.out.print(n.KperB + ", "));
        System.out.println("");
        sorValaszto();
        System.out.print("A legnyagyobb behozatal értéke: ");
        System.out.println(lista.stream().max(Comparator.comparingDouble(CSVfile::getB)).get().B);
        System.out.print("A legkisebb behozatal értéke: ");
        System.out.println(lista.stream().min(Comparator.comparingDouble(CSVfile::getB)).get().B);
        System.out.print("A legnagyobb kivitel értéke: ");
        System.out.println(lista.stream().max(Comparator.comparingDouble(CSVfile::getK)).get().K);
        System.out.print("A legkisebb kivitel értéke: ");
        System.out.println(lista.stream().min(Comparator.comparingDouble(CSVfile::getK)).get().K);
        System.out.print("A legnagyobb cserearány: ");
        System.out.println(lista.stream().max(Comparator.comparingDouble(CSVfile::getKperB)).get().KperB); //Ha több maximum érték is van akkor a tömben a legelsőt fogja visszaadni.
        System.out.print("A legkisebb cserearány: ");
        System.out.println(lista.stream().min(Comparator.comparingDouble(CSVfile::getKperB)).get().KperB);
        sorValaszto();
        System.out.println("Nemvolt ilyen magas a behozatal (ami " + lista.get(lista.size() - 1).B + ") " + (2021 - EvotaB(lista, (x, y) -> x > y)) + "-óta (ami " + lista.get(lista.size() - 1 - EvotaB(lista, (x, y) -> x > y)).B + " volt).");
        System.out.println("Nemvolt ilyen kicsi a behozatal (ami " + lista.get(lista.size() - 1).B + ") " + (2021 - EvotaB(lista, (x, y) -> x < y)) + "-óta (ami " + lista.get(lista.size() - 1 - EvotaB(lista, (x, y) -> x < y)).B + " volt).");
        System.out.println("Nemvolt ilyen magas a kivitel (ami " + lista.get(lista.size() - 1).K + ") " + (2021 - EvotaK(lista, (x, y) -> x > y)) + "-óta (ami " + lista.get(lista.size() - 1 - EvotaK(lista, (x, y) -> x > y)).K + " volt).");
        System.out.println("Nemvolt ilyen kicsi a kivitel (ami " + lista.get(lista.size() - 1).K + ") " + (2021 - EvotaK(lista, (x, y) -> x < y)) + "-óta (ami " + lista.get(lista.size() - 1 - EvotaK(lista, (x, y) -> x < y)).K + " volt).");
        System.out.println("Nemvolt ilyen magas a cserearány (ami " + lista.get(lista.size() - 1).KperB + ") " + (2021 - EvotaKperB(lista, (x, y) -> x > y)) + "-óta (ami " + lista.get(lista.size() - 1 - EvotaKperB(lista, (x, y) -> x > y)).KperB + " volt).");
        System.out.println("Nemvolt ilyen kicsi a cserearány (ami " + lista.get(lista.size() - 1).KperB + ") " + (2021 - EvotaKperB(lista, (x, y) -> x < y)) + "-óta (ami " + lista.get(lista.size() - 1 - EvotaKperB(lista, (x, y) -> x < y)).KperB + " volt).");
        sorValaszto();
        //Folytonosság
        System.out.println("A behozatal jelengleg " + folytonossagB(lista) + "" + folyotnossagEVB(lista) + "-óta ");
        System.out.println("A kivitel jelengleg " + folytonossagK(lista) + "" + folyotnossagEVK(lista) + "-óta ");
        System.out.println("A cserearány jelengleg " + folytonossagKperB(lista) + "" + folyotnossagEVKperB(lista) + "-óta ");
        
    }
    
    public static void sorValaszto() {
        for (int i = 0; i < 100; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }
    
    public static void fileRead(ArrayList<CSVfile> csv, File f) {
        try {
            Scanner scan = new Scanner(f);
            scan.nextLine();
            scan.nextLine();
            while (scan.hasNextLine()) {
                String beolvasottsor = scan.nextLine();
                String sor = beolvasottsor.replace(',', '.'); //A ,-t át kell alakítani .-tá, hogy át lehessen írni számmá.
                String[] adatok = sor.split(";");
                CSVfile csvadat = new CSVfile(Integer.parseInt(adatok[0]), Double.parseDouble(adatok[1]), Double.parseDouble(adatok[2]), Double.parseDouble(adatok[3]));
                csv.add(csvadat);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }

    }
    
    public static void fileName(File f) {
        try {
            Scanner scan = new Scanner(f);
            System.out.println(scan.nextLine());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void fileColumnName(File f) {
        try {
            Scanner scan = new Scanner(f);
            scan.nextLine();
            System.out.println(scan.nextLine());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static void fileColumnName(File f, int i) {
        try {
            Scanner scan = new Scanner(f);
            scan.nextLine();
            String sor = scan.nextLine();
            String[] adatok = sor.split(";");
            System.out.print(adatok[i - 1] + ": ");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    static int EvotaB(ArrayList<CSVfile> lista, Compere filt) {
        //2021-es adat
        double adat2021 = lista.get(lista.size() - 1).B;
        for (int i = lista.size() - 1; i >= 0; i--) {
            if (filt.function(lista.get(i).B, adat2021)) {
                return lista.get(lista.size() - 1).Ev - lista.get(i).Ev;
            }
        }
        return -1;
    }

    static int EvotaK(ArrayList<CSVfile> lista, Compere filt) {
        //2021-es adat
        double adat2021 = lista.get(lista.size() - 1).K;
        for (int i = lista.size() - 1; i >= 0; i--) {
            if (filt.function(lista.get(i).K, adat2021)) {
                return lista.get(lista.size() - 1).Ev - lista.get(i).Ev;
            }
        }
        return -1;
    }

    static int EvotaKperB(ArrayList<CSVfile> lista, Compere filt) {
        //2021-es adat
        double adat2021 = lista.get(lista.size() - 1).KperB;
        for (int i = lista.size() - 1; i >= 0; i--) {
            if (filt.function(lista.get(i).KperB, adat2021)) {
                return lista.get(lista.size() - 1).Ev - lista.get(i).Ev;
            }
        }
        return -1;
    }

    static String folytonossagB(ArrayList<CSVfile> lista) {
        //Ha bármilyen fájl vizsgálnánk akkor kéne egyenlőséget is nézni
        if (lista.get(lista.size() - 1).B > lista.get(lista.size() - 2).B) {
            return "növekszik ";
        } else {
            return "csökken ";
        }
    }

    static int folyotnossagEVB(ArrayList<CSVfile> lista) {
        double Balap = lista.get(lista.size() - 1).B;

        if (lista.get(lista.size() - 1).B > lista.get(lista.size() - 2).B) {
            for (int i = lista.size() - 2; i >= 0; i--) {
                if (lista.get(i).B > Balap) {
                    return lista.get(i + 1).Ev;
                }
                Balap = lista.get(i).B;
            }
        } else {
            for (int i = lista.size() - 2; i >= 0; i--) {
                if (lista.get(i).B < Balap) {
                    return lista.get(i + 1).Ev;
                }
                Balap = lista.get(i).B;
            }
        }
        return -1;
    }

    static String folytonossagK(ArrayList<CSVfile> lista) {
        //Ha bármilyen fájl vizsgálnánk akkor kéne egyenlőséget is nézni
        if (lista.get(lista.size() - 1).K > lista.get(lista.size() - 2).K) {
            return "növekszik ";
        } else {
            return "csökken ";
        }
    }

    static int folyotnossagEVK(ArrayList<CSVfile> lista) {
        double Balap = lista.get(lista.size() - 1).K;

        if (lista.get(lista.size() - 1).K > lista.get(lista.size() - 2).K) {
            for (int i = lista.size() - 2; i >= 0; i--) {
                if (lista.get(i).K > Balap) {
                    return lista.get(i + 1).Ev;
                }
                Balap = lista.get(i).K;
            }
        } else {
            for (int i = lista.size() - 2; i >= 0; i--) {
                if (lista.get(i).K < Balap) {
                    return lista.get(i + 1).Ev;
                }
                Balap = lista.get(i).K;
            }
        }
        return -1;
    }

    static String folytonossagKperB(ArrayList<CSVfile> lista) {
        //Ha bármilyen fájl vizsgálnánk akkor kéne egyenlőséget is nézni
        if (lista.get(lista.size() - 1).KperB > lista.get(lista.size() - 2).KperB) {
            return "növekszik ";
        } else {
            return "csökken ";
        }
    }

    static int folyotnossagEVKperB(ArrayList<CSVfile> lista) {
        double Balap = lista.get(lista.size() - 1).KperB;

        if (lista.get(lista.size() - 1).KperB > lista.get(lista.size() - 2).KperB) {
            for (int i = lista.size() - 2; i >= 0; i--) {
                if (lista.get(i).KperB > Balap) {
                    return lista.get(i + 1).Ev;
                }
                Balap = lista.get(i).KperB;
            }
        } else {
            for (int i = lista.size() - 2; i >= 0; i--) {
                if (lista.get(i).KperB < Balap) {
                    return lista.get(i + 1).Ev;
                }
                Balap = lista.get(i).KperB;
            }
        }
        return -1;
    }
    
}
