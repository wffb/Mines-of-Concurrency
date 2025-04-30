import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Station {

    public final int index;

    private  Cart cart = null;
    private Semaphore cartSem = new Semaphore(1);


    private AtomicBoolean isGemHere = new AtomicBoolean(false);

    public Station(int i){
        this.index =i;
    }


    public void setCart (Cart c){

        while (true){
            //cart is being used
            if(!cartSem.tryAcquire())
                continue;
            // not cart in the station for now
            if(Objects.isNull(this.cart)){
                this.cart = c;

                cartSem.release();
                break;
            }
            cartSem.release();
        }
    }

    public Cart getCartAndGem(){



        Cart cartSet=null;

        while(true){

            //not gem here or cart is being used
            if(!isGemHere.get()  || !cartSem.tryAcquire())
                continue;
            //no cart for now
            if(Objects.isNull(cart)){
                cartSem.release();
                continue;
            }


            //send gem to the cart
             cartSet = cart;
            this.cart = null;
            isGemHere.compareAndSet(true,false);
            cartSet.gems++;

            cartSem.release();
            break;
        }
        return  cartSet;
    }


    public void addGem(){

        while (true){
            // there is already a gem
            if(isGemHere.get())
                continue;
            //add gem
            isGemHere.compareAndSet(false,true);
            break;
        }
    }
}

