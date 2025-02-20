

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over positive integers.
 * 
 * Student 1 Name: Antonella Campania
 * Student 1 Username: Antonella
 * Student 1 ID: 324823095
 * 
 * Student 2 Name: Yonatan Rosh	
 * Student 2 Username: yonatanrosh
 * Student 2 ID: 209135763
 * 
 *
 */
public class BinomialHeap{
	
	public int size;
	public HeapNode last;
	public HeapNode min;

	



	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) 
	{    
		// create rank 0 tree
		BinomialHeap.HeapItem newItem = new BinomialHeap.HeapItem();
		BinomialHeap.HeapNode newNode = new BinomialHeap.HeapNode();
		
		newItem.key = key;
		newItem.info = info;
		newItem.node = newNode;
		
		newNode.item = newItem;
		newNode.next = newNode;
		newNode.child = null;
		newNode.parent = null;
		newNode.rank = 0;
		
		//add it to main heap
		if(empty()) {
			// case main heap empty
			min = newNode;
			last = newNode;
			size=1;
			return newItem;
		}
		
		if(last.next.rank > 0) {
			//case no 0 rank tree in main heap
			newNode.next = last.next;
			last.next = newNode;
			if(min.item.key < newNode.item.key) {
				min = newNode;				
			}
			size++;
		}else {
			
			//create new heap of 0 rank tree and meld 
			BinomialHeap newHeap = new BinomialHeap();  
			newHeap.last = newNode;
			newHeap.min = newNode;
			newHeap.size =1;
			meld(newHeap);
		}	
		return newItem; 
	}

	/**
	 * 
	 * Delete the minimal item
	 * O(log(n)) WC
	 */
	public void deleteMin()
	{
		if(empty()) return;
		
		// create binomial heap from children of min root
		int minTreeSize = (int) Math.pow(2, min.rank); 
		BinomialHeap childHeap = new BinomialHeap();
		HeapNode child = min.child; 
		if(child != null) {
			
			// cut parent from the children
			child.parent = null;
			for(int i=0; i<min.rank-1; i++) {
				child = child.next;
				child.parent = null;

			}

			// update child heap
			childHeap.size = minTreeSize - 1; 
			childHeap.last = min.child;
			childHeap.min = childHeap.searchMin().node; 
			
      }
	
		
		if(numTrees() > 1) {
			// delete min root from roots chain and update heap
			HeapNode before = min;
			HeapNode after = min.next;
			for(int i = 0; i < numTrees()-1; i++) {
				before = before.next;
			}
			before.next = after;
			if(last == min) {
				last = before; 
			}
			size -= minTreeSize;
			min = searchMin().node;
		}else {
			//make empty heap so that after meld we get children heap
			min = null;
			last = null; 
			size = 0;
		}
		// meld
		meld(childHeap);
	}

	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 * O(1)
	 *
	 */
	public HeapItem findMin()	{
		return min.item;
		
	} 
	
	/**
	 * 
	 * search and returns the root's item of the minimal root in the heap
	 * O(log(n))   
	 */
	private HeapItem searchMin(){
		
		if (empty()) return null; 
		
		//find minimum of all roots
		HeapNode node = last.next;
		HeapNode minimal = last.next;
		for(int i =0; i<numTrees(); i++ ) {
			node = node.next;
			if(node.item.key < minimal.item.key) {
				minimal = node;
			}
		}
		return minimal.item; 
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * O(log(n)) WC
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		item.key -= diff;
		
		
		//"bubble" up the item:
		HeapNode node = item.node;
		while(node.parent != null && node.item.key < node.parent.item.key) {
			swapItem(node, node.parent);
			node = node.parent;
		}
		
		//update min if necessary 
		if(node.parent == null && node.item.key < min.item.key){
			min = node;
     
		}

	}
	
	/**
	 * 
	 * Swap between child's and parent's items
   * O(1) WC
	 * 
	 */
	private void swapItem(HeapNode child, HeapNode parent) {
		HeapItem temp = parent.item;
		parent.item = child.item;
		child.item = temp;
		parent.item.node = parent;
		child.item.node = child;
		
	}

	/**
	 * 
	 * Delete the item from the heap.
   * O(log(n)) WC
	 *
	 */
	public void delete(HeapItem item) 
	{    
		decreaseKey(item, item.key - Integer.MIN_VALUE);
		deleteMin();
	}
	

  /**
   * 
   * Meld main heap and heap2 into new heap
   * O(log(n)+log(+k)) WC for mainHeap.size=n and heap2.size=k
   * 
   */
	public void meld(BinomialHeap heap2) {
		//if any heap is empty
		if(heap2.empty()) return;
		
		if(empty()) {
			size = heap2.size;
			min = heap2.min;
			last = heap2.last;
			return;
		}
		
    //merge roots chains of both heaps into one by rank order
		int mainHeapNumTrees = numTrees();
		int heap2NumTrees = heap2.numTrees();
		HeapNode node = mergeRootsChains(last.next, heap2.last.next, mainHeapNumTrees, heap2NumTrees);
        
    //initiate conditions for iterating 
		int newSize = size + heap2.size;  
		int currentNumTrees = mainHeapNumTrees + heap2NumTrees;  
		int finalNumTrees =Integer.bitCount(newSize); 
		
    //initiate pointers to go through merged roots chain
		HeapNode current = node.next; 
	    HeapNode prev = node; 
	    HeapNode next = node.next.next; 
	    prev.next = current;
	    current.next = next;
	    
	    while(currentNumTrees != finalNumTrees) {  
	    	
	    	if (current.rank != next.rank) {
	            //case different rank --> move up the chain
	            prev = current;
	            current = next;
              next = next.next;
	    	} else {
          HeapNode third = next.next;
          if(third != prev && third !=current && third != next  && third.rank == next.rank) {
            //case 3 of same rank 
            //re-order by key and move up the chain
            prev = orderTriplet(current, next, third);
            current = prev.next;
            next = prev.next.next;
            
          }else { 
              //case only 2 of same rank
              //case current is root
              if(current.item.key <= next.item.key) {
            	  current.next = next.next; //chain up
            	  joinTrees(current, next);
                
                //move up
            	  next = current.next;
	    				
	    		}else {//case next is root
	    		joinTrees(next,current);
	    		prev.next = next; //chain down
	    				
	    		//move up 
	    		current = next;
	    		next = next.next;
	    			}
	    		//if we reached here, we have joined 2 trees into one: 
	    		currentNumTrees --;
	    		
            
	    		}
	    	}
	    	
	    }
	    //update main heap:
	    last = findMaxRank(current, finalNumTrees);
	    size = newSize;
	    min = searchMin().node;
    }
		
  /**
   * 
   * @return last node of merged roots chains
   * O(n+k) WC for mainHeap.size=n and heap2.size
   */
	
	private HeapNode mergeRootsChains(HeapNode node1, HeapNode node2, int numTrees1, int numTrees2) {

    //initiate pointers and conditions
		HeapNode temp = new HeapNode();
		HeapNode current = temp;
		int counter1 = 0;
		int counter2 = 0;
		
    //iterate until one root chain is done
		while(counter1 < numTrees1 && counter2 < numTrees2) {   
			if(node1.rank <= node2.rank) {
				current.next = node1;
				node1 = node1.next;
				counter1++;
			}else {
				current.next = node2;
				node2 = node2.next;
				counter2++;
			}
			current = current.next;
		}

    //add remains of undone chain
		while(counter1 < numTrees1 ) {
			current.next = node1;
			node1 = node1.next;
			counter1++;
			current = current.next;
		}
		while(counter2 < numTrees2) {
			current.next = node2;
			node2 = node2.next;
			counter2++;
			current = current.next;
		}
		
    
		current.next = temp.next; //chaind end to start
		return current;
	}

	
	/**
	 * 
	 * 
	 * join 2 roots of same rank (minNode as root and maxNode as child)
   * O(1) WC
   * 
	 */
	private void joinTrees(HeapNode minNode, HeapNode maxNode) {
		
		maxNode.parent = minNode;     //make min parent of max

		//insret max to min's child chain
		if(minNode.child != null) {
			//case min has children
			maxNode.next = minNode.child.next; //connect max to 0 rank child of min
			minNode.child.next = maxNode; //connect min's highest rank child to max
		}else{
			//case min has no children
			maxNode.next = maxNode;
		}
		
		minNode.child = maxNode; //make max min's new highest rank child
		minNode.rank++; //update min's rank
		
	}

  /**
   * 
   * @return first node of new chained triplet by key order
   * O(1) WC
   */
	private HeapNode orderTriplet(HeapNode current, HeapNode next, HeapNode third){

		HeapNode temp = third.next;

    
		if(current.item.key <= next.item.key && current.item.key <= third.item.key){
			if(next.item.key <=  third.item.key){
				//case current --> next --> third 
			}else{
				//case current --> third --> next
				current.next = third;
				third.next = next;
				next.next = temp;
			}
			return current;
		}else if(next.item.key <= current.item.key && next.item.key <= third.item.key){
			if(current.item.key <= third.item.key){
				//case next --> current --> third
				next.next =current;
				current.next = third;
			}else{
				//case next --> third --> current
				third.next = current;
				current.next = temp;
			}
			return next;
		}else if(third.item.key <= current.item.key && third.item.key <= next.item.key){
			if(current.item.key <= next.item.key){
				//case third --> current --> next
				third.next = current;
				next.next = temp;
			}else{
				//case third --> next --> current
				third.next =next;
				next.next= current;
				current.next = temp;
			}
			return third;
		}

		return null;
	}
	
  /**
   * 
   * @return max rank root of given chain
   * O(log(n)) WC
   */
	private HeapNode findMaxRank(HeapNode start, int numTrees) {
		HeapNode maxRank = start;
		start = start.next;
		for(int i= 0 ; i<numTrees -1; i++) {
			if(maxRank.rank < start.rank) {
				maxRank = start;
			}
			start = start.next;
		}
		return maxRank;
	}

	
	/**
	 * 
	 * Return the number of elements in the heap
   * O(1) WC
	 *   
	 */
	public int size()
	{
		return size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
   * O(1) WC
	 *   
	 */
	public boolean empty()
	{
		return last == null; 
	}

	/**
	 * 
	 * Return the number of trees in the heap.
   * O(1) WC
	 * 
	 */
	public int numTrees()
	{
		return Integer.bitCount(size);
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public static class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
	}
}
