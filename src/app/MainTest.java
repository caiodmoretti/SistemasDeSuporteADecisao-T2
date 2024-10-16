package app;

import crawler.*;
import impl.SemanticCrawlerImpl;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

public class MainTest {

	
	public static final String INITIAL_URI = "http://dbpedia.org/resource/Roberto_Ribeiro";
	
	public static void main(String[] args) {

		Model model = ModelFactory.createDefaultModel();
		SemanticCrawler crawler = new SemanticCrawlerImpl();
		crawler.search(model, INITIAL_URI);

		
		Model model_teste = ModelFactory.createDefaultModel();
		String filePath = "src/resources/RobertoRibeiro_UTF-8.rdf";
		model_teste.read(filePath);

		//model_teste.write(System.out, "N-TRIPLES");
		
		System.out.println("================== COMPARANDO OS DOIS GRAFOS ==================");
       if (model.isIsomorphicWith(model_teste)) {
            System.out.println("Os grafos são isomorfos (equivalentes).");
        } else {
            System.out.println("Os grafos não são isomorfos (não equivalentes).");
        }

		 
	}


}
