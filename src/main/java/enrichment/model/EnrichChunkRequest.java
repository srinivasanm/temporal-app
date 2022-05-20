package enrichment.model;

public class EnrichChunkRequest extends EnrichmentData{
    private String idStart;
    private String idEnd;

    public String getIdStart() {
        return idStart;
    }

    public void setIdStart(String idStart) {
        this.idStart = idStart;
    }

    public String getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(String idEnd) {
        this.idEnd = idEnd;
    }

    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("_id Start : ").append(idStart).append("_id End : ").append(idEnd);
        return sb.toString();
    }
}
