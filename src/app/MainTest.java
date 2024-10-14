package app;

import crawler.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class MainTest {

	
	public static final String INITIAL_URI = "http://dbpedia.org/resource/Roger_Federer";
	
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		SemanticCrawler crawler = new SemanticCrawlerImpl();
		crawler.search(model, INITIAL_URI);
		System.out.println("============Triplas============");
		model.write(System.out, "NTRIPLES");
	}

}
