package test.pertinence.story;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import junit.framework.Assert;

public class Story implements Comparable<Story>, Serializable{

	private static final long serialVersionUID = 1L;
	
	Integer id;
	Integer vdmId;
	String originalText;
	
	Map<Integer, StoryVersion> versions = new HashMap<Integer, StoryVersion>();

	public Story(Integer id) {
		this.id = id;
	}
	
	public Map<Integer, StoryVersion> getVersions() {
		return versions;
	}

	public void setVersions(Map<Integer, StoryVersion> versions) {
		this.versions = versions;
	}

	public Integer getVdmId() {
		return vdmId;
	}

	public void setVdmId(Integer vdmId) {
		this.vdmId = vdmId;
	}

	public String getOriginal() {
		return originalText;
	}

	public void setOriginal(String original) {
		this.originalText = original;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void addVersion(Integer i, StoryVersion sv) {
		if (this.versions.get(i) != null)
			throw new IllegalArgumentException("trying to add a duplicate version #" + sv.getId() + " for story #" + this.getId());
		Assert.assertEquals((int)sv.getId(), (int)i);
		this.versions.put(i, sv);
	}

	public void addVersion(StoryVersion sv) {
		this.addVersion(sv.getId(), sv);
	}
	
	@Override
	public String toString() {
		String originalBeginning;
		final int prefixSize = 20;
		if (this.originalText != null && this.originalText.length() > prefixSize) {
			originalBeginning = this.originalText.substring(0, prefixSize) + "...";
		} else
			originalBeginning = this.originalText;
		
		return "story #" + this.id + ", original : [" + originalBeginning + "]";
	}

	public StoryVersion getVersion(int i) {
		StoryVersion elem = this.versions.get(i);
		if (elem == null)
			throw new IllegalArgumentException("there is no version #" + i + " for story " + this.getId());
		return elem;
	}
	
	public boolean hasVersion(int i) {
		try {
			this.getVersion(i);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	

	public String getOriginalText() {
		return originalText;
	}

	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}
	
	public StoryVersion getRandomVersion() {
		Random randomGen = new Random();
		int nth = randomGen.nextInt(this.versions.size());
		Iterator<Integer> it = this.versions.keySet().iterator();
		for (int i = 0; i < nth; i++)
			it.next();
		return this.getVersion(it.next());
	}

	@Override
	public int compareTo(Story o) {
		return this.getId() - o.getId();
	}
}
