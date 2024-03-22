package io.hhplus.tdd.unitTest;

import io.hhplus.tdd.common.UserHistoryReader;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserHistoryReaderTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private UserHistoryReader userHistoryReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("save 테스트")
    @Test
    void saveTest() throws InterruptedException{
        //given
        Long userId = 10L;
        Long amount = 500L;
        Long updateMillis = System.currentTimeMillis();
        PointHistory user = new PointHistory(1L, userId, TransactionType.CHARGE, amount, updateMillis);
        when(pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, updateMillis))
                .thenReturn(user);

        //when
        PointHistory save = userHistoryReader.save(userId, amount, TransactionType.CHARGE, updateMillis);

        //then
        assertNotNull(save);
        assertEquals( amount, save.amount());
    }

    @DisplayName("readList 테스트")
    @Test
    void readListTest() {
        //given
        Long userId = 1L;

        List<PointHistory> list = List.of(
                new PointHistory(1L, userId, TransactionType.CHARGE, 500L, System.currentTimeMillis()),
                new PointHistory(2L, userId, TransactionType.CHARGE, 500L + 500, System.currentTimeMillis()),
                new PointHistory(3L, userId, TransactionType.USE, 500L + 500L - 200L, System.currentTimeMillis())
        );

        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(list);

        //when
        List<PointHistory> pointList = userHistoryReader.readList(userId);

        //then
        assertNotNull(pointList);
        assertEquals(500L, list.get(0).amount());
        assertEquals(500L + 500L, list.get(1).amount());
        assertEquals(1000L - 200L, list.get(2).amount());
    }
}
