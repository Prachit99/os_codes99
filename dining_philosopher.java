import java.util.*;

class Semaphore
{
  int count;
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

class Philosopher extends Thread
{
	int pid;
  Semaphore fork[];

  public Philosopher() {}

	public Philosopher(int pid, Semaphore fork[]) 
  {
		this.pid = pid;
    this.fork = fork;
	}

	public void run() {
		while(true) {
			try{
        if(pid == 4)
        {
          System.out.println(Thread.currentThread().getName()+" is thinking");

          fork[(pid+1)%5].semWait();
          System.out.println(Thread.currentThread().getName()+" picked up the right fork: fork "+(pid+1)%5);

          fork[pid].semWait();
          System.out.println(Thread.currentThread().getName()+" picked up the left fork: fork "+pid);

          System.out.println(Thread.currentThread().getName()+" is eating");
          Thread.sleep(3000);

          fork[pid].semSignal();
          System.out.println(""+Thread.currentThread().getName()+" put down the left fork: fork"+pid);

          fork[(pid+1)%5].semSignal();
          System.out.println(Thread.currentThread().getName()+" put down the right fork: fork"+(pid+1)%5);
        }
        else
        { 
          System.out.println(Thread.currentThread().getName()+" is thinking");
          fork[pid].semWait();
          System.out.println(Thread.currentThread().getName()+" picked up the left fork: fork "+pid);

          fork[(pid+1)%5].semWait();
          System.out.println(Thread.currentThread().getName()+" picked up the right fork: fork "+(pid+1)%5);

          System.out.println(Thread.currentThread().getName()+" is eating");
          Thread.sleep(3000);

          fork[(pid+1)%5].semSignal();
          System.out.println(Thread.currentThread().getName()+" put down the right fork: fork "+(pid+1)%5);

          fork[pid].semSignal();
          System.out.println(""+Thread.currentThread().getName()+" put down the left fork: fork "+pid);
        }
			}catch(Exception e){System.out.println(e);}
		}		
	}
}


class Main
{
	public static void main(String[] args) 
  {
		try
    {
			Thread t = new Thread();
			Semaphore fork[] = new Semaphore[5];
      for(int i=0;i<5;i++)
      {
        fork[i]=new Semaphore(1);
      }
			Philosopher p1 = new Philosopher(0,fork);
			Philosopher p2 = new Philosopher(1,fork);
			Philosopher p3 = new Philosopher(2,fork);
			Philosopher p4 = new Philosopher(3,fork);
			Philosopher p5 = new Philosopher(4,fork);
			p1.setName("Phil 0");
			p2.setName("Phil 1");
			p3.setName("Phil 2");
			p4.setName("Phil 3");
			p5.setName("Phil 4");
			p1.start();
			p2.start();
			p3.start();
			p4.start();
			p5.start();
		}catch(Exception e){  System.out.println(e);  }
	}
}
