package enrichment.workflow;

import enrichment.Constants;
import enrichment.model.EnrichChunkRequest;
import enrichment.model.EnrichmentRequest;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EnrichmentSubmissionWorkflowImpl1 implements EnrichmentSubmissionWorkflow {

    private final ChildWorkflowOptions childWorkflowOptions = ChildWorkflowOptions.newBuilder()
            .setTaskQueue(Constants.ENRICHMENT_CHUNK_TASK_QUEUE)
            .setWorkflowId(Constants.ENRICHMENT_SUBMISSION_WORKFLOW)
            .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
            .build();

    public void enrich(EnrichmentRequest enrichmentRequest){

        List<String> idIntervals= enrichmentRequest.getIdIntervals();

        if(idIntervals==null){
            System.out.println("Id intervals not available");
            //TODO:
        }

        EnrichChunkWorkflow enrichChunkWorkflow = Workflow.newChildWorkflowStub(EnrichChunkWorkflow.class, childWorkflowOptions);
        EnrichChunkRequest enrichChunkRequest =null;
        for(int i = 0 ;i<idIntervals.size()-1;i++) {
            enrichChunkRequest = getChunkRequest(enrichmentRequest);
            enrichChunkRequest.setIdStart(idIntervals.get(i));
            enrichChunkRequest.setIdEnd(idIntervals.get(i+1));
            System.out.println("Submitting Enrichment Chunk Workflow for "+enrichChunkRequest);
          //  WorkflowExecution we = WorkflowClient.start(enrichChunkWorkflow::enrichChunk, enrichChunkRequest);
            try {
                WorkflowClient.execute(enrichChunkWorkflow::enrichChunk, enrichChunkRequest).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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
