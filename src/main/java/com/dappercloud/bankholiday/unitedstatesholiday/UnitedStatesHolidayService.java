package com.dappercloud.bankholiday.unitedstatesholiday;

import com.dappercloud.bankholiday.common.Holiday;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UnitedStatesHolidayService {
  public List<Holiday> map(List<UnitedStatesHolidayEntity> unitedStatesHolidayEntities){
      return unitedStatesHolidayEntities.stream().map(this::map).collect(Collectors.toList());
  }

  public Holiday map(UnitedStatesHolidayEntity unitedStatesHolidayEntity){
      var holiday = new Holiday();
      holiday.setId(unitedStatesHolidayEntity.getId());
      holiday.setName(unitedStatesHolidayEntity.getHolidayName());
      holiday.setDate(unitedStatesHolidayEntity.getHolidayDate());
      holiday.setYear(unitedStatesHolidayEntity.getHolidayYear());
      return holiday;

  }
}
