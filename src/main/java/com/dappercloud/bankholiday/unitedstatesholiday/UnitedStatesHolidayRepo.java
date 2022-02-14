package com.dappercloud.bankholiday.unitedstatesholiday;


import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UnitedStatesHolidayRepo extends
    CrudRepository<UnitedStatesHolidayEntity, Integer> {

  Optional<List<UnitedStatesHolidayEntity>> findByHolidayYear(Integer year);

  Optional<List<UnitedStatesHolidayEntity>> findByHolidayName(String name);

  Optional<UnitedStatesHolidayEntity> findByHash(String hash);
}
