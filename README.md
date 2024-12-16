## 🦋 벌레를 잡아줘, 잡버그
> 개발 기간 : 2024.09.27 ~ 2024.11.13 <br>


<br><br>
> **벌레가 무서운데, 잡아줄 사람 어디 없나요?** <br>
잡버그(JOBBUG)는 위치 기반으로 벌레 퇴치자를 구할 수 있는 벌레 퇴치 커뮤니티 웹서비스입니다.  <br>


<br>

# 👩🏻‍💼 백엔드 팀 소개

<br>


|      오세연       |          채홍무        |                                                                                                   
| :------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------: | 
|   <img src="https://avatars.githubusercontent.com/oosedus?v=4" width=90px alt="오세연"/>       |   <img src="https://avatars.githubusercontent.com/Hong-Mu?v=4" width=90px alt="채홍무"/>                       |
|   [@oosedus](https://github.com/oosedus)   |    [@Hong-Mu](https://github.com/Hong-Mu)  | 
| 서울과학기술대학교 | 서울과학기술대학교 | 

<br>

### 서비스 개발 동기
> 😫 **” 벌레 너무 무서운데, 잡아줄 사람 어디 없나 ”**
<br>
<img width="1146" alt="image" src="https://github.com/user-attachments/assets/a8fdcc17-5a3e-475b-9c25-059cd3c7b58b">


# 📝 서비스 소개

## 핵심 기능
### 1️⃣ **벌레 퇴치 요청**
- 퇴치 장소, 희망 시간, 벌레 종류, 보상을 입력합니다. <br>
![벌레 퇴치 요청 폼](https://github.com/user-attachments/assets/687c7698-a847-4233-a7a2-a1d7d4f3ad49)
<br>

- 벌레 사진을 등록하면 AI가 벌레 위치를 감지해 이모지로 가려줍니다. <br>
![벌레 사진 등록](https://github.com/user-attachments/assets/4a9d1c39-608e-4850-8a50-578c01982dd9)
<br>

### 2️⃣ **위치 기반 벌레 퇴치 요청 조회**
- 설정한 주소를 기반으로 근처 벌레 퇴치 요청들을 조회합니다. <br>
![벌레 퇴치 조회](https://github.com/user-attachments/assets/2fdba79f-23c2-4caa-aa70-33566e61313e)

- 특정 요청을 상세 조회시 혐오감을 일으키는 벌레는 이모지로 가려져서 나타납니다. (사진 클릭 시 이모지 제거된 사진이 보여집니다.) <br>
![요청보기 클릭 시](https://github.com/user-attachments/assets/9e078ee1-4458-453d-9fe1-9658b3c7d87e)

### 3️⃣ **벌레 퇴치 수락 및 채팅 기능**
- 벌레 퇴치를 제안하면 채팅방에서 일정 조율 후 예약폼을 보내 벌레 퇴치 요청을 확정합니다. <br>
![예약폼 시나리오](https://github.com/user-attachments/assets/e06f96e0-26e6-4c9b-a0bf-8ecd6da34de7)
![예약폼 보내기](https://github.com/user-attachments/assets/26042207-d2d7-4297-8a72-f83f5e97e7cb)

### 4️⃣ **평가하기**
- 벌레 퇴치 완료 후 평가합니다.
![평가하기](https://github.com/user-attachments/assets/5875dd1f-98be-4620-ac1d-bea81e19c074)

<br><br>

# 🤖 벌레 감지 AI 모델 학습

### RoboFlow 플랫폼 활용

<img width="247" alt="image" src="https://github.com/user-attachments/assets/eebadcdf-ae18-45eb-ac02-cb08ee7855ff">

- 이미지 처리 및 컴퓨터 비전 모델 학습에 특화된 플랫폼으로, 데이터셋 준비, 모델 훈련 및 배포를 쉽게 할 수 있는 환경을 제공합니다.

### Model : Roboflow 3.0 Object Detection (Fast)
- 성능과 속도 간의 균형을 맞춘 경량화 된 객체 탐지 모델로, 실시간 성능이 요구되는 환경에서 적합합니다.
- 본 서비스는 벌레 객체를 빠르게 탐지해야 하는 서비스의 특성을 고려하여 Fast Object Detection 모델을 선택했습니다.

### Dataset
- Classes : 지네, 바퀴벌레, 그리마, 애벌레, 나방파리, 노린재, 집나방, 거미 (총 8종) 
- 총 데이터 : 3,472장
  - 학습 데이터(Train Set): 3,039장 (88%)
  - 검증 데이터(Valid Set): 286장 (8%)
  - 테스트 데이터(Test Set): 147장 (4%)
 
### 성능
- 성능 지표
  - mAP 50 (Mean Average Precision) : 다양한 임계값에서의 평균 정확도를 의미
  - Precision (정밀도) : 벌레로 예측한 객체 중 실제 벌레인 경우의 비율
  - Recall (재현율) : 실제 객체 중에서 모델이 올바르게 탐지한 비율

    <img width="475" alt="image" src="https://github.com/user-attachments/assets/5c8fea15-c109-4efb-97df-2f06652c580c">


# 💻 Stack

**Language & Framework**  
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white" /> 

**Database & ORM**  
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat&logo=Spring&logoColor=white" /> 
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white" /> 

**Build Tool**  
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white" />

**Cloud & Hosting**  
<img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=flat&logo=AmazonEC2&logoColor=white" /> 
<img src="https://img.shields.io/badge/AmazonRDS-527FFF?style=flat&logo=AmazonRDS&logoColor=white" /> 

**Containerization & CI/CD**  
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white" /> 
<img src="https://img.shields.io/badge/GithubActions-2088FF?style=flat&logo=GithubActions&logoColor=white" />

**Network & Security**  
<img src="https://img.shields.io/badge/Nginx-009639?style=flat&logo=nginx&logoColor=white" />
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=SpringSecurity&logoColor=white" />


<br><br>

# 🏛️ System Architecture
![시스템아키텍쳐](https://github.com/user-attachments/assets/48bed6c7-912b-45c5-b8dc-a6f1a1ebfd13)

<br><br>

# 📊 ERD
![image](https://github.com/user-attachments/assets/8ab0b57b-b9e9-4b7d-b734-a40ea2e7f299)

<br><br>

# 🗒️ API 명세서
https://large-gram-079.notion.site/API-c4c6888055904f04b25a9c8ed9d75e87

<br><br>

# 📏 Convention
## 🔀 Branch Rule
- Github Projects를 이용하여 Issue를 관리합니다.
- 각자의 feature branch에서 작업한 후, main branch로 merge합니다.
- {commit명}/{이슈 번호} 순으로 작명합니다.
- `ex) feat/#3`

## 💬 Commit Convention
| commit 명   | commit 규칙                                              |
|------------|--------------------------------------------------------|
| `feat`     | 새로운 기능 추가 / 일부 코드 추가 / 일부 코드 수정 (리팩토링과 구분) / 디자인 요소 수정 |
| `fix`      | 버그 수정                                                  |
| `refactor` | 코드 리팩토링                                                |
| `style`    | 코드 의미에 영향을 주지 않는 변경사항 (코드 포맷팅, 오타 수정, 변수명 변경, 에셋 추가)   |
| `chore`    | 빌드 부분 혹은 패키지 매니저 수정 사항 / 파일 이름 변경 및 위치 변경 / 파일 삭제      |
| `docs`     | 문서 추가 및 수정                                             |
| `rename`   | 패키지 혹은 폴더명, 클래스명 수정 (단독으로 시행하였을 시)                     |
| `remove`   | 패키지 혹은 폴더, 클래스를 삭제하였을 때 (단독으로 시행하였을 시)                 |

- 이슈 번호를 붙여서 commit
- `ex) #4 [feat] : 로그인 기능 구현`

#### Issue
- Issue Title : **`ConventionType: 작업할 내용`**
- 모든 작업은 `Issue`를 만든 후, 해당 이슈 번호에 대한 branch를 통해 수행
- 수행할 작업에 대한 설명과 할 일을 작성

#### Pull Request
- Pull Request Title : **`[ContentionType/#이슈번호] 작업한 내용`**
- 수행한 작업에 대한 설명을 작성하고 관련 스크린샷을 첨부
- Reviewer, Assigner, Label, Project, Milestone, 관련 이슈를 태그
- 작업 중 참고한 자료 혹은 reviewer에게 전할 내용이 있다면 하단에 작성
