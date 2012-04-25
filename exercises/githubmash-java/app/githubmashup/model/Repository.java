package githubmashup.model;

/**
 * A {@link Repository} is identified by a github user name AND the actual repo name. You may have
 * lots of repositories with any given name.
 * @author petru
 *
 */
public class Repository {
	
	public Repository(String owner, String name) {
		super();
		this.owner = owner;
		this.name = name;
		this.watchers = 0;
	}
	
	public Repository(String owner, String name, int watchers) {
		super();
		this.owner = owner;
		this.name = name;
		this.watchers = watchers;
	}
	
	public String 	owner;
	
	public String 	name;
	
	public int		watchers;

	@Override
	public String toString() {
		return "Repository [owner=" + owner + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Repository other = (Repository) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	
}
