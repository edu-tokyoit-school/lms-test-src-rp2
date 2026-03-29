package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {
	ChromeDriver chromeDriver = new ChromeDriver();

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() throws Exception {
		webDriver.get("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", webDriver.getTitle());
		//ログインIDとパスワード入力欄が表示されているかチェック
		WebElement elemUser = webDriver.findElement(By.id("loginId"));
		WebElement elemPass = webDriver.findElement(By.id("password"));
		assertTrue(elemUser.isDisplayed());
		assertTrue(elemPass.isDisplayed());

		Thread.sleep(5000);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case02_01" + ".png";
		File destFile = new File(destinationPath);
		FileUtils.copyFile(file, destFile);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() throws Exception {
		//DBに登録されていないIDとパスワードでログイン
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAB");
		webDriver.findElement(By.id("password")).sendKeys("StudentAB");
		//ログインを実行
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();

		//エラーメッセージ「ログインに失敗しました。」が表示されることをチェック
		//xpathを使用
		WebElement errorMsg = webDriver
				.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/span"));

		assertTrue(errorMsg.isDisplayed());

		//スクリーンショットをevidenceフォルダに保存

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case02_02" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);

	}

}
