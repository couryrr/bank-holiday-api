package com.dappercloud.bankholiday.common;

import java.io.Serializable;
import java.time.LocalDate;

public class Holiday implements Serializable {
    private Integer id;
    private String name;
    private LocalDate date;
    private Integer year;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
