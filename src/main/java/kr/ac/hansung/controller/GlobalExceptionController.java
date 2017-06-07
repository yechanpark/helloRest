package kr.ac.hansung.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;

// 모든 컨트롤러에 대해 Exception Handling (Application Level)
@ControllerAdvice // Application Level에서 처리하도록 하는 Annotation
public class GlobalExceptionController {
	/* Exception Handlers - Exception 발생 시 수행된다. */
	// UserNotFoundException에 대한 Exception Handler
	@ExceptionHandler(UserNotFoundException.class)
	// Body부분에 ErrorResponse에 대한 내용을 Response Body에 넣어서 넘긴다.
	// ex변수는 id나 username을 끄집어 내기 위해 선언
	// req변수는 error와 관련된 url정보를 얻기 위해 선언
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest req, UserNotFoundException ex) {

		String requestURL = req.getRequestURI().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.notfound.exception"); // 정의하기 나름
		errorResponse.setErrorMsg("User with id " + ex.getUserId() + " not found");

		// ErrorResponse를 Response메시지의 Body부분에 JSON Format으로 넣고, HttpStatus는
		// "NOT_FOUND"
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);

	}

	// UserDuplicatedException에 대한 Exception Handler
	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity<ErrorResponse> handleUserDuplicatedException(HttpServletRequest req,
			UserDuplicatedException ex) {

		String requestURL = req.getRequestURI().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.duplicated.exception"); // 정의하기 나름
		errorResponse.setErrorMsg("Unalbe to create. A user with name " + ex.getUsername() + " already exist.");

		// ErrorResponse를 Response메시지의 Body부분에 JSON Format으로 넣고,
		// HttpStatus는 "CONFLICT"
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);

	}
}
