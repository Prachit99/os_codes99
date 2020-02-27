import java.util.*;

class Semaphore
{
  int count;

  Semaphore() {}

  Semaphore(int count)
  {
    this.count = count;
  }

  public void semWait()
  {
    try
    {
      synchronized(this)
      {
        count--;
        if(count < 0)
        {
          this.wait();
        }
      }   
    }catch(Exception e){  System.out.println(e);  }
  }

  public void semSignal()
  {
    try
    {
      synchronized(this)
      {
        count++;
        if(count <= 0){
          this.notify();
        }
      }
    }catch(Exception e){  System.out.println(e);  }
    
  }
}

class Producer extends Thread 
{
  Semaphore mutex,full,empty;
  int i=0;

  Producer() {}
  
  Producer(Semaphore mutex,Semaphore full,Semaphore empty)
  {
    this.mutex = mutex;
    this.full = full;
    this.empty = empty;
  }

  public void run()
  {
    try
    {
      while(true)
      {
        Thread.sleep(2000);

        
        mutex.semWait();
        empty.semWait();
        full.semSignal();

        System.out.println("Item "+i+" Produced");
        i++; 
        System.out.println("Available buffer: "+empty.count);
        System.out.println("Filled buffer: "+full.count);

        
        mutex.semSignal();
        
        
      }
    }catch(Exception e){  System.out.println(e); };    

  }
}


class Consumer extends Thread
{
  Semaphore mutex,full,empty;
  int i=0;

  Consumer() {}
  
  Consumer(Semaphore mutex,Semaphore full,Semaphore empty)
  {
    this.mutex = mutex;
    this.full = full;
    this.empty = empty;
  }

  public void run()
  {
    try
    {
      while(true)
      {
        Thread.sleep(2000);
        mutex.semWait();
        full.semWait();
        empty.semSignal();
        

        System.out.println("Item "+i+" Consumed");
        i++;
        
        System.out.println("Filled buffer: "+full.count);
        System.out.println("Available buffer: "+empty.count);
        System.out.println();

        
        mutex.semSignal();

        

      }
    }catch(Exception e){  System.out.println(e); };    

  }


}

class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter buffer size");
    int n=sc.nextInt();

    Semaphore mutex = new Semaphore(1);
    Semaphore full = new Semaphore(0);
    Semaphore empty = new Semaphore(n);

    Producer p = new Producer(mutex,full,empty);
    Consumer c = new Consumer(mutex,full,empty);

    p.start();
    c.start();
  }
}