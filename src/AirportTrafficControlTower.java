import java.util.LinkedList;
import java.util.Queue;

//Kontrolltårnet håndterer trafikken på flyplassen
//Køer for å lande og lette

public class AirportTrafficControlTower {

    int planesArriving, planesDeparting = 0;

    Queue<Plane> landingQueue = new LinkedList<>();
    Queue<Plane> takeOffQueue = new LinkedList<>();

    //Antall i kø per tidsenhet
    int numQueueLanding;
    int numQueueTakeOff;

    //avvist i luften (her blir også fly i landingskø lagt til når flyplassen stenger)
    int rejectedPlanes;

    //Totalt antall fly som ankom, både de som blir avvist og de som får lande
    int totalArrived;

    //håndterer trafikken inn
    //Hvis plass i landingskø opprettes flyobjekt, tidspunkt settes og legges til i kø
    public void inComingTraffic(Airport airport) {
        for (int i = 0; i < planesArriving; i++) {
            if (landingQueue.size() < 10) {
                Plane plane = new Plane();
                plane.setTimeArrived(Airport.timeSteps);
                System.out.println(plane + " har fått plass i landingskø");
                landingQueue.add(plane);
            } else {
                System.out.println("Beklager, landingskø på " + airport.name + " er full");
                rejectedPlanes += 1;
            }
        }

        totalArrived += planesArriving;
        numQueueLanding += landingQueue.size();

    }

    //Håndtere utgående trafikk
    //Henter tilfeldig antall fra fly som står parkert på flyplassen
    //Hvis plass i kø så fjernes flyet fra parkerte fly og legges i kø
    public void outGoingTraffic(Airport airport) {

        while (!airport.planesOnAirport.isEmpty() && planesDeparting > 0) {
            for (int i = 0; i < planesDeparting; i++) {
                if (takeOffQueue.size() < 10 && airport.planesOnAirport.size() > 0) {
                    Plane plane = airport.planesOnAirport.remove(airport.planesOnAirport.size()-1);
                    plane.setTimeTakeOff(Airport.timeSteps);
                    takeOffQueue.add(plane);
                    System.out.println(plane + " er satt i kø for takeOff");
                } else {
                    System.out.println("Sorry takeoff kø er full, prøv igjen senere");
                }
            }
            planesDeparting--;
            numQueueTakeOff += takeOffQueue.size();
        }
    }
}
