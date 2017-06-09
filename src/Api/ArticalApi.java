package Api;

import org.hibernate.Session;

import Model.Artical;

public class ArticalApi {
	
	public static void addArtical(Artical artical) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
//		session.save(artical);
		session.getTransaction().commit();
	}

}
