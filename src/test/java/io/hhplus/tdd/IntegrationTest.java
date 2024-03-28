package io.hhplus.tdd;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.service.UserPointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private UserPointService userPointService;


    // 1명의 유저가 동시에 입금 신청 테스트
    @DisplayName("입금 동시성 테스트")
    @Test
    void chargeTest() throws InterruptedException{
        //given
        Long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 0L, System.currentTimeMillis());

        //when
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            try {
                userPointService.charge(userId, 500L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            try {
                userPointService.charge(userId, 1500L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> {
            try {
                userPointService.charge(userId, 5000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);
        allTasks.join(); // 모든 태스크가 완료될 때까지 대기


        UserPoint result = userPointService.read(userId);

        //then
        assertNotNull(result);
        assertEquals(result.point(), 500 + 1500 + 5000); // 최종 포인트 합산 검증
    }

}
