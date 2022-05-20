package enrichment.workflow;

import enrichment.model.EnrichmentRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface EnrichmentSubmissionWorkflow {

    @WorkflowMethod
    public void enrich(EnrichmentRequest enrichmentRequestData);
}
