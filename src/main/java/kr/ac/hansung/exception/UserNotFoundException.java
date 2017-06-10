package kr.ac.hansung.exception;

// ������ Model ������ �ϸ� ���� Exception Handling��
// @ExceptionHander(UserNotFoundException.class) Annotation�� �޾Ƴ��� �޼��忡�� ����
// Serialization�� ���� �ʴ´�.
public class UserNotFoundException extends RuntimeException {

	// Class�� ������ ��Ÿ����.
	private static final long serialVersionUID = -4451685904043827749L;

	private long userId;

	public UserNotFoundException(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

}