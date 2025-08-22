package jp.co.sss.lms.ut.f01_login1;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Seleniumサンプルクラス.<br>
 * Seleniumを動作させるだけのクラスです。
 */
public class SampleTestSelenium {

	/** GoogleChromeDriver. */
	private WebDriver chromeDriver;

	/**
	 * 事前準備.<br>
	 * テストクラス実行時に1度だけ実行します。
	 */
	@BeforeClass
	public static void setUpClass() {

		System.setProperty("webdriver.chrome.driver", "lib\\chromedriver.exe");

	}

	/**
	 * 事前準備.<br>
	 * テストケース実行時に実行します。
	 */
	@Before
	public void setUp() {

		chromeDriver = new ChromeDriver();

		// 各画面表示時に5秒待機する
		//chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	/**
	 * 後処理.<br>
	 * テストケース実行後に実行します。
	 */
	@After
	public void down() {

		// chromeドライバーを終了する
		chromeDriver.quit();

	}

	/**
	 * 正常動作.<br>
	 * 本テストは Selenium の動作確認を行うためのテストメソッドです。
	 */
	@Test
	public void testSelenium() throws Exception {

		try {
			// 指定のURLの画面を開く
			//chromeDriver.get("http://www.google.co.jp");
			chromeDriver.get("https://lms.tokyoitschool.jp/lms/");

			// ユーザー名とパスワードを入力
			WebElement username = chromeDriver.findElement(By.id("input-15"));
			WebElement password = chromeDriver.findElement(By.id("input-16"));

			username.sendKeys("std2025-04190");
			password.sendKeys("123!");

			// ログインボタンをクリック
			WebElement loginBtn = chromeDriver.findElement(By.className("v-btn__content"));
			loginBtn.click();

			// 設計のセオリーを検索キーワードに入力し、Enterキーを押下する
			//chromeDriver.findElement(By.name("q")).sendKeys("設計のセオリー", Keys.ENTER);

			// 検索結果の中からシステム設計のセオリー(Amazonリンク)をクリックしてAmazonサイトを開く
			//chromeDriver.findElement(By.partialLinkText("システム設計のセオリー")).click();

			// 開いたAmazonのページのキャプチャを取得する
			File file = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file, new File("C:\\work\\ScreenShot\\lms_login_before.png"));

			// ms単位の指定 1000ms = 1s
			Thread.sleep(20000);

			// 開いたAmazonのページのキャプチャを取得する
			File file2 = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file2, new File("C:\\work\\ScreenShot\\lms_login_after.png"));

			chromeDriver.get("https://lms.tokyoitschool.jp/lms/user/detail/");

			// ms単位の指定 1000ms = 1s
			Thread.sleep(20000);

			// 開いたAmazonのページのキャプチャを取得する
			File file3 = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(file3, new File("C:\\work\\ScreenShot\\lms_login_user_detail.png"));
			System.out.println("名前 " + chromeDriver.findElement(By.className("v-data-footer__select")));

			System.out.println("情報: " + chromeDriver.getPageSource());

			// 成功後の処理（例：タイトル表示）
			System.out.println("ログイン後タイトル: " + chromeDriver.getTitle());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 数秒待ってからブラウザを閉じる
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ignored) {
			}
			chromeDriver.quit();
		}

	}
}
