package app;

import crawler.*;
import impl.SemanticCrawlerImpl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class MainTest {

	
	public static final String INITIAL_URI = "http://dbpedia.org/resource/Roberto_Ribeiro";
	
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		SemanticCrawler crawler = new SemanticCrawlerImpl();
		crawler.search(model, INITIAL_URI);
		System.out.println("============Triplas============");
		model.write(System.out, "NTRIPLES");
	}

}
