package in.bettergold.restapi.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import in.bettergold.controller.trade.InsufficientBalanceException;
import in.bettergold.controller.trade.OrderNotFoundException;

//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       String error = "Malformed JSON request";
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
   }

   private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
       return new ResponseEntity<>(apiError, apiError.getStatus());
   }
   
   @ExceptionHandler(OrderNotFoundException.class)
   public final ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException ex) {
	   String error = "Order not found";
       return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, error, ex));
   }

   @ExceptionHandler(InsufficientBalanceException.class)
   public final ResponseEntity<Object> handleInsufficientBalanceException(InsufficientBalanceException ex) {
	   String error = "Insufficient balance";
       return buildResponseEntity(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, error, ex));
   }
   //other exception handlers below

}