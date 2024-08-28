package com.transportbooker.rizla.services;

import com.transportbooker.rizla.dto.request.VehicleBookingRequestDTO;
import com.transportbooker.rizla.exceptions.NotFoundHttpException;
import com.transportbooker.rizla.models.*;
import com.transportbooker.rizla.repository.UserRepository;
import com.transportbooker.rizla.repository.VehicleBookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VehicleBookingService {

    private VehicleBookingRepository vehicleBookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public VehicleBooking createVehicleBooking(CustomUser customUser, Vehicle vehicle, VehicleBookingRequestDTO vehicleBookingRequestDTO) throws NotFoundHttpException {
        VehicleBookingStartAndEndTimeHolder startAndEndTimeHolder = getTImeUnitTimeFromRequest(vehicleBookingRequestDTO.getTimeSlot());

        VehicleBooking vehicleBooking = VehicleBooking.builder()
                .vehicle(vehicle)
                .bookingRequestTime(LocalDateTime.now())
                .bookingStartTime(startAndEndTimeHolder.getBookingStartTime())
                .bookingEndTime(startAndEndTimeHolder.getBookingEndTime())
                .passenger(customUser)
                .confirmed(false)
                .build();

        VehicleBooking vehicleBookingSaved = vehicleBookingRepository.save(vehicleBooking);

        return vehicleBookingSaved;
    }

    public boolean doesVehicleBookingExist(Vehicle vehicle, LocalDateTime bookingStartTime) throws NotFoundHttpException {
        Optional<VehicleBooking> vehicleBooking = vehicleBookingRepository.findByVehicle_IdAndBookingStartTime(vehicle.getId(),bookingStartTime);
        return vehicleBooking.isPresent();
    }

    public VehicleBookingStartAndEndTimeHolder getTImeUnitTimeFromRequest(TimeSlot timeSlot) {
        Calendar calendar = Calendar.getInstance();

        Date dateToday = calendar.getTime();
        calendar.setTime(dateToday);

        LocalDateTime bookingStartTime;
        LocalDateTime bookingEndTime;

        switch (timeSlot) {
            case HOUR_1 : //set 7:00AM TO 7:59AM
                bookingStartTime = getCustomTime(calendar,6,0,0);
                bookingEndTime = getCustomTime(calendar,6,59,59);
                break;
            case HOUR_2: //set 8:00:00AM TO 8:59:59AM
                bookingStartTime = getCustomTime(calendar,7,0,0);
                bookingEndTime = getCustomTime(calendar,7,59,59);
                break;
            case HOUR_3 : //set 9:00:00AM TO 9:59:59AM
                bookingStartTime = getCustomTime(calendar,9,0,0);
                bookingEndTime = getCustomTime(calendar,9,59,59);
                break;
                case HOUR_4 : //set 10:00:00AM TO 10:59:59AM
                    bookingStartTime = getCustomTime(calendar,10,0,0);
                    bookingEndTime = getCustomTime(calendar,10,59,59);
                    break;
                    case HOUR_5 : //set 11:00:00AM TO 11:59:59AM
                        bookingStartTime = getCustomTime(calendar,11,0,0);
                        bookingEndTime = getCustomTime(calendar,11,59,59);
                        break;
                        case HOUR_6 : //set 12:00:00PM TO 12:59:59PM
                            bookingStartTime = getCustomTime(calendar,12,0,0);
                            bookingEndTime = getCustomTime(calendar,12,59,59);
                            break;
                            case HOUR_7 : //set 1:00:00PM TO 1:59:59PM
                                bookingStartTime = getCustomTime(calendar,13,0,0);
                                bookingEndTime = getCustomTime(calendar,13,59,59);
                                break;
                                case HOUR_8 : //set 2:00:00PM TO 2:59:59PM
                                    bookingStartTime = getCustomTime(calendar,14,0,0);
                                    bookingEndTime = getCustomTime(calendar,14,59,59);
                                    break;
            default: //default time is 7am to 7:59am
                bookingStartTime = getCustomTime(calendar,6,0,0);
                bookingEndTime = getCustomTime(calendar,6,59,59);
                break;
        }


        return VehicleBookingStartAndEndTimeHolder.builder()
                .bookingStartTime(bookingStartTime)
                .bookingEndTime(bookingEndTime)
                .build();
    }

    private LocalDateTime getCustomTime(Calendar calendar, int hourOfDay, int minute, int second) {
        calendar.add(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.add(Calendar.MINUTE, minute);
        calendar.add(Calendar.SECOND, second);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        //return localDateTime.withHour(hourOfDay).withMinute(minute).withSecond(second);
        return localDateTime;
    }


}
