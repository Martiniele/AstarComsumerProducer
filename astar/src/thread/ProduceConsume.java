package thread;

class Container{
    public int max; //���������������
    public int currentNum;//����������ǰ����
     
    public Container(int max){
        this.max = max;
        currentNum = 0;
    }
}
class Producer implements Runnable{
    public Container con;
    public Producer(Container con){
        this.con = con;
    }
    public void run(){
        while(true){//��������ƻ��
            synchronized(con){
                if(con.currentNum < con.max){//����ǰ�������������������
                    con.notify();//��������֪ͨ���ͷ���
                    con.currentNum++;
                    System.out.println(" ��������������...+1, ��ǰ��Ʒ����"+con.currentNum);
                }else if(con.currentNum == con.max){//
                    System.out.println("�����Ѿ����ͣ�������ֹͣ���������ڵȴ�����..."); 
                    try {
                        con.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }//if else if               
            }//syn  ִ����ͬ����  �ͷ�����������������������������������Ϊ���ͷ�������û�еȴ��̣߳�������ִ�е��ĸ��̵߳�ͬ�����ִ����
             
            try {
                Thread.sleep(100);//����������Ƶ�ʣ������������~~
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//try
        }//while
    }
}
 
class Consumer implements Runnable{
    public Container con;
    public Consumer(Container con){
        this.con = con;
    }
    public void run(){
        while(true){
            synchronized(con){
                if(con.currentNum > 0 ){
                    con.notify();
                    con.currentNum--;
                    System.out.println(" ��������������...-1, ��ǰ��Ʒ����"+con.currentNum);
                }else if(con.currentNum == 0){
                    System.out.println("�����Ѿ����ˣ�������ֹͣ���ѣ����ڵȴ�����..."); 
                    try {
                        con.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }//else if              
            }//syn      
             
            try {
                Thread.sleep(140);//����������Ƶ�ʣ��������׳���~~
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//try
        }//while
    }// run
}

public class ProduceConsume{
    public static void main(String args[]){
        Container container = new Container(10);//������������������˴�Ϊ5
        Producer producer =  new Producer(container);//�����е�ƻ����Ҫͬ�������Խ����Ӷ���������Ϊ�βδ��������ߺ�������
        Consumer consumer = new Consumer(container);//         
        new Thread(producer, "producer").start();//������������ģʽ
        new Thread(consumer, "consumer").start();
    }
}