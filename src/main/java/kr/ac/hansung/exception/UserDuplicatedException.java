package kr.ac.hansung.exception;

// 일종의 Model 역할을 하며 실제 Exception Handling은
// @ExceptionHander(UserDuplicatedExceptio.class) Annotation을 달아놓은 메서드에서 수행
// Serialization을 하지 않는다.
public class UserDuplicatedException extends RuntimeException {

	// Class의 버전을 나타낸다.
	private static final long serialVersionUID = 2081249869346761091L;

	private String username;

	public UserDuplicatedException(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}