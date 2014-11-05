package com.example.hello;

import io.appium.java_client.AppiumDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

@RunWith(BlockJUnit4ClassRunner.class)
public class Test1 extends TestCase {

    private AppiumDriver driver;

    @Before
    public void start() throws MalformedURLException {
        // 使用phonegap打包的hybrid app路径
        String apppath = "D:\\Work\\codes\\hands\\hands-hello-phonegap\\platforms\\android\\bin\\HelloWorld.apk";

        // 初始化AppniumDriver
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "device");// 真机测试android设备
        capabilities.setCapability("platformVersion", "4.2.2");// 真机android版本为4.2.2
        capabilities.setCapability("platformName", "Android");// 真机为android系统
        capabilities.setCapability("app", apppath);// 被测app apk包的位置
        capabilities.setCapability("appPackage", "com.example.hello");// 被测app的Activity类所在包
        capabilities.setCapability("appActivity", ".HelloWorld");// 被测app的Activity类
        capabilities.setCapability("automationName", "selendroid");// 因为真机为4.2版本，所以使用selendroid
        // http://127.0.0.1:4723/wd/hub地址就是AppiumServer的地址
        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"),
                capabilities);
    }

    @Test
    public void main() {
        try {
            Set<String> contextNames = driver.getContextHandles();
            for (String contextName : contextNames) {
                System.out.println(contextName); // 用于返回被测app是NATIVE_APP还是WEBVIEW，如果两者都有就是混合型App
            }

            Thread.sleep(5000);// 等它一会

            driver.context("WEBVIEW_0");// 让appium切换到webview模式以便查找web元素
            WebElement text_baidusearch = driver.findElement(By.id("index-kw"));
            text_baidusearch.click();// 点击百度的搜索输入框（web元素）

            /*
             * appium不支持中文输入 参考了robotium的以js方式为元素直接设置value的做法
             * 利用Selenium中Webdriver执行js方法实现中文输入
             */
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("document.getElementById('index-kw').value='输入法'");

            Thread.sleep(10000);// 等一会观察下效果
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void stop() {
        driver.quit();
    }
 
}