package model;

import java.util.List;

public class Programmer {
    private long id;
    private String name;
    private int years;
    private double salary;
    private long department;
    private List<String> languages;

    public Programmer(long id,String name, int years, double salary, long department, List<String> languages) {
        this.id = id;
        this.name = name;
        this.years = years;
        this.salary = salary;
        this.department = department;
        this.languages = languages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name;}

    public int getYears() {
        return years;
    }

    public void setYears(int years) { this.years = years;}

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {this.salary = salary;}

    public long getDepartment() {
        return department;
    }

    public void setDepartment(long department) { this.department = department;}

    public List<String> getLanguages() { return languages;}

    public void setLanguages(List<String> languages) { this.languages = languages;}
}
