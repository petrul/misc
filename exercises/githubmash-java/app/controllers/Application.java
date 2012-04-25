package controllers;

import play.mvc.Controller;


public class Application extends Controller {

    public static void index() {
        redirect("search/repo");
    }
    
    public static void lsa() {
    	render();
    }
}