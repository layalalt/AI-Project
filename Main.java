import java.util.*;

public class Main 
{

    public static void main(String[] args) 
    {
       
        Grid[][] tenner = generateTennerforForwardChecking();//10 by 4

        displayTenner(tenner);
        
        System.out.println();

        int num =unassignedVariables(tenner);
        
        System.out.println("Number of unassigned variables: " + num +"\n");
        
        Grid[][] solution= forwardChecking(tenner);
        
        displayTenner(solution);
        
        System.out.println();
        
        Grid[][] solution2 = forwardCheckingMRV(tenner);
        
        displayTenner(solution2);

        //Backtracking
        Grid[][] tenner3 = generateTennerforBacktracking();
        
        displayTenner(tenner3);
        
        System.out.println();
        
        int num3 =unassignedVariables(tenner3);
        
        System.out.println("Number of unassigned variables: " + num3 +"\n");
        
        Grid[][] solution3= solveTennerBacktracking(tenner3);
        
        displayTenner(solution3);
        
        System.out.println();

       /** for(int i=0; i<3; i++) //for testing purposes
          for(int j=0; j<10; j++)
          { 
            tenner[i][j].displayDomain();
            System.out.println();
          }*/

    }
    
    
    
    
    public static Grid[][] generateTennerforForwardChecking()
    {
        Grid[][] tenner = new Grid[4][10]; //10 by 4
        for(int i=0; i<4; i++) 
          for(int j=0; j<10; j++)
             tenner[i][j] = new Grid();
        
        Random random = new Random();

        long startTime = System.currentTimeMillis(), endTime = 1000000; //to prevent infinite loops
        boolean infiniteLoop = false; //infinite loops caused my a bad grid generation 
        int counter = 1;
           
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
                infiniteLoop = false;
                endTime = System.currentTimeMillis();
                
                if((endTime - startTime)/1000.0 > counter) //loop took more that a second; stuck
                {    
                    infiniteLoop = true;  
                    counter++;
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
                  else if (j>0 && j<9)
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
            tenner[3][i].clearDomainSum();
        }
        
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
             boolean result = random.nextDouble() < 0.45; //generates probability to make a cell empty
             if(result) //fill grid index
                tenner[i][j].setAssignment(-1);
          }
        } 
        
        for(int i=0; i<3; i++) //domain setters
        {
          for(int j=0; j<10; j++)
          {
             if(tenner[i][j].getAssignment() != -1) //if cell is assigned 
                tenner[i][j].clearDomain();
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
               if(g[i][j].getAssignment() == -1 && g[i][j].domainIsEmpty())
                   return false;
           }
      } 
      return true;   
    }
          
   /** public static boolean satisfiesConstraints(Grid[][] grid, int row, int column, int assignment)
    {
       Grid[][] newAssignment = grid;
       for(int i=0; i<10; i++) //removes assignment from its row's domains
       {
           newAssignment[row][i].removeFromDomain(assignment);
           if(newAssignment[row][i].getAssignment() == -1 && newAssignment[row][i].domainIsEmpty()) //inconsistency
               return false;
       } 
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
       if(!checkDomains(newAssignment))
           return false;    
          
      /** if(row == 0 && newAssignment[1][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          if((assignment + newAssignment[1][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
              return false;
            
       if(row == 2 && newAssignment[1][column].getAssignment() != -1 && newAssignment[0][column].getAssignment() != -1)
          if((assignment + newAssignment[1][column].getAssignment() + newAssignment[0][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
              return false;
            
       if(row == 1 && newAssignment[0][column].getAssignment() != -1 && newAssignment[2][column].getAssignment() != -1)
          if((assignment + newAssignment[0][column].getAssignment() + newAssignment[2][column].getAssignment()) != newAssignment[3][column].getAssignment()) 
              return false;
     // grid = newAssignment;
      return true;       
    }*/
    

   /** public static void forwardChecking(Grid[][] grid)
    {
        boolean failed = false;

        for (int i=0; i<3; i++) 
        {
            for (int j=0; j<10; j++) 
            {
                if(grid[i][j].getAssignment() == -1) //if not assigned
                    grid[i][j].setAssignment(grid[i][j].selectRandomFromDomain()); //assigns randomly from domain
              
                int current = grid[i][j].getAssignment();
                
                if(j<9)
                {
                    for (int k=9; k>j+1; k--) //removes assignment from its row's domains
                    {
                        grid[i][k].removeFromDomain(current);
                        if(grid[i][k].domainIsEmpty())
                        {
                          grid[i][j].removeFromDomain(current);
                          grid[i][k].addToDomain(current);
                          failed = true;
                          break;
                        }
                    }
                    if(failed)
                    {
                        j--;
                        continue;
                    }

                    if(i<2) //removes assignment from adjacent cells' domain for rows 1 and 2
                    {
                        grid[i][j+1].removeFromDomain(current);
                        if(grid[i][j+1].domainIsEmpty())//forward checking
                        {
                          grid[i][j].removeFromDomain(current);
                          grid[i][j+1].addToDomain(current);
                          failed = true;
                          j--;
                          continue;
                        }

                     // if (grid[i][j+1].getAssignment()==-1) 
                         //   grid[i][j+1].removeFromDomain(current); ? 
                        grid[i+1][j].removeFromDomain(current);
                        if(grid[i+1][j].domainIsEmpty())
                        {
                          grid[i][j].removeFromDomain(current);
                          grid[i+1][j].addToDomain(current);
                          failed = true;
                          j--;
                          continue;
                        }
                        grid[i+1][j+1].removeFromDomain(current);
                        if(grid[i+1][j+1].domainIsEmpty())
                        {
                          grid[i][j].removeFromDomain(current);
                          grid[i+1][j+1].addToDomain(current);
                          failed = true;
                          j--;
                          continue;
                        }

                    }
                    else if(i==2) //removes assignment from adjacent cells' domain for row 3
                    {
                        grid[i][j+1].removeFromDomain(current);
                        if(grid[i][j+1].domainIsEmpty())
                        {
                          grid[i][j].removeFromDomain(current);
                          grid[i][j+1].addToDomain(current);
                          failed = true;
                          j--;
                          continue;
                        }

                    }
                }
                else if(j==9 && i<2)
                    grid[i+1][j].removeFromDomain(current);
                    if(grid[i+1][j].domainIsEmpty())
                    {
                      grid[i][j].removeFromDomain(current);
                      grid[i+1][j].addToDomain(current);
                      failed = true;
                      j--;
                      continue;
                    }
                    

            }// end for loop j
        }//end for loop i

    }  */
    
    
  public static Grid[][] forwardChecking(Grid[][] grid) //kind of works most of the time; gets stuck sometimes NOT ANYMORE
  {      
     Random random = new Random();
     int row, column, consistencyChecks = 0;
     Grid[][] current = copyGrid(grid), newAssignment;
     Stack<Grid[][]> s = new Stack();
     s.push(grid);
     boolean backtrack = false, checker = false;
     
     while(!complete(current)) 
     {
        current = copyGrid(s.peek());
        backtrack = false;
        checker = false;
        
        do 
        {
           row = random.nextInt(3);
           column = random.nextInt(10);
        }while(current[row][column].getAssignment() != -1 && !complete(current));

        do
        {
          current[row][column].setAssignment(-1);
          consistencyChecks++;
          if(current[row][column].domainIsEmpty())
          {  
            backtrack = true;
            break;
          }
 
          int assignment = current[row][column].selectRandomFromDomain();
          current[row][column].setAssignment(assignment);
          current[row][column].removeFromDomain(assignment);
           
          newAssignment = copyGrid(current); 
         
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
       }
       
           if(!checkDomains(newAssignment))
              checker = true; 
           
           if(checker)
              continue;
           else
           {   
             newAssignment[row][column].clearDomain();
             s.push(newAssignment);
             break;
           }
           
        }while(true);
       
         if(backtrack)
         {
             consistencyChecks--;
             s.pop(); 
             if(s.empty())
                s.push(grid);
           
         } 
     }
     System.out.println("Consistency checks: " + consistencyChecks);
     return current; 
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
                  System.out.print("grid[" + i + "][" + j + "]-");
               }
           }
       }
       System.out.println();
       return counter;
   }
   
  public static Grid[][] forwardCheckingMRV(Grid[][] grid) //kind of works most of the time; gets stuck sometimes NOT ANYMORE
  {      
     Random random = new Random();
     int row = 0, column = 0, consistencyChecks = 0;
     Grid[][] current = copyGrid(grid), newAssignment;
     Stack<Grid[][]> s = new Stack();
     s.push(grid);
     boolean backtrack = false, checker = false;
     int counter = 0;
   
     
     while(!complete(current)) 
     {
        current = copyGrid(s.peek());
        backtrack = false;
        checker = false;
         counter++;
         int min = Integer.MAX_VALUE;
         for(int i=0; i<3; i++) //removes the sum and all bigger numbers from the domains of its column 
         {
           for(int j=0; j<10; j++)
           { 
               if(current[i][j].getAssignment() == -1 && current[i][j].domainSize() < min)
               {  
                   min = current[i][j].domainSize();
                   row = i;
                   column = j;
               }
           }
         } 
        

      if(counter > 30)
      {
        do 
        {
           row = random.nextInt(3);
           column = random.nextInt(10);
        }while(current[row][column].getAssignment() != -1 && !complete(current));
      }
                
        do
        {
          //checker = false;
          current[row][column].setAssignment(-1);
          consistencyChecks++;
          if(current[row][column].domainIsEmpty())
          {  
            backtrack = true;
            break;
          }
 
          int assignment = current[row][column].selectRandomFromDomain();
          current[row][column].setAssignment(assignment);
          current[row][column].removeFromDomain(assignment);
          newAssignment = copyGrid(current); 
         
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
       } 
       
           if(!checkDomains(newAssignment))
              checker = true; 
           
           if(checker)
              continue;
           else
           {   
             newAssignment[row][column].clearDomain();
             s.push(newAssignment);
             counter--;
             break;
           }
           
        }while(true);
       
         if(backtrack)
         {
             consistencyChecks--;
             s.pop(); 
             if(s.empty())
                s.push(grid);
             counter++;
           
         } 
     }
     System.out.println("Consistency checks: " + consistencyChecks);
     return current; 
  }      

  //Backtracking
    public static Grid[][] generateTennerforBacktracking() {
    Grid[][] tenner = new Grid[4][10]; // 10 by 4
    Random random = new Random();
    for (int i = 0; i < 4; i++)
        for (int j = 0; j < 10; j++)
            tenner[i][j] = new Grid();
     
    if (generateTennerBacktrack(tenner, 0, 0)) {
        for (int i = 0; i < 10; i++) {
            tenner[3][i].setAssignment(tenner[0][i].getAssignment() + tenner[1][i].getAssignment() + tenner[2][i].getAssignment());
            tenner[3][i].clearDomainSum();
        }
    } else {
        System.out.println("No solution found.");
    }
    
    for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
             boolean result = random.nextDouble() < 0.45; //generates probability to make a cell empty
             if(result) //fill grid index
                tenner[i][j].setAssignment(-1);
          }
        }
        
    return tenner;
}

private static boolean generateTennerBacktrack(Grid[][] tenner, int row, int col) {
    if (row == 4) {
        return true; // Grid filled successfully
    }

    int nextRow = row + (col + 1) / 10; // Move to the next row if column exceeds 9
    int nextCol = (col + 1) % 10; // Move to the next column

    if (tenner[row][col].getAssignment() != -1) {
        // If the cell is already assigned, move to the next cell
        if (generateTennerBacktrack(tenner, nextRow, nextCol)) {
            return true;
        }
    } else {
        // Try assigning numbers from 0 to 9
        for (int num = 0; num < 10; num++) {
            if (isValid(tenner, row, col, num)) {
                tenner[row][col].setAssignment(num);
                if (generateTennerBacktrack(tenner, nextRow, nextCol)) {
                    return true;
                }
                tenner[row][col].setAssignment(-1); // Backtrack
            }
        }
    }

    return false; // Unable to find a valid assignment for this cell
}

private static boolean isValid(Grid[][] tenner, int row, int col, int num) {
    // Check if num is already in the same row
    for (int j = 0; j < 10; j++) {
        if (tenner[row][j].getAssignment() == num) {
            return false;
        }
    }

    // Check if num is already in the same column
    for (int i = 0; i < 4; i++) {
        if (tenner[i][col].getAssignment() == num) {
            return false;
        }
    }

    // Check diagonal constraints
    if (row == 1) {
        if (col == 0) {
            if (num == tenner[0][0].getAssignment() || num == tenner[0][1].getAssignment())
                return false;
        } else if (col == 9) {
            if (num == tenner[0][8].getAssignment() || num == tenner[0][9].getAssignment())
                return false;
        } else {
            if (num == tenner[0][col - 1].getAssignment() || num == tenner[0][col].getAssignment() || num == tenner[0][col + 1].getAssignment())
                return false;
        }
    } else if (row == 2) {
        if (col == 0) {
            if (num == tenner[1][0].getAssignment() || num == tenner[1][1].getAssignment())
                return false;
        } else if (col == 9) {
            if (num == tenner[1][8].getAssignment() || num == tenner[1][9].getAssignment())
                return false;
        } else {
            if (num == tenner[1][col - 1].getAssignment() || num == tenner[1][col].getAssignment() || num == tenner[1][col + 1].getAssignment())
                return false;
        }
    }

    return true; // Valid assignment
}

public static Grid[][] solveTennerBacktracking(Grid[][] grid) {
    int consistencyChecks = 0;
    Grid[][] current = copyGrid(grid);
    Stack<Grid[][]> stack = new Stack<>();
    stack.push(grid);

    while (!complete(current)) {
        current = copyGrid(stack.peek());
        boolean backtrack = false;

        // Find the next unassigned cell
        int[] nextCell = findUnassignedCell(current);
        int row = nextCell[0];
        int column = nextCell[1];

        // If no unassigned cell found, puzzle is complete
        if (row == -1 && column == -1) {
            break;
        }

        // Try assigning numbers from 0 to 9
        boolean assigned = false;
        for (int num = 0; num < 10; num++) {
            if (isValid(current, row, column, num)) {
                current[row][column].setAssignment(num);
                stack.push(copyGrid(current));
                backtrack = false;
                assigned = true;
                break;
            }
        }
        
        if (!assigned) {
            consistencyChecks++;
            backtrack = true;
        }

        if (backtrack) {
            stack.pop();
            if (stack.empty())
                stack.push(grid);
        }
    }

    System.out.println("Consistency checks: " + consistencyChecks);
    return current;
}

private static int[] findUnassignedCell(Grid[][] grid) {
    int[] cell = {-1, -1};
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 10; j++) {
            if (grid[i][j].getAssignment() == -1) {
                cell[0] = i;
                cell[1] = j;
                return cell;
            }
        }
    }
    return cell;
}
    
}
 
 

      
  
         





