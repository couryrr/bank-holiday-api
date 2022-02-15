package com.dappercloud.bankholiday.unitedstatesholiday;

import com.dappercloud.bankholiday.common.Holiday;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class UnitedStatesHolidayReceiver {
  Logger logger = LoggerFactory.getLogger(UnitedStatesHolidayReceiver.class);

  @Autowired
  UnitedStatesHolidayRepo repo;

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(List<Holiday> message) {
    logger.info("Received message with " + message.size() + " holiday(s) to add.");
    var entities = new ArrayList<UnitedStatesHolidayEntity>();
    message.stream().forEach(it -> {
      String hash = null;
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Compute message digest of the input
        var input = it.getName() + it.getDate();
        var bytes = md.digest(input.getBytes());
        hash = DatatypeConverter.printHexBinary(bytes);
      } catch (NoSuchAlgorithmException e){
        logger.error(e.getMessage());
      }

      try {
        logger.info(it.getName() + " " + it.getDate());
        var entity = new UnitedStatesHolidayEntity();
        entity.setId(it.getId());
        entity.setHolidayName(it.getName());
        entity.setHolidayDate(it.getDate());
        entity.setHolidayYear(it.getYear());
        entity.setHash(hash);
        repo.save(entity);
      } catch (DataIntegrityViolationException e){
        logger.error(e.getMessage());
      }
    });


    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}
