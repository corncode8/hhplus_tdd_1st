package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    // 포인트 조회
    public UserPoint getUserPoint(Long userId) throws InterruptedException {

        return userPointTable.selectById(userId);
    }

    // 포인트 충전
    public PointHistory chargePoint(Long userId, Long amount) throws InterruptedException {

        UserPoint userPoint = userPointTable.selectById(userId);
        UserPoint updateUser = userPointTable.insertOrUpdate(userPoint.id(), amount + userPoint.point());

        return pointHistoryTable.insert(updateUser.id(), updateUser.point(), TransactionType.CHARGE, System.currentTimeMillis());

    }

    // 포인트 사용
    public UserPoint usePoint(Long userId, Long amount) throws InterruptedException {

        UserPoint userPoint = userPointTable.selectById(userId);
        if (userPoint.point() < amount) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }

        return userPointTable.insertOrUpdate(userId, amount - userPoint.point());

    }


}
