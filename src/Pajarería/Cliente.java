package Pajarería;
import java.util.ArrayList;
        import java.util.Scanner;

public class Cliente {
    private String nombre;
    private String DNI;
    private String teléfono;
    private static String mail;

    public static ArrayList<Cliente> clientes = new ArrayList<>();
    //Constructor completo
    public Cliente(String nombre, String DNI, String teléfono, String mail){
        this.nombre = nombre;
        this.DNI = DNI;
        this.teléfono = teléfono;
        this.mail = mail;


    }
//Buscar cliente por DNI
    public static Cliente buscarPorDni(String dni) {
        for (Cliente c : clientes){
            if (c.getDNI().equalsIgnoreCase(dni)){
                return c;
            }
        }
        return null;
    }

//Dar de alta al cliente
    public static void altaCliente(Scanner sc) {
        System.out.println("DNI: ");
        String dni = sc.nextLine();
        if (buscarPorDni(dni) != null) {
            System.out.println("Ya existe un cliente con ese DNI");
            return;
        }
        System.out.println("Nombre: ");
        String nombre = sc.nextLine();
        System.out.println("Teléfono: ");
        String teléfono = sc.nextLine();
        System.out.println("Mail: ");
        String mail = sc.nextLine();
        Cliente nuevo = new Cliente(nombre, dni, teléfono, mail);
        clientes.add(nuevo);
        System.out.println("Cliente añadido correctamente");
    }
// Dar de baja al cliente
        public static void bajaCliente(Scanner sc) {
            System.out.println("DNI del cliente a eliminar: ");
            String dni = sc.nextLine();
            Cliente c = buscarPorDni(dni);
            if (c == null){
                System.out.println("Cliente no encontrado");
                return;
            }
            clientes.remove(c);
            System.out.println("Cliente eliminado");


    }
//  Modificar cliente
    public static void modificarCliente(Scanner sc) {
        System.out.println("DNI del cliente a modificar: ");
        String dni =sc.nextLine();
        Cliente c = buscarPorDni(dni);
        if (c == null){
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("Nuevo nombre (dejar vacío para no cambiar): ");
        String nombre = sc.nextLine();
        if (!nombre.isBlank()){
            c.setNombre(nombre);
        }
        System.out.println("Nuevo teléfono (dejar en blanco para no cambiar): ");
        String teléfono = sc.nextLine();
        if (!teléfono.isBlank()) {
            c.setTelefono(teléfono);
        }

        System.out.println("Cliente modificado correctamente");

    }
// Listar cliente
    public static void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
        }else{
            for (Cliente c : clientes){
                System.out.println(c);
            }
        }
    }
// Getters y Setters
    public String getDNI() {
        return DNI;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.teléfono = telefono;
    }

    public void setEmail(String mail) {
        this.mail = mail;
    }
// para mostrar clientes correctamente

    public String toString(){
        return "Cliente{" +
                "Nombre='" + nombre + '\'' +
                ", DNI='" + DNI + '\'' +
                ", Teléfono='" + teléfono + '\'' +
                ", Correo='" + mail+ '\'' +
                '}';
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner (System.in); // declaro el escaner para poder introducir el sc.nextLine

        System.out.println("Nombre: ");
        String nombre = sc.nextLine();

        System.out.println("DNI: ");
        String DNI = sc.nextLine();

        System.out.println("Teléfono: ");
        String teléfono = sc.nextLine();

        System.out.println("Correo electrónico: ");
        String mail = sc.nextLine();

        Cliente nuevoCliente = new Cliente(nombre, DNI, teléfono, mail); // lo que queremos que nos devuelva al registrarlo
        clientes.add(nuevoCliente);
        System.out.println("Cliente registrado correctamente:");
        System.out.println("Nombre: " + nombre);
        System.out.println("DNI: " + DNI);
        System.out.println("Teléfono: " + teléfono);
        System.out.println("Correo: " + mail);

        sc.close(); // cerramos el scanner
           }
}
