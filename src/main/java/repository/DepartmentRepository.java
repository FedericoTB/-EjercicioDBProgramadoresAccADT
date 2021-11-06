package repository;

import database.DataBaseController;
import model.Department;
import model.Programmer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DepartmentRepository {
    private static DataBaseController db = DataBaseController.getInstance();
    private static void deleteDepartment(Department department) {
        System.out.println("Deleting Deparment with ID: " + department.getId());
        String query = "DELETE FROM deparments WHERE id = ?";

        try {
            db.open();
            int res = db.delete(query, department.getId());
            db.close();
            if (res > 0)
                System.out.println(department.toString());
        } catch (SQLException e) {
            System.err.println("Error at try to delete the Deparment" + e.getMessage());
        }
    }

    private static void updateDepartment(Department department,String column) {
        System.out.println("Updating Department with ID: " + department.getId());
        String query = "UPDATE departments SET "+column+" = ?";
        DataBaseController db = DataBaseController.getInstance();
        try {
            db.open();
            int res;
            switch(column){
                case "name": res = db.update(query, department.getName());break;
                case "budget": res = db.update(query, department.getBudget());break;
                case "id_manager": res = db.update(query, department.getManager());break;
                default: System.out.println("Error at Update Deparment: not existing column passed for parameter");
            }
            db.close();
            if (res > 0)
                System.out.println(department.toString());
        } catch (SQLException e) {
            System.err.println("Error al actualizar Department" + e.getMessage());
        }

    }

    private static void insertDepartment(Programmer programmer) {
        System.out.println("Insertando Department");
        String query = "INSERT INTO departments VALUES (null, ?, ?, ?, ?, ?)";
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

    private static void selectDepartmentById(int id) {
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

    private static void selectAllDepartments() {
        System.out.println("Obteniendo todos los Departments");
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
