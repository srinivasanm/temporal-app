package enrichment.model;

public class EnrichmentData {
    private String collectionId;
    private String clientId;
    private String sourceCollection;
    private String sinkCollection;
    private String mongoURL;
    private String database;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(String sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public String getSinkCollection() {
        return sinkCollection;
    }

    public void setSinkCollection(String sinkCollection) {
        this.sinkCollection = sinkCollection;
    }

    public String getMongoURL() {
        return mongoURL;
    }

    public void setMongoURL(String mongoURL) {
        this.mongoURL = mongoURL;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
