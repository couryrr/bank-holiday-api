package com.dappercloud.bankholiday.unitedstatesholiday;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitedStatesHolidayServiceUnitTest {

  @Test
  public void entitySingleMap(){
    var mockEntity = new UnitedStatesHolidayEntity();
    mockEntity.setId(1);
    mockEntity.setHolidayDate(LocalDate.parse("2021-01-02"));
    mockEntity.setHolidayName("fake");
    mockEntity.setHolidayYear(2021);

    var service = new UnitedStatesHolidayService();
    var actual = service.map(mockEntity);

    Assertions.assertEquals(mockEntity.getHolidayDate(), actual.getDate());
    Assertions.assertEquals(mockEntity.getHolidayYear(), actual.getYear());
    Assertions.assertEquals(mockEntity.getId(), actual.getId());
    Assertions.assertEquals(mockEntity.getHolidayName(), actual.getName());

  }

  @Test
  public void multipleEntityMap(){


    var mockEntity1 = new UnitedStatesHolidayEntity();
    mockEntity1.setId(1);
    mockEntity1.setHolidayDate(LocalDate.parse("2021-01-02"));
    mockEntity1.setHolidayName("fake 1");
    mockEntity1.setHolidayYear(2021);
    var mockEntity2 = new UnitedStatesHolidayEntity();
    mockEntity2.setId(2);
    mockEntity2.setHolidayDate(LocalDate.parse("2022-01-01"));
    mockEntity2.setHolidayName("fake 2");
    mockEntity2.setHolidayYear(2022);
    var service = new UnitedStatesHolidayService();
    var actualList = service.map(Arrays.asList(mockEntity1, mockEntity2));

    var expectedMap = new HashMap<Integer, UnitedStatesHolidayEntity>();
    expectedMap.put(mockEntity1.getId(), mockEntity1);
    expectedMap.put(mockEntity2.getId(), mockEntity2);

    Assertions.assertEquals(expectedMap.size(), actualList.size());
    actualList.stream().forEach(actual -> {
      Assertions.assertTrue(expectedMap.containsKey(actual.getId()));
      var expected = expectedMap.get(actual.getId());
      Assertions.assertEquals(expected.getHolidayDate(), actual.getDate());
      Assertions.assertEquals(expected.getHolidayYear(), actual.getYear());
      Assertions.assertEquals(expected.getId(), actual.getId());
      Assertions.assertEquals(expected.getHolidayName(), actual.getName());
    });
  }
}
