package githubmashup.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class User {
	@Id
	public Long		id;
	
	public String 	login;
	
	public String 	photo;
	
	public String 	name;	
}
