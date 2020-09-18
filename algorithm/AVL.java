import sun.util.locale.provider.AvailableLanguageTags;

public class AVL {
    int data, height;//结点数据和树的高度
    AVL left, right, root;//左子树右子树根节点 当前节点父节点

    //查找最小值 不断找左子树就行
    AVL findMin(AVL node) {
        AVL iteration = node;
        while (iteration.left != null) {
            iteration = iteration.left;
        }
        return iteration;
    }

    //查找最大值就是找右子树
    AVL findMax(AVL node) {
        AVL iteration = node;
        while (iteration.right != null) {
            iteration = iteration.right;
        }
        return iteration;
    }

    void insert(AVL node) {
        if (root == null) {
            root = node;
            return;
        }
        AVL iterator = root;
        //目标节点小于当前节点 则插入当前节点的左子树
        if (node.data < iterator.data) {
            insert(iterator.left);
            //左右子树高度 计算平衡因子
            int leftHeight = node.left.height;
            int rightHeight = node.right.height;
            //插入左子树导致失衡
            if (leftHeight - rightHeight > 1) {
                //节点比左子树小 就是在左子树的左子树 LL操作
                if (node.data < iterator.left.data) {
                    //操作iterator旋转LL
                } else {
                    //操作iterator旋转lR
                }
            }
        } else {//目标节点大于当前节点 则插入当前节点的右子树
            insert(iterator.right);
            //左右子树高度 计算平衡因子
            int leftHeight = node.left.height;
            int rightHeight = node.right.height;
            //插入右子树导致失衡
            if (rightHeight - leftHeight > 1) {
                if (node.data < iterator.right.data) {
                    //操作iterator旋转RL
                } else {
                    //操作iterator旋转RR
                }
            }
        }
    }

    //感觉写不对 日后补充
    void delete(AVL node) {
        AVL iterator = root;
        //比当前节点小就去左子树找
        if (node.data < iterator.data) {
            delete(iterator.left);
            int leftHeight = node.left.height;
            int rightHeight = node.right.height;
            //右子树比较高 打破平衡
            if (rightHeight - leftHeight > 1) {
                if (node.data > iterator.right.data) {
                    //操作iterator旋转RR
                } else {
                    //操作iterator旋转RL
                }
            }
        } else if (node.data > iterator.data) {
            delete(iterator.right);
            int leftHeight = node.left.height;
            int rightHeight = node.right.height;
            //左子树比较高 打破平衡
            if (leftHeight - rightHeight > 1) {
                if (node.data > iterator.left.data) {
                    //操作iterator旋转LL
                } else {
                    //操作iterator旋转LR
                }
            }
        } else {
            //左右子树都不为空时用右子树最小节点代替被删除节点
            if (node.left != null && node.right != null) {
                delete(findMin(node));
            } else if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            }
        }
    }

    /**
     * 1.	左子树变成新的根节点
     * 2.	旧的根节点变成右子树
     * 3.	左子树的右子树变成原本根节点的左子树
     * @param node
     */
    void rotationLL(AVL node) {
        node.data = node.left.data;
        node.left.data = node.left.right.data;
        node.right.data = node.data;
    }

    void rotationLR(AVL node) {

    }

    void rotationRR(AVL node) {

    }

    void rotationRL(AVL node) {

    }

    public static void main(String[] args) {

    }
}
