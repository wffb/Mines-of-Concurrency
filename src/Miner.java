public class Miner extends Thread {

    private Station station;

    public Miner(Station s){
        station = s;
    }


    public void run() {

        while(!this.isInterrupted()) {

                    try {
                        Thread.sleep(Params.MINING_TIME);
                        station.addGem();
                    }catch (Exception e){
                        System.out.println(e);
                    }
        }
    }
}
