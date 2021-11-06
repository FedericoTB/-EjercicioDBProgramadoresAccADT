package repository;

import database.DataBaseController;
import model.Department;
import model.Languages;
import model.Programmer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProgrammerRepository {
   private static DataBaseController db = DataBaseController.getInstance();
    private static Optional<Programmer> deleteProgrammer(Programmer programmer) {
        System.out.println("Deleting Programmer with ID: " + programmer.getId());
        String query = "DELETE FROM programmers WHERE id_programmer = ?";
        Optional<Programmer> optionalResult = Optional.empty();
        try {
            db.open();
            int res = db.delete(query, programmer.getId());
            db.close();
            if (res > 0)
                optionalResult = Optional.of(programmer);
                System.out.println(programmer.toString());
        } catch (SQLException e) {
            System.err.println("Error at try to delete the Programmer" + e.getMessage());
        }return optionalResult;
    }

    private static Optional<Programmer> updateProgrammer(Programmer programmer, String column) {
        System.out.println("Updating Programmer with ID: " + programmer.getId());
        String query = "UPDATE programmers SET "+column+" = ? WHERE id_programmer = ?";
        DataBaseController db = DataBaseController.getInstance();
        Optional<Programmer> optionalResult = Optional.empty();
        try {
            db.open();
            int res =0;
            switch (column){
                case "name": res = db.update(query, programmer.getName(), programmer.getId());break;
                case "years": res = db.update(query, programmer.getYears(), programmer.getId());break;
                case "salary": res = db.update(query, programmer.getSalary(), programmer.getId());break;
                case "id_department": res = db.update(query, programmer.getDepartment(), programmer.getId());break;
                default: System.out.println("Error at Update Deparment: not existing column passed for parameter");
            }
            db.close();
            if (res > 0)
                optionalResult = Optional.of(programmer);
                System.out.println(programmer.toString());
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario" + e.getMessage());
        }return optionalResult;
    }

    private static Optional<Programmer> insertUser(Programmer programmer) {
        System.out.println("Insertando usuario");
        String query = "INSERT INTO programmers VALUES (null, ?, ?, ?, ?, ?)";
        DataBaseController db = DataBaseController.getInstance();
        Optional<Programmer> optionalResult = Optional.empty();
        try {

            db.open();
            ResultSet result = db.insert(query, programmer.getName(), programmer.getYears(),
                    programmer.getSalary(), programmer.getDepartment(),programmer.getLanguages())
                    .orElseThrow(() -> new SQLException("Error at insert programmer"));
            // Para obtener su ID
            if (result.first()) {
                programmer.setId(result.getLong(1));
                optionalResult = Optional.of(programmer);
                // una vez insertado comprobamos que esta correcto para devolverlo
                db.close();
                System.out.println(programmer.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error at insert programmer" + e.getMessage());
        }return optionalResult;
    }

    private static Optional<Programmer> selectProgrammer(int id) {
        System.out.println("Obteniendo usuario con id: " + id);
        String query = "SELECT * FROM programmers WHERE id_programmer = ?";
        DataBaseController db = DataBaseController.getInstance();
        Optional<Programmer> optionalResult = Optional.empty();
        try {
            db.open();
            ResultSet result = db.select(query, id).orElseThrow(() -> new SQLException("Error al consultar usuario con ID " + id));

            if (result.first()) {

                Programmer programmer = new Programmer(
                        result.getLong("id_programmer"),
                        result.getString("name"),
                        result.getInt("years"),
                        result.getDouble("salary"),
                        result.getLong("id_manager"),
                        Arrays.stream(result.getString("languages").split(";")).collect(Collectors.toList())
                );
                optionalResult = Optional.of(programmer);
                db.close();
                System.out.println(programmer.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario con id: " + id + " - " + e.getMessage());
        }return optionalResult;
    }

    private static Optional<ArrayList<Programmer>> selectAllUsers() {
        System.out.println("Obteniendo todos los usuarios");
        String query = "SELECT * FROM programmers";
        DataBaseController db = DataBaseController.getInstance();
        Optional<ArrayList<Programmer>> optionalResult = Optional.empty();
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
                                result.getLong("id_manager"),
                                Arrays.stream(result.getString("languages").split(";")).collect(Collectors.toList())
                        )
                );
            }
            optionalResult = Optional.of(list);
            db.close();
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error at obtain all programmers: " + e.getMessage());
        }return optionalResult;
    }
}
