package ru.otus.crm.model;

import ru.otus.jdbc.mapper.Column;
import ru.otus.jdbc.mapper.Id;

public class Manager {
    @Id
    @Column("id")
    private Long id;
    @Column("label")
    private String label;
    @Column("param_1")
    private String param1;

    public Manager() {
    }

    public Manager(String label) {
        this.label = label;
    }

    public Manager(Long id, String label, String param1) {
        this.id = id;
        this.label = label;
        this.param1 = param1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "no=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
