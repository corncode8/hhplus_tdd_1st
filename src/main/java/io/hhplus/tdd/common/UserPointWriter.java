package io.hhplus.tdd.common;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointWriter {

    private final UserPointTable userPointTable;

    public UserPoint modify(Long userId, Long amount) throws InterruptedException{
        return userPointTable.insertOrUpdate(userId, amount);
    }
}
