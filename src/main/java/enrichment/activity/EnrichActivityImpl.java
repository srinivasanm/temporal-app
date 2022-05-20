package enrichment.activity;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import ekaenrich.functions.EnrichmentDefinition;
import ekaenrich.functions.ExpressionEvaluator;
import enrichment.model.EnrichChunkRequest;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class EnrichActivityImpl implements EnrichActivity{

    //private final EnrichExpressionEvaluatorActivity enrichExpressionEvaluatorActivity=Workflow.newActivityStub(EnrichExpressionEvaluatorActivity.class);

    private final ExpressionEvaluator expressionEvaluator=new ExpressionEvaluator();
    private final JexlEngine jexl = new JexlBuilder().cache(9999).cacheThreshold(2000).strict(true).debug(false)
            .silent(false).create();

    @Override
    public Void enrich(EnrichChunkRequest enrichChunkRequestData) {

        System.out.println("ActivityStart*********************************************************");
        System.out.println("Enrichment request received "+enrichChunkRequestData);

        String sourceCollection = enrichChunkRequestData.getSourceCollection();
        String sinkCollection = enrichChunkRequestData.getSinkCollection();

        try (MongoClient mongoClient =
                     new MongoClient(new MongoClientURI(enrichChunkRequestData.getMongoURL()))
        ) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(enrichChunkRequestData.getDatabase());

            Bson filter = this.prepareFilter(enrichChunkRequestData);
            System.out.println("Filter : "+filter);
            FindIterable<Document> iterable= mongoDatabase.getCollection(sourceCollection).find(filter, Document.class);

            long count = mongoDatabase.getCollection(sourceCollection).countDocuments(filter);
            System.out.println("Record Count for "+enrichChunkRequestData+" \n "+count);

            if(count==0){
                throw new RuntimeException("Error in processing enrichment, no records found");
            }

            List<EnrichmentDefinition> enrichmentDefinitions=getEnrichmentDefinitions();
            List<Document> enrichedDocuments;
            try(MongoCursor<Document> cursor = iterable.cursor()){
                enrichedDocuments = new ArrayList<>();
                Document document=null;
                Document enrichedDocument = null;
                //int version = 0;
                System.out.println("Processing cursor");
                while(cursor.hasNext()){
                    document=cursor.next();
                    document.remove("_id");
                    //enrichedDocument = enrichExpressionEvaluatorActivity.evaluate(document);
                    //version = enrichedDocument.getInteger("version");
                    //enrichedDocument.put("version",version+1);
                    enrichedDocument=addEnrichments(jexl,enrichmentDefinitions,document);
                    enrichedDocuments.add(enrichedDocument);
                }
                System.out.println("Done with Enrichment");
            }catch (Exception e){
                e.printStackTrace();
                throw  new RuntimeException("Error in navigating cursor for source "+sourceCollection);
            }

            //write enriched data to sink collection
            System.out.println("Writing enriched records of size "+enrichedDocuments.size());
            mongoDatabase.getCollection(sinkCollection).insertMany(enrichedDocuments);
            System.out.println("Done with writing enriched records");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error  in Enrich Activity");
        }

        System.out.println("ActivityEnd*********************************************************");
        return null;
    }

    private Bson prepareFilter(EnrichChunkRequest enrichChunkRequestData){
        BasicDBObject filter=new BasicDBObject();

        if(enrichChunkRequestData.getIdStart()!=null && !enrichChunkRequestData.getIdStart().isEmpty()) {
            BasicDBObject idFilter = new BasicDBObject();
            idFilter.append("$gte", new ObjectId(enrichChunkRequestData.getIdStart()));
            idFilter.append("$lt", new ObjectId(enrichChunkRequestData.getIdEnd()));

            filter.append("_id", idFilter);
        }

        filter.append("collection_id",enrichChunkRequestData.getCollectionId());
        filter.append("client_id",enrichChunkRequestData.getClientId());

        return filter;
    }

    private Document addEnrichments(JexlEngine jexlEngine, List<EnrichmentDefinition> enrichmentDefinitions, Document enrichedDocument){
        expressionEvaluator.evaluate(jexlEngine,enrichmentDefinitions,enrichedDocument);
        return enrichedDocument;
    }


    private List<EnrichmentDefinition> getEnrichmentDefinitions(){

        List<EnrichmentDefinition> enrichmentDefinitions=new ArrayList<>();
        enrichmentDefinitions.add(
                new EnrichmentDefinition("EN0","STRING.substring(_2,1,5)",new String[]{"_2"},new String[]{"STRING"}));
        enrichmentDefinitions.add(
                new EnrichmentDefinition("EN1","_7",new String[]{"_7"}));
        enrichmentDefinitions.add(
                new EnrichmentDefinition("EN2","_11",new String[]{"_11"}));
        return enrichmentDefinitions;
    }

}
