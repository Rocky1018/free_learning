###### activity生命周期
onCreate;onStart 可见状态 还没有显示 不能交互;onResume 可见 可交互;onPause;onStop;onDestroy;
onRestart 正常启动不会被调用;
###### activity异常情况生命周期补充
onSaveInstanceState;onRestoreInstanceState bundle在这个方法里不为空，在onStart里面可能为空；bundle是散列形式
###### activity状态
active：处于栈顶 可见 可交互;paused：可见 不可交互;stop:不可见 ;killed:系统回收
###### activity启动模式
standard；singleTop（栈顶复用）；singleTask（栈内复用 会销毁目标上面所有activity）；
singleInstance（全局唯一性，复用性，独占任务栈）；
##### Android内部通信
eventBus或者广播之类的是比较通用的方式
###### activity之间通信
intent/bundle；类静态变量；全局变量；
###### activity与fragment通信
bundle getArgument可以获取bundle；直接在activity定义方法；
###### fragment与activity通信
接口回调 1.在fragment中定义内部回调方法 2.attach方法中检查 3.detach方法释放activity
###### activity与service通信
1 绑定服务 利用service_connection接口（自己实现binder内部类）；2简单通信 intent传值；3定义callback接口(自己实现binder内部类)
###### service与thread
thread就是正常的线程生命周期，缺陷在于**无法控制**。service运行在主线程。
###### service生命周期
startService：onCreate onStartCommand（执行多次） onDestroy；
bindService：onCreate onBind onUnbind onDestroy；如果已经绑定service，在解绑之前无法销毁。
###### service与intentService
intentService自动停止，内部有子线程处理耗时操作。源码层面上，intentService继承了service，在onCreate内创建handlerThread，并且
绑定looper。
###### 启动服务和绑定服务
启动优先级比绑定高，无论哪种状态都可以直接开启服务。启动服务后即使相关activity被销毁，service也不会销毁。
###### 序列化
把内存中的对象写入磁盘。parcelable接口与serializable接口。
###### AIDL
创建AIDL；服务端新建service，创建binder对象；客户端：实现service_connection,bindService 
###### handler
handler获取当前线程looper，looper取出messageQueue，完成三者绑定。messageQueue内部是链表实现。threadLocal获取当前线程looper。
创建looper的时候（构造函数）创建了messageQueue。handler获取looper是用的myLooper方法，点进去就是threadLocal的get方法。在looper
的loop方法里面有一个死循环，消息不为空的时候回调dispatchMessage方法，最后handler调用handleMessage方法。looper是在主线程的，因此
在子线程中不能直接new handler。
###### asyncTask
本质是handler和线程池的封装，实例只能在主线程中创建。最大线程数=cpu核心数*2+1。
###### butterKnife原理
1 扫描所有Java代码中的butterKnife注解 2 ButterKnifeProcessor会根据className生成viewBinder 3 调用bind方法加载生成的viewBinder类
###### https四次握手
1 客户端请求建立SSL链接，并向服务端发送一个随机数–Client random和客户端支持的加密方法，比如RSA公钥加密，此时是明文传输。<br>
2 服务端回复一种客户端支持的加密方法、一个随机数–Server random、授信的服务器证书和非对称加密的公钥。<br>
3 客户端收到服务端的回复后利用服务端的公钥，加上新的随机数–Premaster secret 通过服务端下发的公钥及加密方法进行加密，发送给服务器。<br>
4 服务端收到客户端的回复，利用已知的加解密方式进行解密，同时利用Client random、Server random和Premaster secret通过一定的算法
生成HTTP链接数据传输的对称加密key – session key。<br>
个人总结和tcp三次握手基本一样，发送的随机数作用就和syn和ack的作用差不多，区别就是每次携带了支持的加密类型或者公钥。最后多出来的一次
握手其实就是服务端做的解密工作。
服务端创建aidl；
服务端实现aidl并向客户端开放接口（创建service ，声明ibinder，实现stub实例。stub实例中实现的方法就是后面供客户端调用的方法）；
客户端调用aidl（包目录保持一致，然后绑定服务即可，绑定的时候走stub的asinterface方法）；
jni与ndk开发
线程池：
主要就是减少线程开销，有固定线程池（核心线程数和最大线程数相等），带缓冲的线程池（核心线程数为0，每次有新的runnable进来就再创建线程），单线程线程池（保证线程顺序执行）等等。执行线程的时候execute没有返回值，submit会返回future。
耗时操作处理
用intentservice，或者线程池，或者handlerthread，或者asynctask。
sqlite支持多线程吗
并发读无限，不支持并发写。基于文件系统的锁机制。
关于锁的知识
mvvm模式：无论业务怎么变，只操作viewmodel