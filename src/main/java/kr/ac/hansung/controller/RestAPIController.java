package kr.ac.hansung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
	// ResponseEntity : Header, Body(JSON), HTTP.status 3가지를 저장하여,
	// Request Massage에 User의 List객체를 JSON Format으로 변경하여 넘겨준다.
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {

		List<User> users = userService.findAllUsers();

		// 유저가 없는 경우 Header, Body에 채울 내용이 없으므로 HTTP.status만 "NO_CONTENT"로넘겨준다.
		if (users.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		// 유저가 있는 경우 users객체를 Body에 담아서 보내고(Header에는 구체적인 정보 X)
		// HTTP.status는 "OK"로 Response를 넘겨준다.
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	/* Retrieve Single User */
	// Template Variable 사용
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {

		User user = userService.findById(id);

		// user가 없는 경우 Custom Exception를 발생
		if (user == null) {
			throw new UserNotFoundException(id);
		}

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/* Create a User */
	// Template Variable 사용, POST이므로 정보가 BODY에 담겨서 넘어온다.
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	// 리턴 시의 Body부분은 없다는 것을 명시
	// 인자는 넘어올 때 RequestBody에 JSON으로 넘어온 사용자 정보가 그대로 객체로 Convert
	// ucBuilder는 이후 새로운 사용자가 추가되면 그 사용자의 Uri를 헤더 정보에 담아서 넘겨주기 위한 것이다.
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {

		if (userService.isUserExist(user)) {
			throw new UserDuplicatedException(user.getName());
		}
		userService.saveUser(user);

		// Header 객체 생성
		HttpHeaders headers = new HttpHeaders();
		// Location 세팅을 하는데, ucBuilder를 사용하여 path에 "api/users/{id}"를 적는다.
		// 이 {id}에 user.getId()를 사용하여 id값을 넣고, "api/users/{id}"를 toUri()를 사용하여
		// Uri형태로 바꾸어 세팅한다.
		headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());

		// 저장 후 응답에서 Body부분은 없다.
		// header를 만들어서 방금 만들어진 사용자의 Uri를 넘겨준다.
		// 생성이 된 것이니 HTTP.status는 "CREATED"로 Response를 넘겨준다.
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/* Update a User */
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	// user 인자에는 사용자가 수정한 내용이 RequestBody에 JSON Format으로 담겨있는 것을 객체로 변환하여 삽입.
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {

		User currentUser = userService.findById(id);

		if (currentUser == null) {
			throw new UserNotFoundException(id);
		}

		// 수정한 내용을 임시 객체에 삽입
		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		// 원래 내용을 수정
		userService.updateUser(currentUser);

		// 방금 수정한 내용을 Body에 담아서 Response 전송.
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
		// Header, Body 없이 HttpStatus만 "NO_CONTENT"로 Response 보냄.
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	/* Delete All Users */
	@RequestMapping(value = "/users", method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteAllUsers() {
		userService.deleteAllUsers();
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}

	// 이 곳에 Exception Handler기술 시 Controller Level
	// (이 컨트롤러 내부에서 발생한 Exception만)
}
