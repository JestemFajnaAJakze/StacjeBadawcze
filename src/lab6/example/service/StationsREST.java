package lab6.example.service;

import lab6.example.PATCH;
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
        if (stations.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else {
            return Response.ok(stations, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/literary-substances") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLiterarySubstances() {
        if (literarySubstances.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else {
            return Response.ok(literarySubstances, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/airquality/{stationId}") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStation(@PathParam("stationId") String stationId) {
        for (StationPOJO stationPOJO : stations) {
            if (stationPOJO.getStationId().equals(stationId)) {
                return Response.ok(stationPOJO, MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/airquality/{stationId}") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStation(StationPOJO stationPOJO, @PathParam("stationId") String stationId) {
        for (StationPOJO station : stations) {
            if (station.getStationId().equals(stationId))
            {
                station = stationPOJO;
                return Response.ok(station, MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }



//    @PUT
//    @Path("/substances/{id}") //update subs o podanym id
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response modifySubstance(SubstancePOJO substancePOJO, @PathParam("id") String id) {
//        for (int i = 0; i < substances.size(); i++) {
//            if (substances.get(i).getSubstanceId().equals(id)) {
//                substances.get(i).setSubstanceId(substancePOJO.getSubstanceId());
//                substances.get(i).setSubstanceName(substancePOJO.getSubstanceName());
//                substances.get(i).setUnit(substancePOJO.getUnit());
//                substances.get(i).setTreshold(substancePOJO.getTreshold());
//                updateEmulatorAndStacjeBadawczeSubstancesLibrary();
//                return Response.ok(substances.get(i), MediaType.APPLICATION_JSON).build();
//            }
//        }
//        return Response.status(Response.Status.NO_CONTENT).build();
//    }

    @GET
    @Path("/airquality/{stationId}/substance/{substanceId}")
    //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubstance(@PathParam("stationId") String stationId, @PathParam("substanceId") String substanceId) {
        for (StationPOJO stationPOJO : stations) {
            if (stationPOJO.getStationId().equals(stationId)) {
                for (SubstancePOJO substancePOJO : stationPOJO.getSubstances()) {
                    if (substancePOJO.getType().equals(substanceId)) {
                        return Response.ok(substancePOJO, MediaType.APPLICATION_JSON).build();
                    }
                }
            }
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PATCH
    @Path("/airquality/{stationId}/substance/{substanceId}")
    //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubstance(SubstancePOJO substance, @PathParam("stationId") String stationId, @PathParam("substanceId") String substanceId) {
        for (StationPOJO stationPOJO : stations) {
            if (stationPOJO.getStationId().equals(stationId)) {
                for (SubstancePOJO substancePOJO : stationPOJO.getSubstances()) {
                    if (substancePOJO.getType().equals(substanceId)) {
                        substancePOJO.setValue(substance.getValue());
                        return Response.ok(substancePOJO, MediaType.APPLICATION_JSON).build();
                    }
                }
            }
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @POST
    @Path("/airquality/{stationId}") //dodaje dane dla stacji pomiarowej o wskazanym id. - to wywołuje emulator
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStation(@PathParam("stationId") String stationId, StationPOJO stationPOJO) {
        System.out.println("Otrzymano dane pomiarowe"); //TODO nadpisywanie danych o stacjach
        if (stationId.isEmpty() || stationPOJO.getStationAddress() == null || stationPOJO.getSubstances().isEmpty()) {
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
        for (LiterarySubstancePOJO literarySubstanceToUpdate : literarySubstancesToUpdate) {
            boolean isLiterarySubstanceExist = false;
            for (LiterarySubstancePOJO literarySubstance : literarySubstances) {
                if (literarySubstance.getSubstanceId().equals(literarySubstanceToUpdate.getSubstanceId())) {
                    literarySubstance.setSubstanceName(literarySubstanceToUpdate.getSubstanceName());
                    literarySubstance.setUnit(literarySubstanceToUpdate.getUnit());
                    literarySubstance.setTreshold(literarySubstanceToUpdate.getTreshold());
                    isLiterarySubstanceExist = true;
                }
            }
            if (!isLiterarySubstanceExist) {
                literarySubstances.add(literarySubstanceToUpdate);
            }
        }


        for (LiterarySubstancePOJO literarySubstance : literarySubstances) {
            System.out.println(literarySubstance.getSubstanceId() + " " + literarySubstance.getSubstanceName());
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
                    if (literarySubstancePOJO.getSubstanceId().equals(substance.getType())) {
                        literarySubstance = literarySubstancePOJO;
                        break;
                    }
                }
                if (substance.getValue() > literarySubstance.getTreshold()) {
                    alarmStations.add(station);
                    break;
                }
            }
        }
        if(alarmStations.isEmpty()){
            return Response.status(Response.Status.NO_CONTENT).build();
        }else {
            return Response.ok(alarmStations, MediaType.APPLICATION_JSON).build();
        }
    }
}
