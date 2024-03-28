import java.util.*;

public class Main 
{

    public static void main(String[] args) 
    {
        int[][] grid = new int[4][10]; //10 by 4
        Random random = new Random();

        for(int i=0; i<10; i++) //fills up last row
        {
           int num = random.nextInt((20 - 7) + 1) + 7; //random number between 7 and 20 
           boolean exists = false;
           for(int j=i; j>=0 ;j--) //checks if it exists already; ensures uniqueness
           {
             if(num == grid[3][j])
                exists = true;
           }
           if(exists)
               i--;
           else
               grid[3][i] = num;
        }
        
        for(int i=0; i<3; i++) //initializer
            for(int j=0; j<10; j++) 
                grid[i][j] = -1;
        
        for(int i=0; i<3; i++) 
        {
          for(int j=0; j<10; j++)
          {
             boolean result = random.nextDouble() < 0.7; //generates probability
             if(result) //fill grid index
             {
                int num = random.nextInt(10);
                while(num > grid[3][j])
                    num = random.nextInt(10);
                boolean exists = false;
                for(int k=j; k>=0; k--) //checks if it exists already; ensures uniqueness
                {
                 if(num == grid[i][k])
                   exists = true;
                }
                if(exists)
                  j--;
                else
                  grid[i][j] = num;
             }
          }
        }
        
        
        for(int i=1; i<3; i++) //adjacent cells checker
        {
          for(int j=0; j<10; j++)
          {
            boolean checker = false;
            if(i == 1) //second row
            {
               if(j == 0)
               {
                 if((grid[1][0] == grid[0][0]) || (grid[1][0] == grid[0][1])) //first element
                    checker = true;
                 else
                     continue;
               }
               else if(j == 9)
               {  
                 if((grid[1][9] == grid[0][8]) || (grid[1][9] == grid[0][9])) //last element  
                    checker = true;
                 else
                     continue;
               }
               else 
               {
                 if((grid[1][j] == grid[0][j - 1]) || (grid[1][j] == grid[0][j]) || (grid[1][j] == grid[0][j + 1]))
                    checker = true; 
                 else
                     continue;
               } 
            }      
            else //(i == 2) 
            {
               if(j == 0)
               {
                 if((grid[2][0] == grid[1][0]) || (grid[2][0] == grid[1][1])) //first element
                    checker = true;
                 else
                     continue;
               }
               else if(j == 9)
               {  
                 if((grid[2][9] == grid[1][8]) || (grid[2][9] == grid[1][9])) //last element  
                    checker = true;
                 else
                     continue;
               }
               else 
               {
                 if((grid[2][j] == grid[1][j - 1]) || (grid[2][j] == grid[1][j]) || (grid[2][j] == grid[1][j + 1]))
                    checker = true; 
                 else
                     continue;
               } 
             }          
              if(checker)
                grid[i][j] = -1;    
          }
        } 
        
        for(int i=0; i<10; i++) //sum checker
        {
          if(grid[0][i] != -1 && grid[1][i] != -1 && grid[2][i] != -1)
          {
              if(grid[0][i] + grid[1][i] + grid[2][i] == grid[3][i])
                  continue;
              else
              {
                double change = random.nextDouble();
                if(change < 0.33333)
                    grid[0][i] = -1;
                else if(change < 0.66667)
                    grid[1][i] = -1;
                else
                    grid[2][i] = -1;
              }
          }
          else if(grid[0][i] != -1 && grid[1][i] != -1)
          {
             if(grid[0][i] + grid[1][i] < grid[3][i])
                continue;
             else
             {
                boolean change = random.nextDouble() < 0.5;
                if(change)
                    grid[0][i] = -1;
                else 
                    grid[1][i] = -1;
             }
          }
          else if(grid[0][i] != -1 && grid[2][i] != -1)
          {
             if(grid[0][i] + grid[2][i] < grid[3][i])
                continue;
             else
             {
                boolean change = random.nextDouble() < 0.5;
                if(change)
                    grid[0][i] = -1;
                else 
                    grid[2][i] = -1;
             }
          }
          else if(grid[1][i] != -1 && grid[2][i] != -1)
          {
             if(grid[1][i] + grid[2][i] < grid[3][i])
                continue;
             else
             {
                boolean change = random.nextDouble() < 0.5;
                if(change)
                    grid[1][i] = -1;
                else 
                    grid[2][i] = -1;
             }
          }    
        }       
       
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<9; j++) 
            {
               if(grid[i][j] == -1)
                 System.out.print(grid[i][j] + "| ");
               else
                 System.out.print(grid[i][j] + " | ");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------");
        for(int i=0; i<9; i++) 
        {
           if(grid[3][i] > 9)
              System.out.print(grid[3][i] + "| ");
           else
              System.out.print(grid[3][i] + " | ");
        }
        System.out.println();

    }

}
