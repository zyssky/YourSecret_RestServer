package Api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.enterprise.inject.New;

import org.hibernate.Session;
import org.hibernate.query.Query;

import Model.Artical;
import Rest.ArticalService;
import antlr.collections.List;

public class ArticalApi {
	
	public static int pageSize = 8;
	
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
	
	public static ArrayList<Artical> getNearArticals(int no,String articalType,double x1,double y1,double x2,double y2){
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<Artical> list = new ArrayList<Artical>(); 
		try {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			Date yesterday = new Date(new Date().getTime()-(long)24*60*60*1000);
			String hql = "from Artical where articalType =:ariticalType and saveType = 1 "
					+ "and latitude >= :x1 and latitude <= :x2 and longtitude <= :y1 and longtitude >= :y2 "
					+ "order by date desc";
			Query query = session.createQuery(hql)
					.setString("ariticalType", articalType)
					.setDouble("x1", x1).setDouble("x2", x2)
					.setDouble("y1", y1).setDouble("y2", y2);
			query.setFirstResult(no*pageSize);
			query.setMaxResults(pageSize);
			java.util.List<Artical> temp = query.getResultList();
			list.addAll(temp);
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
//			if(ans>0){
//				CommentApi.deleteComments(articalHref);
//				return true;
//			}
//				
//			else{
//				return false;
//			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return false;
	}

	public static ArrayList<Artical> getOutSideArticals(int no , String articalType,double x1,double y1,double x2,double y2) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<Artical> list = new ArrayList<Artical>();
		try {
			String hql = "from Artical where articalType = :ariticalType and saveType = 1 "
					+ "and latitude not between :x1 and :x2 and longtitude not between :y2 and :y1 "
					+ "order by commentNum desc";
			Query query = session.createQuery(hql)
					.setString("ariticalType", articalType)
					.setDouble("x1", x1).setDouble("x2", x2)
					.setDouble("y1", y1).setDouble("y2", y2);
			query.setFirstResult(no*pageSize);
			query.setMaxResults(pageSize);
			java.util.List<Artical> temp = query.getResultList();
			list.addAll(temp);
			
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}

	public static ArrayList<Artical> getNearArticals(int no, double x1, double y1, double x2, double y2) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<Artical> list = new ArrayList<Artical>(); 
		try {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			Date yesterday = new Date(new Date().getTime()-(long)24*60*60*1000);
			String hql = "from Artical where saveType = 1 "
					+ "and latitude >= :x1 and latitude <= :x2 and longtitude <= :y1 and longtitude >= :y2 "
					+ "order by date desc";
			Query query = session.createQuery(hql)
					.setDouble("x1", x1).setDouble("x2", x2)
					.setDouble("y1", y1).setDouble("y2", y2);
			query.setFirstResult(no*pageSize);
			query.setMaxResults(pageSize);
			java.util.List<Artical> temp = query.getResultList();
			list.addAll(temp);
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}

	public static void increaseCommentNum(String articalHref) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		String hql = "update Artical set commentNum = commentNum+1 where articalHref = ?";
		session.createQuery(hql).setParameter(0, articalHref).executeUpdate();
		session.getTransaction().commit();
	}

	public static ArrayList<Artical> getPushArticals(double x1, double y1, double x2, double y2) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<Artical> list = new ArrayList<Artical>(); 
		String type1 = ArticalService.ARTICLE_TYPE_HOT;
		String type2 = ArticalService.ARTICLE_TYPE_ARTICLE;
		try {
			String hql = "from Artical where saveType = 1 "
					+ "and latitude between :x1 and :x2 and longtitude between :y2 and :y1 "
					+ "and articalType = :type "
					+ "order by date desc";
			Query query = session.createQuery(hql)
					.setDouble("x1", x1).setDouble("x2", x2)
					.setDouble("y1", y1).setDouble("y2", y2)
					.setString("type", type1);
			query.setMaxResults(1);
			java.util.List<Artical> temp = query.getResultList();
			list.addAll(temp);
			
			Query query2 = session.createQuery(hql)
					.setDouble("x1", x1).setDouble("x2", x2)
					.setDouble("y1", y1).setDouble("y2", y2)
					.setString("type", type2);
			query2.setMaxResults(1);
			java.util.List<Artical> temp2 = query2.getResultList();
			list.addAll(temp2);
			
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}

}
