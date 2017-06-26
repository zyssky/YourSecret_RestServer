import java.util.HashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import Rest.ArticalService;

public class InitProject implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		ArticalService.map = new HashMap<String, String>();
		ArticalService.map.put(ArticalService.ARTICAL_CATOGORY_HOT, ArticalService.ARTICLE_TYPE_HOT);
		ArticalService.map.put(ArticalService.ARTICAL_CATOGORY_PUSH, ArticalService.ARTICLE_TYPE_NOTICE);
		ArticalService.map.put(ArticalService.ARTICAL_CATOGORY_GOOD, ArticalService.ARTICLE_TYPE_ARTICLE);
		ArticalService.map.put(ArticalService.ARTICAL_CATOGORY_OUTSIDE, ArticalService.ARTICLE_TYPE_ARTICLE);
	}
	
}
