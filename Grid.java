import java.util.*;

public class Grid
{
    private int[] domain = {0,1,2,3,4,5,6,7,8,9};
    private int assignment;

    public Grid()
    {
       assignment = -1;  
    }
    
    public Grid(Grid g) //copy constructor
    {
        this.assignment = g.assignment;
        System.arraycopy(g.domain, 0, this.domain, 0, 10);
    }

    public int[] getDomain() 
    {
        return domain;
    }
    
    public int getAssignment()
    {
        return assignment;
    }

    public void setAssignment(int assignment) 
    {
        this.assignment = assignment;
    }
    
    public void clearDomain()
    {
        for(int i=0; i<10; i++)
           domain[i] = -1;
    }
    
   public void clearDomainSum()
    {
        for(int i=0; i<10; i++)
           domain[i] = -1;     
    }
     
    public void removeFromDomain(int index)       
    {
        if(index != -1)
          domain[index] = -1;
    }
    
    public void removeFromDomainGreaterThan(int index)
    {
        for(int i=0; i<10; i++)
        {
          if(i > index)
             domain[i] = -1;
        }
    }
    
    public void displayDomain()
    {
        for(int i=0; i<10; i++)
            System.out.print(domain[i] + "|");
    }
    
    public int selectRandomFromDomain()
    {
        Random random = new Random();
        int num = -1;
        if(assignment == -1)
          while((num = domain[random.nextInt(10)]) == -1);
        return num; //returns a random number form domain using a random index

    }

    public boolean domainIsEmpty()
    {
        if(assignment != -1)
           return false;
        
        for(int i=0; i<domain.length; i++) 
        {
            if(domain[i] != -1) 
               return false;
        }
        return true;
    }

    public void addToDomain(int num)
    {
       if(domain[num] == -1)
          domain[num] = num; 
    }
    
}
