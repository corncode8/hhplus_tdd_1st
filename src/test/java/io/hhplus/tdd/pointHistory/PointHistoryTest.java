package io.hhplus.tdd.pointHistory;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PointHistoryTest {

    @Autowired
    PointHistoryTable pointHistoryTable;

    @Autowired
    PointHistoryService pointHistoryService;

    @Autowired
    UserPointService userPointService;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("포인트 입력 테스트")
    @Test
    void insertPoint() throws InterruptedException {
        //given
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());


        //when
        PointHistory pointHistory = pointHistoryTable.insert(userPoint.id(), userPoint.point(), TransactionType.CHARGE, userPoint.updateMillis());
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(pointHistory.userId());

        //then
        assertThat(pointHistories).isNotNull();
        assertThat(pointHistories.size()).isEqualTo(1);

    }

    @DisplayName("포인트 리스트 테스트")
    @Test
    void getPointList() throws InterruptedException{
        //given
        Long userId = 2L;
        Long point1 = 10000L;
        Long point2 = 22000L;

        userPointService.chargePoint(userId, point1);
        userPointService.chargePoint(userId, point2);

        //when
        List<PointHistory> pointHistory = pointHistoryService.getPointHistory(userId);

        //then
        assertThat(pointHistory).isNotNull();
        assertThat(pointHistory.size()).isEqualTo(2);

    }

}
