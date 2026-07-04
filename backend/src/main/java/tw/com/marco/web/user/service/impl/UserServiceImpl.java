package tw.com.marco.web.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.marco.core.entity.User;
import tw.com.marco.web.user.dao.UserDao;
import tw.com.marco.web.user.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao dao;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User register(User user) {
		if (dao.selectByPhone(user.getPhoneNumber()) != null) {
			user.setMessage("此手機號已被註冊");
			user.setSuccessful(false);
			return user;
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
        int result = dao.insertUser(user);
        
        if (result < 1) {
			user.setMessage("註冊錯誤，請聯絡管理員!");
			user.setSuccessful(false);
			return user;
		}

        user.setMessage("註冊成功");
        user.setSuccessful(true);
		return user; 
	}

	@Override
	public User login(User user) {
		User returnUser = dao.selectByPhone(user.getPhoneNumber());
		if (returnUser == null) {
			user.setMessage("帳號或密碼錯誤");
			user.setSuccessful(false);
			return user;
		}
		
		if (!passwordEncoder.matches(user.getPassword(), returnUser.getPassword())) {
			user.setMessage("帳號或密碼錯誤");
			user.setSuccessful(false);
			return user;
		}
		
		returnUser.setMessage("登入成功");
		returnUser.setSuccessful(true);
		return returnUser;
	}
}
