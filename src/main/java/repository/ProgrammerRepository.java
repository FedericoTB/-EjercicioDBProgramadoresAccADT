package repository;

import database.DataBaseController;
import model.Programmer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ProgrammerRepository {
   private static DataBaseController db = DataBaseController.getInstance();
    private static void deleteUser(Programmer programmer) {
        System.out.println("Deleting Programmer with ID: " + programmer.getId());
        String query = "DELETE FROM programmers WHERE id = ?";

        try {
            db.open();
            int res = db.delete(query, programmer.getId());
            db.close();
            if (res > 0)
                System.out.println(programmer.toString());
        } catch (SQLException e) {
            System.err.println("Error at try to delete the Programmer" + e.getMessage());
        }
    }

    private static void updateUser(Programmer programmer) {
        System.out.println("Updating Programmer with ID: " + programmer.getId());
        String query = "UPDATE programmers SET name = ?, email = ? WHERE id = ?";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            int res = db.update(query, programmer.getNombre(), programmer.getEmail(), programmer.getId());
            db.close();
            if (res > 0)
                System.out.println(programmer.toString());
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario" + e.getMessage());
        }

    }

    private static void insertUser(Programmer programmer) {
        System.out.println("Insertando usuario");
        String query = "INSERT INTO programmers VALUES (null, ?, ?, ?, ?, ?)";
        DataBaseController db = DataBaseController.getInstance();
        try {

            db.open();
            ResultSet result = db.insert(query, programmer.getName(), programmer.getYears(),
                    programmer.getSalary(), programmer.getDepartment(),programmer.getLanguages())
                    .orElseThrow(() -> new SQLException("Error at insert programmer"));
            // Para obtener su ID
            if (result.first()) {
                programmer.setId(result.getLong(1));
                // una vez insertado comprobamos que esta correcto para devolverlo
                db.close();
                System.out.println(programmer.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error at insert programmer" + e.getMessage());
        }
    }

    private static void selectUserById(int id) {
        System.out.println("Obteniendo usuario con id: " + id);
        String query = "SELECT * FROM programmers WHERE id = ?";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            ResultSet result = db.select(query, id).orElseThrow(() -> new SQLException("Error al consultar usuario con ID " + id));
            if (result.first()) {
                Programmer programmer = new Programmer(
                        result.getLong("id_programmer"),
                        result.getString("name"),
                        result.getInt("years"),
                        result.getDouble("salary"),
                        result.getLong("id_department"),
                        Arrays.stream(result.getString("languages").split(";")).collect(Collectors.toList())
                );
                db.close();
                System.out.println(programmer.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario con id: " + id + " - " + e.getMessage());
        }
    }

    private static void selectAllUsers() {
        System.out.println("Obteniendo todos los usuarios");
        String query = "SELECT * FROM programmers";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            ResultSet result = db.select(query).orElseThrow(() -> new SQLException("Error al consultar registros de Usuarios"));
            ArrayList<Programmer> list = new ArrayList<Programmer>();
            while (result.next()) {
                list.add(
                        new Programmer(
                                result.getLong("id_programmer"),
                                result.getString("name"),
                                result.getInt("years"),
                                result.getDouble("salary"),
                                result.getLong("id_department"),
                                Arrays.stream(result.getString("languages").split(";")).collect(Collectors.toList())
                        )
                );
            }
            db.close();
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error at obtain all programmers: " + e.getMessage());
        }
    }
}
