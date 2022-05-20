package others;

import ekaenrich.functions.EnrichmentDefinition;
import ekaenrich.functions.STRING;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.bson.Document;

import java.util.*;
import java.util.stream.Stream;

public class JexlExample {

    private static Map<String, Object> typeInstances = new HashMap<>();

    public static void main(String args[]){

        JexlEngine jexl = new JexlBuilder().cache(9999).cacheThreshold(2000).strict(true).debug(false)
                .silent(false).create();

        Document data = new Document();
        data.put("_28","UL TR Collection for Commodity Spot Instruments");
        data.put("_4","Spot");
        data.put("_11","USD/TONNE");

        List<EnrichmentDefinition> enrichmentDefinitions=getEnrichmentDefinitions();

        for(EnrichmentDefinition enrichmentDefinition:enrichmentDefinitions){

            String expression=enrichmentDefinition.getExpression();
            String[] dependentColArray = enrichmentDefinition.getDependentCols();
            String[] reservedKeywords = enrichmentDefinition.getReservedKeywords();

            String[] colArray =  Stream.concat(Arrays.stream(dependentColArray), Arrays.stream(reservedKeywords))
                    .toArray(String[]::new);
            JexlScript script = jexl.createScript(expression,colArray);

            Object[] columnValues=new Object[dependentColArray.length+reservedKeywords.length];
            int i = 0;
            for(;i<dependentColArray.length;i++){
                columnValues[i]=data.getString(dependentColArray[i]);
            }
            for(int j=0;j<reservedKeywords.length;i++,j++){
                columnValues[i]=getType(reservedKeywords[j]);
            }
            Object expValue = script.execute(null,columnValues);

            System.out.println("Column "+enrichmentDefinition.getColName()+" Value : "+expValue);

        }


    }

    private static List<EnrichmentDefinition> getEnrichmentDefinitions(){

        List<EnrichmentDefinition> enrichmentDefinitions=new ArrayList<>();
        enrichmentDefinitions.add(
                new EnrichmentDefinition("EN0","STRING.substring(_28,1,5)",new String[]{"_28"},new String[]{"STRING"}));
        enrichmentDefinitions.add(
                new EnrichmentDefinition("EN1","_4",new String[]{"_4"}));
        enrichmentDefinitions.add(
                new EnrichmentDefinition("EN2","_11",new String[]{"_11"}));
        return enrichmentDefinitions;
    }
    protected static Object getType(String clazz) {

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
