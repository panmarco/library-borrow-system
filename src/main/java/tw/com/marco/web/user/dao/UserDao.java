package tw.com.marco.web.user.dao;

import tw.com.marco.core.entity.User;

public interface UserDao {
	
    int insertUser(User user);
    
    User selectByPhone(String phone);
}
