package cn.gap.tutorial.domain;

import java.util.HashSet;
import java.util.Set;

/**
 *  _____________        __________________
   |             |      |                  |       _____________
   |   EVENTS    |      |   PERSON_EVENT   |      |             |
   |_____________|      |__________________|      |    PERSON   |
   |             |      |                  |      |_____________|
   | *EVENT_ID   | <--> | *EVENT_ID        |      |             |
   |  EVENT_DATE |      | *PERSON_ID       | <--> | *PERSON_ID  |
   |  TITLE      |      |__________________|      |  AGE        |
   |_____________|                                |  FIRSTNAME  |
                                                  |  LASTNAME   |
                                                  |_____________|
 
 * @author genganpeng
 *
 */
public class Person {
	private Long id;
    private int age;
    private String firstname;
    private String lastname;
    private Set events = new HashSet();
	public Long getId() {
		return id;
	}
	private void setId(Long id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Set getEvents() {
		return events;
	}
	public void setEvents(Set events) {
		this.events = events;
	}
    
}
