package ekaenrich.functions;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ExpressionEvaluator {

    private Map<String, Object> typeInstances = new HashMap<>();

    public Document evaluate(JexlEngine jexlEngine,List<EnrichmentDefinition> enrichmentDefinitions, Document data){


        for(EnrichmentDefinition enrichmentDefinition:enrichmentDefinitions){

            String expression=enrichmentDefinition.getExpression();
            String[] dependentColArray = enrichmentDefinition.getDependentCols();
            String[] reservedKeywords = enrichmentDefinition.getReservedKeywords();

            String[] colArray =  Stream.concat(Arrays.stream(dependentColArray), Arrays.stream(reservedKeywords))
                    .toArray(String[]::new);
            JexlScript script = jexlEngine.createScript(expression,colArray);

            Object[] columnValues=new Object[dependentColArray.length+reservedKeywords.length];
            int i = 0;
            Object val=null;
            for(;i<dependentColArray.length;i++){
                val=data.getString(dependentColArray[i]);
                columnValues[i]=val!=null?val:null;
            }
            for(int j=0;j<reservedKeywords.length;i++,j++){
                columnValues[i]=getType(reservedKeywords[j]);
            }
            Object expValue = script.execute(null,columnValues);
            data.put(enrichmentDefinition.getColInternalId(),expValue);
            //System.out.println("Column "+enrichmentDefinition.getColName()+" Value : "+expValue);

        }

        return data;
    }

    protected Object getType(String clazz) {

        if (typeInstances.containsKey(clazz))
            return typeInstances.get(clazz);
        Object instance;
        if (Integer.class.getSimpleName().equals(clazz))
            instance = new Integer("1");
        else
            instance = STRING.getInstance();
        typeInstances.put(clazz, instance);
        return instance;
    }
}
