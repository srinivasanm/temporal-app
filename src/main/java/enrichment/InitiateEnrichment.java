package enrichment;

import enrichment.model.EnrichmentRequest;
import enrichment.workflow.EnrichmentSubmissionWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InitiateEnrichment {

    public static void main(String args[]){

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Constants.ENRICHMENT_TASK_QUEUE)
                // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate.
                //.setWorkflowId("enrichment-workflow")
                .setWorkflowTaskTimeout(Duration.ofSeconds(100))
                .setWorkflowRunTimeout(Duration.ofSeconds(120))
                .build();
        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);
        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        EnrichmentSubmissionWorkflow workflow = client.newWorkflowStub(EnrichmentSubmissionWorkflow.class, options);

        EnrichmentRequest enrichmentRequestData = getEnrichmentRequestData(args);

        WorkflowExecution we = WorkflowClient.start(workflow::enrich, enrichmentRequestData);
        System.out.printf("\nEnrichment Workflow initiated for \n"+enrichmentRequestData);
        System.out.printf("\nWorkflowID: %s RunID: %s", we.getWorkflowId(), we.getRunId());
        System.exit(0);
    }

    private static EnrichmentRequest getEnrichmentRequestData(String args[]){

        if(args.length<6){
            System.out.println("Mandatory parameters are missing, in order : MongoURI,DB,SourceCollection,CollectionId,ClientId,SinkCollection");
            System.out.println("Followed by Optional parameters for _id range");
            throw new RuntimeException("Parameters missing to execute");
        }

        EnrichmentRequest enrichmentRequestData = new EnrichmentRequest();
        enrichmentRequestData.setMongoURL(args[0]);
        enrichmentRequestData.setDatabase(args[1]);
        enrichmentRequestData.setSourceCollection(args[2]);
        enrichmentRequestData.setCollectionId(args[3]);
        enrichmentRequestData.setClientId(args[4]);
        enrichmentRequestData.setSinkCollection(args[5]);

        if(args.length>6) {
            List<String> idIntervals = new ArrayList<>();
            for(int i=6;i< args.length;i++){
                idIntervals.add(args[i]);
            }
            enrichmentRequestData.setIdIntervals(idIntervals);
        }
        return enrichmentRequestData;
    }
}
