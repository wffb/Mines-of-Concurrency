import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Elevator {



   private Semaphore topSem = new Semaphore(1);
   private Semaphore bottomSem = new Semaphore(1);

    // top-false bottom-true
   private AtomicBoolean position = new AtomicBoolean(false);

   private Cart cartStored =null;

    public Elevator(){

    }


    public Cart depart() {
        Cart c = null;

        while (true){

            if(position.get() || !topSem.tryAcquire())
                continue;
            //空电梯
            if( Objects.isNull(cartStored) || cartStored.gems ==0 ){
                topSem.release();
                continue;
            }

            //非空
             c = this.cartStored;
            this.cartStored =null;
            topSem.release();
            break;
        }
        return c;
    }


    public void arrive(Cart cart) {

        while (true){

            if(position.get() || !topSem.tryAcquire())
                continue;
            //非空电梯
            if(!Objects.isNull(cartStored)){
                topSem.release();
                continue;
            }
            //非空
            this.cartStored=cart;
            topSem.release();
            break;
        }
    }


    public Cart getNewCart() {

        Cart c = null;
        while (true){

            if( !position.get() || !bottomSem.tryAcquire())
                continue;
            //空电梯
            if(Objects.isNull(cartStored)|| cartStored.gems !=0){
                bottomSem.release();
                continue;
            }
            //非空
            c = this.cartStored;
            this.cartStored =null;

            bottomSem.release();
            break;
        }
        return c;
    }


    public void putFinishedCart(Cart cart) {

        while (true){

            if( !position.get() || !bottomSem.tryAcquire())
                continue;
            //非空电梯
            if(!Objects.isNull(cartStored)){
                bottomSem.release();
                continue;
            }
            //空
            this.cartStored=cart;

            bottomSem.release();
            break;
        }
    }




    // position control
    public boolean getPosition(){
        return position.get();
    }

    //During the time when other operations are in progress, we do not want the elevator to move.
    public void changePosition(){

        Random random = new Random();

        while (true){

            boolean posNow = getPosition();

            if(posNow){
                //bottom
                if(!bottomSem.tryAcquire())
                    continue;
                //elevator is not empty
                boolean nextPosition = !posNow;
                //elevator is empty
                if(Objects.isNull(cartStored)){
                    nextPosition = random.nextBoolean();
                }

                position.compareAndSet(posNow,nextPosition);

                try {
                    if(posNow!=nextPosition){
                        Thread.sleep(Params.ELEVATOR_TIME);
                        showPositionChange(posNow,nextPosition);
                    }
                }catch (Exception e){
                    System.out.println(e);
                }

                bottomSem.release();
                break;

            }else {

                //top
                if(!topSem.tryAcquire())
                    continue;
                //elevator is not empty
                boolean nextPosition = !posNow;
                //elevator is empty
                if(Objects.isNull(cartStored)){
                    nextPosition = random.nextBoolean();
                }

                position.compareAndSet(posNow,nextPosition);
                try {
                    if(posNow!=nextPosition){
                        Thread.sleep(Params.ELEVATOR_TIME);
                        showPositionChange(posNow,nextPosition);
                    }
                }catch (Exception e){
                    System.out.println(e);
                }

                topSem.release();
                break;
            }

        }
    }

    private void showPositionChange(Boolean posNow, Boolean nextPosition){

        String cartString="(empty)";
        if(!Objects.isNull(cartStored))
            cartString = "with "+cartStored.toString();
        if(nextPosition!=posNow){

            //to bottom
            if(nextPosition)
                System.out.println("elevator descends with "+cartString);
            //to top
            else
                System.out.println("elevator ascends "+cartString);
        }
    }


}
