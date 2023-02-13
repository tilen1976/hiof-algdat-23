// Klasse for fly.

public class Plane {

    //tid satt i kø
    int timeArrived;

    //tid landet
    int timeLanded;

    //ventetider
    int waitingTimeLandingQueue;

    int waitingTimeTakeOffQueue;

    //tid satt i kø
    int timeTakeOff;

    //tid lettet
    int timeDeparted;

    //id
    int planeId;
    static int flightRegister;

    public Plane(){
        this.planeId = 10 + flightRegister++;
    }

    public void setTimeArrived(int timeArrived) {
        this.timeArrived = timeArrived;
    }

    public void setTimeLanded(int timeLanded) {
        this.timeLanded = timeLanded;
    }

    public void setTimeTakeOff(int timeTakeOff) {
        this.timeTakeOff = timeTakeOff;
    }

    public void setTimeDeparted(int timeDeparted) {
        this.timeDeparted = timeDeparted;
    }

    //utregning ventetider i kø
    public void calcWaitingTimeLanding(){
        this.waitingTimeLandingQueue = this.timeArrived - this.timeLanded;
        System.out.println("Fly["+ this.planeId + "] ventet i landingskø: " + this.waitingTimeLandingQueue + " tidsenheter");
    }

    public void calcWaitingTimeDeparture(){
        this.waitingTimeTakeOffQueue = this.timeTakeOff - this.timeDeparted;
        System.out.println("Fly[" + this.planeId + "] ventet i take-off-kø:  " + this.waitingTimeTakeOffQueue + " tidsenheter");
    }

    public int getWaitingTimeLandingQueue() {
        return waitingTimeLandingQueue;
    }

    public int getWaitingTimeTakeOffQueue() {
        return waitingTimeTakeOffQueue;
    }

    @Override
    public String toString() {
        return "Fly["+ planeId + "]";
    }

}
