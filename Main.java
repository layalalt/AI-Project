import java.util.*;

public class Main 
{

    public static void main(String[] args) 
    {
       
        Grid[][] tenner = generateTenner();//10 by 4

        displayTenner(tenner);
        
        /**for(int i=0; i<3; i++) //for testing purposes
          for(int j=0; j<10; j++)
          { 
            tenner[i][j].displayDomain();
            System.out.println();
          }*/

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
             boolean result = random.nextDouble() < 0.55; //generates probability to make a cell empty
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

}


