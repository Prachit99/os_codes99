import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
 





class Process
{
  Process()
  {}
    int PID,AT,WT,ET,TT,Priority;
    double ratio;
    Process(int PID, int AT, int ET, int WT, int TT, int Priority,double ratio)
    {
        this.PID=PID;
        this.AT=AT;
        this.ET=ET;
        this.WT=WT;
        this.TT=TT;
        this.Priority=Priority;
        this.ratio=ratio;
    }
}

class Create extends Thread
{
    static int count=0;
    Main f=new Main();
    //Random r=new Random();
    int PID,AT,WT,ET,TT,Priority;
    int t1=0;
    int t=0;
    int t2=5;
   
    public void run()
    {
      try
      {
      
        Process p=new Process();
        Process p_exe=new Process();
        Process p_prev=new Process();
        while(true)
        {
          Thread.currentThread().sleep(1000);
          if(f.exe.isEmpty()==false)
          {
            p_exe=f.exe.lastElement();
            t1=p_exe.AT;
            t2=p_exe.ET;
          }
          AT=ThreadLocalRandom.current().nextInt(t1+1,t1+t2+1);
          //System.out.println(AT);
          ET=ThreadLocalRandom.current().nextInt(1,7);
          Priority=ThreadLocalRandom.current().nextInt(1,10);
          //System.out.println(ET);
          p=new Process(PID,AT,ET,0,0,Priority,1.00);
          //Thread.currentThread().sleep((AT-t)*100);
          PID=++count;
          f.ready.add(p);
          
          if(PID==7)
          {
            break;
          }
          
          
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
        Process pr1=new Process();
        Process pr_prev=new Process();
        int counter=0;
        int current_time=0;
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
          /*
          if(f.ready.isEmpty())
          {
            System.out.println("No processes in ready queue");
          }*/
          else if(f.ready.size()!=0 && f.exe.size()==0)
          {
            counter++;
            
            Process q=new Process();
            for(int i=0;i<f.ready.size();i++)
            {
              q=f.ready.get(i);
              if(q.PID==0)
              {
                q.WT=0;
                current_time=q.AT;
              }
              else
              {
                q.WT=current_time-q.AT;
                q.ratio=(double)(q.WT+q.ET)/q.ET;

              }
            }
            


            
            System.out.println("Processes in the ready queue:");
            System.out.println("PID\tAT\tET\tWT\tTT\tPriority");
            //synchronized(f.ready)
            //{
              for(Iterator<Process> iter=f.ready.iterator();iter.hasNext(); )
              {
                Process i=iter.next();
                System.out.println(i.PID+"\t"+i.AT+"\t"+i.ET+"\t"+i.WT+"\t"+i.TT+"\t"+i.Priority+"\t"+i.ratio);
              }
              System.out.println();
            //}
            pr=f.ready.get(0);
            double max=pr.ratio;
            for(int z=0;z<f.ready.size();z++)
            {
              pr1=f.ready.get(z);
              if(pr1.ratio>max)
              {
                max=pr1.ratio;
                pr=pr1;
              }
            }
            f.exe.add(pr);
            gantt_chart+=current_time;
            current_time += pr.ET;
            System.out.println("Current time: "+current_time);
            /*
            if(pr.PID==0)
            {
              pr.WT=0;
              pr.starttime=pr.AT;
            }
            else{
              pr.WT=pr_prev.starttime+pr_prev.ET-pr.AT;
              if(pr.WT<0)
              {
                pr.starttime=pr_prev.starttime+pr_prev.ET;
                gantt_chart+=pr.starttime+"|___CPU IDLE___|";
                pr.starttime=pr_prev.starttime+pr_prev.ET-pr.WT;
                pr.WT=0;
              }
              else
              {
                pr.starttime=pr_prev.starttime+pr_prev.ET;
              }
            }
            */
            pr.TT=pr.ET+pr.WT;
            f.global.add(pr);
            int index=f.ready.indexOf(pr);
            pr_prev=f.ready.get(index);
            f.ready.removeElement(pr);
            gantt_chart+="|___P"+pr.PID+"___|";
            if(counter==7)
            {
              gantt_chart+=(current_time);
              
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