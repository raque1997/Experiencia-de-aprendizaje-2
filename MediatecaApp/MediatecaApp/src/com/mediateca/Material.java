package com.mediateca;

public abstract class Material {
    protected String codigo;
    protected String titulo;
    protected int unidadesDisponibles;

    public Material(String codigo, String titulo, int unidadesDisponibles) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.unidadesDisponibles = unidadesDisponibles;
    }

    public Material() {
        // Constructor vacío para subclases
    }

    public String getCodigo() {
        return codigo; // Retorna el código del material
    }

    public abstract String getAutor(); // Método abstracto para obtener el autor

    public int getUnidadesDisponibles() {
        return unidadesDisponibles; // Retorna las unidades disponibles
    }

    public String getTitulo() {
        return titulo; // Retorna el título del material
    }

    public Object getAutorDirectorArtista() {
        return null; // Puede ser implementado en subclases si es necesario
    }

    public void guardarEnDB() {
        // Implementación para guardar en base de datos
    }

    public void borrarEnDB() {
        // Implementación para borrar en base de datos
    }
}