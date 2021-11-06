package repository;

import database.DataBaseController;
import model.Department;
import model.Programmer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepartmentRepository {
    private static DataBaseController db = DataBaseController.getInstance();
    private static Optional<Department> deleteDepartment(Department department) {
        System.out.println("Deleting Deparment with ID: " + department.getId());
        String query = "DELETE FROM deparments WHERE id = ?";
        Optional<Department> result = Optional.empty();
        try {
            db.open();
            int res = db.delete(query, department.getId());
            db.close();
            if (res > 0)
                System.out.println(department.toString());
        } catch (SQLException e) {
            System.err.println("Error at try to delete the Deparment" + e.getMessage());
        }return result;
    }

    private static Optional<Department> updateDepartment(Department department,String column) {
        System.out.println("Updating Department with ID: " + department.getId());
        String query = "UPDATE departments SET "+column+" = ?";
        DataBaseController db = DataBaseController.getInstance();
        Optional<Department> result = Optional.empty();
        try {
            db.open();
            int res = 0;
            switch(column){
                case "name": res = db.update(query, department.getName());break;
                case "budget": res = db.update(query, department.getBudget());break;
                case "id_manager": res = db.update(query, department.getManager());break;
                default: System.out.println("Error at Update Deparment: not existing column passed for parameter");
            }
            db.close();
            if (res > 0)
                result = Optional.of(department);
                System.out.println(department.toString());
        } catch (SQLException e) {
            System.err.println("Error al actualizar Department" + e.getMessage());
        }
        return result;
    }

    private static Optional<Department> insertDepartment(Department department) {
        System.out.println("Insert in to Department");
        String query = "INSERT INTO departments VALUES (null, ?, ?, ?)";
        DataBaseController db = DataBaseController.getInstance();
        Optional<Department> optionalResult = Optional.empty();
        try {

            db.open();
            ResultSet result = db.insert(query, department.getName(), department.getBudget(),
                            department.getManager())
                    .orElseThrow(() -> new SQLException("Error at insert programmer"));
            // Para obtener su ID
            if (result.first()) {
                department.setId(result.getLong(1));
                // una vez insertado comprobamos que esta correcto para devolverlo
                optionalResult = Optional.of(department);
                db.close();
                System.out.println(department.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error at insert programmer" + e.getMessage());
        }return optionalResult;
    }

    private static Optional<Department> selectDepartmentById(int id) {
        System.out.println("Obteniendo usuario con id: " + id);
        String queryDeparments = "SELECT * FROM departments WHERE id_department = ?";
        String queryProgrammers = "SELECT * FROM programmers WHERE id_department = ?";
        DataBaseController db = DataBaseController.getInstance();
        Optional<Department> department = Optional.empty();
        List<Programmer> listOfProgrammers = null;
        try {
            db.open();
            ResultSet resultDeparment = db.select(queryDeparments, id).orElseThrow(() -> new SQLException("Error al consultar usuario con ID " + id));
            ResultSet resultProgrammers = db.select(queryProgrammers, id).orElseThrow(() -> new SQLException("Error al consultar usuario con ID " + id));

            if (resultDeparment.first()) {

                while (resultProgrammers.next()) {
                     listOfProgrammers.add(new Programmer(
                            resultProgrammers.getLong("id_programmer"),
                            resultProgrammers.getString("name"),
                            resultProgrammers.getInt("years"),
                            resultProgrammers.getDouble("salary"),
                            resultProgrammers.getLong("id_manager"),
                            Arrays.stream(resultProgrammers.getString("languages").split(";")).collect(Collectors.toList())
                    ));
                }
                department = Optional.of(new Department(
                        resultDeparment.getLong("id_department"),
                        resultDeparment.getString("name"),
                        resultDeparment.getDouble("budget"),
                        resultDeparment.getLong("id_manager"),
                        listOfProgrammers
                ));
                db.close();
                System.out.println(department.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario con id: " + id + " - " + e.getMessage());
        }return department;
    }

    private static Optional<ArrayList<Department>> selectAllDepartments() {
        System.out.println("Obteniendo todos los Departments");
        String queryDepartments = "SELECT * FROM departments";
        String queryProgrammers = "SELECT * FROM programmers WHERE id_department = ?";
        DataBaseController db = DataBaseController.getInstance();
        Optional<ArrayList<Department>> optionalResult = Optional.empty();
        try {
            db.open();
            ResultSet result = db.select(queryDepartments).orElseThrow(() -> new SQLException("Error al consultar registros de Usuarios"));

            ArrayList<Department> list = new ArrayList<Department>();

            while (result.next()) {
                ResultSet resultProgrammers = db.select(queryProgrammers, result.getLong("id_department")).orElseThrow(() -> new SQLException("Error al consultar usuario con ID "));
                List<Programmer> programmersList = new ArrayList<Programmer>();
                while(resultProgrammers.next()){
                    programmersList.add(new Programmer(
                            result.getLong("id_programmer"),
                            result.getString("name"),
                            result.getInt("years"),
                            result.getDouble("salary"),
                            result.getLong("id_manager"),
                            Arrays.stream(result.getString("languages").split(";")).collect(Collectors.toList())
                    ));
                }
                list.add(
                        new Department(
                                result.getLong("id_department"),
                                result.getString("name"),
                                result.getDouble("budget"),
                                result.getLong("id_manager"),
                                new ArrayList<>()
                        )
                );
            }
            db.close();
            list.forEach(System.out::println);
            optionalResult = Optional.of(list);
        } catch (SQLException e) {
            System.err.println("Error at obtain all programmers: " + e.getMessage());
        }return optionalResult;
    }
}
