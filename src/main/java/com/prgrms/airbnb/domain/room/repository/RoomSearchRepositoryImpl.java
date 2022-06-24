package com.prgrms.airbnb.domain.room.repository;

import static com.prgrms.airbnb.domain.room.entity.QRoom.room;
import static com.prgrms.airbnb.domain.room.entity.QRoomImage.roomImage;

import com.prgrms.airbnb.domain.room.dto.QRoomSummaryResponse;
import com.prgrms.airbnb.domain.room.entity.RoomType;
import com.prgrms.airbnb.domain.room.dto.RoomSummaryResponse;
import com.prgrms.airbnb.domain.room.dto.SearchRoomRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

public class RoomSearchRepositoryImpl implements RoomSearchRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public RoomSearchRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Slice<RoomSummaryResponse> searchRoom(SearchRoomRequest searchRoomRequest,
      Pageable pageable) {

    JPAQuery<RoomSummaryResponse> roomJPAQuery = jpaQueryFactory
        .select(new QRoomSummaryResponse(
            room.id,
            room.address,
            room.charge,
            room.name,
            room.roomType,
            room.images
        ))
        .from(room)
        .leftJoin(room.images, roomImage)
        .where(
            keywordListContains(searchRoomRequest.getKeyword()),
            roomTypeEq(searchRoomRequest.getRoomType()),
            minCharge(searchRoomRequest.getMinCharge()),
            maxCharge(searchRoomRequest.getMaxCharge()),
            maxGuestNum(searchRoomRequest.getRoomInfo().getMaxGuest()),
            minRoomCount(searchRoomRequest.getRoomInfo().getRoomCount()),
            minBedCount(searchRoomRequest.getRoomInfo().getBedCount()),
            minRating(searchRoomRequest.getRating())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1);

    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder<>(room.getType(), room.getMetadata());

      roomJPAQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }

    List<RoomSummaryResponse> roomList = roomJPAQuery.fetch();
    boolean hasNext = false;

    if (roomList.size() > pageable.getPageSize()) {
      roomList.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(roomList, pageable, hasNext);
  }

  private BooleanExpression roomTypeEq(RoomType roomType) {
    return ObjectUtils.isEmpty(roomType) ? null : room.roomType.eq(roomType);
  }

  private BooleanExpression minCharge(Integer minCharge) {
    return ObjectUtils.isEmpty(minCharge) ? null : room.charge.goe(minCharge);
  }

  private BooleanExpression maxCharge(Integer maxCharge) {
    return ObjectUtils.isEmpty(maxCharge) ? null : room.charge.loe(maxCharge);
  }

  private BooleanExpression maxGuestNum(Integer maxGuest) {
    return ObjectUtils.isEmpty(maxGuest) ? null : room.roomInfo.maxGuest.loe(maxGuest);
  }

  private BooleanExpression minRoomCount(Integer roomCount) {
    return ObjectUtils.isEmpty(roomCount) ? null : room.roomInfo.roomCount.goe(roomCount);
  }

  private BooleanExpression minBedCount(Integer bedCount) {
    return ObjectUtils.isEmpty(bedCount) ? null : room.roomInfo.bedCount.goe(bedCount);
  }

  private BooleanExpression minRating(Double rating) {
    //TODO: reviewInfo null 일 수도 있음 예외처리 필요
    return ObjectUtils.isEmpty(rating) ? null : room.reviewInfo.reviewRating.goe(rating);
  }

  private BooleanBuilder keywordListContains(String keyword) {
    if (ObjectUtils.isEmpty(keyword)) {
      return null;
    }
    BooleanBuilder builder = new BooleanBuilder();
    String[] splitedKeyword = keyword.split(" ");
    for (String value : splitedKeyword) {
      builder.and(room.name.contains(value));
    }
    return builder;
  }


}
