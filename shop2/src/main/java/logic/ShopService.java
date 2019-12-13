package logic;

import dao.UserDao;

public class ShopService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void insertUser(User user) {
		userDao.insert(user);
		
	}
}
