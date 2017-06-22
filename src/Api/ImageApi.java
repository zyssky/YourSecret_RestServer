package Api;

import java.util.ArrayList;

import javax.enterprise.inject.New;

import org.hibernate.Session;

import Model.Image;
import antlr.collections.List;

public class ImageApi {
	public static void addImages(String articalHref,ArrayList<String> subPaths) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			for (String path : subPaths) {
				session.save(new Image(articalHref,path));
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}
	
	public static ArrayList<String> removeImages(String articalHref) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<String> subPaths = new ArrayList<String>();
		try {
			String hql1 = "select subPath from Image where articalHref = ?";
			String hql2 = "delete from Image where articalHref = ?";
			subPaths.addAll(session.createQuery(hql1,String.class).setParameter(0, articalHref).getResultList());
			session.createQuery(hql2).setParameter(0, articalHref).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return subPaths;
	}
}
