package com.dappercloud.bankholiday.unitedstatesholiday;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "united_states")
public class UnitedStatesHolidayEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String holidayName;
  private LocalDate holidayDate;
  private Integer holidayYear;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getHolidayName() {
    return holidayName;
  }

  public void setHolidayName(String holidayName) {
    this.holidayName = holidayName;
  }

  public LocalDate getHolidayDate() {
    return holidayDate;
  }

  public void setHolidayDate(LocalDate holidayDate) {
    this.holidayDate = holidayDate;
  }

  public Integer getHolidayYear() {
    return holidayYear;
  }

  public void setHolidayYear(Integer holidayYear) {
    this.holidayYear = holidayYear;
  }
}
