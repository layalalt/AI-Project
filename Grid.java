
public class Grid
{
    private int[] domain = {0,1,2,3,4,5,6,7,8,9};
    private int assignment;

    public Grid()
    {
       assignment = -1;  
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
        {
          if(domain[i] != assignment)
             domain[i] = -1;
        }
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
    
}