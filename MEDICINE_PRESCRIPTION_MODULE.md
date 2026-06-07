# Member 5 - Medicine & Prescription Module

## Tổng Quát

Module này cung cấp các chức năng quản lý thuốc và đơn thuốc cho hệ thống quản lý y tế.

## Các Entities (Thực thể)

### 1. Medicine (Thuốc)
```java
- id: Long (ID duy nhất)
- name: String (Tên thuốc) *
- description: String (Mô tả)
- price: double (Giá)
- unit: String (Đơn vị tính)
- quantity: int (Số lượng)
- active: boolean (Kích hoạt)
```

### 2. Prescription (Đơn thuốc)
```java
- id: Long (ID duy nhất)
- patient: Patient (Bệnh nhân) *
- doctor: Doctor (Bác sĩ) *
- diagnosis: String (Chẩn đoán)
- notes: String (Ghi chú)
- createdDate: LocalDateTime (Ngày tạo)
- updatedDate: LocalDateTime (Ngày cập nhật)
- prescriptionDetails: Set<PrescriptionDetail> (Chi tiết đơn)
- active: boolean (Kích hoạt)
```

### 3. PrescriptionDetail (Chi tiết đơn thuốc)
Biểu diễn mối quan hệ giữa các thuốc trong một đơn thuốc
```java
- id: Long (ID duy nhất)
- prescription: Prescription (Đơn thuốc) *
- medicine: Medicine (Thuốc) *
- quantity: int (Số lượng)
- dosage: String (Liều lượng)
- instructions: String (Hướng dẫn sử dụng)
- duration: int (Thời gian sử dụng)
- unit: String (Đơn vị thời gian - ngày/tuần/tháng)
```

### 4. Patient (Bệnh nhân)
```java
- id: Long (ID duy nhất)
- name: String (Tên bệnh nhân) *
- email: String (Email)
- phone: String (Số điện thoại)
- address: String (Địa chỉ)
- age: int (Tuổi)
- gender: String (Giới tính)
- medicalHistory: String (Lịch sử bệnh)
- active: boolean (Kích hoạt)
```

## API Endpoints

### Medicine API

#### 1. Lấy danh sách tất cả thuốc
```
GET /api/medicines
Response: List<MedicineDTO>
```

#### 2. Lấy thuốc theo ID
```
GET /api/medicines/{id}
Response: MedicineDTO
```

#### 3. Thêm thuốc mới
```
POST /api/medicines
Request: {
    "name": "Paracetamol",
    "description": "Giảm đau hạ sốt",
    "price": 5000,
    "unit": "viên",
    "quantity": 100,
    "active": true
}
Response: MedicineDTO
```

#### 4. Cập nhật thuốc
```
PUT /api/medicines/{id}
Request: MedicineDTO
Response: MedicineDTO
```

#### 5. Xóa thuốc
```
DELETE /api/medicines/{id}
Response: 204 No Content
```

### Patient API

#### 1. Lấy danh sách tất cả bệnh nhân
```
GET /api/patients
Response: List<PatientDTO>
```

#### 2. Lấy bệnh nhân theo ID
```
GET /api/patients/{id}
Response: PatientDTO
```

#### 3. Thêm bệnh nhân mới
```
POST /api/patients
Request: {
    "name": "Nguyễn Văn A",
    "email": "nguyen@example.com",
    "phone": "0123456789",
    "address": "123 Đường Nguyễn Huệ",
    "age": 30,
    "gender": "Nam",
    "medicalHistory": "Tiểu đường",
    "active": true
}
Response: PatientDTO
```

#### 4. Cập nhật bệnh nhân
```
PUT /api/patients/{id}
Request: PatientDTO
Response: PatientDTO
```

#### 5. Xóa bệnh nhân
```
DELETE /api/patients/{id}
Response: 204 No Content
```

### Prescription API

#### 1. Tạo đơn thuốc mới
```
POST /api/prescriptions
Request: {
    "patientId": 1,
    "doctorId": 1,
    "diagnosis": "Cảm cúm",
    "notes": "Hạ sốt 39 độ",
    "details": [
        {
            "medicineId": 1,
            "quantity": 10,
            "dosage": "2 viên",
            "instructions": "Uống sau bữa ăn",
            "duration": 3,
            "unit": "ngày"
        }
    ]
}
Response: PrescriptionDTO
```

#### 2. Lấy danh sách tất cả đơn thuốc
```
GET /api/prescriptions
Response: List<PrescriptionDTO>
```

#### 3. Lấy đơn thuốc theo ID
```
GET /api/prescriptions/{id}
Response: PrescriptionDTO
```

#### 4. Lấy đơn thuốc của bệnh nhân
```
GET /api/prescriptions/patient/{patientId}
Response: List<PrescriptionDTO>
```

#### 5. Lấy đơn thuốc của bác sĩ
```
GET /api/prescriptions/doctor/{doctorId}
Response: List<PrescriptionDTO>
```

#### 6. Cập nhật đơn thuốc
```
PUT /api/prescriptions/{id}
Request: {
    "diagnosis": "Cảm cúm nặng",
    "notes": "Hạ sốt 40 độ"
}
Response: PrescriptionDTO
```

#### 7. Xóa đơn thuốc
```
DELETE /api/prescriptions/{id}
Response: 204 No Content
```

#### 8. Thêm thuốc vào đơn thuốc
```
POST /api/prescriptions/{id}/medicines
Request: {
    "medicineId": 2,
    "quantity": 15,
    "dosage": "1 viên",
    "instructions": "Uống trước bữa ăn",
    "duration": 7,
    "unit": "ngày"
}
Response: PrescriptionDetailDTO
```

#### 9. Xóa thuốc khỏi đơn thuốc
```
DELETE /api/prescriptions/medicines/{detailId}
Response: 204 No Content
```

## Services

### 1. MedicineService
Quản lý các hoạt động CRUD cho thuốc
- `getAllMedicines()` - Lấy danh sách tất cả thuốc
- `getMedicineById(id)` - Lấy thuốc theo ID
- `createMedicine(dto)` - Tạo thuốc mới
- `updateMedicine(id, dto)` - Cập nhật thuốc
- `deleteMedicine(id)` - Xóa thuốc

### 2. PatientService
Quản lý các hoạt động CRUD cho bệnh nhân
- `getAllPatients()` - Lấy danh sách tất cả bệnh nhân
- `getPatientById(id)` - Lấy bệnh nhân theo ID
- `createPatient(dto)` - Tạo bệnh nhân mới
- `updatePatient(id, dto)` - Cập nhật bệnh nhân
- `deletePatient(id)` - Xóa bệnh nhân

### 3. PrescriptionService
Quản lý các hoạt động CRUD cho đơn thuốc
- `getAllPrescriptions()` - Lấy danh sách tất cả đơn thuốc
- `getPrescriptionById(id)` - Lấy đơn thuốc theo ID
- `getPrescriptionsByPatientId(patientId)` - Lấy đơn thuốc của bệnh nhân
- `getPrescriptionsByDoctorId(doctorId)` - Lấy đơn thuốc của bác sĩ
- `createPrescription(dto)` - Tạo đơn thuốc mới
- `updatePrescription(id, dto)` - Cập nhật đơn thuốc
- `deletePrescription(id)` - Xóa đơn thuốc
- `addMedicineToPrescription(prescriptionId, detailDTO)` - Thêm thuốc vào đơn
- `removeMedicineFromPrescription(detailId)` - Xóa thuốc khỏi đơn

## Mối Quan Hệ (Relationships)

### One-to-Many Relationships
- **Prescription** ← Many → **PrescriptionDetail**
- **Medicine** ← Many → **PrescriptionDetail**
- **Patient** ← Many → **Prescription**
- **Doctor** ← Many → **Prescription**

### Many-to-Many Relationship
- **Medicine** ↔ **Prescription** (thông qua **PrescriptionDetail**)

Một đơn thuốc có thể chứa nhiều thuốc, mỗi loại thuốc có liều lượng, hướng dẫn và thời gian sử dụng khác nhau.

## Unit Tests

Các unit tests được triển khai sử dụng Mockito và JUnit 5:

### MedicineServiceTest
- testGetAllMedicines
- testGetMedicineById
- testGetMedicineByIdNotFound
- testCreateMedicine
- testUpdateMedicine
- testDeleteMedicine
- testDeleteMedicineNotFound

### PatientServiceTest
- testGetAllPatients
- testGetPatientById
- testGetPatientByIdNotFound
- testCreatePatient
- testUpdatePatient
- testDeletePatient
- testDeletePatientNotFound

### PrescriptionServiceTest
- testCreatePrescription
- testGetPrescriptionById
- testGetPrescriptionByIdNotFound
- testGetPrescriptionsByPatientId
- testGetPrescriptionsByDoctorId
- testUpdatePrescription
- testDeletePrescription
- testDeletePrescriptionNotFound
- testAddMedicineToPrescription
- testRemoveMedicineFromPrescription

## DTOs (Data Transfer Objects)

- **MedicineDTO** - Chuyển dữ liệu thuốc
- **PatientDTO** - Chuyển dữ liệu bệnh nhân
- **PrescriptionDTO** - Chuyển dữ liệu đơn thuốc
- **PrescriptionDetailDTO** - Chuyển dữ liệu chi tiết đơn thuốc

## Repositories

- **MedicineRepository** - Truy cập dữ liệu thuốc
- **PatientRepository** - Truy cập dữ liệu bệnh nhân
- **PrescriptionRepository** - Truy cập dữ liệu đơn thuốc
- **PrescriptionDetailRepository** - Truy cập dữ liệu chi tiết đơn thuốc

## Cách Sử Dụng

### 1. Tạo Bệnh Nhân
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyễn Văn A",
    "email": "nguyen@example.com",
    "phone": "0123456789",
    "address": "123 Đường Nguyễn Huệ",
    "age": 30,
    "gender": "Nam",
    "medicalHistory": "Tiểu đường",
    "active": true
  }'
```

### 2. Tạo Thuốc
```bash
curl -X POST http://localhost:8080/api/medicines \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Paracetamol",
    "description": "Giảm đau hạ sốt",
    "price": 5000,
    "unit": "viên",
    "quantity": 100,
    "active": true
  }'
```

### 3. Tạo Đơn Thuốc
```bash
curl -X POST http://localhost:8080/api/prescriptions \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "diagnosis": "Cảm cúm",
    "notes": "Hạ sốt 39 độ",
    "details": [
      {
        "medicineId": 1,
        "quantity": 10,
        "dosage": "2 viên",
        "instructions": "Uống sau bữa ăn",
        "duration": 3,
        "unit": "ngày"
      }
    ]
  }'
```

## Các Tệp Được Tạo

### Entities
- `/src/main/java/com/example/ss21/entity/Patient.java`
- `/src/main/java/com/example/ss21/entity/Prescription.java`
- `/src/main/java/com/example/ss21/entity/PrescriptionDetail.java`
- `/src/main/java/com/example/ss21/medicine/Medicine.java` (Updated)

### DTOs
- `/src/main/java/com/example/ss21/dto/MedicineDTO.java`
- `/src/main/java/com/example/ss21/dto/PatientDTO.java`
- `/src/main/java/com/example/ss21/dto/PrescriptionDTO.java`
- `/src/main/java/com/example/ss21/dto/PrescriptionDetailDTO.java`

### Services
- `/src/main/java/com/example/ss21/service/MedicineService.java` (Updated)
- `/src/main/java/com/example/ss21/service/PatientService.java`
- `/src/main/java/com/example/ss21/service/PrescriptionService.java`

### Controllers
- `/src/main/java/com/example/ss21/controller/MedicineController.java`
- `/src/main/java/com/example/ss21/controller/PatientController.java`
- `/src/main/java/com/example/ss21/controller/PrescriptionController.java`

### Repositories
- `/src/main/java/com/example/ss21/repository/MedicineRepository.java`
- `/src/main/java/com/example/ss21/repository/PatientRepository.java`
- `/src/main/java/com/example/ss21/repository/PrescriptionRepository.java`
- `/src/main/java/com/example/ss21/repository/PrescriptionDetailRepository.java`

### Unit Tests
- `/src/test/java/com/example/ss21/service/MedicineServiceTest.java`
- `/src/test/java/com/example/ss21/service/PatientServiceTest.java`
- `/src/test/java/com/example/ss21/service/PrescriptionServiceTest.java`

### Configuration
- `/src/test/resources/application.yml` (Test configuration with H2 database)

## Tính Năng Chính

1. ✅ CRUD Thuốc (Create, Read, Update, Delete)
2. ✅ CRUD Bệnh Nhân
3. ✅ Tạo Đơn Thuốc
4. ✅ Xem Đơn Thuốc
5. ✅ Quản Lý Chi Tiết Đơn Thuốc (Nhiều Thuốc trong 1 Đơn)
6. ✅ Lấy Đơn Thuốc theo Bệnh Nhân
7. ✅ Lấy Đơn Thuốc theo Bác Sĩ
8. ✅ Unit Tests (25 test cases)
9. ✅ DTO cho tất cả entities
10. ✅ Validation cho các fields

## Công Nghệ Sử Dụng

- Spring Boot 4.0.6
- Spring Data JPA
- Lombok
- JUnit 5
- Mockito
- H2 Database (for testing)
- MySQL Connector (for production)

