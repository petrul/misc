package controllers;

import play.*;
import play.mvc.*;
import systran.exo.TextOnPage;

import java.util.*;

import javax.inject.Inject;

import models.*;

public class xlate extends Controller {

	@Inject
	static TextOnPage text;
	
    public static void original() {
    	Map<Integer, String> paras = text.originalParagraphs;
        render(paras);
    }
    
    public static void translation() {
    	Map<Integer, String> paras = text.translatedParagraphs;
        render(paras);
    }
    
    public static void edit() {
    	Map<Integer, String> paras = text.translatedParagraphs;
        render(paras);
    }
}