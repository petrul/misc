package test.pertinence.story.repository;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import test.pertinence.story.Story;
import test.pertinence.story.StoryVersion;
import test.pertinence.story.TwoStories;
import test.pertinence.story.insertion.InsertionStory;

public class XmlDirStoryRepository implements StoryRepository {
	
	
	Map<Integer, InsertionStory> stories = new HashMap<Integer, InsertionStory>();
	
	public XmlDirStoryRepository(URL xmldir) throws BadStoryException {
		File dir = new File(xmldir.getPath());
		if (! dir.exists())
			throw new IllegalArgumentException("story location " + dir + " does not exist");
		
		String[] list = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".xml");
			}
		});

		if (list.length < 1)
			throw new IllegalArgumentException("no xml story found at location " + dir);
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = factory.newDocumentBuilder();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			xpath = xpathFactory.newXPath();
			
			for (String name : list) {
				Document doc = builder.parse(new File(dir, name));
				
				{
					String _shouldIgnore = xpathAsString(doc, "/story/@ignore");
					if (_shouldIgnore != null) {
						if ("yes".equalsIgnoreCase(_shouldIgnore) || "true".equalsIgnoreCase(_shouldIgnore)) {
							LOG.info("ignoring story file " + name);
							continue;
						}
					}
				}
				
				String strStoryId = this.xpathAsString(doc,"/story/id");
				
				InsertionStory story = new InsertionStory(Integer.parseInt(strStoryId));
				String original = this.xpathAsString(doc, "/story/original");
				original = original.replace("\n", "");
				original = this.capitalize(original.trim());
				
				story.setOriginal(original);
				try {
					story.setVdmId(Integer.parseInt(this.xpathAsString(doc,"/story/vdmid")));  
				} catch (NumberFormatException e) {
					// ignore
				}
				
				
				{
					// code for parsing insert-test
					NodeList mainNodes = this.xpathAsNodeList(doc, "/story/insert-test/main/child::node()");
					
					Integer insertionPosition = null;
					StringBuilder mainText = new StringBuilder();
					
					for (int i = 0; i < mainNodes.getLength();i++) {
						Node node = mainNodes.item(i);
						if (node.getNodeType() == Node.TEXT_NODE)
							mainText.append(node.getTextContent());
						else 
							if ("insert".equalsIgnoreCase(node.getNodeName())) {
								if (insertionPosition != null)
									throw new IllegalStateException("more than one insert elements found for story " + strStoryId);
								insertionPosition = mainText.length();
							}
					}
					if (insertionPosition == null)
						throw new IllegalStateException("no insert elements found for story " + strStoryId);

					story.setMain(this.capitalize(mainText.toString()));
					story.setInsertPosition(insertionPosition);
					
					NodeList optionNodes = this.xpathAsNodeList(doc, "/story/insert-test/alternatives/alt");
					for (int i = 0; i < optionNodes.getLength();i++) {
						Node node = optionNodes.item(i);
						try {
							Integer altid = Integer.parseInt(node.getAttributes().getNamedItem("altid").getTextContent());
							String optionText = node.getTextContent();
							story.addOption(altid, optionText);
						} catch (NumberFormatException e) {
							throw new RuntimeException("expected int altid for story " + strStoryId, e);
						}
					}
				}
				
				{
					// code for parsing compare-test
					
					NodeList alternativesNodes = xpathAsNodeList(doc, "/story/compare-test/alternatives/alt");
					
					Set<Integer> versionIds= new HashSet<Integer>();
					
					for (int i = 0; i < alternativesNodes.getLength(); i++) {
						
						Node item = alternativesNodes.item(i);
						String copyOriginal = this.xpathAsString(item, "@copy-original");
						
						String sVersionId = this.xpathAsString(item, "@altid");
						int versionId = Integer.parseInt(sVersionId);
						StoryVersion sv = new StoryVersion(story, versionId);
						if (versionIds.contains(versionId))
							throw new BadStoryException ("for story " + strStoryId + " several version with id " + versionId);
						
						{
							// should ignore version ?
							String ignoreThisVersion = this.xpathAsString(item, "@ignore");
							ignoreThisVersion = ignoreThisVersion.toLowerCase();
							if ("yes".equals(ignoreThisVersion) || "true".equals(ignoreThisVersion)) {
								continue; 
							}
						}

						if (copyOriginal.toLowerCase().equals("yes"))
							sv.setText(original);
						else {
							String versionText = xpath.compile(".").evaluate(item);
							if (versionText == null || "".equals(versionText))
								throw new BadStoryException("bad story, no text for story " + strStoryId + " ver " + versionId );
							sv.setText(this.capitalize(versionText.trim()));
						}
						story.addVersion(versionId, sv);
					}
					
					this.stories.put(story.getId(), story);
				}

			}
			
			LOG.info("initialized story repository from " + dir + " with " + this.stories.size() + " stories");

		} catch (BadStoryException e) {
			throw e;
		} catch (Exception e) {
			throw new BadStoryException(e); 
		}

		
		
//		LOG.info(ToStringBuilder.reflectionToString(list));
		
	}
	
	private String xpathAsString(Node doc, String xpathExpr) {
		try {
			return xpath.compile(xpathExpr).evaluate(doc);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}	
	}
	
	private NodeList xpathAsNodeList(Document doc, String xpathExpr) {
		try {
			return (NodeList) xpath.compile(xpathExpr).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}	
	}
	
	

	@Override
	public StoryVersion getRandomVersion() {
		int nrStories = this.stories.size();
		Random randomGen = new Random();
		int storyId = randomGen.nextInt(nrStories);
		
		Iterator<Integer> it = this.stories.keySet().iterator();
		for (int i = 0; i < storyId; i++)
			it.next();
		Story story = this.stories.get(it.next());
		return story.getRandomVersion();
	}

	public TwoStories getTwoRandomVersions() {
		TwoStories bothStories = new TwoStories();
		
		Set<Integer> storiesIdSet = new HashSet<Integer>();
		storiesIdSet.addAll(this.stories.keySet()); // make a copy of the set, because we're going to change it
		Random randomGen = new Random();
		
		{
			
			int nth = randomGen.nextInt(storiesIdSet.size());

			Iterator<Integer> it = storiesIdSet.iterator();
			for (int i = 0; i < nth; i++)
				it.next();
			Integer leftId = it.next();
			Story storyLeft = this.stories.get(leftId);
			storiesIdSet.remove(leftId);
			
			bothStories.setLeftStory(storyLeft.getRandomVersion());
		}
		
		{
			int nth = randomGen.nextInt(storiesIdSet.size());

			Iterator<Integer> it = storiesIdSet.iterator();
			for (int i = 0; i < nth; i++)
				it.next();
			Integer rightId = it.next();
			Story storyRight = this.stories.get(rightId);
			storiesIdSet.remove(rightId);
			
			bothStories.setRightStory(storyRight.getRandomVersion());
		}		
		
		return bothStories;
		
	}
	
	@Override
	public StoryVersion getVersion(int storyId, int versionId) {
		Story story = this.getStory(storyId);
		return story.getVersion(versionId);
	}

	@Override
	public InsertionStory getStory(int storyId) {
		InsertionStory story = this.stories.get(storyId);
		if (story == null)
			throw new IllegalArgumentException("there is no story #" + storyId);
		return story;
	}
	
    protected String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

	@Override
	public synchronized ArrayList<InsertionStory> getCompleteSampleForUser() {
		ArrayList<InsertionStory> arrayList = new ArrayList<InsertionStory>(this.stories.values());
		Collections.shuffle(arrayList);
		return arrayList;
	}
	
	private XPath xpath = null;
	static Logger LOG = Logger.getLogger(XmlDirStoryRepository.class);
}
