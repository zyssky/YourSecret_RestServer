package Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.enterprise.inject.New;

import org.hibernate.Session;

import Model.Artical;
import antlr.collections.List;

public class ArticalApi {
	
	public static void addArtical(Artical artical) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			session.save(artical);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
	
	public static ArrayList<Artical> getTodayArticals(String articalType){
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<Artical> list = new ArrayList<Artical>(); 
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date yesterday = new Date(new Date().getTime()-(long)24*60*60*1000);
			String limit = dateFormat.format(yesterday);
			limit = "'"+limit+"'";
			String hql = "from Artical where date >= ? order by date desc";
			list.addAll(session.createQuery(hql,Artical.class).setParameter(0, yesterday).list());
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}

	public static ArrayList<Artical> getUserArticals(String token) {
		// TODO Auto-generated method stub
		ArrayList<Artical> list = new ArrayList<Artical>(); 
		
		String phoneNum = UserApi.getUserPhoneNum(token);
		if(phoneNum ==null || phoneNum.isEmpty()){
			return list;
		}
		
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();

		try {
			String hql = "from Artical where authorId = ? order by date desc";
			list.addAll(session.createQuery(hql,Artical.class).setParameter(0, phoneNum).list());
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}

	public static boolean deleteArtical(String token, String articalHref) {
		// TODO Auto-generated method stub
		
		String phoneNum = UserApi.getUserPhoneNum(token);
		if(phoneNum ==null || phoneNum.isEmpty()){
			return false;
		}
		
		
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();

		try {
			String hql = "delete from Artical where articalHref = :href and authorId = :phonenum";
			int ans = session.createQuery(hql).setString("href", articalHref).setString("phonenum", phoneNum).executeUpdate();
			session.getTransaction().commit();
			if(ans>0)
				return true;
			else{
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return false;
	}
	
	

}
