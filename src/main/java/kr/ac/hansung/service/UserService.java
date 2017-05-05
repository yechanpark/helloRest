package kr.ac.hansung.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import kr.ac.hansung.model.User;

@Service // Service기능을 수행하는 Bean으로 등록
public class UserService {

	private static final AtomicLong counter = new AtomicLong();

	// DB연동하지 않고 메모리 상에 임시적으로 저장시킬 자료구조
	private static List<User> users;

	// 기본적으로 4개의 값을 추가한다.
	public UserService() {

		users = new ArrayList<User>();

		users.add(new User(counter.incrementAndGet(), "Sam", 30, 70000));
		users.add(new User(counter.incrementAndGet(), "Tom", 40, 50000));
		users.add(new User(counter.incrementAndGet(), "Jerome", 45, 30000));
		users.add(new User(counter.incrementAndGet(), "Silvia", 50, 40000));

	}

	// 모든 유저 찾기
	public List<User> findAllUsers() {
		return users;
	}

	// id값으로 유저 찾기
	public User findById(long id) {
		for (User user : users) {
			if (user.getId() == id)
				return user;
		}
		return null;
	}

	// name값으로 유저 찾기
	public User findByName(String name) {
		for (User user : users) {
			if (user.getName().equalsIgnoreCase(name))
				return user;
		}
		return null;
	}

	// 유저 추가
	public void saveUser(User user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}

	// 유저 업데이트
	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	// id값으로 삭제
	public void deleteUserById(long id) {
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if (user.getId() == id)
				iterator.remove();
		}
	}

	// 존재하는지
	public boolean isUserExist(User user) {
		return findByName(user.getName()) != null;
	}

	// 모든 유저 삭제
	public void deleteAllUsers() {
		users.clear();
	}
}
