package kr.ac.hansung.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;
import kr.ac.hansung.model.User;
import kr.ac.hansung.service.UserService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api") // Class Level Mapping
public class RestAPIController {

	@Autowired
	UserService userService;

	/* Retrieve All Users */
	// ResponseEntity : Header, Body(JSON), HTTP.status 3������ �����Ͽ�,
	// Request Massage�� User�� List��ü�� JSON Format���� �����Ͽ� �Ѱ��ش�.
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {

		List<User> users = userService.findAllUsers();

		// ������ ���� ��� Header, Body�� ä�� ������ �����Ƿ� HTTP.status�� "NO_CONTENT"�γѰ��ش�.
		if (users.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		// ������ �ִ� ��� users��ü�� Body�� ��Ƽ� ������(Header���� ��ü���� ���� X)
		// HTTP.status�� "OK"�� Response�� �Ѱ��ش�.
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/* Retrieve Single User */
	// Template Variable ���
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {

		User user = userService.findById(id);

		// user�� ���� ��� Custom Exception�� �߻�
		if (user == null) {
			throw new UserNotFoundException(id);
		}

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/* Create a User */
	// Template Variable ���, POST�̹Ƿ� ������ BODY�� ��ܼ� �Ѿ�´�.
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	// ���� ���� Body�κ��� ���ٴ� ���� ����
	// ���ڴ� �Ѿ�� �� RequestBody�� JSON���� �Ѿ�� ����� ������ �״�� ��ü�� Convert
	// ucBuilder�� ���� ���ο� ����ڰ� �߰��Ǹ� �� ������� Uri�� ��� ������ ��Ƽ� �Ѱ��ֱ� ���� ���̴�.
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {

		if (userService.isUserExist(user)) {
			throw new UserDuplicatedException(user.getName());
		}
		userService.saveUser(user);

		// Header ��ü ����
		HttpHeaders headers = new HttpHeaders();
		// Location ������ �ϴµ�, ucBuilder�� ����Ͽ� path�� "api/users/{id}"�� ���´�.
		// �� {id}�� user.getId()�� ����Ͽ� id���� �ְ�, "api/users/{id}"�� toUri()�� ����Ͽ�
		// Uri���·� �ٲپ� �����Ѵ�.
		headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());

		// ���� �� ���信�� Body�κ��� ����.
		// header�� ���� ��� ������� ������� Uri�� �Ѱ��ش�.
		// ������ �� ���̴� HTTP.status�� "CREATED"�� Response�� �Ѱ��ش�.
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/* Update a User */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	// user ���ڿ��� ����ڰ� ������ ������ RequestBody�� JSON Format���� ����ִ� ���� ��ü�� ��ȯ�Ͽ� ����.
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			throw new UserNotFoundException(id);
		}

		// ������ ������ �ӽ� ��ü�� ����
		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		// ���� ������ ����
		userService.updateUser(currentUser);

		// ��� ������ ������ Body�� ��Ƽ� Response ����.
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	/* Delete a User */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {

		User user = userService.findById(id);

		if (user == null) {
			throw new UserNotFoundException(id);
		}

		userService.deleteUserById(id);
		// Header, Body ���� HttpStatus�� "NO_CONTENT"�� Response ����.
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	/* Delete All Users */
	@RequestMapping(value = "/users", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	// �� ���� Exception Handler��� �� Controller Level
	// (�� ��Ʈ�ѷ� ���ο��� �߻��� Exception��)
}