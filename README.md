# test_spring
# HFT Order Management

## Gioi thieu
Du an HFT Order Management la mot ung dung Spring Boot quan ly cac Order trong he thong giao dich.
## Cau truc thu muc
src/main/java/com/test/hft
- application: service va dto
- domain: entity va repository interface
- infrastructure: repository implementation
- presentation: controller va exception handler
  src/main/resources
- i18n: resource bundle messages
- logback-spring.yml: cau hinh log
- application.yml: cau hinh chinh

## Yeu cau moi truong
- Java 21 tro len
- Maven 3.9 tro len
- Spring Boot 3.3.4

## Cach build va chay du an
1. Build project
   
   mvn clean install
   
2. Chay ung dung voi profile dev
   
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   
3. Ung dung se chay tai http://localhost:8080
- File log duoc luu tai thu muc logs voi ten file logs/app.log
- Cau hinh log duoc dinh nghia trong file src/main/resources/logback-spring.yml
- Log se tu dong luan phien theo ngay va kich thuoc

## Cau hinh ngon ngu
- File message.properties chua cac thong diep
- Ho tro message_en.properties va message_vi.properties
- Locale se duoc chon dua tren thong tin gui kem request

## Cac API chinh
- POST /api/v1/orders tao moi mot order
- GET /api/v1/orders lay danh sach order
- GET /api/v1/orders/{id} lay order theo id
- POST /api/v1/orders/{id}/cancel huy mot order
- POST /api/v1/orders/simulate-execution gia lap thuc thi cac order dang pending

Swagger UI co the truy cap tai duong dan
http://localhost:8080/swagger-ui.html

## kiem tra unit test
    mvn clean test

