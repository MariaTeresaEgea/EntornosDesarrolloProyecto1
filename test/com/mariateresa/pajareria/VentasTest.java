package com.mariateresa.pajareria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Clase de pruebas unitarias para la clase Ventas.
 *
 * Para permitir el aislamiento de la clase Ventas durante las pruebas,
 * se utilizan clases dummy (Cliente y Pajaro) como clases estáticas anidadas.
 * La clase Ventas también se redefine como una clase estática anidada en este test
 * para que utilice estas clases dummy, permitiendo un entorno de prueba autocontenido.
 *
 * En un proyecto real, Cliente, Pajaro y Ventas estarían en sus propios archivos .java
 * y se importarían. Esta estructura es para facilitar la ejecución de la prueba
 * como un fragmento de código independiente.
 */
public class VentasTest {

    // Guarda las referencias originales de System.in y System.out para restaurarlas después
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor; // Captura la salida de System.out

    /*
     * Clase dummy para simular el comportamiento de Cliente en las pruebas.
     * Contiene solo los métodos necesarios para que Ventas funcione en el test.
     */
    static class Cliente {
        private String dni;
        private String nombre;

        public Cliente(String dni, String nombre) {
            this.dni = dni;
            this.nombre = nombre;
        }

        public String getDNI() { return dni; }
        public String getNombre() { return nombre; }

        @Override
        public String toString() { return nombre + " (" + dni + ")"; }

        // Método estático mockeado para la búsqueda de clientes por DNI.
        // Devuelve clientes dummy predefinidos para las pruebas.
        public static Cliente buscarPorDni(String dni) {
            if ("12345678A".equals(dni)) {
                return new Cliente("12345678A", "Juan Perez");
            }
            if ("87654321B".equals(dni)) {
                return new Cliente("87654321B", "Maria Lopez");
            }
            return null; // Cliente no encontrado
        }
    }

    /*
     * Clase dummy para simular el comportamiento de Pajaro en las pruebas.
     * Contiene solo los métodos necesarios para que Ventas funcione en el test.
     */
    static class Pajaro {
        private String especie;
        private double precio;

        public Pajaro(String especie, double precio) {
            this.especie = especie;
            this.precio = precio;
        }

        public String getEspecie() { return especie; }
        public double getPrecio() { return precio; }

        @Override
        public String toString() { return especie + " (" + precio + "€)"; }

        // Método estático mockeado para la búsqueda de pájaros por especie.
        // Devuelve pájaros dummy predefinidos para las pruebas.
        public static Pajaro buscarPorEspecie(String especie) {
            if ("Canario".equals(especie)) {
                return new Pajaro("Canario", 25.0);
            }
            if ("Periquito".equals(especie)) {
                return new Pajaro("Periquito", 15.0);
            }
            return null; // Pájaro no encontrado
        }

        // Método estático mockeado para listar pájaros.
        // Simula una salida simple para la prueba.
        public static void listarPajaros() {
            System.out.println("Pájaros disponibles (mock): Canario, Periquito");
        }
    }

    /*
     * Clase Ventas redefinida como estática anidada para el propósito de esta prueba.
     * Utiliza las clases dummy Cliente y Pajaro definidas arriba.
     * Se han eliminado los métodos no relevantes para las pruebas unitarias de Ventas
     * (como el método main y el menú principal).
     */
    public static class Ventas {
        private Cliente cliente;
        private ArrayList<Pajaro> pajaros;
        private String fecha;
        public static ArrayList<Ventas> ventas = new ArrayList<>(); // Lista estática para guardar todas las ventas

        // Constructor
        public Ventas(Cliente cliente, String fecha) {
            this.cliente = cliente;
            this.fecha = fecha;
            this.pajaros = new ArrayList<>();
        }

        // Añadir un pájaro a la venta
        public void añadirPajaro(Pajaro p) {
            pajaros.add(p);
        }

        // Calcular el total de la venta
        public double calcularTotal() {
            double total = 0;
            for (Pajaro p : pajaros) {
                total += p.getPrecio();
            }
            return total;
        }

        // Getter para el cliente
        public Cliente getCliente() {
            return cliente;
        }

        // Información sobre la venta
        @Override
        public String toString() {
            return "Venta a " + cliente + " | Fecha: " + fecha + " | Total: " + calcularTotal() + "€" + " | Especie: " + pajaros;
        }

        // Crear una nueva venta (interactúa con Scanner y clases dummy)
        public static void nuevaVenta(Scanner sc) {
            System.out.println("DNI del cliente: ");
            String dni = sc.nextLine();
            Cliente cliente = Cliente.buscarPorDni(dni); // Usa la clase dummy Cliente
            if (cliente == null) {
                System.out.println("Cliente no encontrado");
                return;
            }
            System.out.println("Fecha: ");
            String fechas = sc.nextLine();
            Ventas v = new Ventas(cliente, fechas);

            String continuar;
            do {
                Pajaro.listarPajaros(); // Usa la clase dummy Pajaro
                System.out.println("Especie a añadir: ");
                String especie = sc.nextLine();

                Pajaro p = Pajaro.buscarPorEspecie(especie); // Usa la clase dummy Pajaro
                if (p != null) {
                    v.añadirPajaro(p);
                    System.out.println("Pájaro añadido");
                } else {
                    System.out.println("No encontrado");
                }

                System.out.println("¿Añadir otro pájaro?");
                continuar = sc.nextLine();
            } while (continuar.equalsIgnoreCase("s"));

            ventas.add(v);
            System.out.println("Venta registrada");
        }

        // Mostrar las ventas registradas
        public static void mostrarVentas() {
            if (ventas.isEmpty()) {
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
        }
    }


    /**
     * Configuración inicial antes de cada método de prueba.
     * 1. Limpia la lista estática `Ventas.ventas` para asegurar la independencia de las pruebas.
     * 2. Redirige `System.out` para capturar la salida de la consola y poder verificarla.
     */
    @BeforeEach
    void setUp() {
        Ventas.ventas.clear(); // Limpiar la lista estática de ventas antes de cada prueba
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor)); // Redirigir System.out
    }

    /**
     * Restauración después de cada método de prueba.
     * 1. Restaura `System.in` a su valor original.
     * 2. Restaura `System.out` a su valor original.
     */
    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn); // Restaurar System.in
        System.setOut(originalSystemOut); // Restaurar System.out
    }

    /**
     * Prueba el constructor de Ventas y el método getCliente.
     * Verifica que la venta se inicializa correctamente con el cliente y la lista de pájaros vacía.
     */
    @Test
    void testConstructorAndGetCliente() {
        Cliente testCliente = new Cliente("11111111X", "Test Cliente");
        Ventas venta = new Ventas(testCliente, "2023-07-23");

        assertNotNull(venta, "La instancia de Venta no debería ser nula.");
        assertEquals(testCliente, venta.getCliente(), "El cliente de la venta debería ser el cliente proporcionado.");
        assertTrue(venta.pajaros.isEmpty(), "La lista de pájaros debería estar vacía al inicio.");
    }

    /**
     * Prueba el método añadirPajaro.
     * Verifica que los pájaros se añaden correctamente a la lista de la venta.
     */
    @Test
    void testAnadirPajaro() {
        Cliente testCliente = new Cliente("11111111X", "Test Cliente");
        Ventas venta = new Ventas(testCliente, "2023-07-23");
        Pajaro pajaro1 = new Pajaro("Loro", 100.0);
        Pajaro pajaro2 = new Pajaro("Cacatua", 200.0);

        venta.añadirPajaro(pajaro1);
        assertEquals(1, venta.pajaros.size(), "Debería haber 1 pájaro en la venta después de añadir el primero.");
        assertTrue(venta.pajaros.contains(pajaro1), "La venta debería contener el primer pájaro añadido.");

        venta.añadirPajaro(pajaro2);
        assertEquals(2, venta.pajaros.size(), "Debería haber 2 pájaros en la venta después de añadir el segundo.");
        assertTrue(venta.pajaros.contains(pajaro2), "La venta debería contener el segundo pájaro añadido.");
    }

    /**
     * Prueba el método calcularTotal con varios pájaros.
     * Verifica que el total de la venta se calcula correctamente sumando los precios de los pájaros.
     */
    @Test
    void testCalcularTotalConVariosPajaros() {
        Cliente testCliente = new Cliente("11111111X", "Test Cliente");
        Ventas venta = new Ventas(testCliente, "2023-07-23");
        venta.añadirPajaro(new Pajaro("Canario", 25.0));
        venta.añadirPajaro(new Pajaro("Periquito", 15.0));
        venta.añadirPajaro(new Pajaro("Loro", 100.0));

        assertEquals(140.0, venta.calcularTotal(), 0.001, "El total debería ser la suma de los precios de los pájaros (25+15+100=140).");
    }

    /**
     * Prueba el método calcularTotal con una venta vacía.
     * Verifica que el total es 0 cuando no hay pájaros en la venta.
     */
    @Test
    void testCalcularTotalVentaVacia() {
        Cliente testCliente = new Cliente("11111111X", "Test Cliente");
        Ventas venta = new Ventas(testCliente, "2023-07-23");
        assertEquals(0.0, venta.calcularTotal(), 0.001, "El total de una venta vacía debería ser 0.");
    }

    /**
     * Prueba el método toString.
     * Verifica que la representación en cadena de la venta es correcta.
     */
    @Test
    void testToString() {
        Cliente testCliente = new Cliente("12345678A", "Juan Perez");
        Ventas venta = new Ventas(testCliente, "2023-07-23");
        venta.añadirPajaro(new Pajaro("Canario", 25.0));
        venta.añadirPajaro(new Pajaro("Periquito", 15.0));

        String expected = "Venta a Juan Perez (12345678A) | Fecha: 2023-07-23 | Total: 40.0€ | Especie: [Canario (25.0€), Periquito (15.0€)]";
        assertEquals(expected, venta.toString(), "El método toString debería formatear la información de la venta correctamente.");
    }

    /**
     * Prueba el método estático nuevaVenta con un cliente y un pájaro válidos.
     * Simula la entrada del usuario y verifica que la venta se registra correctamente
     * y que la salida de la consola es la esperada.
     */
    @Test
    void testNuevaVentaExito() {
        // Simula la entrada del usuario: DNI, Fecha, Especie del pájaro, 'no' para no añadir más
        String input = "12345678A\n2023-07-24\nCanario\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner sc = new Scanner(System.in);

        Ventas.nuevaVenta(sc); // Llama al método estático a probar

        assertEquals(1, Ventas.ventas.size(), "Debería haberse registrado 1 venta.");
        Ventas ultimaVenta = Ventas.ventas.get(0);
        assertEquals("Juan Perez", ultimaVenta.getCliente().getNombre(), "El nombre del cliente en la venta debe ser 'Juan Perez'.");
        assertEquals("2023-07-24", ultimaVenta.fecha, "La fecha de la venta debe ser '2023-07-24'.");
        assertEquals(1, ultimaVenta.pajaros.size(), "La venta debería contener 1 pájaro.");
        assertEquals("Canario", ultimaVenta.pajaros.get(0).getEspecie(), "La especie del pájaro debe ser 'Canario'.");
        assertEquals(25.0, ultimaVenta.calcularTotal(), 0.001, "El total de la venta debe ser 25.0.");

        // Verifica la salida de la consola
        String expectedOutput = "DNI del cliente: \n" +
                "Fecha: \n" +
                "Pájaros disponibles (mock): Canario, Periquito\n" +
                "Especie a añadir: \n" +
                "Pájaro añadido\n" +
                "¿Añadir otro pájaro?\n" +
                "Venta registrada\n";
        // Se eliminan los espacios en blanco para una comparación más robusta
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida de la consola no coincide con la esperada.");
    }

    /**
     * Prueba el método estático nuevaVenta cuando el cliente no es encontrado.
     * Verifica que no se registra ninguna venta y se muestra el mensaje adecuado.
     */
    @Test
    void testNuevaVentaClienteNoEncontrado() {
        String input = "00000000Z\n"; // DNI no existente
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner sc = new Scanner(System.in);

        Ventas.nuevaVenta(sc);

        assertTrue(Ventas.ventas.isEmpty(), "No debería haberse registrado ninguna venta si el cliente no se encuentra.");
        String expectedOutput = "DNI del cliente: \nCliente no encontrado\n";
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida de la consola debe indicar que el cliente no fue encontrado.");
    }

    /**
     * Prueba el método estático nuevaVenta cuando se intenta añadir un pájaro no encontrado.
     * Verifica que la venta se registra pero sin el pájaro no encontrado.
     */
    @Test
    void testNuevaVentaPajaroNoEncontrado() {
        String input = "12345678A\n2023-07-25\nCuervo\nno\n"; // DNI válido, pero especie de pájaro no existente
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner sc = new Scanner(System.in);

        Ventas.nuevaVenta(sc);

        assertEquals(1, Ventas.ventas.size(), "Debería haberse registrado 1 venta (aunque sin pájaros si no se encontraron).");
        Ventas ultimaVenta = Ventas.ventas.get(0);
        assertTrue(ultimaVenta.pajaros.isEmpty(), "La venta no debería contener pájaros si no se encontraron especies válidas.");
        assertEquals(0.0, ultimaVenta.calcularTotal(), 0.001, "El total de la venta debe ser 0 si no se añadieron pájaros.");

        String expectedOutput = "DNI del cliente: \n" +
                "Fecha: \n" +
                "Pájaros disponibles (mock): Canario, Periquito\n" +
                "Especie a añadir: \n" +
                "No encontrado\n" +
                "¿Añadir otro pájaro?\n" +
                "Venta registrada\n";
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida de la consola no coincide con la esperada (pájaros no encontrados).");
    }

    /**
     * Prueba el método estático mostrarVentas cuando no hay ventas registradas.
     * Verifica que se muestra el mensaje adecuado.
     */
    @Test
    void testMostrarVentasVacio() {
        Ventas.mostrarVentas();
        String expectedOutput = "No hay ventas\n";
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida debería indicar 'No hay ventas'.");
    }

    /**
     * Prueba el método estático mostrarVentas con ventas existentes.
     * Verifica que todas las ventas registradas se muestran correctamente en la consola.
     */
    @Test
    void testMostrarVentasConDatos() {
        // Añadir ventas de prueba
        Cliente cliente1 = new Cliente("12345678A", "Juan Perez");
        Ventas venta1 = new Ventas(cliente1, "2023-07-26");
        venta1.añadirPajaro(new Pajaro("Canario", 25.0));
        Ventas.ventas.add(venta1);

        Cliente cliente2 = new Cliente("87654321B", "Maria Lopez");
        Ventas venta2 = new Ventas(cliente2, "2023-07-27");
        venta2.añadirPajaro(new Pajaro("Periquito", 15.0));
        venta2.añadirPajaro(new Pajaro("Canario", 25.0));
        Ventas.ventas.add(venta2);

        Ventas.mostrarVentas(); // Llama al método estático a probar

        String expectedOutput = "Venta a Juan Perez (12345678A) | Fecha: 2023-07-26 | Total: 25.0€ | Especie: [Canario (25.0€)]\n" +
                "Venta a Maria Lopez (87654321B) | Fecha: 2023-07-27 | Total: 40.0€ | Especie: [Periquito (15.0€), Canario (25.0€)]\n";
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida de las ventas no coincide con la esperada.");
    }

    /**
     * Prueba el método estático mostrarVentasPorCliente para un cliente con ventas.
     * Simula la entrada del DNI del cliente y verifica que solo se muestran sus ventas.
     */
    @Test
    void testMostrarVentasPorClienteEncontrado() {
        // Añadir ventas de prueba
        Cliente cliente1 = new Cliente("12345678A", "Juan Perez");
        Ventas venta1 = new Ventas(cliente1, "2023-07-26");
        venta1.añadirPajaro(new Pajaro("Canario", 25.0));
        Ventas.ventas.add(venta1);

        Cliente cliente2 = new Cliente("87654321B", "Maria Lopez");
        Ventas venta2 = new Ventas(cliente2, "2023-07-27");
        venta2.añadirPajaro(new Pajaro("Periquito", 15.0));
        Ventas.ventas.add(venta2);

        String input = "12345678A\n"; // DNI del cliente a buscar
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner sc = new Scanner(System.in);

        Ventas.mostrarVentasPorCliente(sc); // Llama al método estático a probar

        String expectedOutput = "DNI del cliente: \nVenta a Juan Perez (12345678A) | Fecha: 2023-07-26 | Total: 25.0€ | Especie: [Canario (25.0€)]\n";
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida de ventas por cliente no coincide con la esperada.");
    }

    /**
     * Prueba el método estático mostrarVentasPorCliente para un cliente sin ventas.
     * Simula la entrada del DNI de un cliente sin ventas y verifica el mensaje de salida.
     */
    @Test
    void testMostrarVentasPorClienteNoEncontrado() {
        // Añadir una venta para que la lista no esté vacía, pero el DNI buscado no tenga ventas
        Cliente cliente1 = new Cliente("12345678A", "Juan Perez");
        Ventas venta1 = new Ventas(cliente1, "2023-07-26");
        venta1.añadirPajaro(new Pajaro("Canario", 25.0));
        Ventas.ventas.add(venta1);

        String input = "00000000Z\n"; // DNI no existente en las ventas
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner sc = new Scanner(System.in);

        Ventas.mostrarVentasPorCliente(sc); // Llama al método estático a probar

        String expectedOutput = "DNI del cliente: \nNo hay ventas para ese cliente\n";
        assertEquals(expectedOutput.replaceAll("\\s+", ""), outputStreamCaptor.toString().replaceAll("\\s+", ""), "La salida debería indicar que no hay ventas para el cliente buscado.");
    }
}

