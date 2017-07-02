package thread;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * ͨ��ģ����ѧ���������۲������Ĳ���
 * ����Ĵ�����ģ��������ѧ�Һ�������ӵľ�����ѧ������
 * */

//������Դ��������
class Chopstick{
    private boolean token=false; //�ÿ����Ƿ�ʹ����

    //ʹ�ÿ��ӣ�����ÿ����ѱ�����߳�ʹ�ã���ǰ�̵߳���wait()����
    public synchronized void take() throws InterruptedException{
           while(token){
               wait();
           }
           token=true;
    }

    //����ʹ�����Ժ󣬷��¿��ӣ���������������wait()���߳�
    public synchronized void drop(){
        token=false;
        notifyAll();
    }
}

class Philosopers implements Runnable{

    //��ߺ��ұߵĿ���
    private Chopstick left;
    private Chopstick right;

    private final int id;//��ѧ��ʹ�ÿ��ӵı��
    private final int pauseFactor; //��ͣ����
    private Random rand = new Random(200);

    public Philosopers(Chopstick left,Chopstick right,int id,int pauseFactor){
        this.left=left;
        this.right=right;
        this.id=id;
        this.pauseFactor=pauseFactor;
    }

    //��ͣ���ʱ��
    private void pause() throws InterruptedException{ 
        TimeUnit.MILLISECONDS.sleep(pauseFactor*rand.nextInt(100));
    }

    public void run(){
        try{
            while(!Thread.interrupted()){
                System.out.println(this+" "+"thinking........... ");//��ʾ����˼��
                pause();//ģ��˼��ʱ��
                System.out.println(this+" ȡ����ߵĿ���");
                left.take(); //ȡ����ߵĿ���
                System.out.println(this+" ȡ���ұߵĿ���");
                right.take();
                System.out.println(this+"Eating...........");//�Ͳ�
                pause();//ģ��Ͳ�ʱ��

                //���¿���
                left.drop();
                right.drop();
            }
        }catch(InterruptedException ex){
            System.out.println(this+" ͨ���ж��쳣�˳�");
        }
    }

    public String toString(){
        return (id+1)+"����ѧ�� ";
    }
}

public class Dinner {

   public static void main(String[] args) throws Exception{

       //����ͨ������pauseFactor�Ӷ�������ѧ��˼����ʱ��
       //˼��ʱ��Խ�����̼߳�Թ�����Դ�ľ���ԽǿԽ���ײ�����������
       //pauseFacotr����0��ʱ�򼸺�ÿ�ζ����Կ�����������Ĳ���
       int pauseFactor=0;
       int size=5;
       Chopstick[] chopstick = new Chopstick[size];

       //��ֻ����
       for(int i=0;i<size;i++){
           chopstick[i] = new Chopstick();
       }

       ExecutorService exec = Executors.newCachedThreadPool();
       //����5����ѧ���߳�
       for(int i=0; i<size; i++){
          exec.execute(new Philosopers(chopstick[i],chopstick[(i+1)%size],i,pauseFactor));

          /*//����Ĵ���ͨ����ֹѭ���ȴ�����ֹ�������Ĳ���
           //��ǰ��λ��ѧ������������ߵĿ��ӣ������ұߵĿ��ӣ����õ���λ��ѧ�������ұߵĿ���
           //�������Դ���ѭ���ȴ���������ʵ������ʱ�����λ��ѧ�����ǻ�����ȡ�����ұߵĿ���
           //������(���ұߵĿ����Ѿ�����һλ��ѧ��ȡ��)������Ҳ�Ͳ���ȥ������ߵĿ��ӣ��Ӷ�
           //����λ��ѧ�����ǿ���ȡ����ֻ���ӵ�һ���Ͳͣ��Ӷ��������ѭ���ȴ��Ľ��(������п��Կ�����һ��)��
           if(i<(size-1)){
               exec.execute(new Philosopers(chopstick[i],chopstick[(i+1)%size],i,pauseFactor));
           }
           else{
               exec.execute(new Philosopers(chopstick[(i+1)%size],chopstick[i],i,pauseFactor));
           }
           */
       }

       exec.shutdown();
       System.out.println("press 'Enter' to quit");
       System.in.read();
       exec.shutdownNow();
   }
}