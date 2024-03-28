package io.hhplus.tdd.service;

import io.hhplus.tdd.common.LockHandler;
import io.hhplus.tdd.common.UserPointManager;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserPointService {

    private final LockHandler lockHandler;
    private final UserPointManager userPointManager;

    // 포인트 조회
    public UserPoint read(Long userId) throws InterruptedException {
        return userPointManager.read(userId);
    }

    // 포인트 충전
    public UserPoint charge(Long userId, Long amount) throws InterruptedException {
        return lockHandler.executeOnLock(userId, () -> {
            try {
                return userPointManager.charge(userId, amount);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 포인트 사용
    public UserPoint use(Long userId, Long amount) throws InterruptedException{
        return lockHandler.executeOnLock(userId, () -> {
            try {
                return userPointManager.use(userId, amount);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 포인트 내역 조회
    public List<PointHistory> readList(Long userId) {
        return userPointManager.readList(userId);
    }


}
