package cn.gap.tutorial;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import cn.gap.tutorial.domain.Event;
import cn.gap.tutorial.domain.Person;
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
        else if (args[0].equals("addpersontoevent")) {
            Long eventId = mgr.createAndStoreEvent("My Event", new Date());
            Long personId = mgr.createAndStorePerson("Foo", "Bar", 20);
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);
        }
		
		HibernateUtil.getSessionFactory().close();
	}
	
	private Long createAndStoreEvent(String title, Date theDate) {
		//获取一个session
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		//开始事务
		session.beginTransaction();
		Event event = new Event();
		event.setDate(theDate);
		event.setTitle(title);
		Long id = (Long)session.save(event);
		//此时也会关闭session
		session.getTransaction().commit();
		return id;
	}
	
	private Long createAndStorePerson(String firstname, String lastname, int age) {
		//获取一个session
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		//开始事务
		session.beginTransaction();
		Person person = new Person();
		person.setFirstname(firstname);
		person.setLastname(lastname);
		person.setAge(age);
		Long id = (Long)session.save(person);
		//此时也会关闭session
		session.getTransaction().commit();
		return id;
	}
	
	private List listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //HQL
        List result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
    }
	
	private void addPersonToEvent(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);
        aPerson.getEvents().add(anEvent);
        /**
         * 没有显示的调用save() or update()，因为hibernate会自动检测对象中的属性是否被修改，只要是持久化状态，
         * 内存和数据库同步通常在一个单元的结束的时候，称为flushing，比如commit的时候
         */
        session.getTransaction().commit();
    }
	
	private void addPersonToEventOfDetached(Long personId, Long eventId) {
		 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		 session.beginTransaction();
		 //fetch集合，以便detached的时候能够使用
		 Person aPerson = (Person)session.createQuery("select p from Person p left join fetch p.events where p.id=:pid").setParameter("pid", personId)
				 .uniqueResult();
	     Event anEvent = (Event) session.load(Event.class, eventId);
	     //一个工作单元结束
		 session.getTransaction().commit();
		 
		 //这里aPerson是detached的
		 aPerson.getEvents().add(anEvent);
		 
		 //开始第二个工作单元
		 Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
         session2.beginTransaction();
         //让detached变成persistent
         session2.update(aPerson); 

         session2.getTransaction().commit();
	}
}
