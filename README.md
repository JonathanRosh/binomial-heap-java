# Binomial Heap Implementation

## Overview
This project is an implementation of a **Binomial Heap** in Java, a priority queue supporting efficient merge operations. The heap consists of a collection of **binomial trees**, each following the **min-heap property**. The implementation provides various standard operations such as **insert, deleteMin, meld, decreaseKey, and delete**.

## Features
- **Efficient merging (meld)** of two binomial heaps in **O(log n + log k)** time.
- **Insert** a key into the heap in **O(1)** amortized time.
- **Delete the minimum** key in **O(log n)** worst-case time.
- **Decrease the key** of an item in **O(log n)** time.
- **Delete an arbitrary item** from the heap in **O(log n)** time.
- **Find the minimum item** in **O(1)** time.

## Data Structures
The implementation consists of two main classes:
- **HeapNode**: Represents a node in the binomial heap. Each node has a reference to its parent, child, and next sibling.
- **HeapItem**: Represents an item stored in the heap, linking back to its corresponding node.

## Class Methods

### Public Methods
- `insert(int key, String info)`: Inserts an element with a given key and info, returning the created `HeapItem`.
- `deleteMin()`: Removes the smallest key from the heap.
- `findMin()`: Returns the minimum key without removing it.
- `decreaseKey(HeapItem item, int diff)`: Decreases the key of an item and maintains heap properties.
- `delete(HeapItem item)`: Deletes an arbitrary item by first decreasing its key to **negative infinity** and then removing it.
- `meld(BinomialHeap heap2)`: Merges the current heap with another binomial heap in **O(log n + log k)** time.
- `size()`: Returns the number of elements in the heap.
- `empty()`: Checks if the heap is empty.
- `numTrees()`: Returns the number of binomial trees in the heap.

### Private Helper Methods
- `searchMin()`: Searches for the minimum element in the heap.
- `swapItem(HeapNode child, HeapNode parent)`: Swaps two heap items to maintain heap order.
- `joinTrees(HeapNode minNode, HeapNode maxNode)`: Joins two trees of the same rank by making the smaller root the parent.
- `mergeRootsChains(HeapNode node1, HeapNode node2, int numTrees1, int numTrees2)`: Merges the root lists of two heaps.
- `orderTriplet(HeapNode current, HeapNode next, HeapNode third)`: Orders three nodes by key value.
- `findMaxRank(HeapNode start, int numTrees)`: Finds the highest-ranked node in the heap.

## Complexity Analysis
| Operation     | Worst-Case Complexity |
|--------------|----------------------|
| Insert       | O(1) amortized       |
| DeleteMin    | O(log n)             |
| FindMin      | O(1)                 |
| Meld         | O(log n + log k)     |
| DecreaseKey  | O(log n)             |
| Delete       | O(log n)             |

## Usage
This implementation can be used in applications requiring **priority queues**, such as **Dijkstra's shortest path algorithm**, **task scheduling**, and **graph algorithms** that rely on efficient key updates.

## Author
- Yonatan Rosh ([yonatanrosh]) - ID: 209135763

## License
This project is available for educational and research purposes.

