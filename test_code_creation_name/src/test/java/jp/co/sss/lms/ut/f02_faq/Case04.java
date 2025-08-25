//package jp.co.sss.lms.ut.controller;
package jp.co.sss.lms.ut.f02_faq;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.lms.controller.LoginController;
import jp.co.sss.lms.ct.util.WebDriverUtils;
import jp.co.sss.lms.dto.InfoDto;
import jp.co.sss.lms.service.InfoService;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * JUnitによる機能試験(ホワイトボックステスト)_テストコードサンプル
 * コントローラーを試験する際のサンプルコードです。
 * サービスクラス等と違い、MockMvcのコントローラー独自の記法があるので注意してください。
 * MockMvcについてはテスト技法（JUnit・Mockito・MockMVC）_講義資料.pdfの34ページを参照）
 * 
 * */
@SuppressWarnings("unused")
@SpringBootTest
public class Case04 {

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

	/**
	 * 事前準備.<br>
	 * テストクラス実行時に1度だけ実行します。
	 */
	@BeforeAll
	public static void setUpClass() {
		WebDriverUtils.createDriver();
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

	/**
	 * tearDownメソッド
	 * 
	 * Tips:AfterEachアノテーションを付与すると各テストメソッドが終了した後にに必ず実行される。（必要のない場合、省略可）
	 * 　　　　　
	 * */
	@AfterEach
	public void tearDown() {
		// chromeドライバーを終了する
		WebDriverUtils.closeDriver();
	}

	/**
	 * Case.1_1 ログインコントローラー試験　初期表示テスト(未ログイン時 正常終了)
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：なし
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「200」であること
	 *  ・返却されるViewのパスが「/login/index」であること
	 * @throws InterruptedException 
	 *  
	 * @throws Exception 
	 * */
	@Test
	public void testCase4_1() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case04 instance = new Case04();

		// 指定のURLの画面を開く
		WebDriverUtils.goTo("http://localhost:8080/lms/");
		WebDriverUtils.scrollTo(0);

		suffix = "01_ログイン前(登録済ユーザー)";

		WebDriverUtils.getEvidence(instance, suffix);

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

		WebDriverUtils.getEvidence(instance, suffix);

		// 3秒待つ 
		Thread.sleep(3000);

		// 機能タブをクリック
		WebElement functionTab = WebDriverUtils.getFunctionTab();

		functionTab.click();

		// 3秒待つ 
		Thread.sleep(3000);

		// ヘルプリンクをクリック
		WebElement helpLink = WebDriverUtils.getHelpLink();

		helpLink.click();

		suffix = "03_ヘルプ画面遷移";

		WebDriverUtils.getEvidence(instance, suffix);

		// 5秒待つ 
		Thread.sleep(5000);

		// よくある質問リンクをクリック
		WebElement FAQ = WebDriverUtils.getFAQ();

		FAQ.click();
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "04-1_よくある質問画面遷移(相対パスでクリック)";

		WebDriverUtils.getEvidence(instance, suffix);

		// 指定のURLの画面を開く
		WebDriverUtils.goTo("http://localhost:8080/lms/faq");
		WebDriverUtils.scrollTo(0);
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "04-2_よくある質問画面遷移(絶対パスでクリック)";

		WebDriverUtils.getEvidence(instance, suffix);

		// セッション情報を事前に設定する場合、MockHttpSessionを使用する
		MockHttpSession session = new MockHttpSession();
		ReflectionTestUtils.setField(loginController, "session", session);

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(true);
		when(loginUserUtil.isAdmin()).thenReturn(true);
		when(infoService.getInfo()).thenReturn(new InfoDto());
		when(loginUserUtil.sendDisp()).thenReturn("redirect:/course/list");

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");
		try {
			// getRequestの情報を基にコントローラーを呼び出す
			mockMvc.perform(getRequest)
					//.andDo(print()) 
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/course/list"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		} finally {
			// 数秒待ってからブラウザを閉じる
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ignored) {
			}
			WebDriverUtils.closeDriver();
		}
	}
}
