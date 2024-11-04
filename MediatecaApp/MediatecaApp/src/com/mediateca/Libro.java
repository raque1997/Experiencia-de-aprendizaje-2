package com.mediateca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class Libro extends Material {
    private static final Logger logger = Logger.getLogger(Libro.class);
    private String autor;
    private int paginas;
    private String editorial;
    private String isbn;
    private int anio;

    public Libro(String codigo, String titulo, String autor, int paginas, String editorial, String isbn, int anio, int unidadesDisponibles) {
        super(codigo, titulo, unidadesDisponibles);
        this.autor = autor;
        this.paginas = paginas;
        this.editorial = editorial;
        this.isbn = isbn;
        this.anio = anio;
    }

    @Override
    public String getAutor() {
        return autor; // Retorna el autor del libro
    }

    @Override
    public void guardarEnDB() {
        String sql = "INSERT INTO libros (codigo, titulo, autor, paginas, editorial, isbn, anio, unidades) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, getCodigo());
            pstmt.setString(2, getTitulo());
            pstmt.setString(3, autor);
            pstmt.setInt(4, paginas);
            pstmt.setString(5, editorial);
            pstmt.setString(6, isbn);
            pstmt.setInt(7, anio);
            pstmt.setInt(8, getUnidadesDisponibles());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Libro guardado en la base de datos: " + getTitulo());
            } else {
                logger.warn("No se guardó ningún libro en la base de datos.");
            }
        } catch (SQLException e) {
            logger.error("Error al guardar el libro en la base de datos", e);
        }
    }
}