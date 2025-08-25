package jp.co.sss.lms.ct.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

/**
 * Webドライバーユーティリティ
 * @author holy
 */
public class WebDriverUtils {

	/** Webドライバ */
	public static WebDriver webDriver;

	/**
	 * インスタンス取得
	 * @return Webドライバ
	 */
	public static void createDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
		webDriver = new ChromeDriver();
	}

	/**
	 * インスタンス終了
	 */
	public static void closeDriver() {
		webDriver.quit();
	}

	/**
	 * 画面遷移
	 * @param url
	 */
	public static void goTo(String url) {
		webDriver.get(url);
		pageLoadTimeout(5);
	}

	/**
	 * ページロードタイムアウト設定
	 * @param second
	 */
	public static void pageLoadTimeout(int second) {
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(second));
	}

	/**
	 * 要素の可視性タイムアウト設定
	 * @param locater
	 * @param second
	 */
	public static void visibilityTimeout(By locater, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locater));
	}

	/**
	 * 指定ピクセル分だけスクロール
	 * @param pixel
	 */
	public static void scrollBy(int pixel) {
		//((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
		((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
	}

	/**
	 * 指定位置までスクロール
	 * @param pixel
	 */
	public static void scrollTo(int pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + pixel + ");");
	}

	/**
	 * エビデンス取得
	 * @param instance
	 */
	public static void getEvidence(Object instance) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getSimpleName();
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エビデンス取得（サフィックスあり）
	 * @param instance
	 * @param suffix
	 */
	public static void getEvidence(Object instance, String suffix) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getSimpleName();
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + "_" + suffix + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ログインID取得
	 */
	public static WebElement getUserName() {
		WebElement username = webDriver.findElement(By.id("loginId"));
		return username;
	}

	/**
	 * パスワード取得
	 */
	public static WebElement getPassword() {
		WebElement password = webDriver.findElement(By.id("password"));
		return password;
	}

	/**
	 * ログインボタン取得
	 */
	public static WebElement getLoginBtn() {
		WebElement loginBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return loginBtn;
	}

	/**
	 * 機能タブ取得
	 */
	public static WebElement getFunctionTab() {
		WebElement functionTab = webDriver.findElement(By.cssSelector(".dropdown-toggle"));
		return functionTab;
	}

	/**
	 * ヘルプリンク取得
	 */
	public static WebElement getHelpLink() {
		WebElement helpLink = webDriver.findElement(By.linkText("ヘルプ"));
		return helpLink;
	}

	/**
	 * よくある質問リンク取得
	 */
	public static WebElement getFAQ() {
		WebElement FAQ = webDriver.findElement(By.linkText("よくある質問"));
		return FAQ;
	}

	/**
	 * キーワード検索取得
	 */
	public static WebElement getKeyword() {
		WebElement keyword = webDriver.findElement(By.cssSelector(".form-control"));
		return keyword;
	}

	/**
	 * 検索ボタン取得
	 */
	public static WebElement getSearchBtn() {
		WebElement searchBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return searchBtn;
	}

	/**
	 * クリアボタン取得
	 */
	public static void getClearBtn() {

		webDriver.findElement(By.xpath("//input[@value='クリア']")).click();
	}

	/**
	 * カテゴリー検索の「【研修関係】」情報取得
	 */
	public static WebElement getTrainingRelated() {
		WebElement trainingRelated = webDriver.findElement(By.partialLinkText("【研修関係】"));
		return trainingRelated;
	}

	/**
	 * 「キャンセル料・途中退校について」情報取得
	 */
	public static WebElement getSearchResultFAQ() {
		WebElement searchresultFAQ = webDriver.findElement(By.xpath("//span[text()='キャンセル料・途中退校について']"));
		return searchresultFAQ;
	}

}
