# KShareViewActivityManager
一个兼容Android 5.0 以下Shared Element Transition （页面间共享元素位移动画）库
效果图：
![将就看吧](http://i4.tietuku.com/71be280654257885.gif)

图片地址：http://i4.tietuku.com/71be280654257885.gif

先说缺点吧：在第一个Activity 中的目标View 是wrap_content 时，位移会偏移一点点

使用方法：

1.在第一个Activity中：

```
 img = (ImageView) findViewById(R.id.img);
 title = (TextView) findViewById(R.id.title);
 KShareViewActivityManager.getInstance().startActivity(MainActivity.this, SecondActivity.class,
                                                                      R.layout.activity_second, img, title);
                                                                      
```
上面的语句中，第三个参数 R.layout.activity_second 传入的是第二个Activity 的布局layout id，之后的参数是指需要共享元素动画的View

如何让Manager 知道是哪两个View 在页面之间对应呢？tag 一样即可，如：activity_main 中：

<ImageView
        android:id="@+id/img"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_marginLeft="25dp"
        android:layout_marginTop="25dp"
        android:background="@drawable/splash"
        android:tag="img" />
        
  
在activity_second 中：

<ImageView
        android:id="@+id/img_two"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_centerInParent="true"
        android:background="@drawable/splash"
        android:tag="img" />
        
tag一样即可

2.请在Manifest.xml 中的第二个Activity 处（跳转目标Activity），设置它的
  android:theme="@style/SecondActivityTheme"

好啦
                                                                      
