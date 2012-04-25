package test.pertinence.story.repository;

import static org.junit.Assert.*;


import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import test.pertinence.story.Story;
import test.pertinence.story.StoryVersion;
import test.pertinence.story.TwoStories;
import test.pertinence.story.insertion.InsertionStory;

public class XmlDirStoryRepositoryTest {

	XmlDirStoryRepository storyRep;

	public XmlDirStoryRepositoryTest() {
		BasicConfigurator.configure();
		URL path = this.getClass().getClassLoader().getResource("stories");
		try {
			storyRep = new XmlDirStoryRepository(path);
		} catch (BadStoryException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testProperConstruction() {
		assertNotNull(this.storyRep);
		assertTrue(this.storyRep.stories.size() > 0);
		
		for (Integer id : this.storyRep.stories.keySet()) {
			InsertionStory story = this.storyRep.stories.get(id);

			assertNotNull(story.getId());
			assertTrue(story.getId() != 0);
			assertNotNull(story.getOriginal());
			assertNotNull(story.getId());
			assertNotNull(story.getVersions());
			//assertTrue(story.getVersions().size() >= 2); // at least two version per story 
			
			for (Integer i : story.getVersions().keySet()) {
				StoryVersion ver = story.getVersion(i);
				assertNotNull(ver.getId());
				assertTrue(ver.getId() != 0);
				assertNotNull(ver.getStory());
				assertNotNull(ver.getText());
				assertTrue(ver.getText().length() > 10);
			}
			
			// test for options
			assertNotNull(story.getMain());
			assertTrue(story.getMain().length() > 10);
			assertTrue(story.getOptions().size() > 2);
			assertNotNull(story.getInsertPosition());
			for (Integer opt : story.getOptions().keySet()) {
				String string = story.getOptions().get(opt);
				assertNotNull(string);
				assertTrue(string.length() > 2);
			}
			
		}
		
		{
			/* 
			 * we know that 
			 * test "ignore" version was correctly ignored 
			 */
			Story gifle = this.storyRep.getStory(2); // "gifle phenomenale"
			assertEquals(4, gifle.getVersions().size());
		}
	}
	
	@Test
	public void testGetVersion() {
		StoryVersion version = this.storyRep.getVersion(2, 1);
		assertTrue(version.getId() != 0);
		assertTrue(version.getStory().getId() != 0);
		assertNotNull(version);
	}

	@Test
	public void testGetRandomVersion() {
		for (int i = 0; i < 10000; i++) {
			StoryVersion ver = this.storyRep.getRandomVersion();
			assertNotNull(ver.getId());
			assertNotNull(ver.getStory());
			assertNotNull(ver.getText());
			assertTrue(ver.getText().length() > 10);
		}
		
		for (int i = 0; i < 10000; i++) {
			TwoStories twoStories = this.storyRep.getTwoRandomVersions();
			
			StoryVersion ver = twoStories.getLeftStory();
			assertNotNull(ver.getId());
			
			assertNotNull(ver.getStory());
			assertNotNull(ver.getText());
			assertTrue(ver.getText().length() > 10);
			
			ver = twoStories.getRightStory();
			assertNotNull(ver.getId());
			assertNotNull(ver.getStory());
			assertNotNull(ver.getText());
			assertTrue(ver.getText().length() > 10);
			
			// !!
			assertTrue(twoStories.getLeftStory().getStory().getId() != twoStories.getRightStory().getStory().getId());
		}
		
	}

	
}
