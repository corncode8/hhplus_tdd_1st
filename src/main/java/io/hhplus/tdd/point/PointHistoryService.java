package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private PointHistoryTable pointHistoryTable;

    // 포인트 내역 조회
    public List<PointHistory> getPointHistory(Long userId) {
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(userId);

        if (userId == null) {
            throw new IllegalArgumentException("ID를 입력해주세요");
        }
        return pointHistories;
    }
}
