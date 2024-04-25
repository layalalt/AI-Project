import java.util.*;

public class TennerGrid 
{
  static class Grid implements Comparable<Grid>
  {
      
    int[] domain = {0,1,2,3,4,5,6,7,8,9};
    int assignment;

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
           while((num = domain[random.nextInt(10)]) == -1);
        return num; //returns a random number fromm the domain using a random index

    }

    public boolean domainIsEmpty()
    {
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
    
    public int compareTo(Grid p) //for MRV sorting
    {
        return this.domainSize() - p.domainSize();
    }
     
  }

   public static void main(String[] args) 
   {
       
       Grid[][] tenner = generateTenner(); //generates a 4 by 10 Tenner Grid

       System.out.println("Initial grid:");
       displayTenner(tenner);
             
       displayUnassignedVariables(tenner); 
  
       int num = unassignedVariables(tenner);
        
       System.out.println("Number of unassigned variables: " + num +"\n\n");
       
       System.out.print("Backtracking: ");
       Grid[][] backtrackingSolution = backtracking(tenner);
       
       if(backtrackingSolution == null) //chance grid is unsolvable 
          return;
              
       displayTenner(backtrackingSolution);
       
       System.out.println();
       
       unassignedVariables2(tenner, backtrackingSolution);

       System.out.println();
       
       Grid[][] tenner2 = setForForwardChecking(tenner);

      
       System.out.print("\nForward Checking: ");
       Grid[][] FCSolution = forwardChecking(tenner2);
      
       displayTenner(FCSolution);
       
       System.out.println();
               
       unassignedVariables2(tenner, FCSolution);

       System.out.println();
       
       
       System.out.print("\nForward Checking with MRV: ");
       Grid[][] FCMRVSolution = forwardCheckingMRV(tenner2);
        
       displayTenner(FCMRVSolution);
       
       System.out.println();
       
       unassignedVariables2(tenner, FCMRVSolution);

       System.out.println();
   } 
   

   public static Grid[][] generateTenner()
   {
        Grid[][] tenner = new Grid[4][10]; //4 by 10
        for(int i=0; i<4; i++) 
          for(int j=0; j<10; j++)
             tenner[i][j] = new Grid();
        
        Random random = new Random();

        long startTime = System.currentTimeMillis(), endTime = Integer.MAX_VALUE; //to prevent infinite loops
        boolean infiniteLoop = false; //infinite loops caused by a bad grid generation 
        double counter = 0.1;
           
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
                infiniteLoop = false;
                endTime = System.currentTimeMillis();
                
                if((endTime - startTime)/1000.0 > counter) //loop took more that a 0.1 seconds; stuck
                {    
                    infiniteLoop = true;  
                    counter+=0.1;
                    break;
                }
                
                int num = random.nextInt(10);
                boolean exists = false;
                for(int k=j; k>=0; k--) //checks if it exists already in row; ensures uniqueness
                {
                 if(num == tenner[i][k].assignment)
                   exists = true;
                }
                if(i == 1) //second row diagnoal checker
                {
                  if(j == 0)
                  {
                   if((num == tenner[0][0].assignment) || (num == tenner[0][1].assignment)) //first element
                     exists = true;
                  }
                  else if(j == 9)
                  {  
                   if((num == tenner[0][8].assignment) || (num == tenner[0][9].assignment)) //last element  
                     exists = true;
                  }
                  else if(j>0 && j<9)
                  {  
                   if((num == tenner[0][j - 1].assignment) || (num == tenner[0][j].assignment) || (num == tenner[0][j + 1].assignment)) //middle elements
                     exists = true; 
                  }     
                }      
                else if(i == 2) //third row
                {
                 if(j == 0)
                 {
                  if((num == tenner[1][0].assignment) || (num == tenner[1][1].assignment)) //first element
                     exists = true;
                 }
                 else if(j == 9)
                 {  
                   if((num == tenner[1][8].assignment) || (num == tenner[1][9].assignment)) //last element  
                     exists = true;
                 }
                  else if (j>0 && j<9) //middle elements
                  {  
                   if((num == tenner[1][j - 1].assignment) || (num == tenner[1][j].assignment) || (num == tenner[1][j + 1].assignment))
                     exists = true; 
                  }  
                } 
                if(exists) //violates constraints
                  j--;
                else
                  tenner[i][j].assignment = num; //assign to grid
          }
          if(infiniteLoop) //restart
             i=0;
        }
        
        for(int i=0; i<10; i++) //sum generator
        { 
            tenner[3][i].assignment = tenner[0][i].assignment + tenner[1][i].assignment  + tenner[2][i].assignment;
            tenner[3][i].clearDomain();
        }
        
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
             boolean result = random.nextDouble() < 0.55; //generates probability to make a cell empty
             if(result) //empty grid index
                tenner[i][j].assignment = -1;
             else
                tenner[i][j].clearDomain();  
          }
        } 
        return tenner; 
   }
    
   
   public static Grid[][] setForForwardChecking(Grid[][] g) //sets domains of grid elements for the forward checking algorithms (preprocessing)
   {
        Grid[][] tenner = copyGrid(g);
        for(int i=0; i<3; i++) //domain setters
        {
          for(int j=0; j<10; j++)
          {
             if(tenner[i][j].assignment != -1) //if cell is assigned 
               continue;
             else //not assigned
             {
               tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment);
               
               for(int k=0; k<10; k++) //removes from domain row elements
                  tenner[i][j].removeFromDomain(tenner[i][k].assignment);
               
               if(i == 0 || i == 2) //first and third rows diagnoal checker
                {
                  if(j == 0) //first column
                  {
                     tenner[i][j].removeFromDomain(tenner[1][0].assignment);
                     tenner[i][j].removeFromDomain(tenner[1][1].assignment);
                  }
                  else if(j == 9) //last column
                  {  
                     tenner[i][j].removeFromDomain(tenner[1][8].assignment);
                     tenner[i][j].removeFromDomain(tenner[1][9].assignment);
                  }
                  else if (j>0 && j<9) //middle columns
                  {  
                     tenner[i][j].removeFromDomain(tenner[1][j-1].assignment);
                     tenner[i][j].removeFromDomain(tenner[1][j].assignment);
                     tenner[i][j].removeFromDomain(tenner[1][j+1].assignment);
                  }     
                }      
                else //second row
                {
                 if(j == 0)
                  {
                     tenner[i][j].removeFromDomain(tenner[0][0].assignment);
                     tenner[i][j].removeFromDomain(tenner[0][1].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][0].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][1].assignment);
                  }
                  else if(j == 9)
                  {  
                     tenner[i][j].removeFromDomain(tenner[0][8].assignment);
                     tenner[i][j].removeFromDomain(tenner[0][9].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][8].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][9].assignment);
                  }
                  else if (j>0 && j<9)
                  {  
                     tenner[i][j].removeFromDomain(tenner[0][j-1].assignment);
                     tenner[i][j].removeFromDomain(tenner[0][j].assignment);
                     tenner[i][j].removeFromDomain(tenner[0][j+1].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][j-1].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][j].assignment);
                     tenner[i][j].removeFromDomain(tenner[2][j+1].assignment);
                  }
                }
                       
               if(i == 0)
               {  
                 if(tenner[1][j].assignment != -1 && tenner[2][j].assignment != -1)
                 { 
                    tenner[i][j].clearDomain();
                    tenner[i][j].addToDomain(tenner[3][j].assignment - (tenner[1][j].assignment + tenner[2][j].assignment));
                 }
                 else if(tenner[1][j].assignment != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment - tenner[1][j].assignment);
                 }
                 else if(tenner[2][j].assignment != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment - tenner[2][j].assignment);
                 }
               }
               else if(i == 1)
               {  
                 if(tenner[0][j].assignment != -1 && tenner[2][j].assignment != -1)
                 { 
                    tenner[i][j].clearDomain();
                    tenner[i][j].addToDomain(tenner[3][j].assignment - (tenner[0][j].assignment + tenner[2][j].assignment));
                 }
                 else if(tenner[0][j].assignment != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment - tenner[0][j].assignment);
                 }
                 else if(tenner[2][j].assignment != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment - tenner[2][j].assignment);
                 }  
               }
               else if(i == 2)
               {  
                 if(tenner[0][j].assignment != -1 && tenner[1][j].assignment != -1)
                 { 
                    tenner[i][j].clearDomain();
                    tenner[i][j].addToDomain(tenner[3][j].assignment - (tenner[0][j].assignment + tenner[1][j].assignment));
                 }
                 else if(tenner[0][j].assignment != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment - tenner[0][j].assignment);
                 }
                 else if(tenner[1][j].assignment != -1)
                 {
                    tenner[i][j].removeFromDomainGreaterThan(tenner[3][j].assignment - tenner[1][j].assignment);
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
               if(g[i][j].assignment == -1)
                 System.out.print(g[i][j].assignment + "| ");
               else
                 System.out.print(g[i][j].assignment + " | ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
        for(int i=0; i<10; i++) 
        {
           if(g[3][i].assignment > 9)
              System.out.print(g[3][i].assignment + "| ");
           else
              System.out.print(g[3][i].assignment + " | ");
        }
        System.out.println();
   }
    
    
   public static boolean complete(Grid[][] g) //checks if the grid is fully assigned or not
   {
       for(int i=0; i<3; i++)
       {
           for(int j=0; j<10; j++)
           { 
               if(g[i][j].assignment == -1)
                   return false;
           }
       } 
       return true;
   }
    
    
   public static boolean checkDomains(Grid[][] g) //checks if any unassigned grid elements has an empty domain
   {
      for(int i=0; i<3; i++) 
      {
           for(int j=0; j<10; j++)
           { 
               if(g[i][j].domainIsEmpty() && g[i][j].assignment == -1)
                   return false;
           }
      } 
      return true;   
   }
   
   
   public static int unassignedVariables(Grid[][] grid)
   {
       int counter = 0;
       for(int i=0; i<3; i++)
       {
           for(int j=0; j<10; j++)
           { 
               if(grid[i][j].assignment == -1)
               {  
                  counter++;
               }
           }
       }
       System.out.println();
       return counter;
   }
   
   
   public static void displayUnassignedVariables(Grid[][] grid)
   {
       int counter = unassignedVariables(grid);
       int notLast = 0;
       System.out.print("Grid variables: ");
       for(int i=0; i<3; i++)
       {
           for(int j=0; j<10; j++)
           { 
               if(grid[i][j].assignment == -1)
               {  
                 notLast++;
                 if(notLast != counter)
                   System.out.print("grid[" + i + "][" + j + "], ");
                 else
                   System.out.print("grid[" + i + "][" + j + "] ");
               }
           }
       }
       System.out.println();
   }
   
   
   public static void unassignedVariables2(Grid[][] grid, Grid[][] solution)
   {
       System.out.print("Final grid assignments: ");
       for(int i=0; i<3; i++)
       {
           for(int j=0; j<10; j++)
           { 
               if(grid[i][j].assignment == -1)
               {  
                 System.out.print("grid[" + i + "][" + j + "]=" + solution[i][j].assignment + "  ");
               }
           }
       }
       System.out.println();
   }
   
   
   public static Grid[][] copyGrid(Grid[][] original) //whole grid copy method
   {
     int rows = 4;
     int cols = 10;
     Grid[][] copy = new Grid[rows][cols];

     for(int i=0; i<rows; i++)
        for(int j=0; j<cols; j++) 
            copy[i][j] = new Grid(original[i][j]);
     
     return copy;
   }
   
   
   public static Grid[][] backtracking(Grid[][] grid) 
   {

     int size = unassignedVariables(grid);
     ArrayList<Grid[][]> states = new ArrayList<Grid[][]>(); //list of all the states of the grid
     Grid[] original = new Grid[size]; //stores the original unassigned elements and their domains; helps with resetting after backtracking
     int[][] dim = new int[2][size]; //stores positions of unassigned elements
     Grid[][] current = copyGrid(grid), newAssignment = null;
     int counter = 0, consistencyChecks = 0, row = 0, column = 0;
     boolean noSolution = false, checker = false;
    
     for(int i=0; i<3; i++) 
     {
        for(int j=0; j<10; j++) 
        {
            if(grid[i][j].assignment == -1) 
            {
                original[counter] = new Grid(grid[i][j]);
                dim[0][counter] = i;
                dim[1][counter] = j;
                counter++;
            }
        }
     }
     
     long startTime = System.currentTimeMillis(), endTime = Integer.MAX_VALUE; //to prevent infinite loops
     counter = 0;
     
     long start = System.currentTimeMillis();
     while(!complete(current) || checker) 
     {
        checker = false;
        consistencyChecks++;
        row = dim[0][counter];
        column = dim[1][counter];
        
        endTime = System.currentTimeMillis();
        if((endTime - startTime)/1000.0 > 10) //algorithm took more than 10 seconds; unsolvable
        {    
            System.out.println("Generated grid is unsolvable.");
            noSolution = true;  
            break;
        }   

        while(current[row][column].domainIsEmpty()) //current position has no assignment; backtrack
        {
           
           current[row][column] = new Grid(original[counter]);
           states.remove(--counter);

           consistencyChecks--;
           row = dim[0][counter];
           column = dim[1][counter];
        }

        row = dim[0][counter];
        column = dim[1][counter];

        int assignment = current[row][column].selectRandomFromDomain();
        current[row][column].assignment = assignment;
        current[row][column].removeFromDomain(assignment);
        newAssignment = copyGrid(current);
                
        for(int i=0; i<10; i++) //checks row constraint
        {
           if(newAssignment[row][i].assignment == assignment && i != column)           
           {
               checker = true;
               break;
           }
           
        }

        if(row == 0 || row == 2) //checks diagonal constraints
        {
             if(column == 0) //first column
             { 
               if(newAssignment[1][column].assignment == assignment || newAssignment[1][column+1].assignment == assignment)
                  checker = true;
             } 
             else if(column == 9) //last column
             { 
               if(newAssignment[1][column].assignment == assignment || newAssignment[1][column-1].assignment == assignment)
                  checker = true;
             }
             else if(column > 0 && column < 9) //middle columns
             {
                if(newAssignment[1][column].assignment == assignment || newAssignment[1][column+1].assignment == assignment || newAssignment[1][column-1].assignment == assignment)
                  checker = true;
             }
        } 
        else //second row
        {
             if(column == 0) //first column
             {
               if(newAssignment[0][column].assignment == assignment || newAssignment[0][column+1].assignment == assignment || newAssignment[2][column].assignment == assignment || newAssignment[2][column+1].assignment == assignment)
                  checker = true;
             }
             else if(column == 9) //last column
             {
               if(newAssignment[0][column].assignment == assignment || newAssignment[0][column-1].assignment == assignment || newAssignment[2][column].assignment == assignment || newAssignment[2][column-1].assignment == assignment)
                  checker = true;
             } 
             else if(column > 0 && column < 9) //middle columns
             { 
               if(newAssignment[0][column].assignment == assignment || newAssignment[0][column-1].assignment == assignment || newAssignment[2][column].assignment == assignment || newAssignment[2][column-1].assignment == assignment || newAssignment[0][column+1].assignment == assignment || newAssignment[2][column+1].assignment == assignment)
                  checker = true;
             }
        }
              
        if(row == 0) //sum constraint checker
        {  
          if(newAssignment[1][column].assignment != -1 && newAssignment[2][column].assignment != -1)
          { 
            if((assignment + newAssignment[1][column].assignment + newAssignment[2][column].assignment) != newAssignment[3][column].assignment) 
                  checker = true;
          }
          else if(newAssignment[1][column].assignment != -1)
          {
             if((assignment + newAssignment[1][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment) > 9) 
                  checker = true;
          }
          else if(newAssignment[2][column].assignment != -1)
          {
             if((assignment + newAssignment[2][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment) > 9) 
                  checker = true;
          }
          else if(assignment > newAssignment[3][column].assignment)
                  checker = true;
        }
       
        if(row == 1) //sum constraint checker
        {  
          if(newAssignment[0][column].assignment != -1 && newAssignment[2][column].assignment != -1)
          { 
            if((assignment + newAssignment[0][column].assignment + newAssignment[2][column].assignment) != newAssignment[3][column].assignment) 
                  checker = true;
          }
          else if(newAssignment[0][column].assignment != -1)
          {
             if((assignment + newAssignment[0][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment) > 9) 
                  checker = true;
          }
          else if(newAssignment[2][column].assignment != -1)
          {
             if((assignment + newAssignment[2][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment) > 9) 
                  checker = true;
          }
          else if(assignment > newAssignment[3][column].assignment)
                  checker = true;
        }
       
        if(row == 2) //sum constraint checker
        {  
          if(newAssignment[1][column].assignment != -1 && newAssignment[0][column].assignment != -1)
          { 
            if((assignment + newAssignment[1][column].assignment + newAssignment[0][column].assignment) != newAssignment[3][column].assignment) 
                  checker = true;
          }
          else if(newAssignment[1][column].assignment != -1)
          {
             if((assignment + newAssignment[1][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment) > 9) 
                  checker = true;
          }
          else if(newAssignment[0][column].assignment != -1)
          {
             if((assignment + newAssignment[0][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment) > 9) 
                  checker = true;
          }
          else if(assignment > newAssignment[3][column].assignment)
                  checker = true;
        }
       
        if(!checker) //assignment doesn't violate any constraints
        {
            states.add(counter++,newAssignment); //add it to list
            current = copyGrid(newAssignment);
        }
        else 
           continue; //try another assignment
     }
     
      if(noSolution) //unsolvable
         return null;
      
      System.out.println("Consistency checks: " + consistencyChecks);
      long end = System.currentTimeMillis();
      System.out.println("Execution time: " + (end - start) + " milliseconds");
      return current;
      
   }
   
   
   public static Grid[][] forwardChecking(Grid[][] grid) 
   {

     int size = unassignedVariables(grid);
     ArrayList<Grid[][]> states = new ArrayList<Grid[][]>(); //list of all the states of the grid
     Grid[] original = new Grid[size]; //stores the original unassigned elements and their domains; helps with resetting after backtracking
     int[][] dim = new int[2][size]; //stores positions of unassigned elements
     Grid[][] current = copyGrid(grid), newAssignment = null;
     int counter = 0, consistencyChecks = 0, row = 0, column = 0;
     boolean checker = false;
    
     for(int i=0; i<3; i++) 
     {
        for(int j=0; j<10; j++) 
        {
            if(grid[i][j].assignment == -1) 
            {
                original[counter] = new Grid(grid[i][j]);
                dim[0][counter] = i;
                dim[1][counter] = j;
                counter++;
            }
        }
     }

     counter = 0;
     
     long start = System.currentTimeMillis();
     while(!complete(current) || checker) 
     {
        consistencyChecks++;
        row = dim[0][counter];
        column = dim[1][counter];
        
        while(current[row][column].domainIsEmpty()) //current position has no assignment; backtrack
        {
           
           current[row][column] = new Grid(original[counter]);
           states.remove(--counter);
           if(counter == 0) //reached "root"; restart
           {
              current = copyGrid(grid);
              states.clear();
              counter = 0;
              checker = false;
              consistencyChecks--;
              break;
            }
            consistencyChecks--;
            row = dim[0][counter];
            column = dim[1][counter];
        }

        row = dim[0][counter];
        column = dim[1][counter];

        int assignment = current[row][column].selectRandomFromDomain();
        current[row][column].assignment = assignment;
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
              
        if(row == 0) //sum constraint checker
        {  
          if(newAssignment[1][column].assignment != -1 && newAssignment[2][column].assignment != -1)
          { 
            if((assignment + newAssignment[1][column].assignment + newAssignment[2][column].assignment) != newAssignment[3][column].assignment) 
                checker = true;
          }
          else if(newAssignment[1][column].assignment != -1)
          {
             if((assignment + newAssignment[1][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment));
          }
          else if(newAssignment[2][column].assignment != -1)
          {
             if((assignment + newAssignment[2][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment) > 9) 
                checker = true;
             else
                newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment));
          } 
          else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
          }
          
        }
       
        if(row == 1) //sum constraint checker
        {  
          if(newAssignment[0][column].assignment != -1 && newAssignment[2][column].assignment != -1)
          { 
            if((assignment + newAssignment[0][column].assignment + newAssignment[2][column].assignment) != newAssignment[3][column].assignment) 
                checker = true;
          }
          else if(newAssignment[0][column].assignment != -1)
          {
             if((assignment + newAssignment[0][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment));
          }
          else if(newAssignment[2][column].assignment != -1)
          {
             if((assignment + newAssignment[2][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment) > 9) 
                checker = true;
             else  
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment));
          }
          else
          {
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
          }
          
        }
       
        if(row == 2) //sum constraint checker
        {  
          if(newAssignment[1][column].assignment != -1 && newAssignment[0][column].assignment != -1)
          { 
            if((assignment + newAssignment[1][column].assignment + newAssignment[0][column].assignment) != newAssignment[3][column].assignment) 
                checker = true;
          }
          else if(newAssignment[1][column].assignment != -1)
          {
             if((assignment + newAssignment[1][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment) > 9) 
                checker = true;
              else
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment));
          }
          else if(newAssignment[0][column].assignment != -1)
          {
             if((assignment + newAssignment[0][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment) > 9) 
                checker = true;
             else
                 newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment));
          }
           else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
          } 
        }
        if(!checkDomains(newAssignment)) //if any unassigned element's domain is empty
           checker = true;
        
        if(!checker) //assignment doesn't violate any constraints
        {
            states.add(counter++,newAssignment); //add it to list
            current = copyGrid(newAssignment);
        }
        else 
           continue; //try another assignment
     }

     System.out.println("Consistency checks: " + consistencyChecks);
     long end = System.currentTimeMillis();
     System.out.println("Execution time: " + (end - start) + " milliseconds");
     return current;
   }

   
   public static Grid[][] forwardCheckingMRV(Grid[][] grid) 
   {
 
     int size = unassignedVariables(grid);
     ArrayList<Grid[][]> states = new ArrayList<Grid[][]>(); //list of all the states of the grid
     Grid[] original = new Grid[size]; //stores the original unassigned elements and their domains; helps with resetting after backtracking
     int[][] dim = new int[2][size]; //stores positions of unassigned elements
     Grid[][] current = copyGrid(grid), newAssignment = null;
     int counter = 0, consistencyChecks = 0, row = 0, column = 0;
     boolean checker = false;
    
     for(int i=0; i<3; i++) 
     {
        for(int j=0; j<10; j++) 
        {
            if(grid[i][j].assignment == -1) 
            {
                original[counter] = new Grid(grid[i][j]);
                dim[0][counter] = i;
                dim[1][counter] = j;
                counter++;
            }
        }
     }
     
     Integer[] indices = new Integer[size];
     for(int i=0; i<size; i++) 
        indices[i] = i;

     Arrays.sort(indices, Comparator.comparingInt(i -> original[i].domainSize())); //sort indices according to the unassigned variables domain size 
     Arrays.sort(original); //sorts unassigned elements according to their domain size
     
     int[][] sortedDim = new int[2][size];

     for(int i=0; i<size; i++) //sorted dimensions coincide with the MRV sorting
     {
       sortedDim[0][i] = dim[0][indices[i]];
       sortedDim[1][i] = dim[1][indices[i]];
     }

     dim = sortedDim; 
    
     counter = 0;
     
     long start = System.currentTimeMillis();
     while(!complete(current) || checker) 
     {
        consistencyChecks++;
        row = dim[0][counter];
        column = dim[1][counter];
        
        while(current[row][column].domainIsEmpty()) //current position has no assignment; backtrack
        {
           
           current[row][column] = new Grid(original[counter]);
           states.remove(--counter);
           if(counter == 0) //reached "root"; restart
           {
              current = copyGrid(grid);
              states.clear();
              counter = 0;
              checker = false;
              consistencyChecks--;
              break;
            }
            consistencyChecks--;
            row = dim[0][counter];
            column = dim[1][counter];
        }

        row = dim[0][counter];
        column = dim[1][counter];

        int assignment = current[row][column].selectRandomFromDomain();
        current[row][column].assignment = assignment;
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
              
        if(row == 0) //sum constraint checker
        {  
          if(newAssignment[1][column].assignment != -1 && newAssignment[2][column].assignment != -1)
          { 
            if((assignment + newAssignment[1][column].assignment + newAssignment[2][column].assignment) != newAssignment[3][column].assignment) 
                checker = true;
          }
          else if(newAssignment[1][column].assignment != -1)
          {
             if((assignment + newAssignment[1][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment));
          }
          else if(newAssignment[2][column].assignment != -1)
          {
             if((assignment + newAssignment[2][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment) > 9) 
                checker = true;
             else
                newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment));
          } 
          else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
          }
          
        }
       
        if(row == 1) //sum constraint checker
        {  
          if(newAssignment[0][column].assignment != -1 && newAssignment[2][column].assignment != -1)
          { 
            if((assignment + newAssignment[0][column].assignment + newAssignment[2][column].assignment) != newAssignment[3][column].assignment) 
                checker = true;
          }
          else if(newAssignment[0][column].assignment != -1)
          {
             if((assignment + newAssignment[0][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment) > 9) 
                checker = true;
             else
                 newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment));
          }
          else if(newAssignment[2][column].assignment != -1)
          {
             if((assignment + newAssignment[2][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment) > 9) 
                checker = true;
             else  
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[2][column].assignment));
          }
          else
          {
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
             newAssignment[2][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
          }
          
        }
       
        if(row == 2) //sum constraint checker
        {  
          if(newAssignment[1][column].assignment != -1 && newAssignment[0][column].assignment != -1)
          { 
            if((assignment + newAssignment[1][column].assignment + newAssignment[0][column].assignment) != newAssignment[3][column].assignment) 
                checker = true;
          }
          else if(newAssignment[1][column].assignment != -1)
          {
             if((assignment + newAssignment[1][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment) > 9) 
                checker = true;
              else
                 newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[1][column].assignment));
          }
          else if(newAssignment[0][column].assignment != -1)
          {
             if((assignment + newAssignment[0][column].assignment) > newAssignment[3][column].assignment || newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment) > 9) 
                checker = true;
             else
                 newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - (assignment + newAssignment[0][column].assignment));
          }
           else
          {
             newAssignment[1][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
             newAssignment[0][column].removeFromDomainGreaterThan(newAssignment[3][column].assignment - assignment);
          } 
        }
        if(!checkDomains(newAssignment))
           checker = true;
        
        if(!checker) //assignment doesn't violate any constraints
        {
            states.add(counter++,newAssignment); //add it to list
            current = copyGrid(newAssignment);
        }
        else 
           continue; //try another assignment
     }

     System.out.println("Consistency checks: " + consistencyChecks);
     long end = System.currentTimeMillis();
     System.out.println("Execution time: " + (end - start) + " milliseconds");
     return current;
   }

}
