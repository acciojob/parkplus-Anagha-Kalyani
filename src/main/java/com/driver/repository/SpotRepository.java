package com.driver.repository;

import com.driver.model.Spot;
import com.driver.model.SpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Integer>{
    List<Spot> findByParkingLotIdAndSpotTypeGreaterThanEqual(int parkingLotId, SpotType spotType);

}
