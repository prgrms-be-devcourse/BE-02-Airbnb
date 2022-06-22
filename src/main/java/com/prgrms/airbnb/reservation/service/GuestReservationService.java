package com.prgrms.airbnb.reservation.service;

import com.prgrms.airbnb.reservation.ReservationConverter;
import com.prgrms.airbnb.reservation.domain.Reservation;
import com.prgrms.airbnb.reservation.domain.ReservationStatus;
import com.prgrms.airbnb.reservation.dto.CreateReservationRequest;
import com.prgrms.airbnb.reservation.dto.ReservationDetailResponseForGuest;
import com.prgrms.airbnb.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.room.repository.RoomRepository;
import com.prgrms.airbnb.user.domain.User;
import com.prgrms.airbnb.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GuestReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public GuestReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public ReservationDetailResponseForGuest findDetailById(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        Room room = roomRepository.findById(reservation.getRoomId()).orElseThrow(IllegalArgumentException::new);
        User host = userRepository.findById(room.getUserId()).orElseThrow(IllegalArgumentException::new);
        return ReservationConverter.ofDetailForGuest(reservation, host, room);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ReservationDetailResponseForGuest save(CreateReservationRequest createReservationRequest) {
        Room room = roomRepository.findById(createReservationRequest.getRoomId()).orElseThrow(IllegalArgumentException::new);
        User host = userRepository.findById(room.getUserId()).orElseThrow(IllegalArgumentException::new);
        String reservationId = reservationRepository.createReservationId();
        Reservation reservation = ReservationConverter.toReservation(reservationId, createReservationRequest);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationConverter.ofDetailForGuest(savedReservation, host, room);
    }

    @Transactional
    public void cancel(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        reservation.cancelReservation(ReservationStatus.GUEST_CANCELLED);
    }
}
