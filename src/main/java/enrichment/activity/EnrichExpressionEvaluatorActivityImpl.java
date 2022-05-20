package enrichment.activity;

import org.bson.Document;

import java.time.Instant;
import java.util.Date;

public class EnrichExpressionEvaluatorActivityImpl implements EnrichExpressionEvaluatorActivity{
    @Override
    public Document evaluate(Document record) {
        // add new enrichment fields
        record.put("EN5","Enrichment1");
        record.put("EN6",Math.random());
        record.put("EN7", new Date());
        return record;
    }
}
