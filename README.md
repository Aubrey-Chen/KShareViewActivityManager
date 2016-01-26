# KShareViewActivityManager
一个兼容Android 5.0 以下Shared Element Transition （页面间共享元素位移动画）库

精简到了一个类

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

##注意事项

因为是直接操作的Content 显示区域，所以支持Activity ，Fragment 互相跳，只要是在屏幕中显示的View，都可以进行动画

由于进行动画的View 并不是原来的View，而是重新inflate 出来的，因此部分自定义属性并没有被赋值，Manager 对常见的TextView 和ImageView 的部分属性参照原View进行了复制，但如果是其他View的其他属性，还请在 changeViewProperty(View view) 这个回调函数中对其自定义属性进行设置。
如：

      KShareViewActivityManager.getInstance(this).withAction(new KShareViewActivityAction() {
                        @Override
                        public void onAnimatorStart() {
                        }
                        @Override
                        public void onAnimatorEnd() {
                        }
                        @Override
                        public void changeViewProperty(View view) {
                            if(view.getTag().equals("text")){
                                ((MyTextView)view).setOtherProperty(oringinTextView.getOtherProperty);
                            }
                            if(view.getTag().equals("xxx")){
                                ...
                            }
                        }
                    })；


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


# License
```
Copyright 2015 Kot32

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
