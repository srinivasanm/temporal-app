package enrichment.workflow;

import enrichment.activity.ActivityOptionsUtil;
import enrichment.activity.EnrichActivity;
import enrichment.model.EnrichChunkRequest;
import enrichment.model.EnrichmentRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnrichmentSubmissionWorkflowImpl implements EnrichmentSubmissionWorkflow {

    // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
    private final Map<String, ActivityOptions> perActivityMethodOptions = new HashMap<String, ActivityOptions>(){{
        put("ENRICH", ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
    }};

    private final EnrichActivity enrichActivity = Workflow.newActivityStub(EnrichActivity.class, ActivityOptionsUtil.ENRICH_ACTIVITY_OPTIONS,perActivityMethodOptions);


    public void enrich(EnrichmentRequest enrichmentRequest){

        System.out.println("**********************************************");
        System.out.println("Enrichment Started for "+enrichmentRequest);

        List<String> idIntervals= enrichmentRequest.getIdIntervals();

        if(idIntervals==null){
            System.out.println("Id intervals not available");
            //TODO:
        }

        EnrichChunkRequest enrichChunkRequest = null;
        if(idIntervals!=null && idIntervals.size()>0) {
            List<Promise<Void>> promises = new ArrayList<>(idIntervals.size());
            for (int i = 0; i < idIntervals.size() - 1; i++) {
                //int i=0;
                enrichChunkRequest = getChunkRequest(enrichmentRequest);
                enrichChunkRequest.setIdStart(idIntervals.get(i));
                enrichChunkRequest.setIdEnd(idIntervals.get(i + 1));
                System.out.println("Submitting Enrichment for " + enrichChunkRequest);
                //enrichActivity.enrich(enrichChunkRequest);
                promises.add(Async.function(enrichActivity::enrich, enrichChunkRequest));
            }
            // Invoke all activities in parallel. Wait for all to complete
            Promise.allOf(promises).get();
        }else{
            enrichChunkRequest = getChunkRequest(enrichmentRequest);
            enrichActivity.enrich(enrichChunkRequest);
        }
        System.out.println("Enrichment Completed");
    }


    private EnrichChunkRequest getChunkRequest(EnrichmentRequest enrichmentRequest){
        EnrichChunkRequest enrichChunkRequest=new EnrichChunkRequest();
        enrichChunkRequest.setMongoURL(enrichmentRequest.getMongoURL());
        enrichChunkRequest.setDatabase(enrichmentRequest.getDatabase());
        enrichChunkRequest.setSourceCollection(enrichmentRequest.getSourceCollection());
        enrichChunkRequest.setSinkCollection(enrichmentRequest.getSinkCollection());
        enrichChunkRequest.setCollectionId(enrichmentRequest.getCollectionId());
        enrichChunkRequest.setClientId(enrichmentRequest.getClientId());
        return enrichChunkRequest;
    }
}
