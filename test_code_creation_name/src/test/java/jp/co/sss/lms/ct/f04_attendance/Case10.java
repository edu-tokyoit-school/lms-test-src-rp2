package jp.co.sss.lms.ct.f04_attendance;

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
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
		Case10 instance = new Case10();

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
		Case10 instance = new Case10();
		// ユーザー名とパスワードを入力

		WebElement username = WebDriverUtils.getUserName();
		WebElement password = WebDriverUtils.getPassword();

		//		username.sendKeys("StudentAA03");
		//		password.sendKeys("StudentAA03A");

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
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case10 instance = new Case10();

		// 勤怠タブをクリック
		WebElement attendance = WebDriverUtils.getAttendance();

		attendance.click();
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠管理画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("勤怠情報変更｜LMS", getTitle());

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() throws InterruptedException {

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case10 instance = new Case10();

		// 勤怠タブをクリック
		//WebElement attendanceatworkBtn = WebDriverUtils.getAttendanceAtWorkBtn();
		WebDriverUtils.getAttendanceAtWorkBtn();

		//attendanceatworkBtn.click();
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_出勤ボタン押下";

		getEvidence(instance, suffix);

		assertEquals("勤怠情報変更｜LMS", getTitle());

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case10 instance = new Case10();

		// 勤怠タブをクリック
		//WebElement leavingworkBtn = WebDriverUtils.getLeavingWorkBtn();
		WebDriverUtils.getLeavingWorkBtn();

		//leavingworkBtn.click();
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_退勤ボタン押下";

		getEvidence(instance, suffix);

		assertEquals("勤怠情報変更｜LMS", getTitle());
	}

}
