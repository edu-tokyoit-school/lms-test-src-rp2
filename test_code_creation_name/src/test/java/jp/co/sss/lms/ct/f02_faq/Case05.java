package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		String destinationPath = "evidence/case05_01" + ".png";
		File destFile = new File(destinationPath);
		FileUtils.copyFile(file, destFile);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		//初回ログイン済みのパスワードでログイン
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
		String destinationPath = "evidence/case05_02" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() throws Exception {
		//機能一覧をクリック
		webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]/a")).click();
		Thread.sleep(300);
		//ヘルプを選択、クリック
		webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]/ul/li[4]/a")).click();
		//遷移したページがヘルプページであるか確認
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case05_03" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() throws Exception {
		// 元のタブのハンドル
		String originalTab = webDriver.getWindowHandle();
		// 新しいタブで開きたいリンク要素を取得
		WebElement link = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/p/a"));
		// ActionsクラスでCtrl+Clickを実行
		Actions actions = new Actions(webDriver);
		actions.keyDown(Keys.CONTROL).click(link).keyUp(Keys.CONTROL).build().perform();

		Thread.sleep(2000);

		//タブの切り替え
		ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
		tabs.remove(originalTab);
		webDriver.switchTo().window(tabs.get(0));
		//スクリーンショットをevidenceフォルダに保存
		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case05_04" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() throws Exception {
		// キーワード検索ボックスを確認
		WebElement keyword = webDriver.findElement(By.xpath("//*[@id=\"form\"]"));
		keyword.clear();
		//キーワード「研修」で検索
		keyword.sendKeys("研修");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[2]/div/input[1]")).click();
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		By tableBodyLocator = By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody");
		wait.until(ExpectedConditions.visibilityOfElementLocated(tableBodyLocator));

		//テーブルのボディからすべての行を取得
		List<WebElement> resultRows = webDriver.findElements(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr"));

		// 各行をループしてキーワードが含まれているかチェック
		boolean keywordFound = false;
		for (WebElement row : resultRows) {
			String rowText = row.getText(); // 行全体のテキストを取得
			if (rowText.contains("研修")) {
				keywordFound = true;
				break; // キーワードが見つかったらループを抜ける
			}
		}
		assertTrue(keywordFound, "検索結果のいずれの行にもキーワード「研修」が含まれていませんでした。");
		
		//ページをスクロール
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

		//スクリーンショットをevidenceフォルダに保存
		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case05_05" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() throws Exception {
		// キーワード検索ボックスを取得
		WebElement keyword = webDriver.findElement(By.xpath("//*[@id=\"form\"]"));
		//クリアボタン押下
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[2]/div/input[2]")).click();
		//要素が空かどうか確認
		String textAfterClear = keyword.getAttribute("value");
		assertEquals("", textAfterClear);
		//スクリーンショットをevidenceフォルダに保存
		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath = "evidence/case05_06" + ".png";
		File destFile = new File(destinationPath);

		FileUtils.copyFile(file, destFile);
	}

}
