package enrichment.workflow;

import enrichment.activity.ActivityOptionsUtil;
import enrichment.activity.EnrichActivity;
import enrichment.model.EnrichChunkRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class EnrichChunkWorkflowImpl implements EnrichChunkWorkflow {

    // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
    private final Map<String, ActivityOptions> perActivityMethodOptions = new HashMap<String, ActivityOptions>(){{
        put("ENRICH", ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
    }};

    private final EnrichActivity enrichActivity = Workflow.newActivityStub(EnrichActivity.class, ActivityOptionsUtil.ENRICH_ACTIVITY_OPTIONS,perActivityMethodOptions);


    @Override
    public void enrichChunk(EnrichChunkRequest enrichChunkRequestData) {
        enrichActivity.enrich(enrichChunkRequestData);
    }
}
