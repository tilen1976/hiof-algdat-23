import java.util.Scanner;
import java.util.Stack;

/**
    Programmet oppretter et kvadratisk sjakkbrett og plasserer springeren på oppgitt startfelt
    Målet er at Springeren besøker alle ruter på brettet en og bare en gang
 */
public class SpringerProblemet {

    //Størrelse på sjakkbrettet
    int n;

    //Markere rute som fri
    int fri = -1;

    //Registere flytt i kronologisk rekkefølge
    int flyttNr;

    //Oversikt over totalt antall flytt i et spill
    int totaltAntallFlytt;

    //Startposisjon for x og y
    int startX;

    int startY;

    //Stack for å ta vare på ruten i kronologisk rekkefølge
    Stack<String> ruteStack = new Stack<>();


    /**
     * Metode som tester om en rute i en 2D matrise er innenfor brettets kanter og markert som ledig
     */
    private boolean erLovligFlytt(int[][] losning, int x, int y){
        if(x >= 0 && x < n && y >= 0 && y < n){
            return losning[x][y] == fri;
        }
        return false;
    }


    /**
     * Metode som luker ut ulovlig input ved å sammenligne med returverdien fra
     * Integer.signum() - den returnerer -1 for negative tall, 0 for 0 og 1 for positive tall
     */
    private boolean erIkkeLovligInput(int sig, int input){

        if(Integer.signum(input) < sig){
            System.out.println("For lavt heltall. Velg på nytt: ");
            return true;
        }
        return false;
    }


    //input fra bruker
    private void input(){

        Scanner scanner = new Scanner(System.in);
        System.out.print("Velg antall rader/kolonner for sjakkbrettet: ");
        n = scanner.nextInt();

        while(erIkkeLovligInput(1, n)){
            n = scanner.nextInt();
        }

        System.out.println("Velg startposisjon for X og Y på sjakkbrettet. Heltall >= 0 og < antall rader/kolonner.");
        System.out.print("X: ");
        startX = scanner.nextInt();

        while(erIkkeLovligInput(0, startX)){
            startX = scanner.nextInt();
        }

        System.out.print("Y: ");
        startY = scanner.nextInt();

        while(erIkkeLovligInput(0, startY)){
            startY = scanner.nextInt();
        }

    }


    /**
     * Metode for utførelse av springerens trekk, som rekursivt sjekker mulige trekk
     * @return true hvis det finnes en løsning
     */
    private boolean springerRunden(int[][] losning, int x, int y, int flyttNr, int[] flyttX, int[] flyttY) {

        this.flyttNr = flyttNr;

        //Basis for rekursjonen - avsluttes når antall flytt er lik antall ruter på brettet
        if(flyttNr == n * n){
            return true;
        }

        //Loop som sjekker springerens åtte mulige trekk
        for(int i = 0; i < 8; i++){
            int nesteX = x + flyttX[i];
            int nesteY = y + flyttY[i];

            totaltAntallFlytt += 1;

            //Hvis trekket er lovlig så nummereres posisjonen
            if(erLovligFlytt(losning, nesteX, nesteY)){
                losning[nesteX][nesteY] = flyttNr;

                //Rekursivt kall på metoden
                if(springerRunden(losning, nesteX, nesteY, flyttNr+1, flyttX, flyttY)){
                    //Legger til posisjonen som String i stack for logging
                    ruteStack.add(flyttNr + " [" + nesteX + "," + nesteY + "]");
                    return true;
                }
                //Bactracker når ruten ikke fører fram ved å resette verdien på ruten
                else{
                    losning[nesteX][nesteY] = fri;
                }
            }

        }

        return false;
    }


    /**
     * Metode som setter opp nødvendige data for bruk i springerRunden().
     * Kjører springerRunden() og skriver ut resultatet
     */
    public void springerensHjelper() {
        int i, j;

        //Input fra bruker
        input();

        //Initialiserer 2D matrise og markerer alle ruter som fri
        int[][] losning = new int[n][n];
        for(i = 0; i < n; i++){
            for(j = 0; j < n; j++){
                losning[i][j] = fri;
            }
        }

        //De åtte mulige flyttene for en springer
        int[] flyttX = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] flyttY = {1, 2, 2, 1, -1, -2, -2, -1};

        //Startfelt markeres med 0
        losning[startX][startY] = 0;

        //Hvis vellykket løsning så skrives matrise ut
        if(springerRunden(losning,startX ,startY, 1, flyttX, flyttY)){
            System.out.println("\n");
            for(i = 0; i < n; i++) {
                for(j = 0; j < n; j++) {
                    System.out.print(losning[i][j]+"\t");
                }
                System.out.println("\n");
            }
        }
        else{
            System.out.println("Ingen løsning!");
        }
    }

    /**
     * Utskriftsmetode som rapporterer antall flytt totalt og ruten springeren gikk hvis
     * problemet ble løst
     * */
    public void skrivUt(){

        System.out.println("Totalt antall flytt: " + totaltAntallFlytt + "\n");

        int size = ruteStack.size();
        if(size != 0){
            System.out.println("Dagens rute: \n");
        }
        for(int i = 0; i < size; i++){
            System.out.println(ruteStack.pop());
        }
    }


    public static void main(String[] args) {

        SpringerProblemet springer = new SpringerProblemet();

        springer.springerensHjelper();

        springer.skrivUt();
    }

}
