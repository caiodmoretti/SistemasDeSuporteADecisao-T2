package crawler;


import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.OWL;

import java.util.HashSet;
import java.util.Set;

public class SemanticCrawlerImpl implements SemanticCrawler {

    // Conjunto para armazenar URIs já visitados
    private Set<String> visitedURIs = new HashSet<>();

    public void search(Model model, String resourceURI) {
    	System.out.println("Verificando a URI " + resourceURI);
        if (visitedURIs.contains(resourceURI)) {
        	System.out.println("URI já foi visitada.");
            return; 
        }

        visitedURIs.add(resourceURI); // Marca o URI como visitado

        // 1. Dereferenciar o URI e obter um documento RDF
        Model resourceModel = dereferenceURI(resourceURI);
        if (resourceModel != null) {
            // 2. Coletar os fatos sobre o recurso
            collectFacts(model, resourceModel, resourceURI);
            // 3. Navegar pelos links
            navigateLinks(model, resourceModel, resourceURI);
        }
    }

    private Model dereferenceURI(String uri) {
        Model model = ModelFactory.createDefaultModel();
        try {
            // Carrega o documento RDF do URI
            model.read(uri);
        } catch (Exception e) {
            System.err.println("Erro ao dereferenciar o URI: " + uri);
        }
        return model;
    }

    private void collectFacts(Model model, Model resourceModel, String resourceURI) {
        // Coleta todas as triplas que têm o URI como sujeito
        StmtIterator stmtIterator = resourceModel.listStatements(ResourceFactory.createResource(resourceURI), null, (RDFNode) null);
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            model.add(stmt); // Adiciona a tripla ao grafo passado como parâmetro
        }
    }

    private void navigateLinks(Model model, Model resourceModel, String resourceURI) {
        System.out.println("Navegando pelas triplas com a propriendade OWL.sameAs");
        StmtIterator sameAsStmtIterator = resourceModel.listStatements(ResourceFactory.createResource(resourceURI), OWL.sameAs, (RDFNode) null);
        while (sameAsStmtIterator.hasNext()) {
            Statement stmt = sameAsStmtIterator.nextStatement();
            RDFNode objectNode = stmt.getObject();
            if (objectNode.isResource()) {
                // Se o objeto for um recurso, chama recursivamente
                search(model, objectNode.asResource().getURI());
            } else if (objectNode.isAnon()) {
                // Se o objeto for um nó em branco, coleta mais fatos
                collectFactsFromBlankNode(model, objectNode);
            }
        }

        // Navega pelas triplas onde o recursoURI é o objeto
        StmtIterator inverseSameAsStmtIterator = resourceModel.listStatements((Resource) null, OWL.sameAs, ResourceFactory.createResource(resourceURI));
        while (inverseSameAsStmtIterator.hasNext()) {
            Statement stmt = inverseSameAsStmtIterator.nextStatement();
            Resource subjectResource = stmt.getSubject();
            search(model, subjectResource.getURI()); // Chama recursivamente
        }
    }

    private void collectFactsFromBlankNode(Model model, RDFNode blankNode) {
        // Coleta todas as triplas que têm o nó em branco como sujeito
        StmtIterator stmtIterator = model.listStatements((Resource) blankNode, null, (RDFNode) null);
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            model.add(stmt);
            // Se o objeto for um nó em branco, repete a coleta
            if (stmt.getObject().isAnon()) {
                collectFactsFromBlankNode(model, stmt.getObject());
            }
        }
    }
}
