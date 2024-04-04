public class Node<T> //Nodes that contain Contact as data
{

	private T data;
	private Node next;

	public Node(T s)
   {
	  data = s;
	  next = null;
	}
   
   public void setData(T data)
   {
     this.data = data;
   }
   
   public T getData()
   {
     return data;
   }

	public void setNext(Node next)
   {
	  this.next = next;
	}
   
	public Node getNext()
   {
	  return next;
	}

}
