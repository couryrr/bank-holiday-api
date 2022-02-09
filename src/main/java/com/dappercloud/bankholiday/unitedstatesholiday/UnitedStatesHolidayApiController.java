package com.dappercloud.bankholiday.unitedstatesholiday;


import com.dappercloud.bankholiday.common.Holiday;
import com.dappercloud.bankholiday.common.HolidayResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("united-states")
public class UnitedStatesHolidayApiController {

  @Autowired
  UnitedStatesHolidayRepo repo;

  Logger logger = LoggerFactory.getLogger(UnitedStatesHolidayApiController.class);


  @GetMapping("/id/{id}")
  @Cacheable(value = "UnitedStatesFederalHolidayByIdCache")
  public HolidayResponse getById(@PathVariable Integer id) {
    var optional = repo.findById(id);
    var holiday = new Holiday();
    var response = new HolidayResponse();
    if (optional.isPresent()) {
      var entity = optional.get();
      holiday.setId(entity.getId());
      holiday.setName(entity.getHolidayName());
      holiday.setDate(entity.getHolidayDate());
      holiday.setYear(entity.getHolidayYear());
      response.setHolidays(Arrays.asList(holiday));
    } else {
      response.setMessage("No element exists for id: " + id);
    }

    return response;
  }

  @GetMapping("/year/{year}")
  @Cacheable(value = "UnitedStatesFederalHolidayByHolidayYearCache")
  public HolidayResponse getByYear(@PathVariable Integer year) {
    var optional = repo.findByHolidayYear(year);

    var response = new HolidayResponse();
    if (optional.isPresent()) {
      var entities = optional.get();
      var holidays = entities.stream().map(it -> {
        var holiday = new Holiday();
        holiday.setId(it.getId());
        holiday.setName(it.getHolidayName());
        holiday.setDate(it.getHolidayDate());
        holiday.setYear(it.getHolidayYear());
        return holiday;
      })
      .collect(Collectors.toList());

      response.setHolidays(holidays);
    } else {
      response.setMessage("No element exists for id: " + year);
    }

    return response;
  }

  @GetMapping("/name/{name}")
  @Cacheable(value = "UnitedStatesFederalHolidayByHolidayNameCache")
  public HolidayResponse getByName(@PathVariable String name) {
    var optional = repo.findByHolidayName(name);

    var response = new HolidayResponse();
    if (optional.isPresent()) {
      var entities = optional.get();
      var holidays = entities.stream().map(it -> {
        var holiday = new Holiday();
        holiday.setId(it.getId());
        holiday.setName(it.getHolidayName());
        holiday.setDate(it.getHolidayDate());
        holiday.setYear(it.getHolidayYear());
        return holiday;
      })
      .collect(Collectors.toList());

      response.setHolidays(holidays);
    } else {
      response.setMessage("No element exists for id: " + name);
    }

    return response;
  }

  @GetMapping("fetch-data")
  //@Scheduled(cron = "1 */10 * * * *")
  public HolidayResponse fetchData() {
    var urlString = "https://www.opm.gov/policy-data-oversight/pay-leave/federal-holidays/#url=2022";
    logger.info("Fetching Data for united states at: " + urlString);
    var entities = new ArrayList<UnitedStatesHolidayEntity>();
    var response = new HolidayResponse();

    try {
      Document document = Jsoup.connect(urlString).get();

      var tables = document.select("table.HolidayTable");
      tables.select("span").remove();

      tables.stream().forEach(item -> {
        var year = item.select("caption").html().substring(0, 4);
        var rows = item.select("tbody tr").stream()
            .map(row -> {
              var cells = row.select("td");

              //Removed the day portion because there is a typo in 2011
              var dateString = cells.first().html()
                  .substring(cells.first().html().indexOf(',') + 2);
              if (!dateString.endsWith(", " + (Integer.valueOf(year) - 1))) {
                dateString += ", " + year;
              }

              var formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
              var date = LocalDate.parse(dateString, formatter);

              var holiday = new UnitedStatesHolidayEntity();
              holiday.setHolidayYear(Integer.valueOf(year));
              holiday.setHolidayDate(date);
              holiday.setHolidayName(cells.last().html());
              return holiday;
            })
            .collect(Collectors.toList());
        entities.addAll(rows);
      });
      repo.saveAll(entities);
      var holidays = entities.stream().map(it -> {
        var holiday = new Holiday();
        holiday.setId(it.getId());
        holiday.setName(it.getHolidayName());
        holiday.setDate(it.getHolidayDate());
        holiday.setYear(it.getHolidayYear());
        return holiday;
      })
      .collect(Collectors.toList());

      response.setHolidays(holidays);

    } catch (IOException e) {
      logger.error("An error occurred trying to fetch data for: " + urlString);
      e.printStackTrace();
      response.setMessage("An error occurred trying to fetch data for: " + urlString);

    }

    return response;
  }
}
