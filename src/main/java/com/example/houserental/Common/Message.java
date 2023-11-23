package com.example.houserental.Common;

public class Message {
    
    public static final String SELECT_SUCCESS = "Lấy dữ liệu thành công";
    public static final String SELECT_FAILED = "Lấy dữ liệu thất bại";
    public static final String UPDATE_SUCCESS = "Cập nhật dữ liệu thành công";
    public static final String UPDATE_FAILED = "Cập nhật dữ liệu thất bại";
    public static final String INSERT_SUCCESS = "Thêm dữ liệu thành công";
    public static final String INSERT_FAILED = "Thêm dữ liệu thất bại";
    public static final String DELETE_SUCCESS = "Xóa dữ liệu thành công";
    public static final String DELETE_FAILED = "Xóa dữ liệu thất bại";

    public static final String USERNAME_UNIQUE = "Tên tài khoản này đã được sử dụng";
    public static final String PHONE_UNIQUE = "This phone number already taken";
    public static final String EMAIL_UNIQUE = "This email is already taken";
    public static final String ID_NUM_UNIQUE = "This ID number is already taken";

    public static final String NAME_UNIQUE = "Tên này đã được sử dụng";

    public static final String USERNAME_VALIDATE = "username must be 6-20 characters and contain only letters and numbers";
    public static final String PASSWORD_VALIDATE = "password must be 6-20 characters and contain only letters and numbers";
    public static final String FIRSTNAME_VALIDATE = "Name is not valid a-z 0-9 and space maximum 50 characters";
    public static final String LASTNAME_VALIDATE = "Tên không hợp lệ";
    public static final String EMAIL_VALIDATE = "Email is not valid";
    public static final String PHONE_VALIDATE = "Phone number must be 10 digits";
    public static final String ID_NUM_VALIDATE = "ID number must be 12 digits";
    public static final String ROLE_VALIDATE = "Vị trí làm việc phải là admin hoặc manager";

    public static final String GENDER_VALIDATE = "Gender is not valid";
    public static final String PRICE_VALIDATE = "Giá phải lơn hơn hoặc bằng 200k";
    public static final String QUANTITY_VALIDATE = "Số lượng phải lớn hơn 0";

    public static final String USERNAME_NOT_EMP = "Tên tài khoản không được bỏ trống";
    public static final String PASSWORD_NOT_EMP = "Mật khẩu không được bỏ trống";
    public static final String FIRSTNAME_NOT_EMP = "Họ không được bỏ trống";
    public static final String LASTNAME_NOT_EMP = "Tên không được bỏ trống";
    public static final String BIRTHDAY_NOT_EMP = "Ngày sinh không được bỏ trống";
    public static final String EMAIL_NOT_EMP = "Email không được bỏ trống";

    public static final String PHONE_NOT_EMP = "Số điện thoại không được bỏ trống";
    public static final String GENDER_NOT_EMP = "Giới tính không được bỏ trống";
    public static final String NAME_NOT_EMP = "Tên không được bỏ trống";
    public static final String ADDRESS_NOT_EMP = "Địa chỉ không được bỏ trống";
    public static final String CHECKIN_NOT_EMP = "Địa chỉ không được bỏ trống";
    public static final String CHECKOUT_NOT_EMP = "Địa chỉ không được bỏ trống";

    public static final String LAT_NOT_EMP = "Kinh độ không được bỏ trống";
    public static final String LON_NOT_EMP = "Vĩ độ không được bỏ trống";
    public static final String DESCRIPTION_NOT_EMP = "Nội dung mô tả không được bỏ trống";
    public static final String AVATAR_NOT_EMP = "Ảnh đại diện không được bỏ trống";
    public static final String PRICE_NOT_EMP = "Ảnh đại diện không được bỏ trống";
    public static final String QUANTITY_NOT_EMP = "Ảnh đại diện không được bỏ trống";
    public static final String ID_NOT_EMPTY = "Mã không được bỏ trống";
    
    public static final String BAD_REQUEST = "Cú pháp không hợp lệ";
    public static final String UNAUTHORIZED = "Bạn cần phải đăng nhập";
    public static final String FORBIDDEN = "Không có quyền truy cập";
    public static final String NOT_FOUND = "Trang bạn tìm không tồn tại";
    public static final String REQUEST_TIMEOUT = "Hết thời gian kết nối";

    public static final String SIGNIN_SUCCESS = "Đăng nhập thành công";
    public static final String SIGNIN_FAILED = "Tài khoản hoặc mật khẩu sai";
    public static final String SIGNUP_SUCCESS = "Tạo tài khoản mới thành công";
    public static final String SIGNUP_FAILED = "Tạo tài khoản mới thất bại";
    public static final String CHANGE_PASSWORD_SUCCESS = "Thay đổi mật khẩu thành công";

    public static final String SENT_EMAIL_SUCCESS = "Hãy kiểm tra email để lấy lại mật khẩu !!";
    public static final String SENT_EMAIL_FAILED = "Email chưa đăng ký hoặc hãy thử lại sau 10 Phút !!";
    public static final String ID_NAME_UNIQUE = "Mã hoặc Tên thành phố này đã được sử dụng !!";
    public static final String SIZE_VALIDATE = "Kích thước Hình ảnh tối đa 15MB";


    //Address
    public static final String Exist_ADDRESS = "This address is already taken";


    //House
    public static final String HOUSE_NOTFOUND = "This house is not found";
    public static final String HOUSE_INACTIVE = "This house is Inactive";
    public static final String HOUSE_DONOT_HAVE_ROOM = "This house do not have room";
    public static final String HOUSE_NAME_EXIST = "This house name is already taken";
    public static final String HOUSE_ADDRESS_ID_NOT_MATCH = "This address id is not match with house address id";
    public static final String HOUSE_FACILITY_ID_NOT_MATCH = "facility id is not match with house facility id";


    //Tenant
    public static final String EXIST_LICENSE_PLATE = "This license palate is already taken";
    public static final String TENANT_INACTIVE = "This tenant is already Inactive";
    public static final String TENANT_NOTFOUND = "Not found tenant";
    public static final String TENANT_NOT_RENT_ROOM = "This tenant is not rent room";


    //Room
    public static final String ROOM_NOTFOUND = "Not found room";
    public static final String ROOM_FULL = "This room is full";
    public static final String ROOM_INACTIVE = "This room is Inactive";


    //Unknow
    public static final String UNKNOWN_ERR = "Some unknown error happened";

    //User
    public static final String USER_NOTFOUND = "Not found user";
    public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";
    public static final String PASSWORD_NOT_CHANGE = "New password must be different from old password";
    public static final String USER_INACTIVE = "This account is Inactive, contact your admin to active this account";


    //User
    public static final String Exist_USERNAME = "This username is already taken";
    public static final String Exist_EMAIL = "This email is already taken";
    public static final String Exist_PHONE = "This phone is already taken";
    public static final String Exist_ID_NUM = "This id num is already taken";


    //Facilities
    public static final String FACILITY_NOTFOUND = "Not found facility";
    public static final String NOT_EQUAL_QUANTITY = "Not enough or less facilities quantity of this house";


    //invoice
    public static final String INVOICE_NOTFOUND = "Not found invoice";
    public static final String INVOICE_CANCEL = "This invoice is already cancel";
    public static final String INVALID_INDEX = "current index must larger or equal to the previous index | or if month charge current index is a positive months number";
    public static final String FACILITY_DONT_EXIST = "This facility is not exist in this rented house";
    public static final String NEXT_INDEX_MUST_LARGER = "Some of your current Index update request is larger than the next index, make it smaller and try again!";

    public static final String IMAGE_NOT_FOUND = "Image not found";
}
