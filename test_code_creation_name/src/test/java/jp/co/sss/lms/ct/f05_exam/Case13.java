package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

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
 * 結合テスト 試験実施機能
 * ケース13
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果0点")
public class Case13 {

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

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		scrollTo(0);

		suffix = "01_ログイン前(登録済ユーザー)";

		getEvidence(instance, suffix);

		assertEquals("ログイン | LMS", getTitle());
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();
		// ユーザー名とパスワードを入力

		WebElement username = WebDriverUtils.getUserName();
		WebElement password = WebDriverUtils.getPassword();

		username.sendKeys("StudentAA01");
		password.sendKeys("StudentAA01A");

		// ログインボタンをクリック
		WebElement loginBtn = WebDriverUtils.getLoginBtn();

		loginBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "02_ログイン後(登録済ユーザー)";

		getEvidence(instance, suffix);

		assertEquals("コース詳細 | LMS", getTitle());
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// ログインボタンをクリック
		WebElement exam = WebDriverUtils.getExam();

		exam.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_セクション詳細画面に遷移(試験有)";

		getEvidence(instance, suffix);

		assertEquals("セクション詳細 | LMS", getTitle());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// ログインボタンをクリック
		WebElement examdetailBtn = WebDriverUtils.getExamDetailBtn();

		examdetailBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_試験開始画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("試験【ITリテラシー①】 | LMS", getTitle());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// ログインボタンをクリック
		WebElement examstartBtn = WebDriverUtils.getExamStartBtn();

		examstartBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_試験画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("ITリテラシー① | LMS", getTitle());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 未回答の状態で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// 5秒待つ 
		scrollTo(10000);
		Thread.sleep(5000);

		// ログインボタンをクリック
		WebElement examconfirmationBtn = WebDriverUtils.getExamConfirmationBtn();

		examconfirmationBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_試験確認画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("ITリテラシー① | LMS", getTitle());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// 5秒待つ 
		scrollTo(10000);
		Thread.sleep(5000);

		// ログインボタンをクリック
		//WebElement examanswersubmitBtn = WebDriverUtils.getExamAnswerSubmitBtn();
		WebDriverUtils.getExamAnswerSubmitBtn();

		//examanswersubmitBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_試験結果画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("ITリテラシー① | LMS", getTitle());
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case13 instance = new Case13();

		// 5秒待つ 
		scrollTo(10000);
		Thread.sleep(5000);

		// ログインボタンをクリック
		//WebElement examanswersubmitBtn = WebDriverUtils.getExamAnswerSubmitBtn();
		WebElement exambackBtn = WebDriverUtils.getExamBackBtn();

		exambackBtn.click();

		//examanswersubmitBtn.click();
		WebDriverUtils.getDisplayDown();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_遷移後当該試験の結果が反映される";

		getEvidence(instance, suffix);

		assertEquals("試験【ITリテラシー①】 | LMS", getTitle());
	}

}
