package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpotRepository spotRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Cannot make reservation"));

        // Find the parking lot
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() -> new Exception("Cannot make reservation"));

        // Find all spots in the parking lot that match the vehicle requirements
        List<Spot> availableSpots = spotRepository.findByParkingLotIdAndSpotTypeGreaterThanEqual(parkingLotId, getSpotType(numberOfWheels));

        // Find the spot with the minimum price per hour
        Optional<Spot> optionalSpot = availableSpots.stream()
                .min(Comparator.comparingInt(Spot::getPricePerHour));

        // If no spot is available, throw an exception
        Spot reservedSpot = optionalSpot.orElseThrow(() -> new Exception("Cannot make reservation"));

        // Create a new reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSpot(reservedSpot);
        reservation.setNumberOfHours(timeInHours);
        reservedSpot.setOccupied(true);
        // Calculate total price
        int totalPrice = timeInHours * reservedSpot.getPricePerHour();
        reservation.setTotalPrice(totalPrice);

        // Save the reservation and return the saved reservation
        return reservationRepository.save(reservation);
    }


    // Helper method to determine the spot type based on the number of wheels
    private SpotType getSpotType(Integer numberOfWheels) {
        if (numberOfWheels <= 2) {
            return SpotType.TWO_WHEELER;
        } else if (numberOfWheels <= 4) {
            return SpotType.FOUR_WHEELER;
        } else {
            return SpotType.OTHERS;
        }
    }
}
