import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class NoBlockingElevator {




    /**
     * 在矿井中应当尽可能避免电梯本身的阻塞或是等待，因此决定采用几个IO缓冲区来提升IO效率
     */

    private Semaphore cartStoredSem = new Semaphore(1);
    private Cart cartStored;

    public  void setStored(Cart c){
        try {

            consumeSem.acquire();
            this.cartStored = c;
            consumeSem.release();
        }catch (Exception e){

        }
    }

    public  Cart  getStored(){

        Cart c = null;

        try {
            consumeSem.acquire();
            c = this.cartStored;
            this.cartStored=null;
            consumeSem.release();
        }catch (Exception e){
        }

        return c;
    }



    /**
     *  地上矿车回收
     */

    private ArrayList<Object> consumeWaitingList = new ArrayList<>();
    private Semaphore consumeSem = new Semaphore(1);
    //获取

    public Cart depart() {
        return (Cart)NoblockingIOHelper.noBlockingOutput(consumeSem,consumeWaitingList);
    }


    public void putDepart(Cart cart) {
        NoblockingIOHelper.noBlockingInput(cart,consumeSem,consumeWaitingList);
    }


    /**
     * 地上矿车放入
     */
    private ArrayList<Object> produceWaitingList = new ArrayList<>();
    private Semaphore produceSem = new Semaphore(1);
    //放入

    public void arrive(Cart cart) {
        NoblockingIOHelper.noBlockingInput(cart,produceSem,produceWaitingList);
    }

    public Cart getArrival() {
        return (Cart) NoblockingIOHelper.noBlockingOutput(produceSem,produceWaitingList);
    }


    /**
     * 地下部分矿车放入
     */
    private ArrayList<Object> bottomInputeWaitingList = new ArrayList<>();
    private Semaphore bottomInputSem = new Semaphore(1);


    public void putNewCart(Cart cart) {
        NoblockingIOHelper.noBlockingInput(cart,bottomInputSem,bottomInputeWaitingList);
    }


    public Cart getNewCart() {
        return (Cart) NoblockingIOHelper.noBlockingOutput(bottomInputSem,bottomInputeWaitingList);
    }


    /**
     *  地下部分矿车取出
     */
    private ArrayList<Object> bottomOutputeWaitingList = new ArrayList<>();
    private Semaphore bottomOutputSem = new Semaphore(1);

    public void putFinishedCart(Cart cart) {
        NoblockingIOHelper.noBlockingInput(cart,bottomOutputSem,bottomOutputeWaitingList);
    }


    public Cart getFinishedCart() {
        return (Cart) NoblockingIOHelper.noBlockingOutput(bottomOutputSem,bottomOutputeWaitingList);
    }


}


