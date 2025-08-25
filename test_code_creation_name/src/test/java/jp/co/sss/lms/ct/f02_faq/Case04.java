package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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
	void test01() {
		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");

		scrollTo(0);

		//自分自身をインスタンス化して渡す
		Case04 instance = new Case04();

		getEvidence(instance);
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case04 instance = new Case04();

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		scrollTo(0);

		suffix = "01_ログイン前(登録済ユーザー)";

		getEvidence(instance, suffix);

		// ユーザー名とパスワードを入力

		WebElement username = getUserName();
		WebElement password = getPassword();

		username.sendKeys("StudentAA01");
		password.sendKeys("StudentAA01A");

		// ログインボタンをクリック
		WebElement loginBtn = getLoginBtn();

		loginBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "02_ログイン後(登録済ユーザー)";

		getEvidence(instance, suffix);
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case04 instance = new Case04();

		// 3秒待つ 
		Thread.sleep(3000);

		// 機能タブをクリック
		WebElement functionTab = getFunctionTab();

		functionTab.click();

		// 3秒待つ 
		Thread.sleep(3000);

		// ヘルプリンクをクリック
		WebElement helpLink = getHelpLink();

		helpLink.click();

		suffix = "03_ヘルプ画面遷移";

		getEvidence(instance, suffix);
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case04 instance = new Case04();

		// 5秒待つ 
		Thread.sleep(5000);

		// よくある質問リンクをクリック
		WebElement FAQ = getFAQ();

		FAQ.click();
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "04-1_よくある質問画面遷移(相対パスでクリック)";

		getEvidence(instance, suffix);

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/faq");
		scrollTo(0);
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "04-2_よくある質問画面遷移(絶対パスでクリック)";

		WebDriverUtils.getEvidence(instance, suffix);
	}

}
