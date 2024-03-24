# hhplus_tdd_1st_TDD

### API
- PATCH /point/{id}/charge : 포인트를 충전한다.
- PATCH /point/{id}/use : 포인트를 사용한다.
- GET /point/{id} : 포인트를 조회한다.
- GET /point/{id}/histories : 포인트 내역을 조회한다.

### 요구사항
- 사용자는 포인트 충전, 사용, 조회할 수 있습니다.
- 잔고가 부족할 경우 포인트 사용은 실패해야 합니다.
- 동시에 여러 건의 포인트 충전, 이용 요청이 들어올 경우 순차적으로 처리되어야 합니다.

## 단위테스트
- 다른 클래스에 의존하지 않고 Mock, Stub, Spy를 이용해 기본적으로 단위테스트가 이루어져야 합니다. 
- 외부요소는 Mock이나 Stub을 사용하여 테스트 하려는 클래스를 정확히 테스트
- Stub 테스트 VS Mock 테스트 (상태검증 vs 행위검증)

#### 실패케이스에 집중한 테스트
- success_(Action) : 0 or 1 별로 중요치 않다
- fail_XXException_{Reason} : 모든 커버리지를 채우는 곳 가장 중요하다
- ruleCheck_{Expected} : 이런 동작을 해야 함을 검증 하는 테스트

## 패키지 구조
### common 패키지 (Component Layer)
- Lock Handler : userId를 기반으로 Lock을 관리하는 ConcurrentHashMap을 사용하여 동시성 제어 (ConcurrentHashMap vs Synchronized)
- UserPointManager : 유저의 기능(read, charge, modify, readList)을 정리해놓은 Component
- UserHistoryReader : db에 save, readList 기능을 구현한 Component
- UserPointReader : 유저 포인트 read 기능을 구현한 Component
- UserPointWriter : 유저 포인트 modify 기능을 구현한 Component
### Service 패키지
- UserPointService : 동시성 제어를 이용하여 포인트 충전 및 사용 구현, 포인트 조회와 포인트 내역 조회 구현

## 1주차 TDD 회고
TDD가 아직 생소함 기존에는 테스트코드를 많이 작성해보지도 않았고 SpringBootTest를 이용한 통합테스트만 진행해봤지만 1주차 를 진행하고 코칭을 받으며 TDD의 전개와 단위테스트의 중요성을 깨달았음.
- 테스트 케이스 작성 -> 해당 테스트 케이스를 통과하는 코드 작성 -> 코드 리팩토링
  - 위 구조를 실행하며 코드의 성공 실패의 설계가 잘 이루어진다면 단위테스트 기반으로 구현되기 때문에 유지보수(리팩토링)이 용이하다.
  - 단위테스트 기반으로 구현하기 때문에 다른사람이 코드를 봐도 쉽게 이해할 수 있음 가독성이 매우 좋아짐
- Architecture
  - 단일책임원칙, 가독성 및 유지보수성, 재사용성, 테스트 용이성, 동시성 제어등을 위해 Component Layer 사용 -> 대용량 트래픽에도 좋음
- SpringBootTest를 띄웠을때 의존하고 있는 클래스가 외부환경들을 의존하게 된다면?

##
- 기능은 단순해보이는데 테스트 코드는 너무 길어짐 -> 단순한 오류도 모두 check
- TDD로 개발해본 것은 처음이라 생각도 많아지게 되고 자꾸 기능을 개발하는쪽에 머리가 굴러가게 됨 -> 개발 속도가 많이.. 느려짐 
- 하지만 TDD로 개발하게 되면 기능을 추가할 때 무섭지 않을 것 같음 -> 이미 내가 구현한 메소드들이 어디에 영향을 미치고 안미치는지 정리가됨.
- 개발하다가 무심코 '이렇게 하면 되네' 가 아니라 각 기능마다 검증하며 개발하기 때문에 완성도의 차이는 크다 많 이
- Mock/Stub을 적재적소에 활용하는 것이 중요하다.
 


