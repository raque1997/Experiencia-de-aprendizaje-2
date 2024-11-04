package com.mediateca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class);
    private static final String DB_URL = "jdbc:sqlite:mediateca.db";
    private static Connection connection = null;

    // Método para inicializar la conexión a la base de datos
    public static void initializeConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                logger.info("Conexión a la base de datos establecida correctamente.");
            }
        } catch (SQLException e) {
            logger.error("Error al conectar a la base de datos", e);
        }
    }

    // Método para obtener la conexión activa
    public static Connection getConnection() {
        if (connection == null || !isValid(connection)) {
            initializeConnection();
        }
        return connection;
    }

    // Método para verificar si la conexión es válida
    private static boolean isValid(Connection connection) {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);
        } catch (SQLException e) {
            logger.error("Error al verificar la validez de la conexión", e);
            return false;
        }
    }

    // Método para cerrar la conexión a la base de datos
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logger.info("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión a la base de datos", e);
            }
        }
    }
}
