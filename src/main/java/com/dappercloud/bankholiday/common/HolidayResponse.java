package com.dappercloud.bankholiday.common;

import java.io.Serializable;
import java.util.List;

public class HolidayResponse implements Serializable {
    private List<Holiday> holidays;
    private String message;

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
