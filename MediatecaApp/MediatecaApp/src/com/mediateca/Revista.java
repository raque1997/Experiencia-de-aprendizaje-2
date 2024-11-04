package com.mediateca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Revista extends Material {
    private static final Logger logger = Logger.getLogger(Revista.class);
    private String autor;
    private String periodicidad;
    private String fechaPublicacion;

    public Revista(String codigo, String titulo, String autor, String periodicidad, String fechaPublicacion, int unidadesDisponibles) {
        super(codigo, titulo, unidadesDisponibles);
        this.autor = autor;
        this.periodicidad = periodicidad;
        this.fechaPublicacion = fechaPublicacion;
    }

    @Override
    public String getAutor() {
        return autor; // Retorna el autor de la revista
    }

    @Override
    public void guardarEnDB() {
        String sql = "INSERT INTO revistas (codigo, titulo, autor, periodicidad, fechaPublicacion, unidades) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, getCodigo());
            pstmt.setString(2, getTitulo());
            pstmt.setString(3, autor);
            pstmt.setString(4, periodicidad);
            pstmt.setString(5, fechaPublicacion);
            pstmt.setInt(6, getUnidadesDisponibles());
            pstmt.executeUpdate();
            logger.info("Revista guardada en la base de datos: " + getTitulo());
        } catch (SQLException e) {
            logger.error("Error al guardar la revista en la base de datos", e);
        }
    }
}