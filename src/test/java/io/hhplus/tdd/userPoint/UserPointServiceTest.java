package io.hhplus.tdd.userPoint;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class UserPointServiceTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    private UserPointService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserPointService(userPointTable, pointHistoryTable);
    }

    @DisplayName("포인트 조회 테스트 ")
    @Test
    void selectByIdTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long testPoint = 1000L;
        UserPoint stubUserPoint = new UserPoint(userId, testPoint, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(stubUserPoint);

        //when
        UserPoint result = service.getUserPoint(userId);

        //then
        assertNotNull(result);
        assertEquals(testPoint, result.point());
    }

    @DisplayName("포인트 충전 테스트 ")
    @Test
    void insertOrUpdateTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 1000L;
        UserPoint existUser = new UserPoint(userId, 1000L, System.currentTimeMillis());
        UserPoint updateUser = new UserPoint(userId, 2000L, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(existUser);

        // 충전 스텁 설정
        when(userPointTable.insertOrUpdate(userId, 2000L)).thenReturn(updateUser);

        PointHistory expectedHistory = new PointHistory(1L, userId, TransactionType.CHARGE, amount, System.currentTimeMillis());

        // insert 스텁 설정
        when(pointHistoryTable.insert(eq(updateUser.id()), eq(updateUser.point()), eq(TransactionType.CHARGE), anyLong())).thenReturn(expectedHistory);

        //when
        PointHistory result = service.chargePoint(userId, amount);

        //then
        assertNotNull(result);
        assertEquals(amount, result.amount());
    }

    @DisplayName("포인트 사용 테스트 ")
    @Test
    void usePointTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 1000L;
        UserPoint stubUserPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, amount)).thenReturn(stubUserPoint);
//        UserPoint result = service.chargePoint(userId, amount);

        //when
        UserPoint userPoint = service.usePoint(userId, 500L);

        //then
        assertNotNull(userPoint);
        assertEquals(500L, userPoint.point());
    }

}
