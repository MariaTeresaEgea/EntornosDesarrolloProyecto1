package Pajarería;
import java.util.ArrayList;
import java.util.Scanner;
public class Ventas {//atributos de la clase
    private Cliente cliente;
    private ArrayList<Pajaro> pajaros;
    private String fecha;
// Lista estátitca para guardar todas las ventas
    public static ArrayList<Ventas> ventas = new ArrayList<>();
    public Ventas(Cliente cliente, String fecha) { //Constructor
        this.cliente = cliente;
        this.fecha = fecha;
        this.pajaros = new ArrayList<>();
            }
//Añadir un pájaro a la venta
            public void añadirPajaro (Pajaro p) {
        pajaros.add(p);
                    } //calcular el total de la venta
                    public double calcularTotal (){
        double total = 0;
        for (Pajaro p : pajaros){
            total += p.getPrecio();
        }
        return total;
                    }
// Getter
    public Cliente getCliente() {
        return cliente;
    }
//Información sobre la venta
    public String toString (){
        return "Venta a " + cliente + " | Fecha: " + fecha + " | Total: " + calcularTotal() + "€" + " | Especie: " + pajaros;
    } //    Crear una nueva venta
    public static void nuevaVenta (Scanner sc){
        System.out.println("DNI del cliente: ");
        String dni = sc.nextLine();
        Cliente cliente = Cliente.buscarPorDni(dni);
        if (cliente == null){
            System.out.println("Cliente no encontrado");
            return;
        }
        System.out.println("Fecha: ");
        String fechas = sc.nextLine();
        Ventas v = new Ventas(cliente, fechas);

        String continuar;
        do{
            Pajaro.listarPajaros();
            System.out.println("Especie a añadir: ");
            String especie = sc.nextLine();

                Pajaro p = Pajaro.buscarPorEspecie(especie);
                if (p != null){
                    v.añadirPajaro (p);
                    System.out.println("Pájaro añadido");
                                    } else{
                    System.out.println("No encontrado");
                }

                System.out.println("¿Añadir otro pájaro?");
                continuar = sc.nextLine();
            } while (continuar.equalsIgnoreCase("s"));

            ventas.add(v);
            System.out.println("Venta registrada");
        }
        //Mostrar las ventas registradas
        public static void mostrarVentas(){
        if(ventas.isEmpty()){
            System.out.println("No hay ventas");
        } else {
            for (Ventas v : ventas) {
                System.out.println(v);
            }
        }
    }
    // Mostrar ventas filtradas por DNI de clientes

        public static void mostrarVentasPorCliente(Scanner sc) {
            System.out.print("DNI del cliente: ");
            String dni = sc.nextLine();

            boolean encontrado = false;
            for (Ventas v : ventas) {
                if (v.getCliente().getDNI().equalsIgnoreCase(dni)) {
                    System.out.println(v);
                    encontrado = true;
                }
            }

            if (!encontrado) {
                System.out.println("No hay ventas para ese cliente");
            }

    } // Metodo principal con menú para interactuar con el sistema
            public static void main(String[] args){
            Scanner sc = new Scanner(System.in);
            int opcion;
            do{
                System.out.println("\n=== MENÚ PRINCIPAL ===");
                System.out.println("1. Gestión de clientes");
                System.out.println("2. Gestión de pájaros");
                System.out.println("3. Realizar venta");
                System.out.println("4. Mostrar ventas");
                System.out.println("5. Salir");
                System.out.print("Opción: ");
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion){
                    case 1:
                        System.out.println("--- GESTIÓN CLIENTES ---");
                        System.out.println("1. Alta");
                        System.out.println("2. Baja");
                        System.out.println("3. Modificar");
                        System.out.println("4. Listar");
                        System.out.print("Opción: ");
                        int sub1 = Integer.parseInt(sc.nextLine());
                        switch (sub1) {
                            case 1 -> Cliente.altaCliente(sc);
                            case 2 -> Cliente.bajaCliente(sc);
                            case 3 -> Cliente.modificarCliente(sc);
                            case 4 -> Cliente.listarClientes();
                        }
                        break;
                    case 2:
                        System.out.println("--- GESTIÓN PÁJAROS ---");
                        System.out.println("1. Alta");
                        System.out.println("2. Listar");
                        System.out.print("Opción: ");
                        int sub2 = Integer.parseInt(sc.nextLine());
                        switch (sub2) {
                            case 1 -> Pajaro.altaPajaro(sc);
                            case 2 -> Pajaro.listarPajaros();
                        }
                        break;
                    case 3: Ventas.nuevaVenta(sc);
                    case 4:
                        System.out.println("1. Todas las ventas");
                        System.out.println("2. Ventas por cliente");
                        int sub4 = Integer.parseInt(sc.nextLine());
                        if (sub4 == 1) Ventas.mostrarVentas();
                        else Ventas.mostrarVentasPorCliente(sc);
                        break;
                    case 5: System.out.println("Saliendo del sistema...");
                    default: System.out.println("Opción no válida.");
                }
            } while (opcion != 5);
            }
}

