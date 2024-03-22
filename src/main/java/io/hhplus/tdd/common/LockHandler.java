package io.hhplus.tdd.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class LockHandler {

    // userId 기반으로 Lock을 관리하는 ConcurrentHashMap
    private final Map<Long, Lock> lockMap = new ConcurrentHashMap();

    // userId를 기반으로 Lock을 요청
    // 만약 해당 Lock이 사용중이라면 대기한다.
    // 그 다음 내 차례가 되면 Lock을 사용해서 요청을 수행한다.
    // 특정 userId에 대해 사용/충전 요청이 들어왔을 때
    // Lock에 의해서 한번에 하나씩 밖에 요청 수행이 안됨.
    public <T> T executeOnLock(Long key, Supplier<T> block) throws InterruptedException {
        // 특정 userId 요청이 들어왔는데, 만약 Lock 이 없다면 Lock 을 생성, 있다면 꺼내온다.
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());

        // 5초 동안 Lock을 두드리는 것. (획득하려고 시도)
        // 내가 사용가능해진 순간 ( 내 차례가 되면 ) 바로 들어가서 진행.
        // acquired 값이 true가 되면 동기적으로 로직을 돌릴 수 있음
        boolean acquired = lock.tryLock(5, TimeUnit.SECONDS);
        // 락 획득 실패 시 Exception 발생
        if (!acquired) throw new RuntimeException("Timeout 에러 발생");
        // 락을 획득
        try {
            // 요청을 수행하고 반환값을 돌려준다.
            return block.get();
        } finally {
            // 요청이 끝나면, lcok을 해제해서 다른 요청이 사용 가능하게 해준다.
            lock.unlock();
        }
    }
}
