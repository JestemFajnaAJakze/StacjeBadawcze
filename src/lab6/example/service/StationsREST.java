package lab6.example.service;

import lab6.rest.pojo.LiterarySubstancePOJO;
import lab6.rest.pojo.StationPOJO;
import lab6.rest.pojo.SubstancePOJO;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("")
public class StationsREST {
    List<StationPOJO> stations = new ArrayList<StationPOJO>();
    List<LiterarySubstancePOJO> literarySubstances = new ArrayList<LiterarySubstancePOJO>();
    //android pobiera te dwie listy i je razem wyśiwetla


    private static final String REST_URI = "http://localhost:8080/lab6_v2Web";
    private Client restClient;
    private WebTarget resourceTarget;

    public StationsREST() {
    }

    @GET
    @Path("/airquality") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStations() {
        return Response.ok(stations, MediaType.APPLICATION_JSON).build();
    }
//    @GET
//    @Path("/airquality/{stationId") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getStations(@PathParam("stationId")String stationId) {
//        return Response.ok(stations, MediaType.APPLICATION_JSON).build();
//    }
    @POST
    @Path("/airquality/{stationId}") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStation(@PathParam("stationId")String stationId, StationPOJO stationPOJO) {
        System.out.println("Otrzymano dane pomiarowe"); //TODO nadpisywanie danych o stacjach
        if(stationId.isEmpty() || stationPOJO.getStationAddress() == null || stationPOJO.getSubstances().isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            for (StationPOJO station : stations) {
                if (station.getStationId().equals(stationId)) {
                    station.setSubstances(stationPOJO.getSubstances());
                    return Response.status(Response.Status.CONFLICT).build();
                }
            }
            stations.add(stationPOJO);
        }
        return Response.status(Response.Status.CREATED).build();
    }


    @POST
    @Path("/literary-substances") //nadpisuje słownik substancjigg
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStation(List<LiterarySubstancePOJO> literarySubstancesToUpdate) {
        for (LiterarySubstancePOJO literarySubstanceToUpdate : literarySubstancesToUpdate){
            boolean isLiterarySubstanceExist = false;
            for (LiterarySubstancePOJO literarySubstance : literarySubstances){
                if(literarySubstance.getSubstanceId().equals(literarySubstanceToUpdate.getSubstanceId())){
                    literarySubstance.setSubstanceName(literarySubstanceToUpdate.getSubstanceName());
                    literarySubstance.setUnit(literarySubstanceToUpdate.getUnit());
                    literarySubstance.setTreshold(literarySubstanceToUpdate.getTreshold());
                    isLiterarySubstanceExist = true;
                }
            }
            if(!isLiterarySubstanceExist){
                literarySubstances.add(literarySubstanceToUpdate);
            }
        }


        for (LiterarySubstancePOJO literarySubstance : literarySubstances){
            System.out.println(literarySubstance.getSubstanceId()  + " " + literarySubstance.getSubstanceName());
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/airquality/alarm") // 1 GET zwraca stacje gdzie przekrozono próg
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAlarmStations() {
        List<StationPOJO> alarmStations = new ArrayList<>();
        for (StationPOJO station : stations) {
            for (SubstancePOJO substance : station.getSubstances()) {
                LiterarySubstancePOJO literarySubstance = null;
                for (LiterarySubstancePOJO literarySubstancePOJO : literarySubstances) {
                    if(literarySubstancePOJO.getSubstanceId().equals(substance.getType())){
                        literarySubstance = literarySubstancePOJO;
                        break;
                    }
                }
                if(substance.getValue() > literarySubstance.getTreshold()){
                    alarmStations.add(station);
                    break;
                }
            }
        }
        return Response.ok(alarmStations, MediaType.APPLICATION_JSON).build();
    }
}
