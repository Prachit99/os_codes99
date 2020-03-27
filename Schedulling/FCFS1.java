import java.util.*;
import java.util.concurrent.ThreadLocalRandom;  
import java.util.Collections;




class Process
{
  Process()
  {}
    int PID,AT,WT,ET,TT,Priority,starttime;
    Process(int PID, int AT, int ET, int WT, int TT, int Priority,int starttime)
    {
        this.PID=PID;
        this.AT=AT;
        this.ET=ET;
        this.WT=WT;
        this.TT=TT;
        this.Priority=Priority;
        this.starttime=starttime;
    }
}

class Create extends Thread
{
    static int count=0;
    Main f=new Main();
    //Random r=new Random();
    int PID,AT,WT,ET,TT,Priority,starttime;
    int t=0;
   
    public void run()
    {
       try{
      
      Process p = new Process();
      for(int j=0;j<=6;j++)
      {
        Thread.currentThread().sleep(1000);
        PID=count++;
        AT=ThreadLocalRandom.current().nextInt(t+1,t+5);
        //System.out.println(AT);
        ET=ThreadLocalRandom.current().nextInt(1,10);
        //System.out.println(ET);
        p=new Process(PID,AT,ET,0,0,5,0);
        //Thread.currentThread().sleep((AT-t)*100);
        f.ready.add(p);
        //f.global.add(p);
        t=AT;

        }
        //synchronized(f.ready)
        
          
        
      }catch(Exception e){System.out.println(e);}
    }
  }


class Running extends Thread
{
    String gantt_chart="0";
    Main f=new Main();
    public void run()
    {
      try{
        Thread.currentThread().sleep(200);
        Process pr=new Process();
        Process pr_prev=new Process();
        while(true)
        {
          if(f.ready.isEmpty() && f.exe.isEmpty())
          {
            if(gantt_chart.charAt(gantt_chart.length()-1)!='|')
            {
              gantt_chart+="|___CPU IDLE___|";
              System.out.println(gantt_chart);
            }
            //System.out.println("CPU IDLE");
            Thread.currentThread().sleep(100);
            continue;
          }
          if(f.ready.isEmpty())
          {
            System.out.println("No processes in ready queue");
          }
          else if(f.ready.size()!=0 && f.exe.size()==0)
          {
            System.out.println("Processes in the ready queue:");
            System.out.println("PID\tAT\tET\tWT\tTT\tPriority");
            //synchronized(f.ready)
            //{
              for(Iterator<Process> iter=f.ready.iterator();iter.hasNext(); )
              {
                Process i=iter.next();
                System.out.println(i.PID+"\t"+i.AT+"\t"+i.ET+"\t"+i.WT+"\t"+i.TT+"\t"+i.Priority);
              }
              System.out.println();
            //}
            pr=f.ready.get(0);
            f.exe.add(pr);
            if(pr.PID==0)
            {
              pr.WT=0;
              pr.starttime=pr.AT;
            }
            else{
              pr.WT=pr_prev.starttime+pr_prev.ET-pr.AT;
              if(pr.WT<0){pr.WT=0;}
              pr.starttime=pr_prev.starttime+pr_prev.ET;
            }
            
            pr.TT=pr.ET+pr.WT;
            f.global.add(pr);
            pr_prev=f.ready.get(0);
            f.ready.remove(0);
            gantt_chart+=pr.starttime+"|___P"+pr.PID+"___|";
            if(pr.PID==6)
            {
              gantt_chart+=(pr.starttime+pr.ET);
              
            }
            System.out.println(gantt_chart);
            Thread.currentThread().sleep(f.exe.firstElement().ET*1000);
            f.exe.clear();
            
            //System.out.println(pr.ET);
            //Thread.currentThread().sleep(pr.ET*100);
            if(f.global.size()==7)
            {
              System.out.println();
              System.out.println("Final Gantt Chart:");
              gantt_chart+="|___CPU IDLE___|";
              System.out.println(gantt_chart);
              System.out.println();
              System.out.println("Details of all processes:");
              System.out.println("PID\tAT\tET\tWT\tTT\tPriority");
              for(Iterator<Process> iter=f.global.iterator();iter.hasNext(); )
              {
                Process i=iter.next();
                System.out.println(i.PID+"\t"+i.AT+"\t"+i.ET+"\t"+i.WT+"\t"+i.TT+"\t"+i.Priority);
              }
              break;
            }
          }
          
        }
      }catch(Exception e){System.out.println(e);}
    }
}

class Main
{
    static Vector <Process>ready=new Vector<Process>(1,1);
    static Vector <Process>exe=new Vector<Process>(1,1);
    static Vector <Process>global=new Vector<Process>(1,1);
    public static void main(String args[])
    {
        Create create=new Create();
        Running running=new Running();
        create.start();
      //  try{
      //    create.join();
      //  }catch(Exception e){  System.out.println(e);}
        running.start();
        /*System.out.println("Details of all processes:");
        System.out.println("PID\tAT\tET\tWT\tTT\tPriority");
        for(Iterator<Process> iter=global.iterator();iter.hasNext(); )
        {
            Process i=iter.next();
            System.out.println(i.PID+"\t"+i.AT+"\t"+i.ET+"\t"+i.WT+"\t"+i.TT+"\t"+i.Priority);
        }*/
    }
}