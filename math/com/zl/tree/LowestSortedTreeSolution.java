package zl.study.math;
/**
 * 将有序数组转换成最小高度的二叉排序树
 * 二叉排序树:就是根节点比左节点的值大比右节点的值小 
 * @author zhang
 * @创建时间2017年8月6日
 *
 */
public class LowestSortedTreeSolution {
    
	public static void main(String[] args) {
		int[] a = new int[] {1,2,3,45,66};
		LowestSortedTreeSolution s = new LowestSortedTreeSolution();
		TreeNode root = s.sortedArrayToBST(a);
	}
	
	/**
	 * 
     * @param A: an integer array
     * @return: a tree node
     */
    public TreeNode sortedArrayToBST(int[] A) {  
        // write your code here
        int len = A.length;
        TreeNode root = null;  
        root = recursion(A, 1, len, root);
        A = null;
        return root;  
    }
    
    public TreeNode recursion(int[] array, int lhs, int rhs, TreeNode root)  
    {  
        if(lhs <= rhs)  
        {  
            int mid = (lhs + rhs + 1) >> 1;  
            root = new TreeNode(array[mid-1]);  
            root.left = recursion(array, lhs, mid - 1, root.left);  
            root.right = recursion(array, mid + 1, rhs, root.right);  
        }  
        return root;  
    }
}