import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Transaccion {

    public static void realizarVenta() {
        // Mostrar la lista de productos
        DefaultTableModel model = CRUD.listarProductos();  // Obtener la lista de productos desde la clase CRUD
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Crear un JFrame para mostrar la tabla de productos
        JFrame frame = new JFrame("Productos Disponibles");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // No cierra toda la aplicación
        frame.add(scrollPane);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  // Mostrar la ventana con la tabla de productos

        // Pedir el código del producto a vender
        String codigoProducto = JOptionPane.showInputDialog("Ingrese el código del producto que desea vender:");
        if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un código de producto.");
            return;
        }

        // Validar que el producto esté en el inventario
        int cantidadDisponible = obtenerCantidadDisponible(codigoProducto);
        if (cantidadDisponible == -1) {
            JOptionPane.showMessageDialog(null, "Producto no encontrado.");
            return;
        }

        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad que desea vender:");
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad.");
            return;
        }

        int cantidadVendida = Integer.parseInt(cantidadStr);

        // Verificar si hay suficiente cantidad en el inventario
        if (cantidadVendida > cantidadDisponible) {
            JOptionPane.showMessageDialog(null, "No hay suficiente inventario disponible.");
            return;
        }

        // Obtener el precio del producto
        float precioProducto = obtenerPrecioProducto(codigoProducto);
        if (precioProducto > 0) {
            // Calcular el total
            float total = total(cantidadVendida, precioProducto);
            JOptionPane.showMessageDialog(null, "Total de la venta: " + total);

            // Actualizar el inventario
            actualizarInventario(codigoProducto, cantidadVendida);
        }
    }

    public static float obtenerPrecioProducto(String codigoProducto) {
        String query = "SELECT precioUnitario FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getFloat("precioUnitario");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el precio: " + e.getMessage());
        }
        return 0;
    }

    public static int obtenerCantidadDisponible(String codigoProducto) {
        String query = "SELECT cantidadProducto FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, codigoProducto);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("cantidadProducto");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener la cantidad disponible: " + e.getMessage());
        }
        return -1;  // Producto no encontrado
    }

    public static void actualizarInventario(String codigoProducto, int cantidadVendida) {
        // Actualizar la cantidad en el inventario
        String query = "UPDATE producto SET cantidadProducto = cantidadProducto - ? WHERE codigoProducto = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, cantidadVendida);
            pst.setString(2, codigoProducto);
            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Inventario actualizado.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el inventario.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar inventario: " + e.getMessage());
        }
    }

    public static float total(int cantidadVendida, float precioProducto) {
        // Calcular el total de la venta
        return cantidadVendida * precioProducto;
    }
}
