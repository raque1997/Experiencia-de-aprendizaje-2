package com.mediateca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import java.awt.*;
import java.util.ArrayList;

public class MediatecaApp extends JFrame {
    private static final Logger logger = Logger.getLogger(MediatecaApp.class);
    private final ArrayList<Material> materiales = new ArrayList<>();
    private final JTable table;
    private final DefaultTableModel tableModel;

    public MediatecaApp() {
        setTitle("Mediateca");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el modelo de la tabla
        String[] columnNames = {"Código", "Título", "Tipo", "Autor/Director/Artista", "Unidades Disponibles"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Crear botones
        JButton addButton = new JButton("Agregar Material");
        JButton modifyButton = new JButton("Modificar Material");
        JButton deleteButton = new JButton("Borrar Material");
        JButton searchButton = new JButton("Buscar Material");
        JButton exitButton = new JButton("Salir");

        // Añadir componentes al JPanel
        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(exitButton);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners para los botones
        addButton.addActionListener(e -> agregarMaterial());
        modifyButton.addActionListener(e -> modificarMaterial());
        deleteButton.addActionListener(e -> borrarMaterial());
        searchButton.addActionListener(e -> buscarMaterial());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void agregarMaterial() {
        String[] categorias = {"Material Escrito", "Material Audiovisual"};
        String categoria = (String) JOptionPane.showInputDialog(this, "Seleccione la categoría de material:",
                "Agregar Material", JOptionPane.QUESTION_MESSAGE, null, categorias, categorias[0]);

        if (categoria == null) return; // Si se cancela, salir

        String tipo = null;
        if (categoria.equals("Material Escrito")) {
            String[] tiposEscritos = {"Libro", "Revista"};
            tipo = (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de material escrito:",
                    "Agregar Material", JOptionPane.QUESTION_MESSAGE, null, tiposEscritos, tiposEscritos[0]);
        } else if (categoria.equals("Material Audiovisual")) {
            String[] tiposAudiovisuales = {"CD", "DVD"};
            tipo = (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de material audiovisual:",
                    "Agregar Material", JOptionPane.QUESTION_MESSAGE, null, tiposAudiovisuales, tiposAudiovisuales[0]);
        }

        if (tipo == null) return;

        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código de identificación del material:");
        if (codigo == null || codigo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código no puede estar vacío.");
            return;
        }

        Material material = null;

        switch (tipo.toLowerCase()) {
            case "libro":
                material = agregarLibro(codigo);
                break;
            case "revista":
                material = agregarRevista(codigo);
                break;
            case "cd":
                material = agregarCD(codigo);
                break;
            case "dvd":
                material = agregarDVD(codigo);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Tipo de material no válido.");
                return;
        }

        if (material != null) {
            materiales.add(material);
            tableModel.addRow(new Object[]{codigo, material.getTitulo(), tipo, material.getAutor(), material.getUnidadesDisponibles()});
            logger.info("Material agregado: " + tipo + " con código: " + codigo);

            // Almacenar en la base de datos
            material.guardarEnDB();
        }
    }

    private CD agregarCD(String codigo) {
        JTextField tituloField = new JTextField();
        JTextField artistaField = new JTextField();
        JTextField generoField = new JTextField();
        JTextField duracionField = new JTextField();
        JTextField numCancionesField = new JTextField();
        JTextField unidadesField = new JTextField();

        Object[] message = {
                "Código de Identificación:", codigo,
                "Título:", tituloField,
                "Artista:", artistaField,
                "Género:", generoField,
                "Duración (en minutos):", duracionField,
                "Número de Canciones:", numCancionesField,
                "Unidades Disponibles:", unidadesField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agregar CD", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String titulo = tituloField.getText();
                String artista = artistaField.getText();
                String genero = generoField.getText();
                int duracion = Integer.parseInt(duracionField.getText());
                int numCanciones = Integer.parseInt(numCancionesField.getText());
                int unidades = Integer.parseInt(unidadesField.getText());

                return new CD(codigo, titulo, artista, genero, duracion, numCanciones, unidades);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Se requiere un número válido para duración, número de canciones y unidades.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
        return null;
    }


    private DVD agregarDVD(String codigo) {
        JTextField tituloField = new JTextField();
        JTextField directorField = new JTextField();
        JTextField duracionField = new JTextField();
        JTextField generoField = new JTextField();
        JTextField unidadesField = new JTextField();

        Object[] message = {
                "Código de Identificación:", codigo,
                "Título:", tituloField,
                "Director:", directorField,
                "Duración (en minutos):", duracionField,
                "Género:", generoField,
                "Unidades Disponibles:", unidadesField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agregar DVD", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String titulo = tituloField.getText();
                String director = directorField.getText();
                int duracion = Integer.parseInt(duracionField.getText());
                String genero = generoField.getText();
                int unidades = Integer.parseInt(unidadesField.getText());

                return new DVD(codigo, titulo, director, duracion, genero, unidades);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Se requiere un número válido para duración y unidades.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
        return null;
    }


    private Revista agregarRevista(String codigo) {
        JTextField tituloField = new JTextField();
        JTextField editorialField = new JTextField();
        JTextField periodicidadField = new JTextField();
        JTextField fechaPublicacionField = new JTextField();
        JTextField unidadesField = new JTextField();

        Object[] message = {
                "Código de Identificación:", codigo,
                "Título:", tituloField,
                "Editorial:", editorialField,
                "Periodicidad:", periodicidadField,
                "Fecha de Publicación:", fechaPublicacionField,
                "Unidades Disponibles:", unidadesField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agregar Revista", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String titulo = tituloField.getText();
                String editorial = editorialField.getText();
                String periodicidad = periodicidadField.getText();
                String fechaPublicacion = fechaPublicacionField.getText(); // Asegúrate de que sea un formato válido
                int unidades = Integer.parseInt(unidadesField.getText());

                return new Revista(codigo, titulo, editorial, periodicidad, fechaPublicacion, unidades);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Se requiere un número válido para unidades.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
        return null;
    }


    private Libro agregarLibro(String codigo) {
        JTextField tituloField = new JTextField();
        JTextField autorField = new JTextField();
        JTextField paginasField = new JTextField();
        JTextField editorialField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField anioField = new JTextField();
        JTextField unidadesField = new JTextField();

        Object[] message = {
                "Código de Identificación:", codigo,
                "Título:", tituloField,
                "Autor:", autorField,
                "Número de Páginas:", paginasField,
                "Editorial:", editorialField,
                "ISBN:", isbnField,
                "Año de Publicación:", anioField,
                "Unidades Disponibles:", unidadesField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agregar Libro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String titulo = tituloField.getText();
                String autor = autorField.getText();
                int paginas = Integer.parseInt(paginasField.getText());
                String editorial = editorialField.getText();
                String isbn = isbnField.getText();
                int anio = Integer.parseInt(anioField.getText());
                int unidades = Integer.parseInt(unidadesField.getText());

                // Crear el objeto Libro con todos los campos adecuados
                return new Libro(codigo, titulo, autor, paginas, editorial, isbn, anio, unidades);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error: Se requiere un número válido.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
        return null;
    }


    private void modificarMaterial() {
        logger.info("Funcionalidad de modificar no implementada.");
    }

    private void borrarMaterial() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Material material = materiales.get(selectedRow);
            material.borrarEnDB();
            materiales.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            logger.info("Material borrado de la lista.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un material para borrar.");
        }
    }

    private void buscarMaterial() {
        String codigoBuscado = JOptionPane.showInputDialog(this, "Ingrese el código de identificación del material a buscar:");
        if (codigoBuscado == null || codigoBuscado.trim().isEmpty()) {
            return;
        }

        boolean encontrado = false;
        for (Material material : materiales) {
            if (material.getCodigo().equalsIgnoreCase(codigoBuscado)) {
                JOptionPane.showMessageDialog(this, "Material encontrado: " + material.getTitulo() +
                        "\nTipo: " + material.getClass().getSimpleName() +
                        "\nAutor/Director/Artista: " + material.getAutor() +
                        "\nUnidades Disponibles: " + material.getUnidadesDisponibles());
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "Material no encontrado.");
        }

        logger.info("Búsqueda realizada para el código: " + codigoBuscado);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MediatecaApp app = new MediatecaApp();
            app.setVisible(true);
            logger.info("MediatecaApp iniciada");
        });
    }
}
