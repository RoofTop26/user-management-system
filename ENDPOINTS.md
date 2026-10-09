## Tổng cộng: 34 endpoint (26 admin + 8 portal)

## Admin (26 endpoint)

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| POST | /admin/login | PUBLIC | Admin đăng nhập, trả về token role ADMIN |
| POST | /admin/admins | PUBLIC | Tạo tài khoản Admin mới |
| GET | /admin/admins | ADMIN | Lấy danh sách tất cả admin |
| GET | /admin/admins/{id} | ADMIN | Lấy chi tiết 1 admin |
| PUT | /admin/admins/{id} | ADMIN | Sửa toàn bộ thông tin 1 admin |
| PATCH | /admin/admins/{id} | ADMIN | Sửa 1 phần thông tin 1 admin |
| DELETE | /admin/admins/{id} | ADMIN | Admin xóa 1 admin (chặn tự xóa chính mình) |
| GET | /admin/users | ADMIN | Lấy danh sách toàn bộ User |
| GET | /admin/users/{id} | ADMIN | Lấy chi tiết 1 User theo id |
| POST | /admin/users | ADMIN | Admin tạo 1 User mới |
| PUT | /admin/users/{id} | ADMIN | Admin sửa toàn bộ thông tin 1 User |
| PATCH | /admin/users/{id} | ADMIN | Admin sửa 1 phần thông tin User |
| DELETE | /admin/users/{id} | ADMIN | Admin xoá 1 User (không chặn chính mình) |
| GET | /admin/categories | ADMIN | Lấy danh sách tất cả danh mục |
| GET | /admin/categories/{id} | ADMIN | Lấy chi tiết 1 danh mục theo id |
| POST | /admin/categories | ADMIN | Admin tạo 1 danh mục mới (trùng name trả 409) |
| PUT | /admin/categories/{id} | ADMIN | Admin sửa toàn bộ thông tin 1 danh mục |
| PATCH | /admin/categories/{id} | ADMIN | Admin sửa 1 phần thông tin danh mục |
| DELETE | /admin/categories/{id} | ADMIN | Admin xoá 1 danh mục |
| GET | /admin/products | ADMIN | Lấy danh sách tất cả sản phẩm |
| GET | /admin/products/{id} | ADMIN | Lấy chi tiết 1 sản phẩm theo id |
| GET | /admin/products/category/{categoryId} | ADMIN | Lấy danh sách sản phẩm theo danh mục |
| POST | /admin/products | ADMIN | Admin tạo 1 sản phẩm mới |
| PUT | /admin/products/{id} | ADMIN | Admin sửa toàn bộ thông tin 1 sản phẩm |
| PATCH | /admin/products/{id} | ADMIN | Admin sửa 1 phần thông tin sản phẩm |
| DELETE | /admin/products/{id} | ADMIN | Admin xoá 1 sản phẩm |

## Portal (8 endpoint)

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| POST | /portal/register | PUBLIC | User tự đăng ký tài khoản |
| POST | /portal/verify-email | PUBLIC | Xác thực email bằng token từ email để kích hoạt tài khoản |
| POST | /portal/login | PUBLIC | User đăng nhập, trả về token role USER |
| POST | /portal/forgot-password | PUBLIC | Gửi email đặt lại mật khẩu nếu email tồn tại |
| POST | /portal/reset-password | PUBLIC | Đặt lại mật khẩu bằng token từ email |
| GET | /portal/me | USER | User xem hồ sơ của chính mình |
| PATCH | /portal/me | USER | User sửa hồ sơ (chỉ name, dob) |
| PATCH | /portal/me/password | USER | User tự đổi password |