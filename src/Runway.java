import java.util.Queue;

// Klasse for rullebanen, tar seg av landing og avganger. Samler totalt antall for landet, lettet.
// Kalkulerer ventetid for et fly og legger dette sammen for totale ventetider

public class Runway {

    int totalLanded;
    int totalDepartures;

    int emptyRunwaySlots = 0;

    int totalWaitingLanding;
    int totalWaitingTakeOff;


    // Metode for å lande et fly.
    // Fjerner fly fra landingskø, legger i liste for parkerte fly, setter tidspunkt landing
    public void landing(Queue<Plane> queue, Airport airport) {
        if (!queue.isEmpty()) {
            Plane plane = queue.remove();
                totalLanded += 1;
                airport.planesOnAirport.add(plane);
                plane.setTimeLanded(Airport.timeSteps);
                System.out.println(plane + " landet");
                plane.calcWaitingTimeLanding();
                totalWaitingLanding += plane.getWaitingTimeLandingQueue();
            }

    }

    // Metode for å lette, Fjerner fly fra takeoff-kø
    // Setter tid avgang og ventetid i kø
    public void departing(Queue<Plane> queue) {
        if (!queue.isEmpty()) {
            Plane plane = queue.remove();
            totalDepartures += 1;
            plane.setTimeDeparted(Airport.timeSteps);
            System.out.println(plane + " har tatt av");
            plane.calcWaitingTimeDeparture();
            totalWaitingTakeOff += plane.getWaitingTimeTakeOffQueue();
        }
    }

}
