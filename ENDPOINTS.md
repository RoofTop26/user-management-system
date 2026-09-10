## Admin (8 endpoint)

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| POST | /admin/login | PUBLIC | Admin đăng nhập, trả token role ADMIN |
| POST | /admin/admins | PUBLIC | Tạo tài khoản Admin mới |
| GET | /admin/users | ADMIN | Lấy danh sách toàn bộ User |
| GET | /admin/users/{id} | ADMIN | Lấy chi tiết 1 User theo id |
| POST | /admin/users | ADMIN | Admin tạo 1 User mới |
| PUT | /admin/users/{id} | ADMIN | Admin sửa toàn bộ thông tin 1 User |
| PATCH | /admin/users/{id} | ADMIN | Admin sửa 1 phần thông tin User |
| DELETE | /admin/users/{id} | ADMIN | Admin xoá 1 User (không được tự xoá chính mình) |

## Portal (5 endpoint)

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| POST | /portal/register | PUBLIC | User tự đăng ký tài khoản |
| POST | /portal/login | PUBLIC | User đăng nhập, trả token role USER |
| GET | /portal/me | USER | User xem hồ sơ của chính mình |
| PATCH | /portal/me | USER | User sửa hồ sơ (chỉ name, dob) |
| PATCH | /portal/me/password | USER | User tự đổi password |