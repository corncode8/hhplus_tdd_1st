package io.hhplus.tdd.common;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointReader {

    private final UserPointTable userPointTable;

    public UserPoint read(Long userId) throws InterruptedException{
        return userPointTable.selectById(userId);
    }

}
