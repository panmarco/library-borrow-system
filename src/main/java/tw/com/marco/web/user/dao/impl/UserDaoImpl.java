package tw.com.marco.web.user.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import tw.com.marco.core.entity.User;
import tw.com.marco.web.user.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private Session session;

	@Override
	public int insertUser(User user) {
		// 建立Stored Procedure
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_insert_user");

		// 註冊參數
		query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);

		// 帶入Java變數
		query.setParameter("p_phone", user.getPhoneNumber());
		query.setParameter("p_password", user.getPassword());
		query.setParameter("p_name", user.getUserName());

		return query.executeUpdate();
	}

	@Override
	public User selectByPhone(String phone) {
		StoredProcedureQuery query = session.createStoredProcedureQuery("sp_select_user_phone", User.class);

		query.registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN);
		query.setParameter("p_phone", phone);
		
		List<User> resultList = query.getResultList();
		if (resultList == null || resultList.isEmpty()) {
            return null; 
        }
        
        return resultList.get(0);
	}
}
