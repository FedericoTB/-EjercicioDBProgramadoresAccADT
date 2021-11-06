package model;

import java.util.List;

public class Department {
    private long id;
    private String name;
    private double budget;
    private Programmer manager;
    private List<Programmer> employs;

    public Department(long id, String name, Programmer manager, List<Programmer> employs) {
        this.id = id;
        this.name = name;
        if(!employs.contains(manager)) this.manager = manager;
        this.employs = employs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Programmer getManager() {
        return manager;
    }

    public void setManager(Programmer manager) {
        if(!employs.contains(manager)) this.manager = manager;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<Programmer> getEmploys() {
        return employs;
    }

    public void setEmploys(List<Programmer> employs) {
        this.employs = employs;
    }
}
