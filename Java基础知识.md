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
###### get与post的区别
本质上没区别，都是tcp链接。给GET加上request body，给POST带上url参数，技术上是完全行的通的。只是HTTP的规定和浏览器/服务器的限制，
导致他们在应用过程中体现出一些不同。 restful api的要求是get请求用来从服务器上获得资源，而post是用来向服务器提交数据。另外，对于GET
方式的请求，浏览器会把http header和data一并发送出去，服务器响应200（返回数据）;而对于POST，浏览器先发送header，服务器响应100 
continue，浏览器再发送data，服务器响应200 ok（返回数据）。在网络环境好的情况下，发一次包的时间和发两次包的时间差别基本可以无视。
而在网络环境差的情况下，两次包的TCP在验证数据包完整性上，有非常大的优点。并且，也不是所有浏览器post都发两次，firefox就只发一次。
##### 准备开始啃哈希表
hashset和hashtable都是基于hashmap实现的，先啃hashmap源码。LinkedHashMap增加了双向链表，啃完hashmap就开始啃这个。然后啃红黑树。
然后啃扩容机制（array list和hashmap）。
#### hashmap源码解读
哈希表的核心原理是基于和哈希值的桶和链表，缺陷是哈希碰撞。
##### hashmap的经典实现（jdk1.7）
###### hashmap初始容量是多少？为什么是这个数字？
初始容量是16，负载因子是0.75。容量是2的幂，如果不是2的幂会向上取幂。默认的哈希桶有16个，hash值有42E个，如何把42E个hash值放进16个
hash桶？首先想到的是取模。取模的问题有两点：第一是负数求模还是正数，第二是效率不如位运算。负载因子是一个在时间与空间开销上折衷的选择。
过高的负载因子可以减少空间使用，但是会增加查找消耗。
###### 为什么容量一定是2的幂？
确定hash桶的方式是长度-1然后与hash值进行按位与运算：(length-1)&hash。只有容量是2的n次方的时候，-1操作才能拿到全部都是1的值，这样
进行按位与操作的时候才能快速拿到分布均匀的数组下标。
PS：为了减少哈希碰撞，1.7的hash值做了很多的异或运算，在1.8被废弃。
###### 扩容原理
已使用超过容量*负载因子数量的时候就会产生很多哈希碰撞，开始扩容：对所有元素重新计算hash值，同时容量翻倍。另外即使容量减少也不会出现
缩容操作。
###### jdk1.7的问题
不是线程安全，容易碰到死锁。潜在的安全隐患，致命的哈希碰撞（哈希表变链表，复杂度变成o(n)）（能生成大量相同的hash值）
##### jdk1.8里的hashmap
数组+链表的组合变成了数组+红黑树的组合。注释上说哈希桶容量超过8之后会变成红黑树，在随机产生hashcode的条件下这个概率小于千万分之一，
因此这个很少被用到。hash值的计算变成了和高16位直接进行的异或运算。1.8不会产生死锁，具体分析待查（酷客上找找）
##### 关于红黑树
明天再啃
##### array list扩容机制
###### 极简总结
初始容量为10，扩容时每次增加当前容量一半。
###### 详细分析
1 初始化方法：以无参数构造方法创建 ArrayList 时，实际上初始化赋值的是一个空数组。当真正对数组进行添加元素操作时，才真正分配容量。
2 add方法：ArrayList添加元素的实质就相当于为数组赋值
3 判断是否需要扩容：minCapacity（最小扩容量） - elementData.length > 0
4 最小扩容量 添加元素时会比较minCapacity与DEFAULT_CAPACITY，返回较大值。
5 实际扩容操作 int newCapacity = oldCapacity + (oldCapacity >> 1);然后会比较minCapacity与newCapacity，取较大值作为实际扩容量。
如果minCapacity大于最大容量（MAX_ARRAY_SIZE），则新容量则为Integer.MAX_VALUE。
6 最后简单复制一下数组：elementData = Arrays.copyOf(elementData, newCapacity);