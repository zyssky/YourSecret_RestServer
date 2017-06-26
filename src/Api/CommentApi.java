package Api;

import java.util.ArrayList;
import java.util.*;

import org.hibernate.Session;

import Model.Comment;

public class CommentApi {
	
	public static ArrayList<Comment> getComments(String articalHref) {
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		ArrayList<Comment> list = new ArrayList<Comment>();
		try {
			String hql= "from Comment where articalHref = ? order by date desc";
			list.addAll(session.createQuery(hql,Comment.class).setParameter(0, articalHref).getResultList());
			session.getTransaction().commit();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}
	
	public static boolean addComment(Comment comment){
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
			try {
				session.save(comment);
				session.getTransaction().commit();
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				session.getTransaction().rollback();
			}
			return false;
	}
	
	public static ArrayList<Comment> getUserComments(String token,String date) {
		ArrayList<Comment> list = new ArrayList<Comment>();
		String phoneNum = UserApi.getUserPhoneNum(token);
		if(phoneNum==null){
			return list;
		}
		
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		
		try {

			long time = Long.parseLong(date);
			String hql= "select t1.content,t1.date,t1.nickName,t1.iconPath,t2.title,"
					+ "t2.imageUri,t2.introduction,t1.articalHref from Comment as t1, Artical as t2 where t1.authorId = :phoneNum and"
					+ " t2.articalHref = t1.articalHref and t1.date > :time order by t1.date desc";
			List<Object> list2 = session.createQuery(hql).setString("phoneNum", phoneNum).setLong("time", time).getResultList();
			for (Object object : list2) {
//					System.out.println(object);
				Object[] obj = (Object[]) object;
				Comment comment = new Comment();
				comment.setContent((String) obj[0]);
				comment.setDate((Long) obj[1]);
				comment.setNickName((String) obj[2]);
				comment.setIconPath((String) obj[3]);
				comment.setTitle((String) obj[4]);
				comment.setImageUri((String) obj[5]);
				comment.setIntroduction((String) obj[6]);
				comment.setArticalHref((String) obj[7]);
				comment.setAuthorId(phoneNum);
				list.add(comment);
			}
			

			session.getTransaction().commit();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return list;
	}

	public static void deleteComments(String articalHref) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getCurrentSession();
		session.getTransaction().begin();
		try {
			String hql = "delete from Comment where articalHref = ?";
			session.createQuery(hql).setParameter(0, articalHref).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.getTransaction().rollback();
		}
	}

}
