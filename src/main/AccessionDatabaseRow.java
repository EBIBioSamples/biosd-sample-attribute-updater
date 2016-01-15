package main;

/**
 * Created by lucacherubin on 07/01/2016.
 */
public class AccessionDatabaseRow {

    private String sampleId;
    private String attrKey;
    private String attrValue;
    private String termSourceRef;
    private String termSourceId;
    private String termSourceUri;
    private String termSourceVersion;
    private String unit;

    public AccessionDatabaseRow(String unparsedString) throws IllegalArgumentException {

        String[] fields = unparsedString.split("\\t");
        if (fields.length != 8) {
            throw new IllegalArgumentException("Provided string is not in the right format");
        }

        sampleId = fields[0];
        attrKey = fields[1];
        attrValue = fields[2];
        termSourceRef = fields[3];
        termSourceId = fields[4];
        termSourceUri = fields[5];
        termSourceVersion = fields[6];
        unit = fields[7];

    }

    public String getSampleId() {
        return sampleId;
    }

    public String getAttrKey() {
        return attrKey;
    }

    public String getAttrValue() {
        return returnValueOrNull(attrValue);
    }

    public String getTermSourceRef() {
        return returnValueOrNull(termSourceRef);
    }

    public String getTermSourceId() {
        return returnValueOrNull(termSourceId);
    }

    public String getTermSourceUri() {
        return returnValueOrNull(termSourceUri);
    }

    public String getTermSourceVersion() {
        return returnValueOrNull(termSourceVersion);
    }

    public String getUnit() {
        return returnValueOrNull(unit);
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%s|%s",
                getSampleId(),getAttrKey(),getAttrValue(),getTermSourceRef(),
                getTermSourceId(),getTermSourceUri(),getTermSourceVersion(),getUnit());
    }

    private boolean isNULL(String value) {
        return value.equals("NULL");
    }

    private String returnValueOrNull(String value) {
        if (this.isNULL(value)) {
            return null;
        }
        return value;
    }

}
