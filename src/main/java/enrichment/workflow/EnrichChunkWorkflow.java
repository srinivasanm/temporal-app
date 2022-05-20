package enrichment.workflow;

import enrichment.model.EnrichChunkRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface EnrichChunkWorkflow {
    @WorkflowMethod
    public void enrichChunk(EnrichChunkRequest enrichActivityRequestData);
}
