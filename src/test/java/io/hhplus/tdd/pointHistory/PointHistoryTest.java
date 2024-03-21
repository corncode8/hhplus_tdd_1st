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



    @BeforeEach
    void setUp() {

    }

    @DisplayName("포인트 입력 테스트")
    @Test
    void insertPoint() throws InterruptedException {
        //given


        //when

        //then


    }

    @DisplayName("포인트 리스트 테스트")
    @Test
    void getPointList() throws InterruptedException{
        //given


        //when


        //then

    }

}
