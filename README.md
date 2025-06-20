# [바로인턴 13기] 백엔드 개발 과제 (Java)

## 목차

1. [📖 프로젝트 개요](#-프로젝트-개요)
2. [💭 공통 요구사항](#-공통-요구사항)
3. [🌐 배포 정보](#-배포-정보)
   - [🔗 AWS EC2 API 엔드포인트 URL](#-aws-ec2-api-엔드포인트-url)
   - [🔗 Swagger UI 주소](#-swagger-ui-주소)
4. [API 명세서](#-api-명세서)
   - [회원가입 API](#회원가입-api)
   - [로그인 API](#로그인-api)
   - [관리자 권한 부여 API](#관리자-권한-부여-api)

---

## 📖 프로젝트 개요

### Spring Boot 기반 JWT 인증/인가 및 AWS 배포

1. **Spring Boot**를 이용하여 JWT 인증/인가 로직과 API를 구현한다.
2. **Junit** 기반의 테스트 코드를 작성한다.
3. **Swagger** 로 API를 문서화 한다.
4. 애플리케이션을 **AWS EC2**에 배포하고, 실제 환경에서 실행되도록 구성한다.

---

## 💭 공통 요구사항

1. 기본으로 설정된 서버의 주소와 포트는 `0.0.0.0:8080` 이고, 이를 수정하지 않는다.
2. 모든 API 응답은 **적절한 HTTP 상태 코드**와 함께 `application/json` 형식으로 반환한다.
3. 실제 데이터베이스나 파일 시스템을 사용하지 않으며, 모든 데이터는 메모리 내에서 처리된다.

---

## 🌐 배포 정보

### 🔗 AWS EC2 API 엔드포인트 URL

http://3.37.36.102:8080

### 🔗 Swagger UI 주소

http://3.37.36.102:8080/swagger-ui/index.html

→ API 테스트 및 문서 확인 시, 위 링크 사용

---

## 📋 API 명세서

### 회원가입 API

| Method | Endpoint                         | Description |
|:------:|:---------------------------------|:------------|
|  POST  | `http://3.37.36.102:8080/signup` | 회원가입        |

<br>

### 로그인 API

| Method | Endpoint                        | Description |
|:------:|:--------------------------------|:------------|
|  POST  | `http://3.37.36.102:8080/login` | 로그인         |

<br>

### 관리자 권한 부여 API

| Method | Endpoint                                             | Description     |
|:------:|:-----------------------------------------------------|:----------------|
| PATCH  | `http://3.37.36.102:8080/admin/users/{userId}/roles` | 사용자에게 관리자 권한 부여 |

<br>
