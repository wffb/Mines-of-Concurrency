import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class NoblockingIOHelper {

    //最大队列长度
    private static final int maxListLength = 100;
    //
    public static final int NO_BLOCKING_WAIT_TIME = 100;


    public static void noBlockingInput(Cart cart, Semaphore semaphore, ArrayList<Object> list){

        while (true){
            try {
                //不可操作
                if(!semaphore.tryAcquire())
                    continue;
                //为满
                if( list.size() >= maxListLength){
                    semaphore.release();
                    Thread.sleep(NO_BLOCKING_WAIT_TIME);
                }
                //非满
                else {
                    list.add(cart);

                    semaphore.release();
                    break;
                }

            }catch (Exception e){
            }
        }

    }


    public static Object noBlockingOutput(Semaphore semaphore, ArrayList<Object> list){

        Cart cart;

        while (true){
            try {
                //不可操作
                if(!semaphore.tryAcquire())
                    continue;
                //为空
                if( list.isEmpty()){
                    semaphore.release();
                    Thread.sleep(NO_BLOCKING_WAIT_TIME);
                }
                //非空
                else {
                    int lastOne = list.size()-1;
                    cart = (Cart) list.get(lastOne);
                    list.remove(lastOne);

                    semaphore.release();
                    break;
                }

            }catch (Exception e){
            }
        }

        return  cart;

    }






}