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
import org.openqa.selenium.support.ui.Select;
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
 * ケース12
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
		Case12 instance = new Case12();

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
		Case12 instance = new Case12();
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
		Case12 instance = new Case12();

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
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case12 instance = new Case12();

		// 勤怠タブをクリック
		WebElement attendancedirectediting = WebDriverUtils.getAttendanceDirectEditing();
		attendancedirectediting.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠情報直接変更画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("勤怠情報変更｜LMS", getTitle());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case12 instance = new Case12();

		// 勤怠タブをクリック
		Select attendanceatworkhour = getAttendanceAtWorkHour();
		Select attendanceatworkminutes = getAttendanceAtWorkMinutes();
		Select leavingworkHour = getLeavingWorkHour();
		Select leavingworkMinutes = getLeavingWorkMinutes();
		Select stepout = getStepOut();
		WebElement remarks = getRemarks();
		attendanceatworkhour.selectByValue("9");
		attendanceatworkminutes.selectByValue("");
		leavingworkHour.selectByValue("");
		leavingworkMinutes.selectByValue("");
		stepout.selectByValue("");
		remarks.clear();
		//remarks.sendKeys("あいうえお");

		getDisplayDown();
		Thread.sleep(3000);

		WebDriverUtils.getAttendanceUpdateBtn();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠情報直接変更画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("* 出勤時間が正しく入力されていません。", getAttendanceAtWorkErorrMessage());
		assertEquals("勤怠情報変更｜LMS", getTitle());

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case12 instance = new Case12();

		// 勤怠タブをクリック
		Select attendanceatworkhour = getAttendanceAtWorkHour();
		Select attendanceatworkminutes = getAttendanceAtWorkMinutes();
		Select leavingworkHour = getLeavingWorkHour();
		Select leavingworkMinutes = getLeavingWorkMinutes();
		Select stepout = getStepOut();
		WebElement remarks = getRemarks();
		attendanceatworkhour.selectByValue("");
		attendanceatworkminutes.selectByValue("");
		leavingworkHour.selectByValue("18");
		leavingworkMinutes.selectByValue("0");
		stepout.selectByValue("");
		remarks.clear();
		//remarks.sendKeys("あいうえお");

		getDisplayDown();
		Thread.sleep(3000);

		WebDriverUtils.getAttendanceUpdateBtn();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠情報直接変更画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("* 出勤情報がないため退勤情報を入力出来ません。", getAttendanceAtWork2ErorrMessage());
		assertEquals("勤怠情報変更｜LMS", getTitle());

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case12 instance = new Case12();

		// 勤怠タブをクリック
		Select attendanceatworkhour = getAttendanceAtWorkHour();
		Select attendanceatworkminutes = getAttendanceAtWorkMinutes();
		Select leavingworkHour = getLeavingWorkHour();
		Select leavingworkMinutes = getLeavingWorkMinutes();
		Select stepout = getStepOut();
		WebElement remarks = getRemarks();
		attendanceatworkhour.selectByValue("18");
		attendanceatworkminutes.selectByValue("0");
		leavingworkHour.selectByValue("9");
		leavingworkMinutes.selectByValue("0");
		stepout.selectByValue("");
		remarks.clear();
		//remarks.sendKeys("あいうえお");

		getDisplayDown();
		Thread.sleep(3000);

		WebDriverUtils.getAttendanceUpdateBtn();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠情報直接変更画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("* 退勤時刻[0]は出勤時刻[0]より後でなければいけません。", getAttendanceAtWork3ErorrMessage());
		assertEquals("勤怠情報変更｜LMS", getTitle());
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() throws InterruptedException {
		String suffix = null;
		//自分自身をインスタンス化して渡す
		Case12 instance = new Case12();

		// 勤怠タブをクリック
		Select attendanceatworkhour = getAttendanceAtWorkHour();
		Select attendanceatworkminutes = getAttendanceAtWorkMinutes();
		Select leavingworkHour = getLeavingWorkHour();
		Select leavingworkMinutes = getLeavingWorkMinutes();
		Select stepout = getStepOut();
		WebElement remarks = getRemarks();

		attendanceatworkhour.selectByValue("9");
		attendanceatworkminutes.selectByValue("0");
		leavingworkHour.selectByValue("17");
		leavingworkMinutes.selectByValue("30");
		stepout.selectByValue("465");
		remarks.clear();
		//remarks.sendKeys("あいうえお");

		getDisplayDown();
		Thread.sleep(3000);

		WebDriverUtils.getAttendanceUpdateBtn();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠情報直接変更画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("* 中抜け時間が勤務時間を超えています。", getAttendanceAtWork4ErorrMessage());
		assertEquals("勤怠情報変更｜LMS", getTitle());
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() throws InterruptedException {
		String suffix = null;
		//自分自身をインスタンス化して渡す
		Case12 instance = new Case12();

		// 勤怠タブをクリック
		Select attendanceatworkhour = getAttendanceAtWorkHour();
		Select attendanceatworkminutes = getAttendanceAtWorkMinutes();
		Select leavingworkHour = getLeavingWorkHour();
		Select leavingworkMinutes = getLeavingWorkMinutes();
		Select stepout = getStepOut();
		WebElement remarks = getRemarks();

		attendanceatworkhour.selectByValue("9");
		attendanceatworkminutes.selectByValue("0");
		leavingworkHour.selectByValue("17");
		leavingworkMinutes.selectByValue("30");
		stepout.selectByValue("30");
		remarks.clear();
		remarks.sendKeys(
				"今日は天気が良く、公園で散歩を楽しんだ。公園にはたくさんの花が咲いており、その美しさに心が癒された。特に桜の花が素晴らしく、その下で一息つくのは最高だった。今日は天気が良く、公園で散歩を楽しんだ。公園にはたくさんの花が咲いており、その美しさに心が癒された。特に桜の花が素晴らしく、その下で一息つくのは最高だった。");

		getDisplayDown();
		Thread.sleep(3000);

		WebDriverUtils.getAttendanceUpdateBtn();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_勤怠情報直接変更画面に遷移";

		getEvidence(instance, suffix);

		assertEquals("* 備考の長さが最大値(100)を超えています。", getAttendanceAtWork5ErorrMessage());
		assertEquals("勤怠情報変更｜LMS", getTitle());
	}

}
