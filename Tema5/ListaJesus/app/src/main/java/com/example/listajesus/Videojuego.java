package com.example.listajesus;

public class Videojuego {

    private String titulo;
    private String plataforma;
    private int imagen;

    public Videojuego(String titulo, String plataforma, int imagen) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public int getImagen() {
        return imagen;
    }
}