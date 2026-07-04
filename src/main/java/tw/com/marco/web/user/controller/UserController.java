package tw.com.marco.web.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import tw.com.marco.core.entity.User;
import tw.com.marco.web.user.service.UserService;


@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService service;
	
	@PostMapping("register")
	public User register(@RequestBody User user) {
		if (user == null) {
			user = new User();
			user.setMessage("無會員資訊");
			user.setSuccessful(false);
			return user;
		}
		return service.register(user);
	}
	
	@PostMapping("login")
	public User login(@RequestBody User user, HttpServletRequest req) {
		user = service.login(user);
		
		if (user != null && user.isSuccessful()) {
			if (req.getSession(false) != null) {
				req.changeSessionId();
			}
			HttpSession session = req.getSession();
			session.setAttribute("user", user);
		}
		return user;
	}
}