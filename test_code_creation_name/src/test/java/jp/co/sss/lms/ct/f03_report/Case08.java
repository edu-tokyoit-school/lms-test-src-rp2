package jp.co.sss.lms.ct.f03_report;

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
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case08 instance = new Case08();

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
		Case08 instance = new Case08();
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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case08 instance = new Case08();

		//scrollTo(250);
		scrollTo(0);

		// 未提出情報を取得
		WebElement submitted = WebDriverUtils.getSubmitted();

		//WebElement detailBtn = WebDriverUtils.getDetailBtn();

		//detailBtn.click();
		submitted.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_セクション詳細画面遷移(未提出ステータス)";

		getEvidence(instance, suffix);

		assertEquals("セクション詳細 | LMS", getTitle());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case08 instance = new Case08();

		//scrollTo(250);

		WebElement dailyreportsubmittedBtn = WebDriverUtils.getDailyReportSubmittedBtn();

		dailyreportsubmittedBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_レポート画面遷移(提出済ステータス)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case08 instance = new Case08();

		// 報告内容を入力

		WebElement reportcontent = getReportContent();

		//クリアすることを考える。
		//reportcontent.sendKeys("かきくけこ");
		reportcontent.clear();

		reportcontent.sendKeys("今日はよくできました。");

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_報告内容を入力";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 提出するボタンをクリック
		WebElement submitBtn = getSubmitBtn();

		submitBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "02_報告内容を提出";

		getEvidence(instance, suffix);

		assertEquals("セクション詳細 | LMS", getTitle());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() throws InterruptedException {
		// TODO ここに追加

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case08 instance = new Case08();

		//scrollTo(250);
		scrollTo(0);

		// ようこそ受験生○○さん情報取得

		String examinee = "ようこそ受講生ＡＡ１さん";

		WebElement welcomeexaminee = WebDriverUtils.getWelcomeExaminee(examinee);
		welcomeexaminee.click();

		scrollTo(600);

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_ユーザー詳細画面遷移";

		getEvidence(instance, suffix);

		assertEquals("ユーザー詳細", getTitle());

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws InterruptedException {
		// TODO ここに追加

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case08 instance = new Case08();

		//String reportdate = "2022年10月5日(水)";
		String reportdate = "2022年10月1日(土)";
		WebElement applicablereportdetailBtn = WebDriverUtils.getApplicableReportDetailBtn(reportdate);
		applicablereportdetailBtn.click();

		//scrollTo(250);
		//scrollTo(0);

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_レポート詳細画面遷移";

		getEvidence(instance, suffix);

		assertEquals("レポート詳細 | LMS", getTitle());
	}

}
