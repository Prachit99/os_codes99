import java.util.*;


class Main {
  static Vector<Integer> track=new Vector<Integer>(1,1);
  public static void main(String[] args) {
    int seek;
    Scanner sc=new Scanner(System.in);
    System.out.println("Enter max tracks");
    int maxTrack=sc.nextInt();
    System.out.println("Enter the no of tracks to be searched");
    int n=sc.nextInt();
    System.out.println("Enter values of tracks");
    for(int i=0;i<n;i++) {
      track.add(sc.nextInt());
    }
    System.out.println("Enter head position");
    int head=sc.nextInt();
    System.out.println("Enter time for one seek in ms");
    int s_time=sc.nextInt();
    FCFS(head,s_time);
    SSTF(head,s_time);
    L_SCAN(head,s_time);
    R_SCAN(head,maxTrack,s_time);
    L_CSCAN(head,maxTrack,s_time);
    R_CSCAN(head,maxTrack,s_time);
    L_LOOK(head,s_time);
    R_LOOK(head,s_time);
    L_CLOOK(head,s_time);
    R_CLOOK(head,s_time);
  }

  public static void FCFS(int head,int s_time) {
    int seek=0;
    System.out.println();
    System.out.println("---------FCFS----------");
    System.out.println("Order: ");
    System.out.println(track);
    for(int i=0;i<track.size();i++) {
      seek += Math.abs(track.get(i)-head);
      head = track.get(i);
    }
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
    System.out.println();
    
  }

  public static void SSTF(int head,int s_time) {
    System.out.println();
    System.out.println("-----------SSTF---------");
    int seek=0;
    int min;
    int index=0;
    Vector v=new Vector(1,1);
    Vector<Integer> track_copy=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      track_copy.add(track.get(i));
    }
    for(int i=0;i<track.size();i++) {
      min=9999;
      for(int j=0;j<track_copy.size();j++) {
        if(min>Math.abs(track_copy.get(j)-head)) {
          index=j;
          min=Math.abs(track_copy.get(j)-head);
        }
      }
      seek+=min;
      head=track_copy.get(index);
      //System.out.println("Newhead: "+head);
      v.add(track_copy.get(index));
      track_copy.remove(index);
    }
    System.out.println("Order: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }

  public static void L_SCAN(int head,int s_time) {
    System.out.println();
    System.out.println("-----------SCAN-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();
    for(int i=0;i<=n2;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(max<l2.get(j)) {
          max=l2.get(j);
          index=j;
        }
      }
      if(l2.size()==0) {
        seek+=Math.abs(max-head);
        head=0;
        v.add(0);
      }
      else {
        seek += Math.abs(max-head);
        head=l2.get(index);
        v.add(max);
        l2.remove(index);
      }
      
    }
    for(int i=0;i<n1;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(min>l1.get(j)) {
          min=l1.get(j);
          index=j;
        }
      }
      seek += Math.abs(min-head);
      head=l1.get(index);
      v.add(min);
      l1.remove(index);
    }
    System.out.println("Order-Left: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }


  public static void R_SCAN(int head,int maxTrack,int s_time) {
    System.out.println();
    //System.out.println("-----------SCAN-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();
    for(int i=0;i<=n1;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(min>l1.get(j)) {
          min=l1.get(j);
          index=j;
        }
      }
      if(l1.size()==0) {
        seek += Math.abs(maxTrack-head);
        head=maxTrack-1;
        v.add(maxTrack-1);
      }
      else {
        seek += Math.abs(min-head);
        head=l1.get(index);
        v.add(min);
        l1.remove(index);
      }
    }

    for(int i=0;i<n2;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(max<l2.get(j)) {
          max=l2.get(j);
          index=j;
        }
      }
      seek += Math.abs(max-head);
      head=l2.get(index);
      v.add(max);
      l2.remove(index);
      
    }
    System.out.println("Order-Right: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }

  public static void L_CSCAN(int head,int maxTrack,int s_time) {
    System.out.println();
    System.out.println("-----------CSCAN-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();
    for(int i=0;i<=n2;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(max<l2.get(j)) {
          max=l2.get(j);
          index=j;
        }
      }
      if(l2.size()==0) {
        seek+=Math.abs(max-head);
        head=0;
        v.add(0);
      }
      else {
        seek += Math.abs(max-head);
        head=l2.get(index);
        v.add(max);
        l2.remove(index);
      }
      
    }
    //seek+=maxTrack-1;
    seek+=1;
    head=maxTrack-1;
    v.add(maxTrack-1);

    for(int i=0;i<n1;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(max<l1.get(j)) {
          max=l1.get(j);
          index=j;
        }
      }
      seek += Math.abs(max-head);
      head=l1.get(index);
      v.add(max);
      l1.remove(index);
    }
    System.out.println("Order-Left: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }

  public static void R_CSCAN(int head,int maxTrack,int s_time) {
    System.out.println();
    //System.out.println("-----------CSCAN-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();
    for(int i=0;i<=n1;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(min>l1.get(j)) {
          min=l1.get(j);
          index=j;
        }
      }
      if(l1.size()==0) {
        seek+=Math.abs(maxTrack-head);
        head=maxTrack-1;
        v.add(maxTrack-1);
      }
      else {
        seek += Math.abs(min-head);
        head=l1.get(index);
        v.add(min);
        l1.remove(index);
      }
    }
    seek+=1;
    head=0;
    v.add(0);
    for(int i=0;i<n2;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(min>l2.get(j)) {
          min=l2.get(j);
          index=j;
        }
      }
      seek += Math.abs(min-head);
      head=l2.get(index);
      v.add(min);
      l2.remove(index);

    }
    //seek+=maxTrack-1;
    
    System.out.println("Order-Right: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }


  public static void L_LOOK(int head,int s_time) {
    System.out.println();
    System.out.println("-----------LOOK-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();
    for(int i=0;i<n2;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(max<l2.get(j)) {
          max=l2.get(j);
          index=j;
        }
      }
      seek += Math.abs(max-head);
      head=l2.get(index);
      v.add(max);
      l2.remove(index);
    }
    for(int i=0;i<n1;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(min>l1.get(j)) {
          min=l1.get(j);
          index=j;
        }
      }
      seek += Math.abs(min-head);
      head=l1.get(index);
      v.add(min);
      l1.remove(index);
    }
    System.out.println("Order-Left: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }

  public static void R_LOOK(int head,int s_time) {
    System.out.println();
    //System.out.println("-----------LOOK-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();

    for(int i=0;i<n1;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(min>l1.get(j)) {
          min=l1.get(j);
          index=j;
        }
      }
      seek += Math.abs(min-head);
      head=l1.get(index);
      v.add(min);
      l1.remove(index);
    }


    for(int i=0;i<n2;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(max<l2.get(j)) {
          max=l2.get(j);
          index=j;
        }
      }
      seek += Math.abs(max-head);
      head=l2.get(index);
      v.add(max);
      l2.remove(index);
    }
    
    System.out.println("Order-right: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }

  public static void L_CLOOK(int head,int s_time) {
    System.out.println();
    System.out.println("-----------CLOOK-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();
    for(int i=0;i<n2;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(max<l2.get(j)) {
          max=l2.get(j);
          index=j;
        }
      }
      seek += Math.abs(max-head);
      head=l2.get(index);
      v.add(max);
      l2.remove(index);
    }

    for(int i=0;i<n1;i++) {
      int max=0;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(max<l1.get(j)) {
          max=l1.get(j);
          index=j;
        }
      }
      seek += Math.abs(max-head);
      head=l1.get(index);
      v.add(max);
      l1.remove(index);
    }
    System.out.println("Order-Left: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }

  public static void R_CLOOK(int head, int s_time) {
    System.out.println();
    //System.out.println("-----------CLOOK-------------");
    int seek=0;
    Vector v=new Vector();
    Vector<Integer> l1=new Vector<Integer>(1,1);
    Vector<Integer> l2=new Vector<Integer>(1,1);
    for(int i=0;i<track.size();i++) {
      if(track.get(i)>head){
        l1.add(track.get(i));
      }
      else {
        l2.add(track.get(i));
      }
    }
    int n1=l1.size();
    int n2=l2.size();

    for(int i=0;i<n1;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l1.size();j++) {
        if(min>l1.get(j)) {
          min=l1.get(j);
          index=j;
        }
      }
      seek += Math.abs(min-head);
      head=l1.get(index);
      v.add(min);
      l1.remove(index);
    }

    for(int i=0;i<n2;i++) {
      int min=999;
      int index=0;
      for(int j=0;j<l2.size();j++) {
        if(min>l2.get(j)) {
          min=l2.get(j);
          index=j;
        }
      }
      seek += Math.abs(min-head);
      head=l2.get(index);
      v.add(min);
      l2.remove(index);
    }

    
    System.out.println("Order-Right: ");
    System.out.println(v);
    System.out.println("Total no of seeks: "+seek);
    System.out.println("Total seek time: "+(s_time*seek));
  }



}