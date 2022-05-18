/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package url;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Link {
    private String mainLink;
    private int depth;
    private int maxDepth;    
    private int valid;
    private int invalid;
    private ExecutorService e;

    public Link(String link, int depth, int maxDepth, int threads) {
        mainLink = link;
        this.depth = depth;
        this.maxDepth = maxDepth;
        this.e = Executors.newFixedThreadPool(threads);
    }

    public String getLink() {
        return mainLink;
    }

    public void setLink(String link) {
        mainLink = link;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
      public int getValid() {
        return valid;
    }

    public int getInvalid() {
        return invalid;
    }


   //gets the depth 0 links and put them in an array and call validateLinks 
public void extractLinks(){
    long start = System.currentTimeMillis();
      String links[]=getLinks(mainLink);
        for (String link : links) {
            e.execute(new Runnable() {
                public void run() {
                    validateLink(link, depth, maxDepth);
                }
            });
        }
        e.shutdown();
        try {
            e.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(Link.class.getName()).log(Level.SEVERE, null, ex);
        }
       long end = System.currentTimeMillis() - start;
       JFrame f = new JFrame();
       JOptionPane.showMessageDialog(f,"Number of links : "+ this.getValid()+this.getInvalid() + "\n" + "Valid = "+this.getValid() + "\n" + "Time: " + end + " ms");
  }

//calls isValid if valid gets the children links 
public void validateLink(String link, int depth, int maxDepth){ //validates all the links after the first depth

    if(isLinkValid(link)){ // check first if the parent link coming is valid if not then cant get the children
       System.out.println("Valid : "+link);
   
          if(depth==maxDepth)
                return ;
             
            
          String links[]=getLinks(link);
          for (int i = 0; i <links.length; i++) {
              validateLink(links[i],depth+1,maxDepth);
              
          }
             }
        else{
        System.err.println("Invalid : " + link);}
          
      
          
          // ThreadValidation t1= new ThreadValidation(Link l,x,depth+1,maxDepth);
          
}


// checks each link if valid or not
public boolean isLinkValid(String link){ // checks if the link is valid or not
            boolean check=false;
        try {
            Document doc= Jsoup.connect(link).get();
            check=true;
            valid++;

        }
        catch(HttpStatusException ex){ // when page not found (404 error)
         invalid++;
 
        }
        catch (IOException ex) { // timeout to connect exc
              invalid++;
        }
      
        
        return check;
        
  
}

//get links and put them in String array
public static String[] getLinks(String link){


       
        try {
            Document doc=Jsoup.connect(link).get();
            Elements element=doc.select("a[href]");
            String links[]=new String[element.size()];
            for (int i = 0; i < links.length; i++) {
               String x=element.get(i).attr("href");
                if(!x.startsWith("http")){
                 
                      URL url=new URL(link);
                      String baseLink=url.getProtocol()+"://"+url.getHost();
                      x=baseLink+x;}
                links[i]=x;
             
  
            }
        return links;
            
            
            
         
        } catch (Exception ex) {}
            
        return null;

}





// eaither choose between arrayList or String array both can do the tasks needed





// get links and put them in Array List
public static ArrayList<String> getLinksArrayList(String link){

    ArrayList<String> y= new ArrayList();
       
        try {
            Document doc=Jsoup.connect(link).get();
            Elements element=doc.select("a[href]");
            for (int i = 0; i < element.size(); i++) {
               String x=element.get(i).attr("href");
                if(!x.startsWith("http")){
                 
                      URL url=new URL(link);
                      String baseLink=url.getProtocol()+"://"+url.getHost();
                      x=baseLink+x;}
                  y.add(x);
          
             
  
            }
   
            
            
            
         
        } catch (Exception ex) {}
            
  
return y;
}
}




















