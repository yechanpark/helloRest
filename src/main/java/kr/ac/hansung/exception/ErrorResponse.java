package kr.ac.hansung.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private String errorCode;
	private String errorMsg;
	private String requestURL; // 어떤 Request에 대해서 에러가 발생했는지
	
}
