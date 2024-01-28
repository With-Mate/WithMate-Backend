# WithMate-Backend
Google SolutionChallenge 위드메이트 팀의 백엔드 공간입니다.

## GitHub Role

### Commit Convention

- feat : 새로운 기능 추가
- fix : 버그 수정
- docs : 문서 수정
- style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- refactor: 코드 리펙토링
- test: 테스트 코드, 리펙토링 테스트 코드 추가
- chore : 빌드 업무 수정, 패키지 매니저 수정
- rename : 파일 혹은 폴더명을 수정
- remove : 파일을 삭제

### Enginnering Stack
- JDK : 17
- Spring : 3.1.8


## File Structure


```markdown
.
├── README.md
└── withmate
    ├── build.gradle
    ├── gradle
    │   └── wrapper
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle
    └── src
        ├── main
        │   ├── java
        │   │   └── com
        │   │       └── gdscewha
        │   │           └── withmate
        │   │               ├── WithMateApplication.java
        │   │               ├── common
        │   │               │   ├── response
        │   │               │   │   └── exception
        │   │               │   │       ├── DefaultException.java
        │   │               │   │       ├── ErrorCode.java
        │   │               │   │       ├── JourneyException.java
        │   │               │   │       ├── MatchingException.java
        │   │               │   │       ├── MateRelationException.java
        │   │               │   │       ├── MemberException.java
        │   │               │   │       ├── MemberRelationException.java
        │   │               │   │       ├── StickerException.java
        │   │               │   │       └── WeekException.java
        │   │               │   └── validation
        │   │               │       └── ValidationService.java
        │   │               └── domain
        │   │                   ├── journey
        │   │                   │   ├── controller
        │   │                   │   │   └── JourneyController.java
        │   │                   │   ├── entity
        │   │                   │   │   └── Journey.java
        │   │                   │   ├── repository
        │   │                   │   │   └── JourneyRepository.java
        │   │                   │   └── service
        │   │                   │       └── JourneyService.java
        │   │                   ├── matching
        │   │                   │   ├── dto
        │   │                   │   │   └── MatchingDTO.java
        │   │                   │   ├── entity
        │   │                   │   │   └── Matching.java
        │   │                   │   ├── repository
        │   │                   │   │   └── MatchingRepository.java
        │   │                   │   └── service
        │   │                   │       └── MatchingService.java
        │   │                   ├── member
        │   │                   │   ├── controller
        │   │                   │   │   └── MemberController.java
        │   │                   │   ├── dto
        │   │                   │   │   ├── MemberProfileDto.java
        │   │                   │   │   └── MemberSettingsDto.java
        │   │                   │   ├── entity
        │   │                   │   │   └── Member.java
        │   │                   │   ├── repository
        │   │                   │   │   └── MemberRepository.java
        │   │                   │   └── service
        │   │                   │       └── MemberService.java
        │   │                   ├── memberrelation
        │   │                   │   ├── entity
        │   │                   │   │   └── MemberRelation.java
        │   │                   │   ├── repository
        │   │                   │   │   └── MemberRelationRepository.java
        │   │                   │   └── service
        │   │                   │       └── MemberRelationService.java
        │   │                   ├── model
        │   │                   │   ├── Category.java
        │   │                   │   └── Messages.java
        │   │                   ├── relation
        │   │                   │   ├── controller
        │   │                   │   │   └── RelationController.java
        │   │                   │   ├── dto
        │   │                   │   │   ├── RelationHomeDto.java
        │   │                   │   │   ├── RelationManageDto.java
        │   │                   │   │   └── RelationReportDto.java
        │   │                   │   ├── entity
        │   │                   │   │   └── Relation.java
        │   │                   │   ├── repository
        │   │                   │   │   └── RelationRepository.java
        │   │                   │   └── service
        │   │                   │       └── RelationMateService.java
        │   │                   ├── sticker
        │   │                   │   ├── dto
        │   │                   │   │   ├── StickerCreateDTO.java
        │   │                   │   │   └── StickerUpdateDTO.java
        │   │                   │   ├── entity
        │   │                   │   │   └── Sticker.java
        │   │                   │   ├── repository
        │   │                   │   │   └── StickerRepository.java
        │   │                   │   └── service
        │   │                   │       └── StickerService.java
        │   │                   └── week
        │   │                       ├── controller
        │   │                       │   └── WeekController.java
        │   │                       ├── entity
        │   │                       │   └── Week.java
        │   │                       ├── repository
        │   │                       │   └── WeekRepository.java
        │   │                       └── service
        │   │                           └── WeekService.java
        │   └── resources
        │       ├── application-local.yml
        │       └── application.yml
        └── test
            └── java
                └── com
                    └── gdscewha
                        └── withmate
                            ├── WithMateApplicationTests.java
                            └── domain
                                └── MemberServiceTest.java
```

