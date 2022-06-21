package com.prgrms.airbnb.reservation.service;

import com.prgrms.airbnb.reservation.ReservationConverter;
import com.prgrms.airbnb.reservation.domain.Reservation;
import com.prgrms.airbnb.reservation.domain.ReservationStatus;
import com.prgrms.airbnb.reservation.dto.ReservationDetailResponseForHost;
import com.prgrms.airbnb.reservation.repository.ReservationRepository;
import com.prgrms.airbnb.room.domain.Room;
import com.prgrms.airbnb.room.repository.RoomRepository;
import com.prgrms.airbnb.user.domain.User;
import com.prgrms.airbnb.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HostReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public HostReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }


    public ReservationDetailResponseForHost findDetailById(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        User guest = userRepository.findById(reservation.getUserId()).orElseThrow(IllegalArgumentException::new);
        Room room = roomRepository.findById(reservation.getRoomId()).orElseThrow(IllegalArgumentException::new);
        return ReservationConverter.ofDetailForHost(reservation, guest, room);
    }

    @Transactional
    public void cancel(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        reservation.cancelReservation(ReservationStatus.ACCEPTED_AFTER_CANCELLED);
    }
}
