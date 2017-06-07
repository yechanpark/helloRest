package kr.ac.hansung.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;

// ��� ��Ʈ�ѷ��� ���� Exception Handling (Application Level)
@ControllerAdvice // Application Level���� ó���ϵ��� �ϴ� Annotation
public class GlobalExceptionController {
	/* Exception Handlers - Exception �߻� �� ����ȴ�. */
	// UserNotFoundException�� ���� Exception Handler
	@ExceptionHandler(UserNotFoundException.class)
	// Body�κп� ErrorResponse�� ���� ������ Response Body�� �־ �ѱ��.
	// ex������ id�� username�� ������ ���� ���� ����
	// req������ error�� ���õ� url������ ��� ���� ����
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest req, UserNotFoundException ex) {

		String requestURL = req.getRequestURI().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.notfound.exception"); // �����ϱ� ����
		errorResponse.setErrorMsg("User with id " + ex.getUserId() + " not found");

		// ErrorResponse�� Response�޽����� Body�κп� JSON Format���� �ְ�, HttpStatus��
		// "NOT_FOUND"
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);

	}

	// UserDuplicatedException�� ���� Exception Handler
	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity<ErrorResponse> handleUserDuplicatedException(HttpServletRequest req,
			UserDuplicatedException ex) {

		String requestURL = req.getRequestURI().toString();

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setRequestURL(requestURL);
		errorResponse.setErrorCode("user.duplicated.exception"); // �����ϱ� ����
		errorResponse.setErrorMsg("Unalbe to create. A user with name " + ex.getUsername() + " already exist.");

		// ErrorResponse�� Response�޽����� Body�κп� JSON Format���� �ְ�,
		// HttpStatus�� "CONFLICT"
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);

	}
}
