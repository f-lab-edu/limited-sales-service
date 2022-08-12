_limited-sales-service_
---
상품을 한정된 수량으로 고객에게 제공하는 서비스입니다.<br>
Rest API 형 서버로서 프론트는 프로토타입으로 제작해 시각적으로만 제공하고있으며<br>
서버 공부에 더 집중할 수 있도록 하고 있습니다.<br>
구현 정보는 WIKI 와 PR 에서 확인가능합니다.

_프로젝트 전체적인 구조_
---
<img width="629" alt="image" src="https://user-images.githubusercontent.com/63729676/184303429-e0835a89-7878-4fd6-a5d3-0ea70fcc3095.png">

- Oracle Cloud 에서 무료로 제공하는 인스턴스를 이용해 서버를 구성했습니다.
---
<img width="557" alt="image" src="https://user-images.githubusercontent.com/63729676/184303508-14f423a8-b20c-4e91-9db5-d9b590ff4870.png">

- DB 에서 Write 요청은 비용이 매우 비쌉니다. 만약 사용자가 주문하게 된다면 Insert와 Update 를 하게 되고 한 요청당 Write 작업을 두 번 하게됩니다.
- 만약 과도한 요청이 발생했을 때 DB는 견뎌내지 못하고 Down 될 것으로 판단됩니다.
- 따라서 과도한 요청이 발생했을 때 DB의 부하를 분산시키고자 Rabbit MQ 를 반영했습니다.
- DB가 처리하는 속도는 이전보다 느리지만, 운영 관점에서 볼 때 안전하게 유지보수할 수 있는 환경을 제공하는게 큰 메리트라고 보여집니다. 



_브렌치 전략_
---
- Main    : none
- Develop : 완전한 기능 구현이 끝난 부분에 대해서 Merge를 진행합니다.
- Feature : 기능 개발을 진행할 때 사용합니다.
- Release : 배포를 위한 브렌치 입니다.

_사용 기술 및 환경_
---
- Docker
- Gradle
- Java11
- Mybatis
- Mysql
- Oracle Cloud
- Redis
- Spring boot

_프로토타입 화면_
---
- https://ovenapp.io/view/Yapx8ZCTg8GcfMCgKMXbQo5oWj84EJC6/
