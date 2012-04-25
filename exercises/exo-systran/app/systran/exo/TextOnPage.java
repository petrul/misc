package systran.exo;

import java.util.HashMap;
import java.util.Map;

public class TextOnPage {

	public Map<Integer, String> originalParagraphs = new HashMap<Integer, String>();
	public Map<Integer, String> translatedParagraphs = new HashMap<Integer, String>();

	public TextOnPage() {
		
		int counter = 1;
		originalParagraphs.put(counter, "Avec les combattants rebelles, sur le front de Syrte");
		translatedParagraphs.put(counter, "With the rebellious combatants, on the face of Syrte");
		counter++;
		
		originalParagraphs.put(counter, "L'Iran est en discussion avec Moscou pour construire une deuxième centrale nucléaire, a déclaré vendredi 23 septembre le président iranien Mahmoud Ahmadinejad, lors d'une conférence de presse à New York. \"Il y a des discussions en cours avec les Russes à ce sujet\", a-t-il affirmé. \"Mais ce sont des conversations très générales\", a-t-il ajouté.");
		translatedParagraphs.put(counter, "Iran is under discussion with Moscow to build a second nuclear power plant, declared Friday, September 23 president Iranian Mahmoud Ahmadinejad, at the time of a press conference in New York. “There are discussions in progress with the Russians on this subject”, he affirmed. “But they are very general conversations”, he added.");
		counter++;
		
		originalParagraphs.put(counter, "Les frappes de l'OTAN ont scindé en deux les troupes ennemies, désormais éparpillées au nord et au sud de la route, explique le commandant. La voie est dégagée. La brigade du Matin du 17-Février doit partir sur le front, \"nettoyer\" la zone des ennemis qui pourraient prendre à revers le front par le bord de mer ou, de l'autre côté de la route, à travers les champs.");
		translatedParagraphs.put(counter, "Strikings of NATO divided into two the enemy troops, from now on scattered in the north and the south of the road, explains the commander. The way is released. The brigade of the Morning of 17-February must leave on the face, “to clean” the zone of the enemies who could take with reverse the face by the seaside or, on the other side of the road, through the fields.");
		counter++;
		
		originalParagraphs.put(counter, "Cinq minutes plus tard, les pick-up démarrent en pétaradant, les hommes accrochés à leurs armes et soudés par le cri de \"Kadhafi, on t'aura !\" Une unité s'est engagée à travers champs, rampant aux milieux des arbres fruitiers, faisant le guet derrière les palmiers, pour fouiller les fermes et bergeries des alentours. Les autres ont foncé vers la porte de Syrte. Le grand portique en béton blanc et vert qui symbolise l'entrée de la ville natale de Mouammar Kadhafi est pris d'assaut, aussitôt gribouillé de slogans révolutionnaires et des noms des martyrs. \"Syrte ! Nous voilà !\" Ils hurlent de joie et tirent vers le ciel.");
		translatedParagraphs.put(counter, "Five minutes later, the record players start while put-putting, the men fixed on their weapons and welded by the cry of “Kadhafi, one will have you!” A unit began through fields, crawling in the mediums of the fruit trees, making the watch behind the palm trees, to excavate the farms and sheep-folds of the neighborhoods. The others have dark towards the door of Syrte. The large gantry out of white concrete and green which symbolizes the entry of the birthplace of Mouammar Kadhafi is taken by storm, scribbled at once of revolutionary slogans and the names of the martyrs. “Syrte! Us here!” They howl of joy and draw towards the sky.");
		counter++;
		
		
		originalParagraphs.put(counter, "Cécile Hennion, quelle journaliste ! Elle se trouvait place Tahrir aux moments les plus dangereux, puis à Benghazi quand rien n’était joué, et la voilà maintenant aux abords de Syrte, l’endroit le plus dangereux de Libye. Chapeau bas, madame !");
		translatedParagraphs.put(counter, "Cecile Hennion, what a journalist! She was Tahrir place at the most dangerous times, then in Benghazi when nothing was played, and here it is now with the accesses of Syrte, the most dangerous place of Libya. Low hat, Madam!");
		counter++;
		
	}

}
