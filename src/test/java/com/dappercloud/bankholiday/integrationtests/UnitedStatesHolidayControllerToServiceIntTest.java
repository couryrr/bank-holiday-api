package com.dappercloud.bankholiday.integrationtests;

import com.dappercloud.bankholiday.bankholiday.unitedstatesholiday.UnitedStatesHolidayApiController;
import com.dappercloud.bankholiday.bankholiday.unitedstatesholiday.UnitedStatesHolidayEntity;
import com.dappercloud.bankholiday.bankholiday.unitedstatesholiday.UnitedStatesHolidayRepo;
import com.dappercloud.bankholiday.bankholiday.unitedstatesholiday.UnitedStatesHolidayService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UnitedStatesHolidayControllerToServiceIntTest {

  UnitedStatesHolidayRepo repo;
  UnitedStatesHolidayApiController controller;

  @BeforeEach
  public void before(){
    var repo = Mockito.mock(UnitedStatesHolidayRepo.class);

    var mockEntity1 = new UnitedStatesHolidayEntity();
    mockEntity1.setId(1);
    mockEntity1.setHolidayDate(LocalDate.parse("2021-01-02"));
    mockEntity1.setHolidayName("fake");
    mockEntity1.setHolidayYear(2021);

    var mockEntity2 = new UnitedStatesHolidayEntity();
    mockEntity2.setId(2);
    mockEntity2.setHolidayDate(LocalDate.parse("2022-01-02"));
    mockEntity2.setHolidayName("fake");
    mockEntity2.setHolidayYear(2022);

    var mockEntity3 = new UnitedStatesHolidayEntity();
    mockEntity3.setId(3);
    mockEntity3.setHolidayDate(LocalDate.parse("2022-01-02"));
    mockEntity3.setHolidayName("another-fake");
    mockEntity3.setHolidayYear(2022);

    Mockito.when(repo.findById(1)).thenReturn(java.util.Optional.of(mockEntity1));

    Mockito.when(repo.findByHolidayName("fake"))
        .thenReturn(java.util.Optional.of(Arrays.asList(mockEntity1, mockEntity2)));

    Mockito.when(repo.findByHolidayYear(2022))
      .thenReturn(java.util.Optional.of(Arrays.asList(mockEntity2, mockEntity3)));

    this.repo = repo;
    this.controller = new UnitedStatesHolidayApiController(repo, new UnitedStatesHolidayService());
  }

  @Test
  public void getHolidayByIdFound(){

    var actual = controller.getById(1);

    var expected = repo.findById(1).get();

    Assertions.assertEquals(null, actual.getMessage());
    Assertions.assertEquals(1, actual.getHolidays().size());

    Assertions.assertEquals(expected.getHolidayDate(), actual.getHolidays().get(0).getDate());
    Assertions.assertEquals(expected.getHolidayYear(), actual.getHolidays().get(0).getYear());
    Assertions.assertEquals(expected.getId(), actual.getHolidays().get(0).getId());
    Assertions.assertEquals(expected.getHolidayName(), actual.getHolidays().get(0).getName());

  }

  @Test
  public void getHolidayByIdNotFound(){

    var actual = controller.getById(200);

    Assertions.assertEquals("No element exists for id: 200", actual.getMessage());
    Assertions.assertEquals(null, actual.getHolidays());

  }

  @Test
  public void getHolidayByYearFound(){

    var actual = controller.getByYear(2022);

    var expectedList = repo.findByHolidayYear(2022).get();

    Assertions.assertEquals(null, actual.getMessage());
    Assertions.assertEquals(expectedList.size(), actual.getHolidays().size());

    var expectedMap = expectedList.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

    actual.getHolidays().stream().forEach(item -> {
      Assertions.assertTrue(expectedMap.containsKey(item.getId()));
      var expected = expectedMap.get(item.getId());
      Assertions.assertEquals(expected.getHolidayDate(), item.getDate());
      Assertions.assertEquals(expected.getHolidayYear(), item.getYear());
      Assertions.assertEquals(expected.getId(), item.getId());
      Assertions.assertEquals(expected.getHolidayName(), item.getName());
    });
  }

  @Test
  public void getHolidayByYearNotFound(){

    var actual = controller.getByYear(2050);

    Assertions.assertEquals("No element exists for year: 2050", actual.getMessage());
    Assertions.assertEquals(null, actual.getHolidays());
  }

  @Test
  public void getHolidayByNameFound(){

    var actual = controller.getByName("fake");

    var expectedList = repo.findByHolidayName("fake").get();

    Assertions.assertEquals(null, actual.getMessage());
    Assertions.assertEquals(expectedList.size(), actual.getHolidays().size());

    var expectedMap = expectedList.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));

    actual.getHolidays().stream().forEach(item -> {
      Assertions.assertTrue(expectedMap.containsKey(item.getId()));
      var expected = expectedMap.get(item.getId());
      Assertions.assertEquals(expected.getHolidayDate(), item.getDate());
      Assertions.assertEquals(expected.getHolidayYear(), item.getYear());
      Assertions.assertEquals(expected.getId(), item.getId());
      Assertions.assertEquals(expected.getHolidayName(), item.getName());
    });
  }

  @Test
  public void getHolidayByNameNotFound(){

    var actual = controller.getByName("real");

    Assertions.assertEquals("No element exists for name: real", actual.getMessage());
    Assertions.assertEquals(null, actual.getHolidays());
  }

}
