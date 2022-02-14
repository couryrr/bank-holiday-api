package com.dappercloud.bankholiday.unittest.common;

import com.dappercloud.bankholiday.common.Holiday;
import com.dappercloud.bankholiday.common.HolidayResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HolidayContractTest {

  @Test
  public void holidayResponseFields(){
    var expected = new HashMap<String, Class>();
    expected.put("holidays", List.class);
    expected.put("message",String.class);

    var actual = HolidayResponse.class.getDeclaredFields();
    Assertions.assertEquals(expected.size(), actual.length, "The number of fields should match");
    Arrays.asList(actual).stream().forEach(field -> {
      Assertions.assertTrue(expected.containsKey(field.getName()), "Filed " + field.getName() + " should exist");
      Assertions.assertEquals(expected.get(field.getName()), field.getType(), "Filed " + field.getName() + " should be type " + field.getType());
    });
  }

  @Test
  public void holidayFields(){
    var expected = new HashMap<String, Class>();
    expected.put("id", Integer.class);
    expected.put("name",String.class);
    expected.put("date",LocalDate.class);
    expected.put("year",Integer.class);

    var actual = Holiday.class.getDeclaredFields();
    Assertions.assertEquals(expected.size(), actual.length, "The number of fields should match");
    Arrays.asList(actual).stream().forEach(field -> {
      Assertions.assertTrue(expected.containsKey(field.getName()), "Filed " + field.getName() + " should exist");
      Assertions.assertEquals(expected.get(field.getName()), field.getType(), "Filed " + field.getName() + " should be type " + field.getType());
    });
  }
}