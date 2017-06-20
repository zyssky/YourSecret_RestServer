package Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.enterprise.inject.New;

import org.hibernate.Session;

import Model.Artical;

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
	
	

}
