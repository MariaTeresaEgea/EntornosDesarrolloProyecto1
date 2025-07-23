package com.mariateresa.pajareria;

import java.util.ArrayList;
import java.util.Scanner;

public class Pajaro {
    private String especie;
    private String color;
    private double precio;

    public static ArrayList<Pajaro> catalogo = new ArrayList<>();

    public Pajaro(String especie, String color, double precio) {
        this.especie = especie;
        this.color = color;
        this.precio = precio;
    }

    // Dar de alta a un pájaro:
    public static void altaPajaro(Scanner sc) {
        System.out.print("Especie: ");
        String especie = sc.nextLine();
        if (buscarPorEspecie(especie) != null) {
            System.out.println("Ya existe un pájaro con esa especie.");
            return;
        }

        System.out.print("Color: ");
        String color = sc.nextLine();

        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());

        catalogo.add(new Pajaro(especie, color, precio));
        System.out.println("Pájaro añadido correctamente.");
    }

    // Dar de baja a un pájaro

    public static void bajaPajaro(Scanner sc) {
        System.out.print("Especie del pájaro a eliminar: ");
        String especie = sc.nextLine();

        Pajaro p = buscarPorEspecie(especie);
        if (p == null) {
            System.out.println("Pájaro no encontrado.");
            return;
        }

        catalogo.remove(p);
        System.out.println("Pájaro eliminado.");
    }

    public static void listarPajaros() {
        if (catalogo.isEmpty()) {
            System.out.println("No hay pájaros en el catálogo.");
        } else {
            for (Pajaro p : catalogo) {
                System.out.println(p);
            }
        }
    }

    public static Pajaro buscarPorEspecie(String especie) {
        for (Pajaro p : catalogo) {
            if (p.getEspecie().equalsIgnoreCase(especie)) {
                return p;
            }
        }
        return null;
    }

    // Getters
    public String getEspecie() {
        return especie;
    }

    public String getColor() {
        return color;
    }

    public double getPrecio() {
        return precio;
    }

    // Setters
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // toString
    public String toString() {
        return especie + " - " + color + " - " + precio + "€";
    }
}
