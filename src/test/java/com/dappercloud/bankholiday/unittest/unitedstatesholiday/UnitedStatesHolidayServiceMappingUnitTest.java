package com.dappercloud.bankholiday.unittest.unitedstatesholiday;

import com.dappercloud.bankholiday.bankholiday.unitedstatesholiday.UnitedStatesHolidayEntity;
import com.dappercloud.bankholiday.bankholiday.unitedstatesholiday.UnitedStatesHolidayService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UnitedStatesHolidayServiceMappingUnitTest {

  @Test
  public void entitySingleMap(){
    var expected = new UnitedStatesHolidayEntity();
    expected.setId(1);
    expected.setHolidayDate(LocalDate.parse("2021-01-02"));
    expected.setHolidayName("fake");
    expected.setHolidayYear(2021);

    var service = new UnitedStatesHolidayService();
    var actual = service.map(expected);

    Assertions.assertEquals(expected.getHolidayDate(), actual.getDate());
    Assertions.assertEquals(expected.getHolidayYear(), actual.getYear());
    Assertions.assertEquals(expected.getId(), actual.getId());
    Assertions.assertEquals(expected.getHolidayName(), actual.getName());

  }

  @Test
  public void multipleEntityMap(){


    var expected1 = new UnitedStatesHolidayEntity();
    expected1.setId(1);
    expected1.setHolidayDate(LocalDate.parse("2021-01-02"));
    expected1.setHolidayName("fake 1");
    expected1.setHolidayYear(2021);

    var expected2 = new UnitedStatesHolidayEntity();
    expected2.setId(2);
    expected2.setHolidayDate(LocalDate.parse("2022-01-01"));
    expected2.setHolidayName("fake 2");
    expected2.setHolidayYear(2022);

    var service = new UnitedStatesHolidayService();
    var actualList = service.map(Arrays.asList(expected1, expected2));

    var expectedMap = new HashMap<Integer, UnitedStatesHolidayEntity>();
    expectedMap.put(expected1.getId(), expected1);
    expectedMap.put(expected2.getId(), expected2);

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
