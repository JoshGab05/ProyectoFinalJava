import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Usuario:"));
        
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(new LoginAction());
        panel.add(btnLogin);
        add(panel);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());

            // Aquí puedes poner los datos correctos para validar el acceso
            if (usuario.equals("Josue") && password.equals("@Support20_")) {
                JOptionPane.showMessageDialog(Login.this, "Acceso concedido");
                PanelPrincipal crudGUI = new PanelPrincipal();
                crudGUI.setVisible(true);
                dispose();  // Cerrar la ventana de inicio de sesión
            } else {
                JOptionPane.showMessageDialog(Login.this, "Usuario o contraseña incorrectos");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
