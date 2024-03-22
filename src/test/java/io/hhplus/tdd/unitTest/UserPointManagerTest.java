package io.hhplus.tdd.unitTest;

import io.hhplus.tdd.common.UserHistoryReader;
import io.hhplus.tdd.common.UserPointManager;
import io.hhplus.tdd.common.UserPointReader;
import io.hhplus.tdd.common.UserPointWriter;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointManagerTest {

    @Mock
    private UserPointReader userPointReader;

    @Mock
    private UserPointWriter userPointWriter;

    @Mock
    private UserHistoryReader userHistoryReader;

    @InjectMocks
    private UserPointManager userPointManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("read 테스트")
    @Test
    void readTest() throws InterruptedException{
        //given
        Long userId = 5L;
        UserPoint user = new UserPoint(userId, 0L, System.currentTimeMillis());
        when(userPointReader.read(userId)).thenReturn(user);

        //when
        UserPoint userPoint = userPointManager.read(userId);

        //then
        assertNotNull(userPoint);
        assertEquals(5L, userPoint.id());
        assertEquals(0L, userPoint.point());

    }

    @DisplayName("테스트")
    @Test
    void chargeTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 500L;
        UserPoint existUser = new UserPoint(userId, 500L, System.currentTimeMillis());
        UserPoint updateUser = new UserPoint(userId, 1000L, System.currentTimeMillis());

        when(userPointReader.read(userId)).thenReturn(existUser);
        when(userPointWriter.modify(eq(userId), eq(existUser.point() + amount))).thenReturn(updateUser);

        //when
        UserPoint result = userPointManager.charge(userId, amount);

        //then
        assertNotNull(result);
        assertEquals(1000L, result.point());
    }

    @DisplayName("use 테스트")
    @Test
    void useTest() throws InterruptedException{
        //given
        Long userId = 2L;
        Long amount = 200L;
        UserPoint existUser = new UserPoint(userId, 1000L, System.currentTimeMillis());
        UserPoint updateUser = new UserPoint(userId, 800L, System.currentTimeMillis());

        when(userPointReader.read(userId)).thenReturn(existUser);
        when(userPointWriter.modify(eq(userId), eq(existUser.point() - amount))).thenReturn(updateUser);

        //when
        UserPoint result = userPointManager.use(userId, amount);

        //then
        assertNotNull(result);
        assertEquals(800L, result.point());
    }

    @DisplayName("readList 테스트")
    @Test
    void readListTest() {
        //given
        Long userId = 1L;

        List<PointHistory> list = List.of(
                new PointHistory(5L, userId, TransactionType.CHARGE, 500L, System.currentTimeMillis()),
                new PointHistory(9L, userId, TransactionType.CHARGE, 500L + 500, System.currentTimeMillis()),
                new PointHistory(15L, userId, TransactionType.USE, 500L + 500L - 200L, System.currentTimeMillis())
        );

        when(userPointManager.readList(eq(userId))).thenReturn(list);

        //when
        List<PointHistory> result = userPointManager.readList(userId);

        //then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(r -> r.userId().equals(userId)));
    }
}
