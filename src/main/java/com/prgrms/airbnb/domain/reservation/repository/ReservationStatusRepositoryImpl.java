package com.prgrms.airbnb.domain.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static com.prgrms.airbnb.domain.reservation.entity.QReservation.reservation;
import static com.prgrms.airbnb.domain.reservation.entity.ReservationStatus.*;

public class ReservationStatusRepositoryImpl implements ReservationStatusRepository {
    private JPAQueryFactory jpaQueryFactory;

    public ReservationStatusRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void updateReservationStatus() {
        //이용 후 다음 날부터 리뷰를 남길 수 있다.
        jpaQueryFactory.update(reservation)
                .set(reservation.reservationStatus, WAIT_REVIEW)
                .where(reservation.endDate.after(LocalDate.now()), reservation.reservationStatus.eq(ACCEPTED))
                .execute();
        //리뷰는 14일 이내에 남길 수 있다.
        jpaQueryFactory.update(reservation)
                .set(reservation.reservationStatus, COMPLETE)
                .where(reservation.endDate.after(LocalDate.now().minusDays(14)), reservation.reservationStatus.eq(WAIT_REVIEW))
                .execute();
    }
}
