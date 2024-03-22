package io.hhplus.tdd.common;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserHistoryReader {

    private final PointHistoryTable pointHistoryTable;

    public PointHistory save(Long id, Long amount, TransactionType transactionType, Long updateMillis) throws InterruptedException{
        return pointHistoryTable.insert(id, amount, transactionType, updateMillis);
    }

    public List<PointHistory> readList(Long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}
