package enrichment.model;

import java.util.List;

public class EnrichmentRequest extends  EnrichmentData{

    private List<String> idIntervals;

    public List<String> getIdIntervals() {
        return idIntervals;
    }

    public void setIdIntervals(List<String> idIntervals) {
        this.idIntervals = idIntervals;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Collection Id : ").append(this.getCollectionId())
                .append("Source Collection : ").append(this.getSourceCollection())
                .append("Sink Collection : ").append(this.getSinkCollection());
        return sb.toString();
    }
}
