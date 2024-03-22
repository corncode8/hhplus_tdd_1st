package io.hhplus.tdd.unitTest;


import io.hhplus.tdd.common.UserPointWriter;
import io.hhplus.tdd.database.UserPointTable;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointWriterTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private UserPointWriter userPointWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("modify 테스트")
    @Test
    void modifyTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 500L;
        UserPoint expectedUser = new UserPoint(userId, amount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(eq(userId), anyLong())).thenReturn(expectedUser);

        //when
        UserPoint modify = userPointWriter.modify(userId, amount);

        //then
        assertNotNull(modify);
        assertEquals(500L, modify.point());
    }

    @DisplayName("modify 실패 테스트")
    @Test
    void modifyFailTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 500L;

        // insertOrUpdate 메소드 호출 시 InterruptedException이 발생하면
        doThrow(InterruptedException.class).when(userPointTable).insertOrUpdate(anyLong(), anyLong());

        // UserPointWriter의 modify 메소드가 이 예외를 올바르게 전파하는지 검증
        //when & then
        assertThrows(InterruptedException.class, () -> userPointWriter.modify(userId, amount));

    }
}
