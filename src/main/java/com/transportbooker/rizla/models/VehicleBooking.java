package com.transportbooker.rizla.models;

import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    @ToString.Exclude
    private CustomUser passenger;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @ToString.Exclude
    private CustomUser driver;

    private boolean confirmed;
}
