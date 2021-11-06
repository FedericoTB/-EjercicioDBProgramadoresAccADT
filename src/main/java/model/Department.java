package model;

import java.util.List;

public class Department {
    private long id;
    private String name;
    private double budget;
    private long manager;
    private List<Programmer> employs;

    public Department(long id, String name, double budget, long manager, List<Programmer> employs) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.manager = manager;
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

    public long getManager() {
        return manager;
    }

    public void setManager(long manager) {
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

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                ", manager=" + manager +
                ", employs=" + employs +
                '}';
    }
}
