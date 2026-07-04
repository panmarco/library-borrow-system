package tw.com.marco.web.user.service;

import tw.com.marco.core.entity.User;

public interface UserService {
	
	User register(User user);

	User login(User user);
}
