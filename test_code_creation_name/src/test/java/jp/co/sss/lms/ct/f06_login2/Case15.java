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
 * ケース15
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース15 受講生 初回ログイン 利用規約に不同意")
public class Case15 {

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

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");

		scrollTo(0);

		//自分自身をインスタンス化して渡す
		Case15 instance = new Case15();

		getEvidence(instance);

		assertEquals("ログイン | LMS", getTitle());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case15 instance = new Case15();

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		scrollTo(0);

		suffix = "01_ログイン前(初回ログイン登録済ユーザー)";

		getEvidence(instance, suffix);

		// ユーザー名とパスワードを入力

		WebElement username = getUserName();
		WebElement password = getPassword();

		//username.sendKeys("StudentAA02");
		//password.sendKeys("StudentAA02");

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
	@DisplayName("テスト03 「同意します」チェックボックスにチェックをせず「次へ」ボタンを押下")
	void test03() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case15 instance = new Case15();

		scrollTo(250);

		WebElement radionegativeBtn = WebDriverUtils.getRadioNegativeBtn();

		//radionegativeBtn.click();

		// 次へボタンをクリック
		WebElement nextBtn = WebDriverUtils.getNextBtn();

		nextBtn.click();

		scrollTo(250);

		// 5秒待つ 	
		Thread.sleep(5000);

		suffix = "01_セキュリティ規約画面で次へ(チェック入れない)";

		getEvidence(instance, suffix);

		assertEquals("セキュリティ規約 | LMS", getTitle());

		assertEquals("セキュリティ規約への同意は必須です。", getSecurityConsentErorrMessage());

	}

}
