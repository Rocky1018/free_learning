###### ==和equals的区别：
==的作用是判断两个对象的地址是否相等，如果是基本数据类型的话就是比较两个值。equals默认的方法等价于==。String的equals方法被重写过了，
比较的是值。创建String类行的对象的时候，虚拟机会在常量池中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。
如果没有就在常量池中重新创建一个 String 对象。

    public class test1 {
        public static void main(String[] args) {
            String a = new String("ab"); // a 为一个引用
            String b = new String("ab"); // b为另一个引用,对象的内容一样
            String aa = "ab"; // 放在常量池中
            String bb = "ab"; // 从常量池中查找
            if (aa == bb) // true
                System.out.println("aa==bb");
            if (a == b) // false，非同一对象
                System.out.println("a==b");
            if (a.equals(b)) // true
                System.out.println("aEQb");
            if (42 == 42.0) { // true
                System.out.println("true");
            }
        }
    }
###### hashCode()与 equals()
hashCode()的默认行为是对堆上的对象产生独特值。hashCode() 所使用的杂凑算法也许刚好会让多个对象传回相同的杂凑值，如果 HashSet在对比
的时候，同样的 hashcode 有多个对象，它会使用 equals() 来判断是否真的相同。也就是说 hashcode 只是用来缩小查找成本。另外hashcode
只有在散列表中才有用。
###### try-catch注意事项
如果try和finally里面都有return，那么程序最后会返回finally里面的return语句。面对必须要关闭的资源，我们总是应该优先使用 try-with-
resources 而不是try-finally
###### 线程的基本状态
new runnable waiting time-waiting(可以自动返回) terminated blocked(阻塞状态，被锁了)
###### 既然有了字节流,为什么还要有字符流
不管是文件读写还是网络发送接收，信息的最小存储单元都是字节,但是将字节流转换成字符流的过程非常耗时。
