package kr.ac.hansung.exception;

// 일종의 Model 역할을 하며 실제 Exception Handling은
// @ExceptionHander(UserNotFoundException.class) Annotation을 달아놓은 메서드에서 수행
// Serialization을 하지 않는다.
public class UserNotFoundException extends RuntimeException {

	// Class의 버전을 나타낸다.
	private static final long serialVersionUID = -4451685904043827749L;

	private long userId;

	public UserNotFoundException(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

}