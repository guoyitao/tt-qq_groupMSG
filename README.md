# tt-qq_groupMSG
适合双十一叠楼群发的java消息自动群发软件

基于模拟按键的群发软件，鼠标激活聊天窗口后会自动切换聊天面板发送消息

#使用
java -jar autoMsg.jar 启动

###  配置文件说明 
延迟时间在代码里面是随机模拟人类操作

#### com/bjlemon/auto/pros.txt
fasongyanchi:发送延迟时间毫秒
ctrlandtabyanchi:聊天窗口切换延迟时间毫秒
suijiqiehuan:随机切换聊天窗口的随机因子（建议设置成群的数量）

#### com/bjlemon/auto/ImagesAndTexts.json

txt:因为出了某些bug来不及搞了，就先不要配置配置了会出现乱码
images：可以配置多张图片每次都会随机发送不同数量的不同图片，数量范围在    1-图片数量     图片路径必须配置成你自己电脑上的图片例如"images": [
    "C:\\Users\\g\\Desktop\\1.jpg","C:\\Users\\g\\Desktop\\2.png","C:\\Users\\g\\Desktop\\3.png"
  ]
这样

