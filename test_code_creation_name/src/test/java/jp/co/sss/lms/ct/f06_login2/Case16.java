package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.lms.controller.LoginController;
import jp.co.sss.lms.ct.util.WebDriverUtils;
import jp.co.sss.lms.service.InfoService;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

	// Spring MVCのモック
	public MockMvc mockMvc;

	public MockHttpServletRequestBuilder getRequest;

	// 既に完成されたクラスは、ReflectionTestUtilsで設定出来る。
	@Autowired
	private MessageUtil messageUtil;

	// Mock、Spyの対象となるクラスを宣言
	@Mock
	private InfoService infoService;
	@Mock
	private LoginUserUtil loginUserUtil;
	@Mock
	private LoginService loginService;

	// @Mock、@Spyで宣言したクラスをテスト対象クラスに注入
	@InjectMocks
	private LoginController loginController;

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/**
	 * setupメソッド
	 * 
	 * Tips: BeforeEachアノテーションを付与するとテストメソッドを実行する前に必ず実行される（必要のない場合、省略可）
	 *       テストクラス全体で事前に設定すべき処理を記載することでソースの記載量を削減できる。
	 * */
	@BeforeEach
	public void setup() {
		// Spring MVCにテスト対象のコントローラを設定する 
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

		// 既に完成されているクラスに対して、@BeforEachにReflectionTestUtilsを設定することで、各テストメソッドで呼び出す処理を省略出来る。
		ReflectionTestUtils.setField(loginController, "messageUtil", messageUtil);
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	//ディスプレイでテストしないとエビデンスがおかしくなる。
	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");

		scrollTo(0);

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		getEvidence(instance);

		assertEquals("ログイン | LMS", getTitle());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		scrollTo(0);

		suffix = "01_ログイン前(初回ログイン登録済ユーザー)";

		getEvidence(instance, suffix);

		// ユーザー名とパスワードを入力

		WebElement username = getUserName();
		WebElement password = getPassword();

		//		username.sendKeys("StudentAA02");
		//		password.sendKeys("StudentAA02");
		username.sendKeys("StudentAA01");
		password.sendKeys("StudentAA01");

		// ログインボタンをクリック
		WebElement loginBtn = getLoginBtn();

		loginBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "02_ログイン後(初回ログイン登録済ユーザー)";

		getEvidence(instance, suffix);

		assertEquals("セキュリティ規約 | LMS", getTitle());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		scrollTo(250);

		WebElement radioactiveBtn = WebDriverUtils.getRadioAcitveBtn();

		//radioactiveBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "01_セキュリティ規約画面でチェックを入れる";

		getEvidence(instance, suffix);

		assertEquals("セキュリティ規約 | LMS", getTitle());

		// 次へボタンをクリック
		WebElement nextBtn = WebDriverUtils.getNextBtn();

		nextBtn.click();

		//scrollTo(250);

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "02_次へをクリック後にパスワード変更画面遷移";

		getEvidence(instance, suffix);

		assertEquals("パスワード変更 | LMS", getTitle());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "01_パスワード未入力で変更ボタンクリック";

		getEvidence(instance, suffix);

		scrollTo(250);

		WebElement changeBtn = WebDriverUtils.getChangeBtn();

		changeBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "02_パスワード未入力で変更ボタンクリック(モーダル)";

		getEvidence(instance, suffix);

		//assertEquals("セキュリティ規約 | LMS", getTitle());

		WebElement changemodalBtn = WebDriverUtils.getChangeModalBtn();

		changemodalBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "03_パスワード変更(未入力)";

		getEvidence(instance, suffix);

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		// 現在のパスワード、新しいパスワード、確認パスワードを入力

		WebElement currentpassword = getCurrentPassword();
		WebElement newpassword = getNewPassword();
		WebElement checkpassword = getCheckPassword();

		currentpassword.sendKeys("StudentAA02StudentAA02");
		newpassword.sendKeys("StudentAA02StudentAA02");
		checkpassword.sendKeys("StudentAA02StudentAA02");

		suffix = "01_20文字以上のパスワードを入力";

		getEvidence(instance, suffix);

		// 5秒待つ 	
		Thread.sleep(5000);

		//変更ボタンをクリック
		WebElement changeBtn = WebDriverUtils.getChangeBtn();

		changeBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "02_20文字以上のパスワードを入力(モーダル)";

		getEvidence(instance, suffix);

		WebElement changemodalBtn = WebDriverUtils.getChangeModalBtn();

		changemodalBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "03_パスワード変更(20文字以上)";

		getEvidence(instance, suffix);

		assertEquals("パスワードの長さが最大値(20)を超えています。", getLongPasswordErorrMessage());
		assertEquals("パスワード変更 | LMS", getTitle());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		// 現在のパスワード、新しいパスワード、確認パスワードを入力

		WebElement currentpassword = getCurrentPassword();
		WebElement newpassword = getNewPassword();
		WebElement checkpassword = getCheckPassword();

		currentpassword.sendKeys("#####");
		newpassword.sendKeys("#####");
		checkpassword.sendKeys("#####");

		suffix = "01_ポリシーに合わないパスワードを入力";

		getEvidence(instance, suffix);

		// 5秒待つ 	
		Thread.sleep(5000);

		//変更ボタンをクリック
		WebElement changeBtn = WebDriverUtils.getChangeBtn();

		changeBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "02_ポリシーに合わないパスワードを入力(モーダル)";

		getEvidence(instance, suffix);

		WebElement changemodalBtn = WebDriverUtils.getChangeModalBtn();

		changemodalBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "03_パスワード変更(ポリシーに合わない)";

		getEvidence(instance, suffix);

		assertEquals("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。", getNotMatchPolicyErorrMessage());
		assertEquals("パスワード変更 | LMS", getTitle());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case16 instance = new Case16();

		// 現在のパスワード、新しいパスワード、確認パスワードを入力

		WebElement currentpassword = getCurrentPassword();
		WebElement newpassword = getNewPassword();
		WebElement checkpassword = getCheckPassword();

		//		currentpassword.sendKeys("StudentAA02");
		//		newpassword.sendKeys("StudentAA02A");
		//		checkpassword.sendKeys("StudentAA02B");

		currentpassword.sendKeys("StudentAA01");
		newpassword.sendKeys("StudentAA01A");
		checkpassword.sendKeys("StudentAA01B");

		suffix = "01_一致しない確認パスワードを入力";

		getEvidence(instance, suffix);

		// 5秒待つ 	
		Thread.sleep(5000);

		//変更ボタンをクリック
		WebElement changeBtn = WebDriverUtils.getChangeBtn();

		changeBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "02_一致しない確認パスワードを入力(モーダル)";

		getEvidence(instance, suffix);

		WebElement changemodalBtn = WebDriverUtils.getChangeModalBtn();

		changemodalBtn.click();

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "03_パスワード変更(一致しない確認パスワード)";

		getEvidence(instance, suffix);

		assertEquals("パスワードと確認パスワードが一致しません。", getNotMatchCheckErorrMessage());
		assertEquals("パスワード変更 | LMS", getTitle());
	}

}
