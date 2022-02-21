package com.dappercloud.bankholiday.unitedstatesholiday;


import com.dappercloud.bankholiday.common.Holiday;
import com.dappercloud.bankholiday.common.HolidayResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("united-states")
public class UnitedStatesHolidayApiController {
  @Value("${TOPIC_EXCHANGE_NAME:bank-holiday}")
  private String topicExchangeName;

  @Value("${QUEUE_NAME:add-united-states-holiday}")
  private String queueName;

  @Value("${ROUTING_KEY:add.united.states}")
  private String routingKey;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  UnitedStatesHolidayReceiver unitedStatesHolidayReceiver;

  @Autowired
  UnitedStatesHolidayRepo repo;

  @Autowired
  UnitedStatesHolidayService service;

  Logger logger = LoggerFactory.getLogger(UnitedStatesHolidayApiController.class);

  public UnitedStatesHolidayApiController(UnitedStatesHolidayRepo repo,
      UnitedStatesHolidayService service){
    this.repo = repo;
    this.service = service;
  }


  @GetMapping("/id/{id}")
  @Cacheable(value = "UnitedStatesFederalHolidayByIdCache")
  public HolidayResponse getById(@PathVariable Integer id) {
    var optional = repo.findById(id);

    var response = new HolidayResponse();
    if (optional.isPresent()) {
      response.setHolidays(Arrays.asList(service.map(optional.get())));
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
      response.setHolidays(service.map(optional.get()));
    } else {
      response.setMessage("No element exists for year: " + year);
    }

    return response;
  }

  @GetMapping("/name/{name}")
  @Cacheable(value = "UnitedStatesFederalHolidayByHolidayNameCache")
  public HolidayResponse getByName(@PathVariable String name) {
    var optional = repo.findByHolidayName(name);

    var response = new HolidayResponse();
    if (optional.isPresent()) {
      response.setHolidays(service.map(optional.get()));
    } else {
      response.setMessage("No element exists for name: " + name);
    }

    return response;
  }

  //@GetMapping("fetch-data")
  //Mid-night on the first day of any month any day
  @Scheduled(cron = "0 0 0 1 * *")
  @Caching(evict = {
      @CacheEvict(value = "UnitedStatesFederalHolidayByIdCache", allEntries = true),
      @CacheEvict(value = "UnitedStatesFederalHolidayByHolidayYearCache", allEntries = true),
      @CacheEvict(value = "UnitedStatesFederalHolidayByHolidayNameCache", allEntries = true),
  })
  public HolidayResponse fetchData() throws InterruptedException {
    var urlString = "https://www.opm.gov/policy-data-oversight/pay-leave/federal-holidays";
    logger.info("Fetching Data for united states at: " + urlString);

    var response = new HolidayResponse();

    try {
      Document document = Jsoup.connect(urlString).get();

      var tables = document.select("table.HolidayTable");
      tables.select("span").remove();

      var holidays = new ArrayList<Holiday>();

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

              var holiday = new Holiday();
              holiday.setYear(Integer.valueOf(year));
              holiday.setDate(date);
              holiday.setName(cells.last().html());
              return holiday;
            })
            .collect(Collectors.toList());
        holidays.addAll(rows);
      });

      logger.info("Sending message with " + holidays.size() + " holidays to add.");
      rabbitTemplate.convertAndSend(topicExchangeName, routingKey, holidays);
      unitedStatesHolidayReceiver.getLatch().await(10000, TimeUnit.MILLISECONDS);

      response.setHolidays(holidays);

    } catch (IOException e) {
      logger.error("An error occurred trying to fetch data for: " + urlString);
      e.printStackTrace();
      response.setMessage("An error occurred trying to fetch data for: " + urlString);
    }
    return response;
  }

}
