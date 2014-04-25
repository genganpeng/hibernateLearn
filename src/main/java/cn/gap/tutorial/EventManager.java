package cn.gap.tutorial;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import cn.gap.tutorial.domain.Event;
import cn.gap.tutorial.util.HibernateUtil;


public class EventManager {
	public static void main(String[] args) {
		EventManager mgr = new EventManager();
		//mvn exec:java -Dexec.mainClass="cn.gap.tutorial.EventManager" -Dexec.args="store"
		if (args[0].equals("store")) {
            mgr.createAndStoreEvent("My Event", new Date());
        }
		//mvn exec:java -Dexec.mainClass="cn.gap.tutorial.EventManager" -Dexec.args="list"
        else if (args[0].equals("list")) {
            List events = mgr.listEvents();
            for (int i = 0; i < events.size(); i++) {
                Event theEvent = (Event) events.get(i);
                System.out.println(
                        "Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate()
                );
            }
        }
		
		HibernateUtil.getSessionFactory().close();
	}
	
	private void createAndStoreEvent(String title, Date theDate) {
		//获取一个session
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		//开始事务
		session.beginTransaction();
		Event event = new Event();
		event.setDate(theDate);
		event.setTitle(title);
		session.save(event);
		//此时也会关闭session
		session.getTransaction().commit();
	}
	
	private List listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //HQL
        List result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
    }
}
