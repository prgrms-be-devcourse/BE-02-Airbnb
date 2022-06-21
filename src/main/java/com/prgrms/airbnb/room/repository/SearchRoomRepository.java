package com.prgrms.airbnb.room.repository;

import com.prgrms.airbnb.room.domain.RoomType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;


@RequiredArgsConstructor
public class SearchRoomRepository implements RoomCustomRepository {

    private final JPAQueryFactory queryFactory;

//  @Override
//  public Slice<RoomSummaryResponse> findAllRoomPageable(SearchRoomRequest searchRoomRequest,
//      Pageable pageable) {
//    JPAQuery<RoomSummaryResponse> roomJPAQuery = queryFactory
//        .select(new QRoomSummaryResponse(
//            room.id,
//            room.address,
//            room.charge,
//            room.name,
//            room.roomType,
//            room.images
//        ))
//        .from(room)
//        .leftJoin(room.images, roomImage)
//        .where(
//            roomTypeEq(searchRoomRequest.getRoomType()),
////            minCharge(searchRoomRequest.getMinCharge()),
////            maxCharge(searchRoomRequest.getMaxCharge()),
//            maxGuestNum(searchRoomRequest.getRoomInfo().getMaxGuest()),
//            minRoomCount(searchRoomRequest.getRoomInfo().getRoomCount()),
//            minBedCount(searchRoomRequest.getRoomInfo().getBedCount()),
//            minRating(searchRoomRequest.getRating())
//        )
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize() + 1);
//
//    for (Sort.Order o : pageable.getSort()) {
//      PathBuilder pathBuilder = new PathBuilder(room.getType(), room.getMetadata());
//      roomJPAQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
//          pathBuilder.get(o.getProperty())));
//    }
//    List<RoomSummaryResponse> roomList = roomJPAQuery.fetch();
//    boolean hasNext = false;
//
//    if (roomList.size() > pageable.getPageSize()) {
//      roomList.remove(pageable.getPageSize());
//      hasNext = true;
//    }
//
//    return new SliceImpl<>(roomList, pageable, hasNext);
/*    return null;
}*/

    private BooleanExpression roomTypeEq(RoomType roomType) {
        return ObjectUtils.isEmpty(roomType) ? null : room.roomType.eq(roomType);
    }

//  @Override
//  public Slice<RoomSummaryResponse> findAll(SearchRoomRequest searchRoomRequest,
//      Pageable pageable) {
//    JPAQuery<RoomSummaryResponse> roomJPAQuery = queryFactory
//        .select(new QRoomSummaryResponse(
//            room.id,
//            room.address,
//            room.charge,
//            room.name,
//            room.roomType,
//            room.images
//        ))
//        .from(room)
//        .leftJoin(room.images, roomImage)
//        .fetchJoin()
//        .where(
//            roomTypeEq(searchRoomRequest.getRoomType()),
//            minCharge(searchRoomRequest.getMinCharge()),
//            maxCharge(searchRoomRequest.getMaxCharge()),
//            maxGuestNum(searchRoomRequest.getRoomInfo().getMaxGuest()),
//            minRoomCount(searchRoomRequest.getRoomInfo().getRoomCount()),
//            minBedCount(searchRoomRequest.getRoomInfo().getBedCount()),
//            minRating(searchRoomRequest.getRating())
//        )
//        .offset(pageable.getOffset())
//        .limit(pageable.getPageSize() + 1);
//
//    for (Sort.Order o : pageable.getSort()) {
//      PathBuilder pathBuilder = new PathBuilder(room.getType(), room.getMetadata());
//      roomJPAQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
//          pathBuilder.get(o.getProperty())));
//    }
//    List<RoomSummaryResponse> roomList = roomJPAQuery.fetch();
//    boolean hasNext = false;
//
//    if (roomList.size() > pageable.getPageSize()) {
//      roomList.remove(pageable.getPageSize());
//      hasNext = true;
//    }
//
//    return new SliceImpl<>(roomList, pageable, hasNext);
//  }
//
//  private BooleanExpression roomTypeEq(RoomType roomType) {
//    return ObjectUtils.isEmpty(roomType) ? null : room.roomType.eq(roomType);
//  }
//
//  private BooleanExpression minCharge(Money minCharge) {
//    return ObjectUtils.isEmpty(minCharge) ? null : room.charge.value.goe(minCharge.getValue());
//  }
//
//  private BooleanExpression maxCharge(Money maxCharge) {
//    return ObjectUtils.isEmpty(maxCharge) ? null : room.charge.value.loe(maxCharge.getValue());
//  }


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
