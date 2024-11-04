package com.mediateca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class CD extends Material {
    private static final Logger logger = Logger.getLogger(CD.class);
    private String artista;
    private String genero;
    private int duracion; // en minutos
    private int numCanciones;

    public CD(String codigo, String titulo, String artista, String genero, int duracion, int numCanciones, int unidadesDisponibles) {
        super(codigo, titulo, unidadesDisponibles);
        this.artista = artista;
        this.genero = genero;
        this.duracion = duracion;
        this.numCanciones = numCanciones;
    }

    @Override
    public String getAutor() {
        return artista; // Retorna el artista del CD
    }

    @Override
    public void guardarEnDB() {
        String sql = "INSERT INTO cds (codigo, titulo, artista, genero, duracion, numCanciones, unidades) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, getCodigo());
            pstmt.setString(2, getTitulo());
            pstmt.setString(3, artista);
            pstmt.setString(4, genero);
            pstmt.setInt(5, duracion);
            pstmt.setInt(6, numCanciones);
            pstmt.setInt(7, getUnidadesDisponibles());
            pstmt.executeUpdate();
            logger.info("CD guardado en la base de datos: " + getTitulo());
        } catch (SQLException e) {
            logger.error("Error al guardar el CD en la base de datos", e);
        }
    }
}