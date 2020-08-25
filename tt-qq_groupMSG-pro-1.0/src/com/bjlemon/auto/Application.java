package com.bjlemon.auto;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.google.gson.*;
import com.melloware.jintellitype.*;


/**
 * QQ群发工具 具备窗体界面的功能
 * 需要监听电脑的F11 F12这两个热键
 */
public class Application extends JFrame {

    static Random random = new Random(12321);
    private Robot robot;//机器人类，java提供自动化测试的核心api
    private JTextArea txtArea;//群发消息的内容存放文本框
    private Map<String, String> propsMap = new HashMap<>();
    private List<Image> imageList = new ArrayList<>(); //图片数组
    private List<String> textList = new ArrayList<>();  //文案数组
    /*
     *
     *
     *
     * */
    private boolean isPause = true;
    //将热键的值转化成自己定义的常量
    private static final int GLOBAL_HOTKEY_F10 = 0;
    private static final int GLOBAL_HOTKEY_F11 = 1;

    /**
     * 构造方法 Applicaiton类被实例化的时候要用来显示
     */
    public Application() {
        //设置窗体的标题
        this.setTitle("涛涛QQ群发工具");
        //设置窗体的大小
        this.setSize(300, 300);
        //创建面板
        JPanel jpanel = new JPanel();
        //用Label展示提示信息
        JLabel jLabel = new JLabel("F10开始群发 F11结束群发");
        jpanel.add(jLabel);//将提示信息加入到面板
        txtArea = new JTextArea(10, 20);//用来输入要群发的消息内容
        jpanel.add(txtArea);//输入框文本域加入到面板

        init();

        this.add(jpanel);//将面板加入到窗体
        try {
            robot = new Robot();//初始化机器人类
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //第三方插件 注册热键 F10的常量值是是121 F11的常量值是122
        JIntellitype.getInstance().registerHotKey(GLOBAL_HOTKEY_F10, 0, 121);
        JIntellitype.getInstance().registerHotKey(GLOBAL_HOTKEY_F11, 0, 122);
        //监听热键 匿名内部类
        JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {

            @Override
            public void onHotKey(int arg0) {//arg0 就是转换后的值

                switch (arg0) {
                    case GLOBAL_HOTKEY_F10:

                        isPause = false;
                        //群发的代码
                        //会用到多线程
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                send();
                            }
                        }).start();//启动线程
                        break;
                    case GLOBAL_HOTKEY_F11:
                        //停止群发
                        isPause = true;
                        break;
                }

            }
        });

        //设置默认屏幕居中
        this.setLocationRelativeTo(null);
        //设置窗体关闭的时候 退出进程
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置可见
        this.setVisible(true);
    }

    public void init() {
        loadPros();
        initImagesAndTexts();
    }

    /**
     * 群发消息
     */
    public synchronized void send() {
        /**
         * 1.复制txtArea里面的文本内容到系统的粘贴板
         * 2.获取鼠标的当前位置 属于初始位置
         * 3.鼠标单击一次选中要被发送消息的好友
         * 4.按下回车 打开聊天界面 ，聊天界面的焦点会自动在文本框
         * 5.执行粘贴操作 ctrl+v
         * 6.按下回车进行消息的发送
         * 7.按下esc键推出当前的聊天窗口
         * 8.将鼠标移动到初始位置 单击
         * 9.按下方向键的下 将选择的好友移动至下一位
         *
         * 4-9循环操作
         *
         */
        //设置文本到剪贴板


        //用到Java MouseInfo 可以获取到鼠标在屏幕上的坐标
        Point p = MouseInfo.getPointerInfo().getLocation();
        //鼠标单击 两个步骤  按下 和 放开
        mouseClick(robot, InputEvent.BUTTON1_MASK);//代表单击左键
        while (!isPause) {
            //执行回车操作
//			keyInput(robot,KeyEvent.VK_ENTER);
            robot.delay(getfasongyanchi());//延迟 ，主要考虑 qq打开聊天框的时间 等待聊天框准备好

            setImageAndTextToQQ(getctrlandtabyanchi());


            //按下回车 发送消息
            keyInput(robot, KeyEvent.VK_ENTER);
            robot.delay(getctrlandtabyanchi());
            //切换群的次数 随机

            int change = random.nextInt(getsuijiqiehuan()) + 1;
            System.out.println("切换群的次数：" + change);
            for (int i = 1; i < change; i++) {
                ctrlAndTab();
            }
        }
        //按下ESC键推出聊天窗口
        keyInput(robot, KeyEvent.VK_ESCAPE);
        //回到初始位置单击
        robot.mouseMove(p.x, p.y);
        mouseClick(robot, InputEvent.BUTTON1_MASK);
        //方向键往下按一下
        keyInput(robot, KeyEvent.VK_DOWN);
        robot.delay(getctrlandtabyanchi());
    }

    private void setImageAndTextToQQ(long yanchi) {

        String oneTestInRandom = getOneTestInRandom();

        Util.setSysClipboardText(oneTestInRandom+txtArea.getText());
        //执行Ctrl+V
        sleep(yanchi);
        ctrlAndV();
        sleep(yanchi);
        for (Image image : getImagesInRandom()) {
            Util.setClipboardImage(image);
            sleep(yanchi);
            ctrlAndV();
            sleep(yanchi);
        }
    }


    private void sleep(long yanchi) {
        try {
            Thread.sleep(yanchi);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void ctrlAndV() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
    }

    private void ctrlAndTab() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(getctrlandtabyanchi());
        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(getctrlandtabyanchi());
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(getctrlandtabyanchi());
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.delay(getctrlandtabyanchi());
    }


    /**
     * 鼠标的单击操作
     *
     * @param robot       用来实际模拟鼠标操作的类
     * @param mouseButton 代表的是鼠标的哪个键
     */
    public static void mouseClick(Robot robot, int mouseButton) {
        robot.mousePress(mouseButton);//按下
        robot.mouseRelease(mouseButton);//释放
    }

    /**
     * 随机取出文案一份
     *
     * @return
     * @author guoyitao
     * @date 2019/11/1 13:42
     * @params
     */
    public String getOneTestInRandom() {
        return textList.get(random.nextInt(textList.size()));
    }

    /**
     * 随机取出多份图片
     *
     * @author guoyitao
     * @date 2019/11/1 13:47
     * @params
     * @return
     */
    public List<Image> getImagesInRandom() {
        List<Image> randImgList = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            randImgList.add(getImageInRandom());
        }
       return randImgList;
    }




    /**
     * 随机取1份图片
     *
     * @author guoyitao
     * @date 2019/11/1 13:47
     * @params
     * @return
     */
    public Image getImageInRandom() {
        int index = random.nextInt(imageList.size());
        Image image = imageList.get(index);
        System.out.println("getImageInRandom： " +index);
        return image;

    }

    /**
     * 输入键盘操作
     *
     * @param robot
     * @param keyCode
     */
    public static void keyInput(Robot robot, int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }

    //ctrlandtabyanchi
    public Integer getfasongyanchi() {
        return random.nextInt(Integer.parseInt(propsMap.get("fasongyanchi")));
    }

    public Integer getctrlandtabyanchi() {
        return random.nextInt(Integer.parseInt(propsMap.get("ctrlandtabyanchi")));
    }

    public Integer getsuijiqiehuan() {
        return Integer.parseInt(propsMap.get("suijiqiehuan"));
    }


    public void initImagesAndTexts() {
        BufferedReader bufferedReaderFromClassPath = getBufferedReaderFromClassPath("com/bjlemon/auto/ImagesAndTexts.json");
        String result ="";

        JsonObject jsonOb =null;

        try {
            String line = "";

            while ((line = bufferedReaderFromClassPath.readLine()) != null) {
                result+=line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //读json
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            jsonOb = element.getAsJsonObject();


            try {
                bufferedReaderFromClassPath.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("读取ImagesAndText转成json：" + jsonOb.toString());

        JsonArray txt = jsonOb.getAsJsonArray("txt");
        JsonArray images = jsonOb.getAsJsonArray("images");
//
        for (JsonElement o : txt) {
            textList.add(o.getAsString());
        }
        for (JsonElement o : images) {
            Image image = Util.getImageFromFile(o.getAsString());
            imageList.add(image);
        }

    }


    private void loadPros() {
        BufferedReader br = getBufferedReaderFromClassPath("com/bjlemon/auto/pros.txt");
        try {

            String line = "";
            String[] arrs = null;
            while ((line = br.readLine()) != null) {
                arrs = line.split("=");
                propsMap.put(arrs[0], arrs[1]);
                System.out.println(arrs[0] + " : " + arrs[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    private BufferedReader getBufferedReaderFromClassPath(String path) {
        InputStream is1 = Application.class.getClassLoader().getResourceAsStream(path);
        return new BufferedReader(new InputStreamReader(is1));
    }

    /**
     * 开发应用程序  JFrame
     *
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Application application = new Application();
    }

}
