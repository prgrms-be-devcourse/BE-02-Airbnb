package com.prgrms.airbnb.domain.reservation.repository;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.prgrms.airbnb.domain.room.entity.QRoom;
import com.prgrms.airbnb.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import static com.prgrms.airbnb.domain.reservation.entity.QReservation.reservation;
import static com.prgrms.airbnb.domain.reservation.entity.ReservationStatus.*;
import static com.prgrms.airbnb.domain.room.entity.QRoom.room;
import static com.prgrms.airbnb.domain.user.entity.QUser.user;

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

    @Override
    public Slice<Reservation> findAllReservationByHostId(Long hostId, Pageable pageable) {

        List<Reservation> fetch = jpaQueryFactory.selectFrom(reservation)
            .join(room).on(reservation.roomId.eq(room.id)).fetchJoin()
            .where(room.userId.eq(hostId))
            .fetch();
        boolean hasNext = false;
        if (fetch.size() > pageable.getPageSize()) {
            fetch.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(fetch, pageable, hasNext);
    }
}
