package edu.umg.programacion2.proyecto.dao;

import edu.umg.programacion2.proyecto.modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroDAO {

    public Libro crear(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, categoria, precio, existencias, anio_publicacion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getCategoria());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getExistencias());
            ps.setInt(6, libro.getAnioPublicacion());
            ps.setInt(6, libro.getAnioPublicacion());
            ps.setDate(7, libro.getFechaIngreso() != null ? java.sql.Date.valueOf(libro.getFechaIngreso()) : java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setInt(8, libro.getId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    libro.setId(rs.getInt(1));
                }
            }
        }
        return libro;
    }

    public List<Libro> listarTodos() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, categoria, precio, existencias, anio_publicacion, fecha_ingreso FROM libros ORDER BY id DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

        	while (rs.next()) {
        	    java.sql.Date fechaSql = rs.getDate("fecha_ingreso");
        	    java.time.LocalDate fechaIngreso = (fechaSql != null) ? fechaSql.toLocalDate() : java.time.LocalDate.now();

        	    Libro libro = new Libro(
        	            rs.getInt("id"),
        	            rs.getString("titulo"),
        	            rs.getString("autor"),
        	            rs.getString("categoria"),
        	            rs.getDouble("precio"),
        	            rs.getInt("existencias"),
        	            rs.getInt("anio_publicacion"),
        	            fechaIngreso
        	    );
        	    lista.add(libro);
        	}
        }
        
        return lista;
    }

    public Optional<Libro> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo, autor, categoria, precio, existencias, anio_publicacion,fecha_ingreso FROM libros WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                	java.sql.Date fechaSql = rs.getDate("fecha_ingreso");
                    java.time.LocalDate fechaIngreso = (fechaSql != null) ? fechaSql.toLocalDate() : java.time.LocalDate.now();
                    
                    Libro libro = new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("existencias"),
                            rs.getInt("anio_publicacion"),
                            fechaIngreso
                    );
                    return Optional.of(libro);
                }
            }
        }
        return Optional.empty();
    }

    public boolean actualizar(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, categoria = ?, precio = ?, existencias = ?, anio_publicacion = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getCategoria());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getExistencias());
            ps.setInt(6, libro.getAnioPublicacion());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}