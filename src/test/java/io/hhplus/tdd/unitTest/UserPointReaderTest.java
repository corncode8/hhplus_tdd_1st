package io.hhplus.tdd.unitTest;

import io.hhplus.tdd.common.UserPointReader;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserPointReaderTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private UserPointReader userPointReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("테스트")
    @Test
    void test() {
        //given

        //when

        //then
    }
}
