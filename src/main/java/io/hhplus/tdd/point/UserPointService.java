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
        if (userId == null) {
            throw new IllegalArgumentException("ID를 입력해주세요");
        }
        return userPointTable.selectById(userId);
    }

    // 포인트 충전
    public UserPoint chargePoint(Long userId, Long amount) throws InterruptedException {
        if (userId == null || amount == null) {
            throw new IllegalArgumentException("ID 및 충전포인트를 입력해주세요.");
        }
        if (amount < 1000) {
            throw new IllegalArgumentException("충전포인트는  1000원이상 충전할 수 있습니다.");
        }
        UserPoint userPoint = userPointTable.selectById(userId);
        userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());
        pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, System.currentTimeMillis());

        return userPoint;
    }

    // 포인트 사용
    public UserPoint usePoint(Long userId, Long amount) throws InterruptedException {
        if (userId == null || amount == null) {
            throw new IllegalArgumentException("ID 및 충전포인트를 입력해주세요.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("충전포인트는 1원 이상부터 사용할 수 있습니다.");
        }
        UserPoint userPoint = userPointTable.selectById(userId);
        if (userPoint.point() < amount) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }
        amount -= userPoint.point();
        return userPointTable.insertOrUpdate(userId, amount);
    }


}
