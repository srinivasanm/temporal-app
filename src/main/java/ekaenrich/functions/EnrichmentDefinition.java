package ekaenrich.functions;

public class EnrichmentDefinition {

    private String colName;
    private String colInternalId;
    private String expression;
    private String[] reservedKeywords=new String[]{};
    private String[] dependentCols;

    public EnrichmentDefinition(String colName, String expression, String[] dependentCols) {
        this.colName = colName;
        this.colInternalId=colName;
        this.expression = expression;
        this.dependentCols = dependentCols;
    }

    public EnrichmentDefinition(String colName, String expression, String[] dependentCols,String[] reservedKeywords) {
        this.colName = colName;
        this.colInternalId=colName;
        this.expression = expression;
        this.dependentCols = dependentCols;
        this.reservedKeywords = reservedKeywords;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColInternalId() {
        return colInternalId;
    }

    public void setColInternalId(String colInternalId) {
        this.colInternalId = colInternalId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String[] getReservedKeywords() {
        return reservedKeywords;
    }

    public void setReservedKeywords(String[] reservedKeywords) {
        this.reservedKeywords = reservedKeywords;
    }

    public String[] getDependentCols() {
        return dependentCols;
    }

    public void setDependentCols(String[] dependentCols) {
        this.dependentCols = dependentCols;
    }
}
