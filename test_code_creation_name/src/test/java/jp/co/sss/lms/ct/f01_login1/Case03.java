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

/**
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

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
		String destinationPath = "evidence/case03_01" + ".png";
		File destFile = new File(destinationPath);
		FileUtils.copyFile(file, destFile);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		//DBに登録されていないIDとパスワードでログイン
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA01");
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA01a");
		//ログインを実行
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();

		//メッセージ「ようこそ受講生AA1さん」が表示されることを確認
		WebElement welcomeUser = webDriver
				.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a"));

		assertTrue(welcomeUser.isDisplayed());

		//スクリーンショットをevidenceフォルダに保存

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case03_02" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);

	}

}
