
import java.util.*;

public class Trial {

static class Grid implements Comparable<Grid>
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
        if(!this.domainIsEmpty())
         {while((num = domain[random.nextInt(10)]) == -1);} 
        return num; //returns a random number form domain using a random index

    }

    public boolean domainIsEmpty()
    {
        //if(assignment != -1)
            //return false;
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
    
    public int domainSize()
    {
        int count = 0;
        for(int i=0; i<10; i++)
        {
            if(domain[i] != -1)
                count++;
        } 
        return count;
    }
    
    public int compareTo(Grid p) //for SJF sorting
    {
        return this.domainSize() - p.domainSize();
    }
    
  }
   public static void main(String[] args) 
    {
       
        Grid[][] tenner = generateTenner();//10 by 4

        displayTenner(tenner);
             
        System.out.println();
  
        int num =unassignedVariables(tenner);
        
       // System.out.println("Number of unassigned variables: " + num +"\n");
        
      Grid[][] backtrackingSolution = backtracking(tenner);
       
       displayTenner(backtrackingSolution);
       
       System.out.println();
       
       Grid[][] tenner2 = setForForwardChecking(tenner);
       
            
      /**  for(int i=0; i<3; i++) //for testing purposes
          for(int j=0; j<10; j++)
          { 
            tenner2[i][j].displayDomain();
            System.out.println();
          }*/
        
       Grid[][] FCSolution = forwardChecking(tenner2);
        
      //
      
      displayTenner(FCSolution);
        
      
       Grid[][] FCMRVSolution = forwardCheckingMRV(tenner2);
        
       displayTenner(FCMRVSolution);
        
        
       System.out.println();
       


    }
    public static Grid[][] generateTenner()
    {
        Grid[][] tenner = new Grid[4][10]; //10 by 4
        for(int i=0; i<4; i++) 
          for(int j=0; j<10; j++)
             tenner[i][j] = new Grid();
        
        Random random = new Random();

        long startTime = System.currentTimeMillis(), endTime = 1000000; //to prevent infinite loops
        boolean infiniteLoop = false; //infinite loops caused my a bad grid generation 
        double counter = 0.1;
           
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
                infiniteLoop = false;
                endTime = System.currentTimeMillis();
                
                if((endTime - startTime)/1000.0 > counter) //loop took more that a second; stuck
                {    
                    infiniteLoop = true;  
                    counter+=0.1;
                    break;
                }
                
                int num = random.nextInt(10);
                boolean exists = false;
                for(int k=j; k>=0; k--) //checks if it exists already in row; ensures uniqueness
                {
                 if(num == tenner[i][k].getAssignment())
                   exists = true;
                }
                if(i == 1) //second row diagnoal checker
                {
                  if(j == 0)
                  {
                   if((num == tenner[0][0].getAssignment()) || (num == tenner[0][1].getAssignment())) //first element
                     exists = true;
                  }
                  else if(j == 9)
                  {  
                   if((num == tenner[0][8].getAssignment()) || (num == tenner[0][9].getAssignment())) //last element  
                     exists = true;
                  }
                  else if(j>0 && j<9)
                  {  
                   if((num == tenner[0][j - 1].getAssignment()) || (num == tenner[0][j].getAssignment()) || (num == tenner[0][j + 1].getAssignment())) //middle elements
                     exists = true; 
                  }     
                }      
                else if(i == 2) //third row
                {
                 if(j == 0)
                 {
                  if((num == tenner[1][0].getAssignment()) || (num == tenner[1][1].getAssignment())) //first element
                     exists = true;
                 }
                 else if(j == 9)
                 {  
                   if((num == tenner[1][8].getAssignment()) || (num == tenner[1][9].getAssignment())) //last element  
                     exists = true;
                 }
                  else if (j>0 && j<9) //middle elements
                  {  
                   if((num == tenner[1][j - 1].getAssignment()) || (num == tenner[1][j].getAssignment()) || (num == tenner[1][j + 1].getAssignment()))
                     exists = true; 
                  }  
                } 
                if(exists) //violates constraints
                  j--;
                else
                  tenner[i][j].setAssignment(num); //assign to grid
          }
          if(infiniteLoop) //restart
             i=0;
        }
        
        for(int i=0; i<10; i++) //sum generator
        { 
            tenner[3][i].setAssignment(tenner[0][i].getAssignment() + tenner[1][i].getAssignment()  + tenner[2][i].getAssignment());
            tenner[3][i].clearDomain();
        }
        
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
             boolean result = random.nextDouble() < 0.55; //generates probability to make a cell empty
             if(result) //empty grid index
                tenner[i][j].setAssignment(-1);
             else
                tenner[i][j].clearDomain();  
          }
        } 
        return tenner; 
    }
    
    public static Grid[][] setForForwardChecking(Grid[][] g)
    {
        Grid[][] tenner = copyGrid(g);
        for(int i=0; i<3; i++) //domain setters
        {
          for(int j=0; j<10; j++)
          {
             if(tenner[i][j].getAssignment() != -1) //if cell is assigned 
             {}
             else //not assigned
             {
               tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment());
               
               for(int k=0; k<10; k++) //removes from domain row elements
                  tenner[i][j].removeFromDomain(tenner[i][k].getAssignment());
               
               if(i == 0 || i == 2) //first and third rows diagnoal checker
                {
                  if(j == 0) //first column
                  {
                     tenner[i][j].removeFromDomain(tenner[1][0].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[1][1].getAssignment());
                  }
                  else if(j == 9) //last column
                  {  
                     tenner[i][j].removeFromDomain(tenner[1][8].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[1][9].getAssignment());
                  }
                  else if (j>0 && j<9) //middle columns
                  {  
                     tenner[i][j].removeFromDomain(tenner[1][j-1].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[1][j].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[1][j+1].getAssignment());
                  }     
                }      
                else //second row
                {
                 if(j == 0)
                  {
                     tenner[i][j].removeFromDomain(tenner[0][0].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[0][1].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][0].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][1].getAssignment());
                  }
                  else if(j == 9)
                  {  
                     tenner[i][j].removeFromDomain(tenner[0][8].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[0][9].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][8].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][9].getAssignment());
                  }
                  else if (j>0 && j<9)
                  {  
                     tenner[i][j].removeFromDomain(tenner[0][j-1].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[0][j].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[0][j+1].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][j-1].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][j].getAssignment());
                     tenner[i][j].removeFromDomain(tenner[2][j+1].getAssignment());
                  }
                }
                       
               if(i == 0)
               {  
                 if(tenner[1][j].getAssignment() != -1 && tenner[2][j].getAssignment() != -1)
                 { 
                    tenner[i][j].clearDomain();
                    tenner[i][j].addToDomain(tenner[3][j].getAssignment()-(tenner[1][j].getAssignment()+tenner[2][j].getAssignment()));
                 }
                 else if(tenner[1][j].getAssignment() != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment() - tenner[1][j].getAssignment());
                 }
                 else if(tenner[2][j].getAssignment() != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment() - tenner[2][j].getAssignment());
                 }
               }
               else if(i == 1)
               {  
                 if(tenner[0][j].getAssignment() != -1 && tenner[2][j].getAssignment() != -1)
                 { 
                    tenner[i][j].clearDomain();
                    tenner[i][j].addToDomain(tenner[3][j].getAssignment()-(tenner[0][j].getAssignment()+tenner[2][j].getAssignment()));
                 }
                 else if(tenner[0][j].getAssignment() != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment() - tenner[0][j].getAssignment());
                 }
                 else if(tenner[2][j].getAssignment() != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment() - tenner[2][j].getAssignment());
                 }  
               }
               else if(i == 2)
               {  
                 if(tenner[0][j].getAssignment() != -1 && tenner[1][j].getAssignment() != -1)
                 { 
                    tenner[i][j].clearDomain();
                    tenner[i][j].addToDomain(tenner[3][j].getAssignment()-(tenner[0][j].getAssignment()+tenner[1][j].getAssignment()));
                 }
                 else if(tenner[0][j].getAssignment() != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment() - tenner[0][j].getAssignment());
                 }
                 else if(tenner[1][j].getAssignment() != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].getAssignment() - tenner[1][j].getAssignment());
                 }  
               }
             }
             
          }
        }
        return tenner;
    }
 
    
    
    public static void displayTenner(Grid[][] g)
    {
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<10; j++) 
            {
               if(g[i][j].getAssignment() == -1)
                 System.out.print(g[i][j].getAssignment() + "| ");
               else
                 System.out.print(g[i][j].getAssignment() + " | ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
        for(int i=0; i<10; i++) 
        {
           if(g[3][i].getAssignment() > 9)
              System.out.print(g[3][i].getAssignment() + "| ");
           else
              System.out.print(g[3][i].getAssignment() + " | ");
        }
        System.out.println();
    }
    
    
    public static boolean complete(Grid[][] g)
    {
       for(int i=0; i<3; i++) //removes the sum and all bigger numbers from the domains of its column 
       {
           for(int j=0; j<10; j++)
           { 
               if(g[i][j].getAssignment() == -1)
                   return false;
           }
       } 
       return true;
    }
    
    
    public static boolean checkDomains(Grid[][] g)
    {
      for(int i=0; i<3; i++) 
      {
           for(int j=0; j<10; j++)
           { 
               if(g[i][j].domainIsEmpty() && g[i][j].getAssignment() == -1)
                   return false;
           }
      } 
      return true;   
    }
   
   public static int unassignedVariables(Grid[][] grid)
   {
       int counter = 0;
       for(int i=0; i<3; i++) //removes the sum and all bigger numbers from the domains of its column 
       {
           for(int j=0; j<10; j++)
           { 
               if(grid[i][j].getAssignment() == -1)
               {  
                  counter++;
                 // System.out.print("grid[" + i + "][" + j + "]-");
               }
           }
       }
       System.out.println();
       return counter;
   }
   public static Grid[][] copyGrid(Grid[][] original) 
   {
     int rows = 4;
     int cols = 10;
     Grid[][] copy = new Grid[rows][cols];

     for(int i=0; i<rows; i++)
        for(int j=0; j<cols; j++) 
            copy[i][j] = new Grid(original[i][j]);
     
     return copy;
   }
   
  
  public static Grid[][] forwardChecking(Grid[][] grid) 
  {
    int size = unassignedVariables(grid);
    Grid[] unass = new Grid[size];
    ArrayList<Grid[][]> states = new ArrayList<Grid[][]>(size);
    //states.add(0, grid);
    Grid[] og = new Grid[size];
    int[][] dim = new int[2][size];
    Grid[][] current = copyGrid(grid), newAssignment = null;
    int counter = 0, consistencyChecks = 0, row = 0, column = 0;
    boolean checker = false;
    
    for(int i=0; i<3; i++) 
    {
        for(int j=0; j<10; j++) 
        {
            if(grid[i][j].getAssignment() == -1) 
            {
                unass[counter] = new Grid(grid[i][j]);
                og[counter] = new Grid(grid[i][j]);
                dim[0][counter] = i;
                dim[1][counter] = j;
                counter++;
            }
        }
    }

    counter = 0;

    while (counter < size) 
    {
        //unass[counter].setAssignment(-1);
        consistencyChecks++;
        // System.out.println(" here 2");
//displayTenner(current);
        while(unass[counter].domainIsEmpty()) 
        {
           if(counter < states.size())
           states.remove(counter);
    
         //  System.out.println(" here 1");
          // displayTenner(states.get(counter-1));
            unass[counter] = new Grid(og[counter]);
            row = dim[0][counter];
            column = dim[1][counter];
            current[row][column] = new Grid(og[counter]);
              counter--;

            if(counter < 0) 
            {
                         //  System.out.println(" here 3");

                // Reset the assignment and the grid to original state
                System.arraycopy(og, 0, unass, 0, size);
                current = copyGrid(grid);
                states.clear();
                //states.ensureCapacity(size);
                counter = 0;
                //states.add(counter, current);
                checker = false;
                            consistencyChecks--;

                break;
            }
            consistencyChecks--;
        }

        //if (counter < 0) continue;

        row = dim[0][counter];
        column = dim[1][counter];

        int assignment = unass[counter].selectRandomFromDomain();
        unass[counter].setAssignment(assignment);
        unass[counter].removeFromDomain(assignment);

        current[row][column].setAssignment(assignment);
        current[row][column].removeFromDomain(assignment);
        newAssignment = copyGrid(current);

        checker = false;
        
        for(int i=0; i<10; i++) //removes assignment from its row's domains
             newAssignment[row][i].removeFromDomain(assignment);
         
          if(row == 0 || row == 2) //removes assignment from adjacent cells' domain
          {
             if(column == 0) //first column
             { 
               newAssignment[1][column].removeFromDomain(assignment);
               newAssignment[1][column + 1].removeFromDomain(assignment);
             } 
             else if(column == 9) //last column
             { 
               newAssignment[1][column].removeFromDomain(assignment);
               newAssignment[1][column - 1].removeFromDomain(assignment);
             }
             else if(column > 0 && column < 9) //middle columns
             {
               newAssignment[1][column].removeFromDomain(assignment);
               newAssignment[1][column - 1].removeFromDomain(assignment);
               newAssignment[1][column + 1].removeFromDomain(assignment);
             }
          } 
          else //second row
          {
             if(column == 0) //first column
             {
               newAssignment[0][column].removeFromDomain(assignment);
               newAssignment[0][column + 1].removeFromDomain(assignment);
               newAssignment[2][column].removeFromDomain(assignment);
               newAssignment[2][column + 1].removeFromDomain(assignment);
             }
             else if(column == 9) //last column
             {
               newAssignment[0][column].removeFromDomain(assignment);
               newAssignment[0][column - 1].removeFromDomain(assignment);
               newAssignment[2][column].removeFromDomain(assignment);
               newAssignment[2][column - 1].removeFromDomain(assignment);
             } 
             else if(column > 0 && column < 9) //middle columns
             { 
               newAssignment[0][column].removeFromDomain(assignment);
               newAssignment[0][column - 1].removeFromDomain(assignment);
               newAssignment[0][column + 1].removeFromDomain(assignment);
               newAssignment[2][column].removeFromDomain(assignment);
               newAssignment[2][column - 1].removeFromDomain(assignment);
               newAssignment[2][column + 1].removeFromDomain(assignment);
             }
          }
              
       if(row == 0)
       {  
          if(newAssignment[1][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[1][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                checker = true;
          }
          else if(newAssignment[1][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[1][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()));
          }
          else if(newAssignment[2][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[2][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()) > 9) 
                checker = true;
             else
                newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()));
          } 
          else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
          }
          
       }
       
       if(row == 1)
       {  
          if(newAssignment[0][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[0][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                checker = true;
          }
          else if(newAssignment[0][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[0][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()));
          }
          else if(newAssignment[2][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[2][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()) > 9) 
                checker = true;
             else  
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()));
          }
          else
          {
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
          }
          
       }
       
       if(row == 2)
       {  
          if(newAssignment[1][column].getAssignment() != -1 && newAssignment[0][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[1][column].getAssignment() + newAssignment[0][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                checker = true;
          }
          else if(newAssignment[1][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[1][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()) > 9) 
                checker = true;
              else
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()));
          }
          else if(newAssignment[0][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[0][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()) > 9) 
                checker = true;
             else
                 newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()));
          }
           else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
          } 
       }
        if (!checkDomains(newAssignment))
            checker = true;
        
        if (!checker) 
        {
            states.add(counter++,newAssignment);
        }
    }

    System.out.println("Consistency checks: " + consistencyChecks);
    return states.get(size-1);
}

  public static Grid[][] backtracking(Grid[][] grid) 
  {
    int size = unassignedVariables(grid);
    Grid[] unass = new Grid[size];
    ArrayList<Grid[][]> states = new ArrayList<Grid[][]>(size);
    //states.add(0, grid);
    Grid[] og = new Grid[size];
    int[][] dim = new int[2][size];
    Grid[][] current = copyGrid(grid), newAssignment = null;
    int counter = 0, consistencyChecks = 0, row = 0, column = 0;
    boolean checker = false;
    
    for(int i=0; i<3; i++) 
    {
        for(int j=0; j<10; j++) 
        {
            if(grid[i][j].getAssignment() == -1) 
            {
                unass[counter] = new Grid(grid[i][j]);
                og[counter] = new Grid(grid[i][j]);
                dim[0][counter] = i;
                dim[1][counter] = j;
                counter++;
            }
        }
    }

    counter = 0;

    while (counter < size) 
    {
        //unass[counter].setAssignment(-1);
        consistencyChecks++;
          // System.out.println("stuck here 2");
//displayTenner(current);
        while(unass[counter].domainIsEmpty()) 
        {
           if(counter < states.size())
           states.remove(counter);
    
          // System.out.println("stuck here 1");
          // displayTenner(states.get(counter-1));
            unass[counter] = new Grid(og[counter]);
            row = dim[0][counter];
            column = dim[1][counter];
            current[row][column] = new Grid(og[counter]);
              counter--;

            if(counter < 0 || counter == states.size()) 
            {
                // Reset the assignment and the grid to original state
                System.arraycopy(og, 0, unass, 0, size);
                current = copyGrid(grid);
                states.clear();
                //states.ensureCapacity(size);
                counter = 0;
                //states.add(counter, current);
                checker = false;
                            consistencyChecks--;

                break;
            }
            consistencyChecks--;
        }

        //if (counter < 0) continue;

        row = dim[0][counter];
        column = dim[1][counter];

        int assignment = unass[counter].selectRandomFromDomain();
        unass[counter].setAssignment(assignment);
        unass[counter].removeFromDomain(assignment);

        current[row][column].setAssignment(assignment);
        current[row][column].removeFromDomain(assignment);
        newAssignment = copyGrid(current);

        checker = false;
        
        
       for(int i=0; i<10; i++) //removes assignment from its row's domains
          {
              if(newAssignment[row][i].getAssignment() == assignment);
                 continue;
          }
         
          if(row == 0 || row == 2) //removes assignment from adjacent cells' domain
          {
             if(column == 0) //first column
             { 
               if(newAssignment[1][column].getAssignment() == assignment || newAssignment[1][column+1].getAssignment() == assignment)
                  continue;
             } 
             else if(column == 9) //last column
             { 
               if(newAssignment[1][column].getAssignment() == assignment || newAssignment[1][column-1].getAssignment() == assignment)
                  continue;
             }
             else if(column > 0 && column < 9) //middle columns
             {
                if(newAssignment[1][column].getAssignment() == assignment || newAssignment[1][column+1].getAssignment() == assignment || newAssignment[1][column-1].getAssignment() == assignment)
                   continue;
             }
          } 
          else //second row
          {
             if(column == 0) //first column
             {
               if(newAssignment[0][column].getAssignment() == assignment || newAssignment[0][column+1].getAssignment() == assignment || newAssignment[2][column].getAssignment() == assignment || newAssignment[2][column+1].getAssignment() == assignment)
                  continue;
             }
             else if(column == 9) //last column
             {
               if(newAssignment[0][column].getAssignment() == assignment || newAssignment[0][column-1].getAssignment() == assignment || newAssignment[2][column].getAssignment() == assignment || newAssignment[2][column-1].getAssignment() == assignment)
                  continue;
             } 
             else if(column > 0 && column < 9) //middle columns
             { 
               if(newAssignment[0][column].getAssignment() == assignment || newAssignment[0][column-1].getAssignment() == assignment || newAssignment[2][column].getAssignment() == assignment || newAssignment[2][column-1].getAssignment() == assignment || newAssignment[0][column+1].getAssignment() == assignment || newAssignment[2][column+1].getAssignment() == assignment)
                  continue;
             }
          }
              
       if(row == 0)
       {  
          if(newAssignment[1][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[1][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                continue;
          }
          else if(newAssignment[1][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[1][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()) > 9) 
                 continue;
          }
          else if(newAssignment[2][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[2][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()) > 9) 
                 continue;
          }
          else if(assignment > newAssignment[3][column].getAssignment())
              continue;
       }
       
       if(row == 1)
       {  
          if(newAssignment[0][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[0][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                continue;
          }
          else if(newAssignment[0][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[0][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()) > 9) 
                 continue;
          }
          else if(newAssignment[2][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[2][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()) > 9) 
                 continue;
          }
          else if(assignment > newAssignment[3][column].getAssignment())
              continue;
       }
       
       if(row == 2)
       {  
          if(newAssignment[1][column].getAssignment() != -1 && newAssignment[0][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[1][column].getAssignment() + newAssignment[0][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                continue;
          }
          else if(newAssignment[1][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[1][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()) > 9) 
                 continue;
          }
          else if(newAssignment[0][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[0][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()) > 9) 
                continue;
          }
          else if(assignment > newAssignment[3][column].getAssignment())
               continue;
       }
       
   
            states.add(counter,newAssignment);
            //current = copyGrid(newAssignment);
            counter++;
        
    }

    System.out.println("Consistency checks: " + consistencyChecks);
    return newAssignment;
}

  public static Grid[][] forwardCheckingMRV(Grid[][] grid) 
  {
    int size = unassignedVariables(grid);
    Grid[] unass = new Grid[size];
    ArrayList<Grid[][]> states = new ArrayList<Grid[][]>(size);
    //states.add(0, grid);
    Grid[] og = new Grid[size];
    int[][] dim = new int[2][size];
    Grid[][] current = copyGrid(grid), newAssignment = null;
    int counter = 0, consistencyChecks = 0, row = 0, column = 0;
    boolean checker = false;
    
    for(int i=0; i<3; i++) 
    {
        for(int j=0; j<10; j++) 
        {
            if(grid[i][j].getAssignment() == -1) 
            {
                unass[counter] = new Grid(grid[i][j]);
                og[counter] = new Grid (grid[i][j]);
                dim[0][counter] = i;
                dim[1][counter] = j;
                counter++;
            }
        }
    }


Integer[] indices = new Integer[size];
for (int i = 0; i < size; i++) {
    indices[i] = i;
}

Arrays.sort(indices, Comparator.comparingInt(i -> unass[i].domainSize()));
Arrays.sort(unass);
Arrays.sort(og);
// Create a temporary array to hold sorted dimensions
int[][] sortedDim = new int[2][size];

// Rearrange the dim array based on the sorted indices
for (int i = 0; i < size; i++) {
    sortedDim[0][i] = dim[0][indices[i]];
    sortedDim[1][i] = dim[1][indices[i]];
}

// Update the dim array with the sorted dimensions
dim = sortedDim; 
    
    counter = 0;

    while (counter < size) 
    {
        //unass[counter].setAssignment(-1);
        consistencyChecks++;
           //System.out.println("stuck here 2");
        while(unass[counter].domainIsEmpty()) 
        {
           if( counter < states.size())
           states.remove(counter);
    
          // System.out.println("stuck here 1");
          // displayTenner(states.get(counter-1));
            unass[counter] = new Grid(og[counter]);
            row = dim[0][counter];
            column = dim[1][counter];
            current[row][column] = new Grid(og[counter]);
              counter--;

            if(counter < 0) 
            {
                // Reset the assignment and the grid to original state
                System.arraycopy(og, 0, unass, 0, size);
                current = copyGrid(grid);
                states.clear();
                //states.ensureCapacity(size);
                counter = 0;
                //states.add(counter, current);
                checker = false;
                            consistencyChecks--;

                break;
            }
            consistencyChecks--;
        }

        //if (counter < 0) continue;

        row = dim[0][counter];
        column = dim[1][counter];

        int assignment = unass[counter].selectRandomFromDomain();
        unass[counter].setAssignment(assignment);
        unass[counter].removeFromDomain(assignment);

        current[row][column].setAssignment(assignment);
        current[row][column].removeFromDomain(assignment);
        newAssignment = copyGrid(current);

        checker = false;
        
        for(int i=0; i<10; i++) //removes assignment from its row's domains
             newAssignment[row][i].removeFromDomain(assignment);
         
          if(row == 0 || row == 2) //removes assignment from adjacent cells' domain
          {
             if(column == 0) //first column
             { 
               newAssignment[1][column].removeFromDomain(assignment);
               newAssignment[1][column + 1].removeFromDomain(assignment);
             } 
             else if(column == 9) //last column
             { 
               newAssignment[1][column].removeFromDomain(assignment);
               newAssignment[1][column - 1].removeFromDomain(assignment);
             }
             else if(column > 0 && column < 9) //middle columns
             {
               newAssignment[1][column].removeFromDomain(assignment);
               newAssignment[1][column - 1].removeFromDomain(assignment);
               newAssignment[1][column + 1].removeFromDomain(assignment);
             }
          } 
          else //second row
          {
             if(column == 0) //first column
             {
               newAssignment[0][column].removeFromDomain(assignment);
               newAssignment[0][column + 1].removeFromDomain(assignment);
               newAssignment[2][column].removeFromDomain(assignment);
               newAssignment[2][column + 1].removeFromDomain(assignment);
             }
             else if(column == 9) //last column
             {
               newAssignment[0][column].removeFromDomain(assignment);
               newAssignment[0][column - 1].removeFromDomain(assignment);
               newAssignment[2][column].removeFromDomain(assignment);
               newAssignment[2][column - 1].removeFromDomain(assignment);
             } 
             else if(column > 0 && column < 9) //middle columns
             { 
               newAssignment[0][column].removeFromDomain(assignment);
               newAssignment[0][column - 1].removeFromDomain(assignment);
               newAssignment[0][column + 1].removeFromDomain(assignment);
               newAssignment[2][column].removeFromDomain(assignment);
               newAssignment[2][column - 1].removeFromDomain(assignment);
               newAssignment[2][column + 1].removeFromDomain(assignment);
             }
          }
              
       if(row == 0)
       {  
          if(newAssignment[1][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[1][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                checker = true;
          }
          else if(newAssignment[1][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[1][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()));
          }
          else if(newAssignment[2][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[2][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()) > 9) 
                checker = true;
             else
                newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()));
          } 
          else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
          }
          
       }
       
       if(row == 1)
       {  
          if(newAssignment[0][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[0][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                checker = true;
          }
          else if(newAssignment[0][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[0][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()));
          }
          else if(newAssignment[2][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[2][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()) > 9) 
                checker = true;
             else  
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[2][column].getAssignment()));
          }
          else
          {
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
          }
          
       }
       
       if(row == 2)
       {  
          if(newAssignment[1][column].getAssignment() != -1 && newAssignment[0][column].getAssignment() != -1)
          { 
            if((assignment + newAssignment[1][column].getAssignment() + newAssignment[0][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
                checker = true;
          }
          else if(newAssignment[1][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[1][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()) > 9) 
                checker = true;
              else
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[1][column].getAssignment()));
          }
          else if(newAssignment[0][column].getAssignment() != -1)
          {
             if((assignment + newAssignment[0][column].getAssignment()) > newAssignment[3][column].getAssignment() || newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()) > 9) 
                checker = true;
             else
                 newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - (assignment + newAssignment[0][column].getAssignment()));
          }
           else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].getAssignment() - assignment);
          } 
       }
        if (!checkDomains(newAssignment))
            checker = true;
        
        if (!checker) 
        {
            states.add(counter++,newAssignment);
           // if(counter != 0)
                //counter++;
        }
    }

    System.out.println("Consistency checks: " + consistencyChecks);
    return newAssignment;
}
  
  }      



