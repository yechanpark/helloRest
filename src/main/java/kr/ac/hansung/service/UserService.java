package kr.ac.hansung.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.ac.hansung.model.User;

@Service // Service����� �����ϴ� Bean���� ���
public class UserService {

	private static final AtomicLong counter = new AtomicLong();

	// DB�������� �ʰ� �޸� �� �ӽ������� �����ų �ڷᱸ��
	private static List<User> users;

	// �⺻������ 4���� ���� �߰��Ѵ�.
	public UserService() {

		users = new ArrayList<User>();

		users.add(new User(counter.incrementAndGet(), "Sam", 30, 70000));
		users.add(new User(counter.incrementAndGet(), "Tom", 40, 50000));
		users.add(new User(counter.incrementAndGet(), "Jerome", 45, 30000));
		users.add(new User(counter.incrementAndGet(), "Silvia", 50, 40000));

	}

	// ��� ���� ã��
	public List<User> findAllUsers() {
		return users;
	}

	// id������ ���� ã��
	public User findById(long id) {
		for (User user : users) {
			if (user.getId() == id)
				return user;
		}
		return null;
	}

	// name������ ���� ã��
	public User findByName(String name) {
		for (User user : users) {
			if (user.getName().equalsIgnoreCase(name))
				return user;
		}
		return null;
	}

	// ���� �߰�
	public void saveUser(User user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}

	// ���� ������Ʈ
	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	// id������ ����
	public void deleteUserById(long id) {
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if (user.getId() == id)
				iterator.remove();
		}
	}

	// �����ϴ���
	public boolean isUserExist(User user) {
		return findByName(user.getName()) != null;
	}

	// ��� ���� ����
	public void deleteAllUsers() {
		users.clear();
	}
}
