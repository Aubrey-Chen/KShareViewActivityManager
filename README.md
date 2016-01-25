# KShareViewActivityManager
一个兼容Android 5.0 以下Shared Element Transition （页面间共享元素位移动画）库

## 2016 1 25 更新 支持TextView 的子类颜色渐变了

效果图：
![将就看吧](http://i8.tietuku.com/aa5726b8302ae711.gif)

图片地址：http://i8.tietuku.com/aa5726b8302ae711.gif

这张是应用到自己的一个APP（还没写完） 的效果
![将就看吧](http://i4.tietuku.com/39966d2c684d7c06.gif)

图片地址：http://i4.tietuku.com/39966d2c684d7c06.gif


先说缺点吧：在第一个Activity 中的目标View 是wrap_content 时，位移会偏移一点点

使用方法：

##A->B

1.在第一个Activity中：


      img = (ImageView) findViewById(R.id.img);
      title = (TextView) findViewById(R.id.title);
      KShareViewActivityManager.getInstance(MainActivity.this).startActivity(MainActivity.this, SecondActivity.class,R.layout.activity_main,R.layout.activity_second, img, title);
                                                                      

第三个参数是要进行动画的View 所在的layout 的xml ，举例：如果要对ListView 的Item 进行动画，请传入item 布局的xml。

第四个参数 R.layout.activity_second 传入的是第二个Activity 的布局layout id，之后的参数是指需要共享元素动画的View

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


##B->A （回退）

请在ActivityB 中添加：

    @Override
    public void onBackPressed() {
        KShareViewActivityManager.getInstance(SecondActivity.this).finish(SecondActivity.this);
    }

好啦

##其他用法（添加监听器，设置动画时间），以下是第二张效果图的代码：

      KShareViewActivityManager.getInstance(this).withAction(new KShareViewActivityAction() {
                        @Override
                        public void onAnimatorStart() {
                        }
                        @Override
                        public void onAnimatorEnd() {
                        }
                        @Override
                        public void changeViewProperty(View view) {
                            view.setBackgroundColor(0x00ffffff);
                        }
                    }).setDuration(1000).startActivity(this, CountDownActivity.class, R.layout.list_item, R.layout.activity_count_down,
                                     (View) ((CommandEvent) event).obj);

                                                                      

差点忘了，还可以 withIntent 来自定义Intent 
