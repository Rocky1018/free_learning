package sword_offer;

import java.util.ArrayList;
import java.util.Collections;

//输入一个链表，输出该链表中倒数第k个结点。
public class JZ14 {
    public ListNode FindKthToTail(ListNode head, int k) {
        if (head == null || k == 0) {
            return null;
        }
        ArrayList<ListNode> list = new ArrayList<>();
        list.add(head);
        while (head.next != null) {
            list.add(head.next);
            head = head.next;
        }
        Collections.reverse(list);
        if (k > list.size())
            return null;
        return list.get(k - 1);
    }
}
