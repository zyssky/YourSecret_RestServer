package Api;

import javax.imageio.spi.ServiceRegistry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public final class HibernateUtil {
	private static SessionFactory sessionFactory;  
    private HibernateUtil(){  
          
    }  
      
    static{  
    	try {
//            Configuration cfg=new Configuration();
//            cfg.configure();  
            
            
//            sessionFactory=cfg.buildSessionFactory();  
             
//            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
//            StandardServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
//            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
            
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

        
          
    }  
    
    public static Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
  
    public static SessionFactory getSessionFactory() {  
        return sessionFactory;  
    }  
      
    public static Session getSession() {  
        return sessionFactory.openSession();  
    }  
}
