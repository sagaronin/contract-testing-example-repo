package provider;

import java.util.HashMap;
import java.util.Map;

public class Information {
    
    private Integer salary;
    private String name;
    private String nationality;

    public String getStrength() {
        return strength;
    }

    public void setStrength(final String strength) {
        this.strength = strength;
    }

    private String strength;

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(final String weakness) {
        this.weakness = weakness;
    }

    private String weakness;
    private Map<String, String> contact = new HashMap<String, String>();

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getContact() {
        return contact;
    }

    public void setContact(Map<String, String> contact) {
        this.contact = contact;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
