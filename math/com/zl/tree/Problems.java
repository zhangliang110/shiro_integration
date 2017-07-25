package zl.study.base.study.linked;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author zhang
 * @创建时间2017年7月25日
 * 一些关于树的相关问题的答案
 */
public class Problems {
	private static Problems a = new Problems();
	public static void main(String[] args) {
		BinaryTreeNode root = new BinaryTreeNode(2);
		root.left = new BinaryTreeNode(3);
		root.right = new BinaryTreeNode(12);
		root.left.left = new BinaryTreeNode(3);
		root.left.left.left = new BinaryTreeNode(3);
		System.out.println(a.getNodeNumDepthLevel(root, 2));
	}
	
	//递归获取二叉树的节点总数
	public int getNodeNum(BinaryTreeNode root) {
		if (root == null) {
			return 0;
		}
		return getNodeNum(root.left) + getNodeNum(root.right) + 1;
	}
	
	//递归获取二叉树的层数
	public int getNodeDepth(BinaryTreeNode root) {
		if (root == null) {
			return 0;
		}
		
		int leftDepth = getNodeDepth(root.left);
		int rightDepth = getNodeDepth(root.right);
		return leftDepth > rightDepth ? (leftDepth + 1) : (rightDepth + 1);
	}
	
	//前序遍历二叉树
	public void preOrderTraverse(BinaryTreeNode root) {
		if (root == null) {
			return ;
		}
		preOrderTraverse(root.left);	//	前序遍历左子树
		System.out.println(root.value);	//访问根节点
		preOrderTraverse(root.right);	//	前序遍历右子树
	}
	
	//中序遍历二叉树
	public void inOrderTraverse(BinaryTreeNode root) {
		if (root == null) {
			return ;
		}
		System.out.println(root.value);	//首先访问根节点
		inOrderTraverse(root.left);		//然后中序遍历左子树
		inOrderTraverse(root.right);	//中序遍历右子树
	}
	
	//后序遍历二叉树
	public void postOrderTraverse(BinaryTreeNode root) {
		if (root == null) {
			return ;
		}
		postOrderTraverse(root.right);	//首先后序遍历右子树
		postOrderTraverse(root.left);	//然后后序遍历左子树
		System.out.println(root.value);	//然后访问根节点
	}
	
	//分层遍历二叉树 使用到了队列
	public void levelTraverse(BinaryTreeNode root) {
		if (root == null) {
			return ;
		}
		Queue<BinaryTreeNode> queue = new LinkedList<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			BinaryTreeNode currNode = queue.poll();
			System.out.println(currNode.value);
			if (currNode.left != null) {
				queue.add(currNode.left);
			}
			if (currNode.right != null) {
				queue.add(currNode.right);
			}
		}
		return;
	}
	
	
	//将二叉查找树变成有序的双向链表
	public void convertToLinkedList(BinaryTreeNode root, BinaryTreeNode firstNode, BinaryTreeNode lastNode) {
		BinaryTreeNode firstLeft = new BinaryTreeNode(), lastLeft = new BinaryTreeNode(), firstRight = new BinaryTreeNode(), lastRight= new BinaryTreeNode();
		if (root == null) {
			//如果二叉树中为空，则双向链表的第一个节点和最后一个节点为空
			firstNode = null;
			lastNode = null;
			return ;
		}
		if (root.left == null) {
			//如果左子树为空,对应双向链表的第一个节点就是树的根节点
			firstNode = root;
		} else {
			//先转换左子树
			convertToLinkedList(root.left, firstLeft, lastLeft);
			//二叉查找树对应双向链表的第一个节点就是左子树转换后双向有序链表的第一个节点
			firstNode = firstLeft;
			//将根节点和左子树转换后的双向有序链表的最后一个节点连接
			root.left = lastLeft;
			lastLeft.right = root;
		}
		
		if (root.right == null) {
			//对应双向有序链表的最后一个节点是根节点
			lastNode = root;
		} else {
			convertToLinkedList(root, firstRight, lastRight);
			//对应双向链表的最后一个节点就是右子树转换后双向有序链表的最后一个节点
			lastNode = lastRight;
			//将根节点和右子树转换后的双向有序链表的第一个节点连接
			root.right = firstRight;
			firstRight.left = root;
		}
		return ;
	}
	
	//求二叉树第k层的节点个数
	/**
	 * 递归解法：如果二叉树为空或者 k<1 返回 0
	 * 	如果二叉树不为空并且	k==1 则返回1
	 *  如果二叉树不为空且	k>1，返回左子树中k-1层的节点个数与右子树k-1层的节点个数之和
	 */
	int getNodeNumDepthLevel(BinaryTreeNode root, int k) {
		if (root == null || k < 1) {
			return 0;
		}
		if (k == 1) {
			return 1;
		}
		return getNodeNumDepthLevel(root.left, k - 1) + getNodeNumDepthLevel(root.right, k -1 );
	}
	
	//求二叉树中叶子节点的个数
	/**
	 *  递归解法 : 如果二叉树为空则返回0
	 *  如果二叉树不为空且左右子树为空 则返回1
	 *  如果二叉树不为空且左右子树不为空,则返回左子树叶子节点+右子树叶子节点
	 */
	int getLeafNodeNum(BinaryTreeNode root) {
		if (root == null) {
			return 0;
		}
		if (root.left == null && root.right == null) {
			return 1;
		}
		
		return getLeafNodeNum(root.left) + getLeafNodeNum(root.right);
	}
	
}
