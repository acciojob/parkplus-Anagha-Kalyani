package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot.setSpotList(new ArrayList<>());
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        //ParkingLot parkingLot = parkingLotOptional.get();
        SpotType spotType = getSpotType(numberOfWheels);
        Spot spot = new Spot();
        spot.setParkingLot(parkingLot);
        //spot.setNumberOfWheels(numberOfWheels);
        spot.setPricePerHour(pricePerHour);
        spot.setSpotType(spotType);
        spot.setOccupied(false);
        spot.setReservationList(new ArrayList<>());
        parkingLot.getSpotList().add(spot);
        ParkingLot savedPL=parkingLotRepository1.save(parkingLot);
        return savedPL.getSpotList().get(savedPL.getSpotList().size()-1);

    }

    private SpotType getSpotType(Integer numberOfWheels) {
        if (numberOfWheels <= 2) {
            return SpotType.TWO_WHEELER;
        } else if (numberOfWheels <= 4) {
            return SpotType.FOUR_WHEELER;
        } else {
            return SpotType.OTHERS;
        }
    }


    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Optional<Spot> spotOptional = spotRepository1.findById(spotId);
        if (spotOptional.isPresent()) {
            Spot spot = spotOptional.get();
            if (spot.getParkingLot().getId() == parkingLotId) {
                spot.setPricePerHour(pricePerHour);
                return spotRepository1.save(spot);
            }
        }
        return null;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
