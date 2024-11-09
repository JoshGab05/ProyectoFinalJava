import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/bdfarmaciakevin";
    private static final String USER = "root";
    private static final String PASSWORD = "@Support20_";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar: " + e.getMessage());
            return null;
        }
    }
}
