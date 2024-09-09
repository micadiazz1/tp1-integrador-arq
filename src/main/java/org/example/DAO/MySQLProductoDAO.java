package org.example.DAO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class MySQLProductoDAO implements ProductoDAO{

    private Connection conn;

    public MySQLProductoDAO(Connection conn){
        try {
            this.conn = DbSingleton.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión", e);
        }
    }

    @Override
    public void createTable() {
        String createProductoTable = "CREATE TABLE Producto (" +
                "idProducto INT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(45)," +
                "valor FLOAT" +
                ")";
        try(PreparedStatement statement = conn.prepareStatement(createProductoTable)) {
           statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void insertProduct() {
        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader("src/main/resources/clientes.csv"));

            String insert = "INSERT INTO producto (idProducto,nombre,valor) VALUES (?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insert);

            for (CSVRecord row : parser) {
                System.out.println(row.get("idProducto"));
                System.out.println(row.get("nombre"));
                System.out.println(row.get("valor"));

                statement.setString(1, row.get("idProducto"));
                statement.setString(2, row.get("nombre"));
                statement.setString(3, row.get("valor"));
                statement.executeUpdate();

            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<String, Object> getTopProduct() {
        String query = "SELECT p.idProducto, p.nombre, p.valor, fp.total_vendido FROM Producto p " +
                "JOIN ( " +
                "    SELECT fp.idProducto, (SUM(fp.cantidad) * p.valor) AS total_vendido " +
                "    FROM Factura_Producto fp " +
                "    JOIN Producto p ON p.idProducto = fp.idProducto " +
                "    GROUP BY fp.idProducto " +
                "    ORDER BY total_vendido DESC " +
                ") fp ON fp.idProducto = p.idProducto " +
                "LIMIT 1;";

        HashMap<String, Object> productData = new HashMap<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                productData.put("idProducto", rs.getInt("idProducto"));
                productData.put("nombre", rs.getString("nombre"));
                productData.put("valor", rs.getString("valor"));
                productData.put("total_vendido", rs.getString("total_vendido"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return productData;
    }
}
