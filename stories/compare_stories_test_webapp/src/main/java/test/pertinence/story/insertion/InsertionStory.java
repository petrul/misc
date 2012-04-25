package test.pertinence.story.insertion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import test.pertinence.story.Story;

/**
 * story having a main text, some alternative text inserts and an insertion position in the main text
 * @author dadi
 */
public class InsertionStory extends Story {

	private static final long serialVersionUID = 1L;
	
	Map<Integer, String> options = new HashMap<Integer, String>();
	String main;
	Integer insertPosition;

	public InsertionStory(Integer id) {
		super(id);
	}
	
	public Map<Integer, String> getOptions() {
		return options;
	}
	
	public void setOptions(Map<Integer, String> options) {
		this.options = options;
	}
	
	public String getMain() {
		return main;
	}
	
	public void setMain(String main) {
		this.main = main;
	}
	
	public Integer getInsertPosition() {
		return insertPosition;
	}
	
	public void setInsertPosition(Integer insertPosition) {
		this.insertPosition = insertPosition;
	}

	public void addOption(Integer altid, String optionText) {
		if (this.options.get(altid) != null)
			throw new IllegalArgumentException("option " + altid + " of story " + super.getId() + " is already set. You may not set it twice.");
		this.options.put(altid, optionText);
	}

	public Map<Integer,String> getRandomOptionSample(int howMany) {
		
		if (howMany > this.options.size())
			throw new IllegalArgumentException("there are only " + this.options.size() + " options for this story, cannot get a sample of " + howMany);
		
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		
		Set<Integer> optionIndexSet = new HashSet<Integer>();
		optionIndexSet.addAll(this.getOptions().keySet());
		
		Random rnd =  new Random();
		
		for (int i = 0; i < howMany; i++) {
			
			Iterator<Integer> it = optionIndexSet.iterator();
			for (int j = 0; j < rnd.nextInt(optionIndexSet.size()); j++)
				it.next();
			Integer optIdx = it.next();
			String optValue = this.options.get(optIdx);
			result.put(optIdx, optValue);
			optionIndexSet.remove(optIdx);
		}
		
		return result;
	}

	@Override
	public String toString() {
		String originalBeginning;
		final int prefixSize = 20;
		String mainText = this.main.trim().replace("\n", "");
		if (mainText != null && mainText.length() > prefixSize) {
			originalBeginning = mainText.substring(0, prefixSize) + "...";
		} else
			originalBeginning = mainText;
		
		return "story #" + super.getId() + ", original : [" + originalBeginning + "]";
	}
	
}
