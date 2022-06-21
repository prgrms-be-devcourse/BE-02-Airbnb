package com.prgrms.airbnb.reservation.service;

import com.prgrms.airbnb.reservation.ReservationConverter;
import com.prgrms.airbnb.reservation.domain.Reservation;
import com.prgrms.airbnb.reservation.dto.ReservationSummaryResponse;
import com.prgrms.airbnb.reservation.repository.ReservationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Slice<ReservationSummaryResponse> reservationList(Long userId, Pageable pageable) {
        Slice<Reservation> reservationList = reservationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return new SliceImpl<>(
                reservationList.getContent().stream()
                        .map(
                                ReservationConverter::ofSummary).collect(Collectors.toList()),
                pageable,
                reservationList.hasNext());
    }
}
