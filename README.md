# WayGoFrontend

Ứng dụng Android lập kế hoạch du lịch, sử dụng **SQLite (Room)** làm cache local và gọi **Spring Boot API** (backend riêng) để tương tác với **Firebase** (Authentication + Firestore) qua Firebase Admin SDK.

Android không kết nối Firestore trực tiếp. Auth dùng **Firebase Auth client** (trên Android), dữ liệu trip/booking/destination gọi qua **Retrofit → Spring Boot API → Firebase Admin SDK → Firestore**.

## 📱 Thông tin dự án

- **Ngôn ngữ:** Kotlin
- **Min SDK:** 24
- **Target SDK:** 37
- **Package:** `com.example.waygo` (khớp với `google-services.json`)
- **Application ID:** `com.example.waygo`
- **Database local:** Room (SQLite)
- **Authentication:** Firebase Auth client (Email/Password) — Android gọi trực tiếp
- **Cloud Data:** Spring Boot API → Firebase Admin SDK (Firestore)
- **HTTP client:** Retrofit
- **Backend:** `WayGoBackend` tại `/home/khiemsigma/Git-projects/WayGoBackend`

## 🏗️ Kiến trúc

```
UI (Fragments + ViewModels)
      ↓
RepositoryImpl (nguồn duy nhất)
      ├─ LocalDataSource (Room SQLite) — lưu dữ liệu trên máy, dùng offline
      ├─ RemoteDataSource (Retrofit API) — gọi Spring Boot API (trips, bookings, destinations)
      └─ FirebaseAuthHelper (Firebase Auth client) — đăng nhập/đăng ký trực tiếp
            ↓
      Spring Boot API (WayGoBackend, localhost:8080)
            ↓
      Firebase Admin SDK → Firestore (cloud)
```

## 📦 Các module chính

### Frontend (WayGoFrontend)

| Module | Mô tả |
|--------|-------|
| `data/local/` | Room database, DAO, LocalDataSource |
| `data/firebase/` | FirebaseAuthHelper (Auth client) |
| `data/remote/` | Retrofit API client, RemoteDataSource |
| `data/repository/` | RepositoryImpl, các interface repository |
| `di/` | AppContainer, ViewModelFactory |
| `ui/viewmodel/` | ViewModel cho các màn hình |
| `ui/state/` | UI State classes |
| `views/` | Fragments (TripPlanner, Destinations, Profile, Booking, ...) |

### Backend (WayGoBackend) — Đã hoàn thành giai đoạn 1

| Module | Mô tả |
|--------|-------|
| `config/FirebaseConfig.java` | Khởi tạo FirebaseApp + Firestore bean từ serviceAccountKey.json |
| `model/` | User, Trip, Booking, Destination, ApiResponse<T> |
| `dto/` | LoginRequest, RegisterRequest |
| `service/FirebaseService.java` | Thao tác Firestore (CRUD destinations, trips, bookings, users) |
| `controller/` | DestinationController, TripController, BookingController |

## 🗄️ Cơ sở dữ liệu

### SQLite (Room — Local)

Lưu cache toàn bộ dữ liệu trên điện thoại để dùng offline:

- `users` — thông tin người dùng
- `trips` — lịch trình du lịch
- `bookings` — đặt chỗ/đặt vé
- `destinations` — điểm đến
- `sync_status` — đánh dấu dữ liệu đã sync lên cloud chưa (PENDING/SYNCED)

### Firebase (Cloud — qua Spring Boot API)

- **Firebase Authentication:** Đăng ký, đăng nhập, quản lý session (Email/Password) — Android gọi Firebase Auth client trực tiếp (dùng `google-services.json`), không qua backend.
- **Firestore:** Lưu `users`, `trips`, `bookings`, `destinations` trên đám mây — Backend dùng Firebase Admin SDK (`serviceAccountKey.json`) để đọc/ghi.
- **Firestore Storage (tùy chọn):** Lưu ảnh avatar, ảnh điểm đến.

## 🔐 Quy tắc chia dữ liệu

| Dữ liệu | SQLite | Firebase (qua API) | Ghi chú |
|---------|--------|--------------------|---------|
| User, Trip, Booking, Destination | ✅ | ✅ | Sync qua API |
| Cài đặt app, bản nháp, cache tạm | ✅ | ❌ | Chỉ local |
| Tài khoản đăng nhập (email, password, UID) | ❌ | ✅ | Quản lý bởi Firebase Auth (Android client) |
| Bảng sync_status | ✅ | ❌ | Quản lý nội bộ |

## 🚀 Cách chạy project

### 1. Chạy Backend (Spring Boot)

Project backend nằm tại: `/home/khiemsigma/Git-projects/WayGoBackend`

1. File credentials đã được đặt tại:
   `WayGoBackend/src/main/resources/waygo-9ff8d-firebase-adminsdk-fbsvc-5bd09fdac3.json`
   (tải từ Firebase Console → Project Settings → Service Accounts)
2. Chạy ứng dụng Spring Boot:
   ```bash
   cd WayGoBackend
   ./gradlew bootRun
   ```
   Backend chạy trên cổng `8080`.

### 2. Chạy Frontend (Android)

1. Mở project bằng **Android Studio**.
2. File `google-services.json` đã được đặt tại `app/google-services.json`
   (tải từ Firebase Console → Project Settings → General → Your apps → Android app).
3. Cập nhật base URL của Retrofit trong `data/remote/RetrofitClient.kt` trỏ về backend:
   - Emulator: `http://10.0.2.2:8080/api`
   - Thiết bị thực: `http://<IP_MAY_CHU>:8080/api`
4. Sync Gradle và chạy ứng dụng.

## 🛠️ API Endpoints (Backend)

| Endpoint | Method | Mô tả |
|----------|--------|-------|
| `/api/destinations` | GET | Lấy tất cả điểm đến |
| `/api/destinations/{id}` | GET | Chi tiết điểm đến |
| `/api/trips?userId=...` | GET | Lấy trips của user |
| `/api/trips` | POST | Tạo trip mới |
| `/api/trips/{id}` | PUT | Cập nhật trip |
| `/api/trips/{id}` | DELETE | Xóa trip |
| `/api/bookings?tripId=...` | GET | Lấy bookings của trip |
| `/api/bookings` | POST | Tạo booking |

## 🔒 Bảo mật

- Firebase Access được kiểm soát hoàn toàn ở phía backend qua Firebase Admin SDK.
- Android app chỉ gọi REST API cho dữ liệu; không cần file `google-services.json` cho dữ liệu (chỉ cần cho Firebase Auth client).
- Backend nên triển khai kiểm tra xác thực (JWT / session / Firebase ID token) trước khi truy cập Firestore.

## 📄 License

© 2026 WayGoFrontend