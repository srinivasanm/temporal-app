package enrichment.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import org.bson.Document;

@ActivityInterface
public interface EnrichExpressionEvaluatorActivity {
    @ActivityMethod
    public Document evaluate(Document record);
}
