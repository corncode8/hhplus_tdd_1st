package io.hhplus.tdd.service;

import io.hhplus.tdd.common.LockHandler;
import io.hhplus.tdd.common.UserPointManager;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointServiceTest {

    @Mock
    private LockHandler lockHandler;
    @Mock
    private UserPointManager userPointManager;
    @InjectMocks
    private UserPointService service;

    @DisplayName("포인트 조회 테스트")
    @Test
    void 포인트_조회() throws InterruptedException{
        //given
        Long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 500L, System.currentTimeMillis());

        when(userPointManager.read(userId)).thenReturn(userPoint);

        //when
        UserPoint result = service.read(userId);

        //then
        assertNotNull(result);
        assertEquals(userPoint.id(), result.id());
    }

    @DisplayName("포인트 충전 테스트")
    @Test
    void 포인트_충전() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 5000L;
        UserPoint userPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        when(userPointManager.charge(userId, amount)).thenReturn(userPoint);

        //when
        when(lockHandler.executeOnLock(eq(userId), any())).thenAnswer(invocation -> {
            Callable<UserPoint> callable = invocation.getArgument(1);
            return callable.call(); // 람다 표현식 실행
        });

        UserPoint result = service.charge(userId, amount);

        //then

        assertEquals(userPoint, result);
        // userPointManager.charge가 정확히 한 번 호출되었는지 검증
        verify(userPointManager).charge(userId, amount);
    }

    @DisplayName("포인트 사용 테스트")
    @Test
    void 포인트_사용() throws InterruptedException{
        //given

        Long userId = 1L;
        Long amount = 50L;
        UserPoint expectedUserPoint = new UserPoint(userId, amount, System.currentTimeMillis());

        when(userPointManager.use(userId, amount)).thenReturn(expectedUserPoint);

        when(lockHandler.executeOnLock(eq(userId), any())).thenAnswer(invocation -> {
            Callable<UserPoint> callable = invocation.getArgument(1);
            return callable.call(); // 람다 표현식 실행
        });
        //when
        UserPoint result = service.use(userId, amount);

        //then

        assertEquals(expectedUserPoint, result);
        verify(userPointManager).use(userId, amount);
    }

    @DisplayName("포인트 내역 조회 테스트")
    @Test
    void 포인트_내역_조회() {
        //given
        Long userId = 1L;

        List<PointHistory> list = List.of(
                new PointHistory(5L, userId, TransactionType.CHARGE, 500L, System.currentTimeMillis()),
                new PointHistory(9L, userId, TransactionType.CHARGE, 500L + 500, System.currentTimeMillis()),
                new PointHistory(15L, userId, TransactionType.USE, 500L + 500L - 200L, System.currentTimeMillis())
        );

        when(userPointManager.readList(userId)).thenReturn(list);

        //when
        List<PointHistory> result = service.readList(userId);

        //then
        assertNotNull(result);
        assertEquals(result.size(), list.size());
    }

}
