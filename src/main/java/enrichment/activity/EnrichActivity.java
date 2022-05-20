package enrichment.activity;

import enrichment.model.EnrichChunkRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface EnrichActivity {
    @ActivityMethod
    public Void enrich(EnrichChunkRequest enrichChunkRequestData);
}