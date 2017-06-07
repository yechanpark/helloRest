package kr.ac.hansung.exception;

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