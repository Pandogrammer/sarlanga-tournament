package farguito.sarlanga.tournament;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

//this is even more frula.
@Component
public class SarlangaContext implements ApplicationContextAware {
	
	@Autowired
	static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getAppContext() {
		return context;
	}
}