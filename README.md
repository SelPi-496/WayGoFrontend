# WayGoFrontend

Ứng dụng Android lập kế hoạch du lịch, sử dụng kết hợp **SQLite (Room)** lưu local và **Firebase** (Authentication + Firestore) lưu đám mây.

## 📱 Thông tin dự án

- **Ngôn ngữ:** Kotlin
- **Min SDK:** 24
- **Target SDK:** 37
- **Package:** `com.example.waygofrontend`
- **Database local:** Room (SQLite)
- **Authentication & Cloud:** Firebase Auth + Firestore

## 🏗️ Kiến trúc

```
UI (Fragments + ViewModels)
      ↓
RepositoryImpl (nguồn duy nhất)
      ├─ LocalDataSource (Room SQLite) — lưu dữ liệu trên máy, dùng offline
      ├─ RemoteDataSource (Retrofit API)
      └─ FirebaseDataSource (Firebase Auth + Firestore) — auth + cloud sync
```

## 📦 Các module chính

| Module | Mô tả |
|--------|-------|
| `data/local/` | Room database, DAO, LocalDataSource |
| `data/firebase/` | Firebase Auth helper, Firestore helper, FirebaseDataSource |
| `data/remote/` | Retrofit API client, RemoteDataSource |
| `data/repository/` | RepositoryImpl, các interface repository |
| `di/` | AppContainer, ViewModelFactory |
| `ui/viewmodel/` | ViewModel cho các màn hình |
| `ui/state/` | UI State classes |
| `views/` | Fragments (TripPlanner, Destinations, Profile, Booking, ...) |

## 🗄️ Cơ sở dữ liệu

### SQLite (Room — Local)

Lưu cache toàn bộ dữ liệu trên điện thoại để dùng offline:

- `users` — thông tin người dùng (firebaseUid, syncedAt)
- `trips` — lịch trình du lịch (firebaseId, syncedAt, isDeleted)
- `bookings` — đặt chỗ/đặt vé (firebaseId, syncedAt, isDeleted)
- `destinations` — điểm đến (firebaseId, syncedAt, isDeleted)
- `sync_status` — đánh dấu dữ liệu đã sync lên Firebase chưa (PENDING/SYNCED)

### Firebase (Cloud)

- **Firebase Authentication:** Đăng ký, đăng nhập, quản lý session (Email/Password)
- **Firestore:** Lưu `users`, `trips`, `bookings`, `destinations` trên đám mây để sync giữa các thiết bị
- **Firestore Storage (tùy chọn):** Lưu ảnh avatar, ảnh điểm đến

## 🔐 Quy tắc chia dữ liệu

| Dữ liệu | SQLite | Firebase | Ghi chú |
|---------|--------|----------|---------|
| User, Trip, Booking, Destination | ✅ | ✅ | Sync 2 chiều (local ↔ cloud) |
| Cài đặt app, bản nháp, cache tạm | ✅ | ❌ | Chỉ local |
| Tài khoản đăng nhập (email, password, UID) | ❌ | ✅ | Quản lý bởi Firebase Auth |
| Bảng sync_status | ✅ | ❌ | Quản lý nội bộ |

## 🚀 Cách chạy project

1. Mở project bằng **Android Studio**
2. Sync Gradle để tải dependencies
3. Tạo project trên [Firebase Console](https://console.firebase.google.com)
4. Thêm Android app, tải file `google-services.json` vào thư mục `app/`
5. Bật **Authentication** (Email/Password) và **Firestore Database** trên Firebase
6. Cấu hình Firestore Security Rules (xem chi tiết dưới)
7. Chạy ứng dụng trên thiết bị hoặc emulator

## 🛠️ Công nghệ chính

- [Room](https://developer.android.com/training/data-storage/room) — SQLite ORM
- [Firebase Auth](https://firebase.google.com/docs/auth) — Xác thực
- [Firebase Firestore](https://firebase.google.com/docs/firestore) — Cloud NoSQL database
- [Retrofit](https://square.github.io/retrofit/) — HTTP client (nếu có API riêng)
- [Kotlin Coroutines + Flow](https://kotlinlang.org/docs/coroutines-overview.html) — Xử lý bất đồng bộ
- [ViewModel + LiveData](https://developer.android.com/topic/libraries/architecture/viewmodel) — Quản lý trạng thái UI

## 🔒 Firestore Security Rules (mẫu)

Trong Firebase Console → Firestore Database → Rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /trips/{tripId} {
      allow read, write: if request.auth != null && request.auth.uid == resource.data.userId;
    }
    match /bookings/{bookingId} {
      allow read, write: if request.auth != null && request.auth.uid == resource.data.userId;
    }
    match /destinations/{destinationId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

## 📄 License

© 2026 WayGoFrontend
