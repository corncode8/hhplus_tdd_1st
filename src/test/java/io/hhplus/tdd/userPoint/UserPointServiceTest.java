package io.hhplus.tdd.userPoint;

import io.hhplus.tdd.point.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserPointServiceTest {

    @Autowired
    UserPointService userPointService;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("포인트 조회 테스트 성공")
    @Test
    void getUserPoint() throws InterruptedException{
        //given
        Long userId = 10L;
        Long amount = 2000L;
        UserPoint user = userPointService.chargePoint(userId, amount);

        //when
        UserPoint userPoint = userPointService.getUserPoint(userId);

        //then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.point()).isEqualTo(amount);
    }

    @DisplayName("포인트 충전 테스트 성공")
    @Test
    void chargePointTest() throws InterruptedException{
        //given
        Long userId = 1L;
        Long amount = 10000L;

        //when
        UserPoint userPoint = userPointService.chargePoint(userId, amount);


        //then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(amount);
    }

    @DisplayName("포인트 충전 테스트 실패")
    @Test
    void chargePointFailCase() {
        //given
        Long userId = 1L;
        Long amount = 500L;

        //when & then
        assertThrows(IllegalArgumentException.class, () -> userPointService.chargePoint(userId, amount), "충전포인트는  1000원이상 충전할 수 있습니다.");
    }

    @DisplayName("포인트 사용 테스트 성공")
    @Test
    void usePointTest() throws InterruptedException{
        //given
        Long userId = 5L;
        Long amount = 20000L;
        Long useAmount = 5000L;
        UserPoint user = userPointService.chargePoint(userId, amount);

        //when
        UserPoint usePoint = userPointService.usePoint(userId, useAmount);

        //then
        assertThat(usePoint).isNotNull();
        assertThat(usePoint.point()).isEqualTo(amount - useAmount);
    }

    @DisplayName("포인트 사용 테스트 실패 (잔액 부족)")
    @Test
    void usePointFailCase1() throws InterruptedException{
        //given
        Long userId = 5L;
        Long amount = 20000L;
        Long useAmount = 25000L;
        userPointService.chargePoint(userId, amount);

        //when & then
        assertThrows(IllegalArgumentException.class, () -> userPointService.usePoint(userId, useAmount), "포인트 잔액이 부족합니다.");
    }

    @DisplayName("포인트 사용 테스트 실패 (0 포인트 사용)")
    @Test
    void usePointFailCase2() throws InterruptedException{
        //given
        Long userId = 5L;
        Long amount = 20000L;
        Long useAmount = 0L;
        userPointService.chargePoint(userId, amount);

        //when & then
        assertThrows(IllegalArgumentException.class, () -> userPointService.usePoint(userId, useAmount), "충전포인트는 1원 이상부터 사용할 수 있습니다.");
    }
}
