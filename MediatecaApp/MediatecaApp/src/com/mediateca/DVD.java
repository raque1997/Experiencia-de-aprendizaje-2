package com.mediateca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class DVD extends Material {
    private static final Logger logger = Logger.getLogger(DVD.class);
    private String director;
    private int duracion; // en minutos
    private String genero;

    public DVD(String codigo, String titulo, String director, int duracion, String genero, int unidadesDisponibles) {
        super(codigo, titulo, unidadesDisponibles);
        this.director = director;
        this.duracion = duracion;
        this.genero = genero;
    }

    @Override
    public String getAutor() {
        return director; // Retorna el director del DVD
    }

    @Override
    public void guardarEnDB() {
        String sql = "INSERT INTO dvds (codigo, titulo, director, duracion, genero, unidades) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, getCodigo());
            pstmt.setString(2, getTitulo());
            pstmt.setString(3, director);
            pstmt.setInt(4, duracion);
            pstmt.setString(5, genero);
            pstmt.setInt(6, getUnidadesDisponibles());
            pstmt.executeUpdate();
            logger.info("DVD guardado en la base de datos: " + getTitulo());
        } catch (SQLException e) {
            logger.error("Error al guardar el DVD en la base de datos", e);
        }
    }
}