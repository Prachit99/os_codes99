import java.util.*;

class Main{
  public static void main(String args[]){
    Scanner sc=new Scanner(System.in);
    Vector <Integer>v=new Vector<Integer>(1,1);
    System.out.println("Enter the number of Processes");
    int n=sc.nextInt();
    System.out.println("Enter the number of resources");
    int m=sc.nextInt();
    int resource[]=new int[m];
    int res_bak[]=new int[m];
    int max[][]=new int[n][m];
    int allocated[][]=new int[n][m];
    int needed[][]=new int[n][m];
    System.out.println("Enter total available resource");
    for(int i=0;i<resource.length;i++)
    {
      resource[i]=sc.nextInt();
      res_bak[i]=resource[i];
    }

    System.out.println();

    System.out.println("Enter total need(max) of all processes");
    for(int i=0;i<n;i++){
      for(int j=0;j<m;j++){
        max[i][j] = sc.nextInt();
      }
    }

    for(int i=0;i<n;i++){
      for(int j=0;j<m;j++){
        if(max[i][j]>resource[j])
        {
          System.out.println("As the max need of process"+i+"is more than available resources of resource"+j+",given processes won't be able to execute completely");
        }
      }
    }

    System.out.println("Enter total allocated resources of all processes");
    for(int i=0;i<n;i++){
      for(int j=0;j<m;j++){
        allocated[i][j] = sc.nextInt();
        resource[j]-=allocated[i][j];
      }
    }


    System.out.println("Resources available");
    for(int i=0;i<resource.length;i++)
    {
      System.out.print(resource[i]+"\t");
    }
    System.out.println();
    //Remaining need of all processes
    System.out.println("Need Matrix: ");
    for(int i=0;i<n;i++){
      for(int j=0;j<m;j++){
        needed[i][j] = max[i][j] - allocated[i][j];
        System.out.print(needed[i][j]+" ");
      }
      System.out.println();
    }
    boolean res=false;
    for(int k=0;k<n;k++){
      for(int i=0;i<n;i++){
        int c=0;
        for(int j=0;j<m;j++){
          if(needed[i][j]<=resource[j]){
            c++;
          }
          else{
            continue;
          }
        }
        if(c==m){
          System.out.println("Process "+i+" is able to execute");
          v.add(i);
          for(int j=0;j<m;j++){
            resource[j]+=allocated[i][j];
            needed[i][j] = 999;
          }
        }
      }
      if(Arrays.equals(res_bak,resource)){
        /*
        System.out.println("Resource matrix");
        for(int i=0;i<resource.length;i++)
        {
          System.out.print(resource[i]+"\t");
        }
        System.out.println();
        System.out.println("Back up resource matrix:");
        for(int i=0;i<res_bak.length;i++)
        {
          System.out.print(res_bak[i]+"\t");
        }*/
        System.out.println("All processes got executed without deadlock in following order:");
        System.out.println(v);
        res=true;
        break;
      }
    }
    System.out.println();
    if(res==false){
      Vector <Integer>v_comp=new Vector<Integer>(1,1);
      for(int i=0;i<n;i++){
        if(v.contains(i)){
          continue;
        }
        else{
          v_comp.add(i);
        } 
      }
      System.out.println("Deadlock happened\nProcesses that couldn't run are: "+v_comp);

    }

  }
}