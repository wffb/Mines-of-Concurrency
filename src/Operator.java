import java.util.Objects;

public class Operator extends Thread{

    private Elevator e;


    public Operator(Elevator e){
        this.e=e;
    }


    public void run() {

        while(!this.isInterrupted()) {
            try {

                Thread.sleep(Params.operatorPause());
                e.changePosition();

            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

}
