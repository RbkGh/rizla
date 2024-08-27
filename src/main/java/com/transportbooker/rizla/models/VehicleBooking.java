package com.transportbooker.rizla.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookingRequestTime;

    private LocalDateTime bookingConfirmedTime;

    private LocalDateTime bookingStartTime;

    private LocalDateTime bookingEndTime;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private CustomUser passenger;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private CustomUser driver;

    private boolean confirmed;
}
