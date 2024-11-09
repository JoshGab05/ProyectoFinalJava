import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelPrincipal extends JFrame {
    public PanelPrincipal() {
        setTitle("CRUD de Productos");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));

        JButton btnInsertar = new JButton("Ingresar Registros");
        JButton btnListar = new JButton("Consultar Registros");
        JButton btnEliminar = new JButton("Eliminar Registros");
        JButton btnBuscar = new JButton("<html>Buscar Registro<br/>"+"por Código o nombre<html>");
        JButton btnActualizar = new JButton("Actualizar Registros");
        JButton btnRealizarVenta = new JButton("Transacción");

        panel.add(btnInsertar);
        panel.add(btnListar);
        panel.add(btnEliminar);
        panel.add(btnBuscar);
        panel.add(btnActualizar);
        panel.add(btnRealizarVenta);

        btnInsertar.addActionListener(e -> insertarProductoGUI());
        btnListar.addActionListener(e -> listarProductosGUI());
        btnEliminar.addActionListener(e -> eliminarProductosGUI());
        btnBuscar.addActionListener(e -> buscarProductoGUI());
        btnActualizar.addActionListener(e -> actualizarProductoGUI());
        btnRealizarVenta.addActionListener(e -> realizarVenta());
        add(panel);
    }

    private void insertarProductoGUI() {
        JFrame frame = new JFrame("Ingresar Producto");
        frame.setSize(300, 250);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        JTextField txtCodigo = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtFecha = new JTextField();

        panel.add(new JLabel("Código:"));
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Fecha (AÑO-MES-DÍA):"));
        panel.add(txtFecha);

        JButton btnAgregar = new JButton("Agregar Producto");
        btnAgregar.addActionListener(e -> {
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());
            String fecha = txtFecha.getText();
            CRUD.insertarProducto(codigo, nombre, precio, cantidad, fecha);
            frame.dispose();
        });

        panel.add(btnAgregar);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void listarProductosGUI() {
        DefaultTableModel model = CRUD.listarProductos();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog(this, "Listado de Productos", true);
        dialog.add(scrollPane);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void eliminarProductosGUI() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese el código del producto a eliminar"); 
        if (codigo != null && !codigo.trim().isEmpty()) {
            // Llamar al método eliminarProducto de la clase CRUD
            CRUD.eliminarProducto(codigo);
        } else {
            JOptionPane.showMessageDialog(this, "Debe ingresar un código válido.");
        }
    }

    private void buscarProductoGUI() {
        // Crear un panel con dos campos de texto: uno para el código y otro para el nombre
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField txtCodigo = new JTextField();
        JTextField txtNombre = new JTextField();
        
        panel.add(new JLabel("Código de producto:"));
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre de producto:"));
        panel.add(txtNombre);
    
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Buscar Producto", JOptionPane.OK_CANCEL_OPTION);
    
        // Verificar si el usuario presionó OK
        if (opcion == JOptionPane.OK_OPTION) {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            
            if (!codigo.isEmpty()) {
                // Buscar por código
                String producto = CRUD.buscarProducto(codigo, null);
                JOptionPane.showMessageDialog(this, producto);
            } else if (!nombre.isEmpty()) {
                // Buscar por nombre
                String producto = CRUD.buscarProducto(null, nombre);
                JOptionPane.showMessageDialog(this, producto);
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar el código o el nombre del producto.");
            }
        }
    }
    
    private void actualizarProductoGUI() {
        JFrame frame = new JFrame("Modificar Producto");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(this);
    
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
    
        JTextField txtCodigo = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCantidad = new JTextField();
    
        // Crear los JCheckBox para cada parámetro
        JCheckBox checkNombre = new JCheckBox("Modificar Nombre");
        JCheckBox checkPrecio = new JCheckBox("Modificar Precio");
        JCheckBox checkCantidad = new JCheckBox("Modificar Cantidad");
    
        panel.add(new JLabel("Código:"));
        panel.add(txtCodigo);
        panel.add(checkNombre);
        panel.add(txtNombre);
        panel.add(checkPrecio);
        panel.add(txtPrecio);
        panel.add(checkCantidad);
        panel.add(txtCantidad);
    
        JButton btnActualizar = new JButton("Actualizar Producto");
        btnActualizar.addActionListener(e -> {
            String codigo = txtCodigo.getText().trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El código no puede estar vacío.");
                return;
            }
            String nombre = checkNombre.isSelected() && !txtNombre.getText().trim().isEmpty() ? txtNombre.getText() : null;
            Double precio = checkPrecio.isSelected() && !txtPrecio.getText().trim().isEmpty() ? Double.parseDouble(txtPrecio.getText()) : null;
            Integer cantidad = checkCantidad.isSelected() && !txtCantidad.getText().trim().isEmpty() ? Integer.parseInt(txtCantidad.getText()) : null;
            CRUD.actualizarProducto(codigo, nombre, precio, cantidad);
            frame.dispose();
        });
     
        panel.add(btnActualizar);
        frame.add(panel);
        frame.setVisible(true);
    }    

    private void realizarVenta(){
            // Llamar a la clase Transaccion para realizar la venta
            Transaccion.realizarVenta(); // Aquí estamos llamando al método de la clase Transaccion
        }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PanelPrincipal frame = new PanelPrincipal();
            frame.setVisible(true);
        });
    }
}
