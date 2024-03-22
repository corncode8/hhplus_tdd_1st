package io.hhplus.tdd.common;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPointManager {

    private final UserPointReader userPointReader;
    private final UserPointWriter userPointWriter;
    private final UserHistoryReader userHistoryReader;

    // 포인트 조회
    public UserPoint read(Long userId) throws InterruptedException{
        return userPointReader.read(userId);
    }

    // 포인트 충전
    public UserPoint charge(Long userId, Long amount) throws InterruptedException{
        if (amount < 0) {
            throw new RuntimeException("0원이상 충전할 수 있습니다.");
        }

        UserPoint userPoint = userPointReader.read(userId);

        userHistoryReader.save(userId, userPoint.point() + amount, TransactionType.CHARGE, System.currentTimeMillis());
        return userPointWriter.modify(userId, userPoint.point() + amount);
    }

    // 포인트 사용
    public UserPoint use(Long userId, Long amount) throws InterruptedException {
        UserPoint userPoint = userPointReader.read(userId);
        if (userPoint.point() - amount < 0) {
            throw  new RuntimeException("포인트가 부족합니다.");
        }
        userHistoryReader.save(userId, userPoint.point() - amount, TransactionType.USE, System.currentTimeMillis());
        return userPointWriter.modify(userId, userPoint.point() - amount);
    }

    // 포인트 내역 조회
    public List<PointHistory> readList(Long userId) {
        return userHistoryReader.readList(userId);
    }
}
