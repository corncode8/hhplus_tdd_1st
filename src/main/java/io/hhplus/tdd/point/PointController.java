package io.hhplus.tdd.point;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/point")
@RestController
@RequiredArgsConstructor
public class PointController {

    private final UserPointService userPointService;
    private final PointHistoryService pointHistoryService;
    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public UserPoint point(@PathVariable("id") Long id) throws InterruptedException{

        return userPointService.getUserPoint(id);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public List<PointHistory> history(@PathVariable("id") Long id) {

        return pointHistoryService.getPointHistory(id);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
//    @PatchMapping("{id}/charge")
//    public UserPoint charge(@PathVariable Long id, @RequestBody Long amount) throws InterruptedException{
//
//        if (amount < 1000) {
//            throw new IllegalArgumentException("1000원 이상부터 충전할 수 있습니다.");
//        }
//
//        return userPointService.chargePoint(id, amount);
//    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
//    @PatchMapping("{id}/use")
//    public UserPoint use(@PathVariable Long id, @RequestBody Long amount) throws InterruptedException{
//
//        if (amount < 0) {
//            throw new IllegalArgumentException("충전포인트는 1원 이상부터 사용할 수 있습니다.");
//        }
//
//        return userPointService.usePoint(id, amount);
//    }
}
