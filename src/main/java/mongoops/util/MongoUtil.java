package mongoops.util;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class MongoUtil {

    static final String MONGO_URI ="mongodb://root:rootpassword@data-enrichment-mongodb-1:27017/admin"; //"mongodb://CAC_RO:ReadOnly@172.16.5.117:27027/cac_v331_qa";
    //"mongodb://root:rootpassword@data-enrichment-mongodb-1:27017/admin";
    static final String MONGO_DB="platform_local";
    static final String COLLECTION = "CollectionData";
    static final String COLLECTION_ID="174800";
    static final String CLIENT_ID="8";
    static final int numSplits =5;

    public static void main(String args[]){
        System.out.println("Getting Splits for "+COLLECTION);
        getSplits();
        System.out.println("Done with Splits");


    }

    public static void getSplits() {
        try (MongoClient mongoClient =
                new MongoClient(new MongoClientURI(MONGO_URI))
        ) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGO_DB);
            List<Document> splitKeys;

            // the user defines his desired number of splits
            // calculate the batch size
            long estimatedSizeBytes =
                    getEstimatedSizeBytes(mongoClient, MONGO_DB, COLLECTION);
            long desiredBundleSizeBytes = estimatedSizeBytes / numSplits;

            // now we have the batch size (provided by user or provided by the runner)
            // we use Mongo splitVector command to get the split keys
            BasicDBObject splitVectorCommand = new BasicDBObject();
            splitVectorCommand.append("splitVector", MONGO_DB + "." +COLLECTION);
            splitVectorCommand.append("keyPattern", new BasicDBObject().append("_id", 1));
            splitVectorCommand.append("force", false);
            long defaultMaxChunkSize = 200;
            // maxChunkSize is the Mongo partition size in MB
            System.out.println("Splitting in chunk of {} MB"+ defaultMaxChunkSize);
            splitVectorCommand.append("maxChunkSize", defaultMaxChunkSize);
            System.out.println("Split VECTOR Command "+splitVectorCommand);
            Document splitVectorCommandResult = mongoDatabase.runCommand(splitVectorCommand);
            splitKeys = (List<Document>) splitVectorCommandResult.get("splitKeys");

            System.out.println("Split Keys : ");
            splitKeys.forEach(d->{
                System.out.println(d);
            });
        }catch(Exception e){
            System.out.println("Error in getting Split Keys");
            e.printStackTrace();
        }
    }

    private static long getEstimatedSizeBytes(
            MongoClient mongoClient, String database, String collection) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        // get the Mongo collStats object
        // it gives the size for the entire collection
        BasicDBObject stat = new BasicDBObject();
        stat.append("collStats", collection);
        Document stats = mongoDatabase.runCommand(stat);
        return stats.get("size", Number.class).longValue();
    }

}
