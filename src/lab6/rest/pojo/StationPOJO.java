package lab6.rest.pojo;

import java.util.List;

public class StationPOJO {

    private String stationId;
    private Address stationAddress;
    private List<LiterarySubstancePOJO> substances;

    public StationPOJO() {
    }

    public StationPOJO(String stationId, Address stationAddress, List<LiterarySubstancePOJO> substances) {
        this.stationId = stationId;
        this.stationAddress = stationAddress;
        this.substances = substances;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Address getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(Address stationAddress) {
        this.stationAddress = stationAddress;
    }

    public List<LiterarySubstancePOJO> getSubstances() {
        return substances;
    }

    public void setSubstances(List<LiterarySubstancePOJO> substances) {
        this.substances = substances;
    }
}
