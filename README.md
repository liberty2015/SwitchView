# SwitchView

<img src="https://github.com/liberty2015/SwitchView/blob/master/app.gif" width="227px" height="400px" align="center">

使用方法：该View可以分为两种状态：on和off
在布局文件中：<br>
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#f5f5f5">
    <com.example.hu.myapplication.widget.SwitchView
        android:id="@+id/switchView"
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:onColor="@color/btn_female"//on状态下的View的颜色
        app:offColor="@color/btn_male"//off状态下的View的颜色
        app:onImg="@mipmap/female"//on状态下的圆形中间的图形
        app:offImg="@mipmap/male"//off状态下的圆形中间的图形
        app:onTxt="女性"//on状态下的显示的字符
        app:offTxt="男性"//off状态下的显示的字符
        />
</LinearLayout>
```


在Java代码中：

```
//第一个参数改变状态，第二个参数用于判断是否播放移动动画
switchView.setCurrentState(SwitchView.OFF,true);
//返回SwitchView当前的状态，ON或OFF
switchView.getCurrentState();
```
