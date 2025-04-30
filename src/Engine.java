public class Engine extends Thread {

    private Object from;
    private Object to;

    private Cart cart;

    public Engine(Object from,Object to){
        this.from = from;
        this.to = to;
    }


    private void getCart() {


        //get from station
        if(from instanceof Station){
            cart = ((Station) from).getCartAndGem();
            System.out.println(cart.toString()+"loaded with a gem");
            System.out.println(cart.toString()+"collected from station "+((Station) from).index);
        }


        //get from elevator
        if(from instanceof Elevator){
            cart = ((Elevator) from).getNewCart();
            System.out.println(cart.toString()+"collected from station elevator");
        }

    }


   private void setCart() {

       //get from station
       if(to instanceof Station){
           ((Station) to).setCart(cart);
           System.out.println(cart.toString()+"delivered to station "+((Station) to).index);
           cart=null;
       }

       //get from cart
       if(to instanceof Elevator){
           ((Elevator) to).putFinishedCart(cart);
           System.out.println(cart.toString()+"delivered to elevator");
           cart=null;
       }



    }


    public void run() {

            while(!this.isInterrupted()) {
                try {
                    getCart();
                    Thread.sleep(Params.ENGINE_TIME);
                    setCart();
                }catch (Exception e){
                    System.out.println(e);
                }
            }


    }


}
