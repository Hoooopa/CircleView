# CircleView
> 初步的初步上手自定义view，根据项目的需要，自己做了一个简单的按钮。

> 在view的绘制和事件响应上没啥难度，就是时间的实时使用的是Handler，所以在使用完view之后调用view的stop()的方法。在stop()方法里对handler使用了removeCallback方法(注：《Android开发艺术探索》P201页，刚哥表示，View内部本身提供post系列方法，所以不需要Handler。所以这里mark下后面得试着改一改)

> 可以设置的方法在attr.xml文件里

> 第二张图表示press的状态

![](https://github.com/Hoooopa/CircleView/blob/master/app/pic/S80711-173815.jpg?raw=true)
![](https://github.com/Hoooopa/CircleView/blob/master/app/pic/S80711-173937.jpg?raw=true)