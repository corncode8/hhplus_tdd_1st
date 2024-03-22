package io.hhplus.tdd.common;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserHistoryReader {

    private final PointHistoryTable pointHistoryTable;

    public List<PointHistory> readList(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
