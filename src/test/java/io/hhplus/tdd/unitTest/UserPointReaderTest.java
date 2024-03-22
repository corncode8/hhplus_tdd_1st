package io.hhplus.tdd.unitTest;

import io.hhplus.tdd.common.UserPointReader;
import io.hhplus.tdd.database.UserPointTable;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointReaderTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private UserPointReader userPointReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("read 테스트")
    @Test
    void readTest() throws InterruptedException{
        //given
        Long userId = 1L;
        UserPoint expectedUser = new UserPoint(userId, 500L, System.currentTimeMillis());

        when(userPointTable.selectById(eq(userId))).thenReturn(expectedUser);

        //when
        UserPoint userPoint = userPointReader.read(userId);

        //then
        assertNotNull(userPoint);
        assertEquals(expectedUser.point(), userPoint.point());
    }

    @DisplayName("read 실패 테스트")
    @Test
    void readFailTest() throws InterruptedException{
        //given
        Long userId = null;
        when(userPointReader.read(eq(userId))).thenThrow(new InterruptedException("Exception"));

        //when & then
        assertThrows(InterruptedException.class, () -> userPointReader.read(userId));
    }
}
