package test.pertinence.story;

public class User implements Comparable<User>{
	String login;
	String firstName;
	String lastName;
	Character sex;
	String email;
	String occupation;
	String hostIp;
	String resolvedHostName;
	String comment;

	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getResolvedHostName() {
		return resolvedHostName;
	}
	public void setResolvedHostName(String resolvedHostName) {
		this.resolvedHostName = resolvedHostName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Character getSex() {
		return sex;
	}
	public void setSex(Character sex) {
		this.sex = sex;
	}
	
	@Override
	public String toString() {
		return this.login;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public int compareTo(User o) {
		return this.getLogin().toLowerCase().compareTo(o.getLogin().toLowerCase());
	}
}
