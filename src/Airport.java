import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Airport {

    String name;

    //Gjennomsnittsverdier for fly håndtert per tidsenhet
    double averageArriveMean,averageDepartMean;

    //Tiden simuleringen skal vare
    static int timeSteps;
    //tar vare på startverdien til tiden slik at den kan brukes i utregninger
    static int totalTimeSteps;

    //Parkerte fly på flyplassen
    List<Plane> planesOnAirport = new ArrayList<>();

    //Instanser av flytårn og rullebane
    AirportTrafficControlTower controlTower = new AirportTrafficControlTower();
    Runway runway = new Runway();

    //generere et random tall basert på gjennomsnittsverdi
    private static int getPoissonRandom(double mean) {
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        }
        while (p > L);
        return k - 1;
    }

    public Airport(String name){
        this.name = name;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Velkommen til " + name + " flyplass");
        System.out.print("Hvor mange tidssteg skal simuleringen vare?: ");
        timeSteps = scanner.nextInt();
        totalTimeSteps = timeSteps;
        System.out.println("Gjennomsnittelig antall ankomster per tidsenhet? " +
                "(Et tall mellom 0.1 og 1.0): ");
        averageArriveMean = Double.parseDouble(scanner.next());
        System.out.println("Gjennomsnittelig antall avganger per tidsenhet? " +
                "(Et tall mellom 0.1 og 1.0): ");
        averageDepartMean = Double.parseDouble(scanner.next());
    }

    public String getName() {
        return name;
    }

    //Stenger flyplassen, rydder opp fly som fortsatt står i kø.
    // Fly i landingskø blir avvist og fly i takeOff kø blir parkert
    public void closingAirport(){
        System.out.println(getName() + " flyplass stenger nå");
        if (!controlTower.landingQueue.isEmpty()){
            int queueSize = controlTower.landingQueue.size();
            System.out.println("Det er fortsatt " + controlTower.landingQueue.size() +
                    " fly i landingskø. Dere blir nå sendt videre");
            for(int i = 0; i < queueSize; i++){
                controlTower.landingQueue.remove();
                controlTower.rejectedPlanes += 1;
            }
        }
        if (!controlTower.takeOffQueue.isEmpty()){
            int queueSize = controlTower.takeOffQueue.size();
            System.out.println("Det er fortsatt " + controlTower.takeOffQueue.size() +
                    " fly i takeoffkø." +
                    "Dere vil nå bli parkert på bakken");
            for(int i = 0; i < queueSize;i++){
                planesOnAirport.add(controlTower.takeOffQueue.remove());

            }
        }
    }

    //Utregninger av statistikk for "dagen"
    public void calculatePlanesArriving(double mean){
        controlTower.planesArriving = getPoissonRandom(mean);
        System.out.println("Antall fly ankommer: " + controlTower.planesArriving);
    }

    public void calculatePlanesDeparting(double mean){
        controlTower.planesDeparting = getPoissonRandom(mean);
        System.out.println("Antall fly som ønsker å lette: " + controlTower.planesDeparting);
    }

    public void calculatePercentUsedRunway(){
        double percentUsed;
        if(runway.emptyRunwaySlots > 0){
            double usedTime = totalTimeSteps - runway.emptyRunwaySlots;
            percentUsed = (usedTime/totalTimeSteps) * 100;
        }
        else{
            percentUsed = 100.00;
        }
        System.out.println("Rullebanen var opptatt: " + String.format("%.2f", percentUsed) + " % av tiden");
    }

    public void calcAvgWaitingLanding(){
        double avgWaiting;
        double num = runway.totalWaitingLanding;
        avgWaiting = num / controlTower.numQueueLanding;
        if(avgWaiting == 0.00){
            System.out.println("Ingen fly landet");
        }
        else {
            System.out.println("Gjennomsnittelig ventetid i landingskø: " + String.format("%.2f", avgWaiting));
        }
    }

    public void calcAvgWaitingTakeOff(){
        double avgWaiting;
        double num = runway.totalWaitingTakeOff;
        avgWaiting = num / controlTower.numQueueTakeOff;
        if(avgWaiting == 0.00){
            System.out.println("Ingen fly lettet");
        }
        else {
            System.out.println("Gjennomsnittelig ventetid i takeOffkø: " + String.format("%.2f", avgWaiting));
        }
    }

    public void calcAvgSizeLandingQueue(){
        double avgSize;
        double num = controlTower.numQueueLanding;
        avgSize = num /totalTimeSteps;
        System.out.println("Gjennomsnittelig størrelse på landingskø: " + String.format("%.2f",avgSize));

    }

    public void calcAvgSizeTakeOffQueue(){
        double avgSize;
        double num = controlTower.numQueueTakeOff;
        avgSize = num /totalTimeSteps;
        System.out.println("Gjennomsnittelig størrelse på takeoffkø: " + String.format("%.2f",avgSize));
    }


    //Prioriterer fly som vil lande, hvis ledig kan et fly ta av
    //Ellers så er rullebanen ledig
    public void controlRunway(){
        if(!controlTower.landingQueue.isEmpty()){
            runway.landing(controlTower.landingQueue, this);
        }
        else if(!controlTower.takeOffQueue.isEmpty()){
            runway.departing(controlTower.takeOffQueue);
        }
        else{
            System.out.println("Rullebanen er tom");
            runway.emptyRunwaySlots ++;
        }
    }

    //Utskrift og utregninger etter stengetid
    void printReport(){
        System.out.println(
                "\nRapport " + name +
                ":\nAntall fly som ble avvist i luften: " +
                controlTower.rejectedPlanes + "\nAntall fly som ankom: " + controlTower.totalArrived +
                "\nAntall fly landet: " + runway.totalLanded +
                "\nAntall fly som lettet: " + runway.totalDepartures +
                "\nAntall fly parkert for natten på " + name + " flyplass: " + planesOnAirport.size());

        calculatePercentUsedRunway();
        calcAvgWaitingLanding();
        calcAvgWaitingTakeOff();
        calcAvgSizeLandingQueue();
        calcAvgSizeTakeOffQueue();

    }

    //simulering med tidsenheter
    //Kalkulerer antall fly som ankommer/vil ta av
    //Flytårnet håndterer trafikken
    //Når tiden er ute, stenges flyplassen og rapport skrives ut
    void simulateAirport() {
        while (timeSteps != 0){
            System.out.println("Time: " + timeSteps);
            calculatePlanesArriving(averageArriveMean);
            controlTower.inComingTraffic(this);
            calculatePlanesDeparting(averageDepartMean);
            controlTower.outGoingTraffic(this);

            controlRunway();

            timeSteps --;
            System.out.println();
        }

        closingAirport();
        printReport();
    }

    public static void main(String[] args) {
        Airport airport = new Airport("Nanotopia");
        airport.simulateAirport();

    }
}
