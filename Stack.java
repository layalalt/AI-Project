public class Stack<T> //used for BST traversal
{
  private Node<T> top;
  
  public Stack() 
  { 
    top = null;
  }
  
  public boolean empty()
  { 
    return (top == null);
  }
  
  public void push(T e)
  {
    Node tmp = new Node(e); 
    tmp.setNext(top);
    top = tmp;
  }
  
  public T pop()
  {
    T e = top.getData();
    top = top.getNext();
    return e; 
  }

  public T peek() 
  {
     T tmp = pop();
     push(tmp);
     return tmp; 
  }
}