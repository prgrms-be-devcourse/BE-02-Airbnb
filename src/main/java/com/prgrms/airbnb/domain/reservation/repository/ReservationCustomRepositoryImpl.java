package com.prgrms.airbnb.domain.reservation.repository;

import static com.prgrms.airbnb.domain.reservation.entity.QReservation.reservation;
import static com.prgrms.airbnb.domain.reservation.entity.ReservationStatus.ACCEPTED;
import static com.prgrms.airbnb.domain.reservation.entity.ReservationStatus.COMPLETE;
import static com.prgrms.airbnb.domain.reservation.entity.ReservationStatus.WAIT_REVIEW;
import static com.prgrms.airbnb.domain.room.entity.QRoom.room;

import com.prgrms.airbnb.domain.reservation.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

  private JPAQueryFactory jpaQueryFactory;

  public ReservationCustomRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public void updateReservationStatus() {
    //이용 후 다음 날부터 리뷰를 남길 수 있다.
    jpaQueryFactory.update(reservation)
        .set(reservation.reservationStatus, WAIT_REVIEW)
        .where(reservation.endDate.after(LocalDate.now()),
            reservation.reservationStatus.eq(ACCEPTED))
        .execute();
    //리뷰는 14일 이내에 남길 수 있다.
    jpaQueryFactory.update(reservation)
        .set(reservation.reservationStatus, COMPLETE)
        .where(reservation.endDate.after(LocalDate.now().minusDays(14)),
            reservation.reservationStatus.eq(WAIT_REVIEW))
        .execute();
  }

  @Override
  public Slice<Reservation> findAllReservationByHostId(Long hostId, Pageable pageable) {

    List<Reservation> reservationList = jpaQueryFactory.selectFrom(reservation)
        .join(room).on(reservation.roomId.eq(room.id)).fetchJoin()
        .where(room.userId.eq(hostId))
        .fetch();
    boolean hasNext = false;
    if (reservationList.size() > pageable.getPageSize()) {
      reservationList.remove(pageable.getPageSize());
      hasNext = true;
    }
    return new SliceImpl<>(reservationList, pageable, hasNext);
  }

  @Override
  public boolean existReservation(Long roomId, LocalDate startDate, LocalDate endDate) {

    Integer integer = jpaQueryFactory.selectOne().from(reservation)
        .where(reservation.roomId.eq(roomId),
            checkDate(startDate, endDate))
        .fetchFirst();

    return integer != null;
  }

  private BooleanExpression checkDate(LocalDate newStartDate, LocalDate newEndDate) {
    return (reservation.startDate.between(newStartDate,newEndDate).or(reservation.endDate.between(newStartDate, newEndDate)))
        .or(reservation.startDate.before(newStartDate).and(reservation.endDate.after(newEndDate)));
  }

}
