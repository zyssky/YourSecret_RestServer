package Api;

import java.util.Date;
import java.util.UUID;

import org.hibernate.Session;

import Model.User;
import antlr.collections.List;

public class UserApi {
	
	public static boolean addUser(User user) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			session.save(user);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}
	
	public static boolean updateUser(User user) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			session.update(user);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
			return false;
		}
	}
	
	public static User getUser(String token) {
		User user = null;
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			String hql  = "from User where token = ?";
			user = session.createQuery(hql,User.class).setParameter(0, token).list().get(0);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return user;
	}
	
	public static String getUserPhoneNum(String token) {
		String phoneNum = null;
		User user = null;
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			String hql  = "from User where token = ?";
			java.util.List<User> usersList = session.createQuery(hql,User.class).setParameter(0, token).list();
			if(!usersList.isEmpty()){
				user = usersList.get(0);
				if(user!=null){
					phoneNum = user.getPhoneNum();
					user.setTokenAvailDate(new Date(new Date().getTime()+(long)60*60*24*30*1000));
					session.update(user);
				}
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return phoneNum;
	}
	
	public static User createOrUpdateToken(String identifier) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		User user = null;
		String token = null;
		try {
			String hql  = "from User where identifier = ?";
			user = session.createQuery(hql,User.class).setParameter(0, identifier).list().get(0);
			token = UUID.randomUUID().toString();
			user.setToken(token);
			user.setTokenAvailDate(new Date(new Date().getTime()+(long)60*60*24*30*1000));
			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
			user = null;
		}
		return user;
	}

	public static boolean modifyPassword(String phoneNum,String theOld, String theNew) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			int ans = 0;
			String hql = "update User set identifier = :theNew where phoneNum = :phone and identifier = :theOld";
			ans = session.createQuery(hql).setString("theNew", theNew).setString("phone", phoneNum).setString("theOld", theOld).executeUpdate();
			session.getTransaction().commit();
			if(ans>0)
				return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return false;
	}

}
