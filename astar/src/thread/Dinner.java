package thread;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * 通过模拟哲学家问题来观察死锁的产生
 * 下面的代码是模拟的五个哲学家和五根筷子的经典哲学家问题
 * */

/**
 * 描述：我们知道一个对象可以有synchronized方法或其他形式的加锁机制来防止
 * 别的线程在互斥还没释放的时候就访问这个对象。而且我们知道线程是会变成阻塞状
 * 态的(挂起)，所以有时候就会发生死锁的情况：某个任务在等待另一个任务，而后者
 * 又在等待其它任务，这样一直下去，知道这个链条下的任务又在等待第一个任务释放锁，
 * 这样就形成了一个任务之间相互等待的连续循环，没有任务可以继续的情况。死锁的最
 * 大问题在于它发生的几率非常小，并不是我们一运行程序它就死锁了，而是会不知道那
 * 个时候程序就死锁并且我们很难重现当时出现死锁的情况。在这篇博客里我们只是从哲
 * 学家就餐问题的程序中感受下死锁现象，随便结合分析下出现死锁的几个条件，并不会
 * 讨论如何避免/解决死锁问题。哲学家就餐问题是一个经典的关于死锁的问题，其大概的
 * 描述是：有五个哲学家，他们会花部分时间思考，花部分时间就餐。当他们思考的时候，
 * 他们不需要共享任何资源互不影响。而当他们就餐时，因为这里只有五只筷子，在他们每
 * 人都需要两只筷子的情况下，就会形成对筷子的竞争。这个问题并不是说百分百的会死锁，
 * 仅仅是存在这种可能而已。下面的代码就演示了这个问题，在注释里有对更多的细节的解释：
 * 
 * 
 * 学过操作系统的都知道要发生死锁有四个条件需要满足，下面我们就结合上面的问题来分析
 * 下这四个条件： 
 * 1).互斥。线程使用的资源中至少有一个是不能共享的。这里，一根Chopstick一次就只能
 * 		被一个Philosopher使用 
 * 2).至少有一个任务它必须持有一个资源且正在等待获取一个当前被别的任务持有的资源。也
 * 		就是说，要发生死锁，Philosopher必须持有一个Chopstick并正在等待另一根。 
 * 3）.资源不能被任务抢占，任务必须把释放资源当成普通事件。在上面中Philosopher不会
 * 		去抢其它Philosopher手中的Chopstick。 
 * 4).必须有循环等待，这时候一个任务等待其它任务所持有的资源，后者又在等待另一个任务
 * 		持有的资源，这样一直下去，直到一个任务在等待第一个任务所持有的资源。在上面的
 * 		代码中每个Philosopher都会试图得到左边的Chopstick然后得到右边的Chopstick，
 * 		这样就产生了循环等待。
 * 死锁出现必须要满足上面四个条件，所以避免死锁的方法就是破坏四个条件中的一个。上面有一
 * 段注释掉的代码通过调整最后一位哲学家获取Chopstick的顺序(与其它哲学家相反)而达到了
 * 破坏第四个循环等待条件的效果，从而防止了死锁的产生。其它的防止死锁的方法就不在这里讨论了。
 * Java中并没有从语言层面上有防止死锁的措施，所以要防止死锁的产生，只有靠我们自己的小心谨慎了。
 * @author wxx
 *
 */
//共享资源：筷子类
class Chopstick{
    private boolean token=false; //该筷子是否被使用了

    //使用筷子，如果该筷子已被别的线程使用，则当前线程调用wait()挂起
    public synchronized void take() throws InterruptedException{
           while(token){
               wait();
           }
           token=true;
    }

    //筷子使用完以后，放下筷子，并唤醒其它正在wait()的线程
    public synchronized void drop(){
        token=false;
        notifyAll();
    }
}

class Philosopers implements Runnable{

    //左边和右边的筷子
    private Chopstick left;
    private Chopstick right;

    private final int id;//哲学家使用筷子的编号
    private final int pauseFactor; //暂停因子
    private Random rand = new Random(200);

    public Philosopers(Chopstick left,Chopstick right,int id,int pauseFactor){
        this.left=left;
        this.right=right;
        this.id=id;
        this.pauseFactor=pauseFactor;
    }

    //暂停随机时间
    private void pause() throws InterruptedException{ 
        TimeUnit.MILLISECONDS.sleep(pauseFactor*rand.nextInt(100));
    }

    public void run(){
        try{
            while(!Thread.interrupted()){
                System.out.println(this+" "+"thinking........... ");//表示正在思考
                pause();//模拟思考时间
                System.out.println(this+" 取得左边的筷子");
                left.take(); //取得左边的筷子
                System.out.println(this+" 取得右边的筷子");
                right.take();
                System.out.println(this+"Eating...........");//就餐
                pause();//模拟就餐时间

                //放下筷子
                left.drop();
                right.drop();
            }
        }catch(InterruptedException ex){
            System.out.println(this+" 通过中断异常退出");
        }
    }

    public String toString(){
        return (id+1)+"号哲学家 ";
    }
}

public class Dinner {

   public static void main(String[] args) throws Exception{

       //可以通过调整pauseFactor从而调整哲学家思考的时间
       //思考时间越短则线程间对共享资源的竞争越强越容易产生死锁问题
       //pauseFacotr等于0的时候几乎每次都可以看到死锁问题的产生
       int pauseFactor=0;
       int size=5;
       Chopstick[] chopstick = new Chopstick[size];

       //五只筷子
       for(int i=0;i<size;i++){
           chopstick[i] = new Chopstick();
       }

       ExecutorService exec = Executors.newCachedThreadPool();
       //产生5个哲学家线程
       for(int i=0; i<size; i++){
          exec.execute(new Philosopers(chopstick[i],chopstick[(i+1)%size],i,pauseFactor));

           //下面的代码通过防止循环等待而阻止了死锁的产生
           //让前四位哲学家总是先拿左边的筷子，再拿右边的筷子，而让第五位哲学家先拿右边的筷子
           //这样可以打破循环等待的条件，实际上这时候第五位哲学家总是会由于取不到右边的筷子
           //而阻塞(它右边的筷子已经被第一位哲学家取了)，所以也就不会去拿它左边的筷子，从而
           //第四位哲学家总是可以取得两只筷子第一个就餐，从而不会产生循环等待的结果(从输出中可以看到这一点)．
           /*if(i<(size-1)){
               exec.execute(new Philosopers(chopstick[i],chopstick[(i+1)%size],i,pauseFactor));
           }
           else{
               exec.execute(new Philosopers(chopstick[(i+1)%size],chopstick[i],i,pauseFactor));
           }*/
       }
       exec.shutdown();
       System.out.println("press 'Enter' to quit");
       System.in.read();
       exec.shutdownNow();
   }
}