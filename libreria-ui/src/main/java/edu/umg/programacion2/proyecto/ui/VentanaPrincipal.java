package edu.umg.programacion2.proyecto.ui;

import edu.umg.programacion2.proyecto.dao.LibroDAO;

import edu.umg.programacion2.proyecto.modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class VentanaPrincipal extends JFrame {

    private final LibroDAO libroDAO = new LibroDAO();

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtCategoria;
    private JTextField txtPrecio;
    private JTextField txtExistencias;
    private JTextField txtAnioPublicacion;
    private JTextField txtFechaIngreso;
    private JTable tablaLibros;
    private DefaultTableModel tableModel;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public VentanaPrincipal() {
        setTitle("Catálogo de Librería - Gestión de Libros");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        cargarDatosTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // 1. Inicializar Campos del Formulario
        txtId = new JTextField();
        txtId.setEditable(false);
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtCategoria = new JTextField();
        txtPrecio = new JTextField();
        txtExistencias = new JTextField();
        txtAnioPublicacion = new JTextField();
        txtFechaIngreso= new JTextField(); 
        txtFechaIngreso.setText(LocalDate.now().toString()); 
        
        // 2. Panel de Formulario (4 filas x 4 columnas)
        JPanel pnlFormulario = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Libro"));

        // Fila 1: ID y Título
        pnlFormulario.add(new JLabel("ID (Auto):"));
        pnlFormulario.add(txtId);
        pnlFormulario.add(new JLabel("Título (*):"));
        pnlFormulario.add(txtTitulo);

        // Fila 2: Autor y Categoría
        pnlFormulario.add(new JLabel("Autor (*):"));
        pnlFormulario.add(txtAutor);
        pnlFormulario.add(new JLabel("Categoría:"));
        pnlFormulario.add(txtCategoria);

        // Fila 3: Precio y Existencias
        pnlFormulario.add(new JLabel("Precio (Q) (*):"));
        pnlFormulario.add(txtPrecio);
        pnlFormulario.add(new JLabel("Existencias (*):"));
        pnlFormulario.add(txtExistencias);

        // Fila 4: Año Publicación y Relleno
        pnlFormulario.add(new JLabel("Año Publicación (*):"));
        pnlFormulario.add(txtAnioPublicacion);
        pnlFormulario.add(new JLabel("")); 
        pnlFormulario.add(new JLabel("")); 
        pnlFormulario.add(new JLabel("Fecha Ingreso (AAAA-MM-DD):")); 
        pnlFormulario.add(txtFechaIngreso); 
        
        // 3. Tabla de Libros y Estilos Visuales
        String[] columnas = {"ID", "Título", "Autor", "Categoría", "Precio (Q)", "Existencias", "Año", "Fecha Ingreso"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(tableModel);
        tablaLibros.setRowHeight(28);
        tablaLibros.setShowGrid(true);
        tablaLibros.setGridColor(new Color(224, 224, 224));

        tablaLibros.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaLibros.getTableHeader().setBackground(new Color(236, 239, 241));
        tablaLibros.getTableHeader().setForeground(new Color(38, 50, 56));
        tablaLibros.getTableHeader().setReorderingAllowed(false);

        tablaLibros.setSelectionBackground(new Color(187, 222, 251));
        tablaLibros.setSelectionForeground(Color.BLACK);
        tablaLibros.getSelectionModel().addListSelectionListener(e -> seleccionarFila());

        JScrollPane scrollTabla = new JScrollPane(tablaLibros);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Catálogo de Libros Registrados"));

        // 4. Panel de Botones
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnGuardar = new JButton("Guardar Nuevo");
        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(25, 118, 210));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setOpaque(true);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(211, 47, 47));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setBackground(new Color(117, 117, 117));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setOpaque(true);
        btnLimpiar.setBorderPainted(false);
        btnLimpiar.setFont(new Font("SansSerif", Font.BOLD, 12));

        btnGuardar.addActionListener(e -> guardarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);
     
        //  BOTÓN DE RESUMEN 
        JButton btnVerResumen = new JButton("Ver Resumen");
        btnVerResumen.setBackground(new Color(103, 58, 183));
        btnVerResumen.setForeground(Color.WHITE);
        btnVerResumen.setFocusPainted(false);
        btnVerResumen.setOpaque(true);
        btnVerResumen.setBorderPainted(false);
        btnVerResumen.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        btnVerResumen.addActionListener(e -> mostrarResumen());
        
        pnlBotones.add(btnVerResumen);

        // 5. Panel de Búsqueda
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField txtBuscar = new JTextField(20);
        pnlBusqueda.add(new JLabel("🔍 Buscar por título/autor:"));
        pnlBusqueda.add(txtBuscar);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tablaLibros.setRowSorter(sorter);

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String texto = txtBuscar.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

        // 6. Ensamblar Paneles Principales
        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.add(pnlBusqueda, BorderLayout.NORTH);
        pnlCentro.add(scrollTabla, BorderLayout.CENTER);

        JPanel pnlSuperior = new JPanel(new BorderLayout());
        pnlSuperior.add(pnlFormulario, BorderLayout.CENTER);
        pnlSuperior.add(pnlBotones, BorderLayout.SOUTH);

        add(pnlSuperior, BorderLayout.NORTH);
        add(pnlCentro, BorderLayout.CENTER);
    }
       
    private void cargarDatosTabla() {
        tableModel.setRowCount(0);
        try {
            List<Libro> lista = libroDAO.listarTodos();
            for (Libro l : lista) {
                Object[] fila = {
                        l.getId(),
                        l.getTitulo(),
                        l.getAutor(),
                        l.getCategoria(),
                        String.format("%.2f", l.getPrecio()),
                        l.getExistencias(),
                        l.getAnioPublicacion(),
                        l.getFechaIngreso() != null ? l.getFechaIngreso().toString() : "" 
             
                };
                tableModel.addRow(fila);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al cargar la lista de libros.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarLibro() {
        if (!validarEntradas()) return;

        try {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String categoria = txtCategoria.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int existencias = Integer.parseInt(txtExistencias.getText().trim());
            int anio = Integer.parseInt(txtAnioPublicacion.getText().trim());
            LocalDate fechaIngreso = LocalDate.parse(txtFechaIngreso.getText().trim()); // 👈 Nueva línea

            Libro nuevo = new Libro(titulo, autor, categoria, precio, existencias, anio, fechaIngreso);
            libroDAO.crear(nuevo);
           

            JOptionPane.showMessageDialog(this, "Libro registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla();
            limpiarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el libro en la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarLibro() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro de la tabla para editarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarEntradas()) return;

        try {
            int id = Integer.parseInt(txtId.getText());
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String categoria = txtCategoria.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int existencias = Integer.parseInt(txtExistencias.getText().trim());
            int anio = Integer.parseInt(txtAnioPublicacion.getText().trim());
            LocalDate fechaIngreso = LocalDate.parse(txtFechaIngreso.getText().trim()); // 👈 Nueva línea

            Libro libro = new Libro(id, titulo, autor, categoria, precio, existencias, anio, fechaIngreso);
            boolean exito = libroDAO.actualizar(libro);
            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Libro actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el registro a actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el registro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLibro() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar el libro '" + txtTitulo.getText() + "'?\nEsta acción no se puede deshacer.",
                "Confirmación de Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                boolean eliminado = libroDAO.eliminar(id);

                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Libro eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el registro seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al intentar eliminar el libro de la base de datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarEntradas() {
        if (txtTitulo.getText().trim().isEmpty() || txtAutor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Título y el Autor son obligatorios.", "Validación de Datos", JOptionPane.WARNING_MESSAGE);
            try {
                LocalDate.parse(txtFechaIngreso.getText().trim());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "La fecha de ingreso debe tener el formato AAAA-MM-DD (ej. 2026-03-29).", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return false;
        }

        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número mayor a cero (ej. 145.00).", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para el precio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int existencias = Integer.parseInt(txtExistencias.getText().trim());
            if (existencias < 0) {
                JOptionPane.showMessageDialog(this, "La cantidad de existencias no puede ser negativa.", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entero válido para las existencias.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int anio = Integer.parseInt(txtAnioPublicacion.getText().trim());
            int anioActual = Year.now().getValue();
            if (anio > anioActual) {
                JOptionPane.showMessageDialog(this, "El año de publicación no puede ser mayor al año actual (" + anioActual + ").", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un año válido (ej. 1967).", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void seleccionarFila() {
        int filaVista = tablaLibros.getSelectedRow();
        if (filaVista >= 0) {
        	
            // Convierte el índice de la vista al índice real del modelo
            int fila = tablaLibros.convertRowIndexToModel(filaVista);

            txtId.setText(tableModel.getValueAt(fila, 0).toString());
            txtTitulo.setText(tableModel.getValueAt(fila, 1).toString());
            txtAutor.setText(tableModel.getValueAt(fila, 2).toString());
            txtCategoria.setText(tableModel.getValueAt(fila, 3).toString());
            txtPrecio.setText(tableModel.getValueAt(fila, 4).toString().replace(",", "."));
            txtExistencias.setText(tableModel.getValueAt(fila, 5).toString());
            txtAnioPublicacion.setText(tableModel.getValueAt(fila, 6).toString());
         
            // Carga la fecha si existe en la columna 7
            Object fechaVal = tableModel.getValueAt(fila, 7);
            txtFechaIngreso.setText(fechaVal != null ? fechaVal.toString() : LocalDate.now().toString());
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtExistencias.setText("");
        txtAnioPublicacion.setText("");
        tablaLibros.clearSelection();
        txtFechaIngreso.setText(LocalDate.now().toString());
        tablaLibros.clearSelection();
    }
    private void mostrarResumen() {
        try {
            List<Libro> listaLibros = libroDAO.listarTodos();

            int totalRegistros = listaLibros.size();
            int contadorCondicion = 0; // Contador manual en Java

            for (Libro libro : listaLibros) {
                if (libro.getExistencias() > 0) { // Condición: existencias en inventario mayores a 0
                    contadorCondicion++;
                }
            }

            String mensaje = String.format(
                "=== RESUMEN DEL CATÁLOGO ===\n\n" +
                "• Total de registros cargados: %d\n" +
                "• Libros con existencias en inventario (> 0): %d\n" +
                "• Libros agotados (= 0): %d",
                totalRegistros, contadorCondicion, (totalRegistros - contadorCondicion)
            );

            JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Resumen del Catálogo",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al generar el resumen: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

} 
