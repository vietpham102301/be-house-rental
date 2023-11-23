package com.example.houserental.exception;

import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.ErrorResponse;
import com.example.houserental.exception.facility.FacilityException;
import com.example.houserental.exception.house.HouseAlreadyExistException;
import com.example.houserental.exception.house.HouseException;
import com.example.houserental.exception.image.ImageUploadException;
import com.example.houserental.exception.invoice.InvoiceException;
import com.example.houserental.exception.room.RoomException;
import com.example.houserental.exception.tenant.TenantException;
import com.example.houserental.exception.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value= HouseAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleHouseAlreadyExistsException(HouseAlreadyExistException ex){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex){
        if(ex.getMessage().equals(Message.Exist_USERNAME)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.Exist_EMAIL)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.Exist_PHONE)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.Exist_ID_NUM)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.USER_NOTFOUND)){
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        }else if(ex.getMessage().equals(Message.WRONG_USERNAME_OR_PASSWORD)){
            return handleException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }else if(ex.getMessage().equals(Message.PASSWORD_NOT_CHANGE)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }else if(ex.getMessage().equals(Message.USER_INACTIVE)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Some unknown error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);


    }

    @ExceptionHandler(value= HouseException.class)
    public ResponseEntity<ErrorResponse> handleHouseException(HouseException ex){
        if(ex.getMessage().equals(Message.HOUSE_NOTFOUND)){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }else if (ex.getMessage().equals("House is already Inactive")) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }else if(ex.getMessage().equals(Message.HOUSE_DONOT_HAVE_ROOM)){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }else if(ex.getMessage().equals(Message.HOUSE_INACTIVE)){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }else if(ex.getMessage().equals(Message.HOUSE_NAME_EXIST)){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }else if(ex.getMessage().equals(Message.HOUSE_ADDRESS_ID_NOT_MATCH)){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }else if(ex.getMessage().equals(Message.HOUSE_FACILITY_ID_NOT_MATCH)){
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "goes here");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(value= TenantException.class)
    public ResponseEntity<ErrorResponse> handleTenantException(TenantException ex){
        if(ex.getMessage().equals(Message.Exist_ADDRESS)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.EXIST_LICENSE_PLATE)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.PHONE_UNIQUE)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.EMAIL_UNIQUE)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        }else if(ex.getMessage().equals(Message.ID_NUM_UNIQUE)){
            return handleException(HttpStatus.CONFLICT, ex.getMessage());
        } else if (ex.getMessage().equals(Message.TENANT_INACTIVE)) {
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } else if (ex.getMessage().equals(Message.TENANT_NOTFOUND)) {
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        } else if (ex.getMessage().equals(Message.ROOM_NOTFOUND)) {
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        } else if (ex.getMessage().equals(Message.TENANT_NOT_RENT_ROOM)) {
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } else if (ex.getMessage().equals(Message.ROOM_FULL)) {
            return handleException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }else if(ex.getMessage().equals(Message.ROOM_INACTIVE)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(value= RoomException.class)
    public ResponseEntity<ErrorResponse> handleRoomException(RoomException ex){
        if(ex.getMessage().equals(Message.ROOM_NOTFOUND)){
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        }else if(ex.getMessage().equals(Message.ROOM_INACTIVE)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value= InvoiceException.class)
    public ResponseEntity<ErrorResponse> handleInvoiceException(InvoiceException ex){
        if(ex.getMessage().equals(Message.INVALID_INDEX)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }else if(ex.getMessage().equals(Message.INVOICE_NOTFOUND)){
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        }else if(ex.getMessage().equals(Message.INVOICE_CANCEL)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } else if(ex.getMessage().equals(Message.FACILITY_DONT_EXIST)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } else if(ex.getMessage().equals(Message.NEXT_INDEX_MUST_LARGER));
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value= FacilityException.class)
    public ResponseEntity<ErrorResponse> handleFacilityException(FacilityException ex){
        if(ex.getMessage().equals(Message.FACILITY_NOTFOUND)){
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        }else if(ex.getMessage().equals(Message.NOT_EQUAL_QUANTITY)){
            return handleException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value= ImageUploadException.class)
    public ResponseEntity<ErrorResponse> handleImageUploadException(ImageUploadException ex){
        if(ex.getMessage().equals(Message.IMAGE_NOT_FOUND)){
            return handleException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> handleException(HttpStatus status, String message){
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
