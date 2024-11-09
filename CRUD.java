import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CRUD {
    
    public static void insertarProducto(String codigo, String nombre, double precio, int cantidad, String fecha) {
        String query = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigo);
            pst.setString(2, nombre);
            pst.setDouble(3, precio);
            pst.setInt(4, cantidad);
            pst.setDate(5, java.sql.Date.valueOf(fecha));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto insertado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar producto: " + e.getMessage());
        }
    }

    public static DefaultTableModel listarProductos() {
        String[] columnNames = {"Código", "Nombre", "Precio", "Cantidad", "Fecha"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        String query = "SELECT * FROM producto";
        try (Connection con = ConexionDB.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("codigoProducto"),
                    rs.getString("nombreProducto"),
                    rs.getDouble("precioUnitario"),
                    rs.getInt("cantidadProducto"),
                    rs.getDate("fechaVencimiento")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage());
        }
        return model;
    }
    
    public static String buscarProducto(String codigoProducto, String nombreProducto) {
        StringBuilder producto = new StringBuilder();
        String query;
        
        // Determina si se debe buscar por código o nombre
        if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
            query = "SELECT * FROM producto WHERE codigoProducto = ?";
        } else if (nombreProducto != null && !nombreProducto.trim().isEmpty()) {
            query = "SELECT * FROM producto WHERE nombreProducto = ?";
        } else {
            return "Debe proporcionar un código o un nombre de producto.";
        }
    
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            // Establece el parámetro dependiendo de la consulta
            if (codigoProducto != null && !codigoProducto.trim().isEmpty()) {
                pst.setString(1, codigoProducto);
            } else {
                pst.setString(1, nombreProducto);
            }
    
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    producto.append("Código: ").append(rs.getString("codigoProducto"))
                            .append("\nNombre: ").append(rs.getString("nombreProducto"))
                            .append("\nPrecio: ").append(rs.getDouble("precioUnitario"))
                            .append("\nCantidad: ").append(rs.getInt("cantidadProducto"))
                            .append("\nFecha de Vencimiento: ").append(rs.getDate("fechaVencimiento"));
                } else {
                    return "Producto no encontrado.";
                }
            }
        } catch (SQLException e) {
            return "Error al buscar producto: " + e.getMessage();
        }
    
        return producto.toString();
    }
    
    public static void eliminarProducto(String codigoProducto) {
        String query = "DELETE FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con el código proporcionado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage());
        }
    }
    
    public static void actualizarProducto(String codigoProducto, String nombre, Double precio, Integer cantidad) {
        // Iniciamos la parte de la consulta
        StringBuilder query = new StringBuilder("UPDATE producto SET ");
        boolean setCondition = false;  // Para verificar si alguna condición fue añadida
    
        // Si el nombre no es nulo, lo agregamos a la consulta
        if (nombre != null && !nombre.isEmpty()) {
            query.append("nombreProducto = ?, ");
            setCondition = true;
        }
        // Si el precio no es nulo, lo agregamos a la consulta
        if (precio != null) {
            query.append("precioUnitario = ?, ");
            setCondition = true;
        }
        // Si la cantidad no es nula, la agregamos a la consulta
        if (cantidad != null) {
            query.append("cantidadProducto = ?, ");
            setCondition = true;
        }
    
        // Si no se seleccionó ningún campo para actualizar, terminamos aquí
        if (!setCondition) {
            JOptionPane.showMessageDialog(null, "No se seleccionaron campos para actualizar.");
            return;
        }
    
        // Eliminar la última coma de la cadena
        query.setLength(query.length() - 2);  // Eliminar la coma final
    
        // Añadimos la condición WHERE
        query.append(" WHERE codigoProducto = ?");
    
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query.toString())) {
            int index = 1;
    
            // Establecer los valores de los parámetros en el PreparedStatement
            if (nombre != null && !nombre.isEmpty()) {
                pst.setString(index++, nombre);
            }
            if (precio != null) {
                pst.setDouble(index++, precio);
            }
            if (cantidad != null) {
                pst.setInt(index++, cantidad);
            }
    
            // Establecer el código del producto
            pst.setString(index, codigoProducto);
    
            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el producto con ese código.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage());
        }
    }
    
    
}
