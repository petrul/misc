package controllers;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import play.mvc.Controller;

import systran.exo.TextOnPage;

public class segment extends Controller {
	
	@Inject
	static TextOnPage text;
	
    public static void original(Integer id) {
    	renderText(text.originalParagraphs.get(id));
    }
    
    public static void translated(Integer id) {
    	renderText(text.translatedParagraphs.get(id));
    }
    
    public static void updateTranslated(Integer id, String text) {
    	LOG.info("update xlate" + id + text);
    	segment.text.translatedParagraphs.put(id, text);
    }
    
    static Logger LOG = Logger.getLogger(segment.class);
}
