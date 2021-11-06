import database.DataBaseController;
import model.Department;
import model.Programmer;
import repository.DepartmentRepository;
import repository.ProgrammerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
   public static void main(String[] args){
       DataBaseController dbc = DataBaseController.getInstance();
        checkServer();
       System.out.println("\n Probando los Crud de Programmers \n");
       ProgrammerRepository.selectAllUsers();
       ProgrammerRepository.selectProgrammer(1);
       List<String> lang = new ArrayList<>();
       lang.add("Java");
       lang.add("Kotlin");
       Programmer p1 = new Programmer(13,"Ludeus",5,25000.00,3,lang);
       ProgrammerRepository.insertUser(p1);
       ProgrammerRepository.updateProgrammer(p1,"name");
       ProgrammerRepository.deleteProgrammer(p1);

       System.out.println("\n Probando los Crud de Programmers \n");
       DepartmentRepository.selectAllDepartments();
       DepartmentRepository.selectDepartmentById(1);
       List<Programmer> plist = new ArrayList<Programmer>();
       plist.add(p1);
       Department dep1 = new Department(3,"Moda",60000.00,13,plist);
       DepartmentRepository.insertDepartment(dep1);
       DepartmentRepository.updateDepartment(dep1,"name");
       DepartmentRepository.deleteDepartment(dep1);
   }
    private static void checkServer() {
        System.out.println("Comprobamos la conexión al Servidor BD");
        DataBaseController controller = DataBaseController.getInstance();
        try {
            controller.open();
            Optional<ResultSet> rs = controller.select("SELECT * FROM programmers");
            if (rs.isPresent()) {
                System.out.println(rs.get().next());;
                controller.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.getMessage());
            System.exit(1);
        }
    }
}
