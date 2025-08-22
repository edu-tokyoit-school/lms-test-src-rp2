//package jp.co.sss.lms.ut.controller;
package jp.co.sss.lms.ut.f01_login1;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
@SpringBootTest
public class LoginControllerTestCase01 {

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
	 *  
	 * @throws Exception 
	 * */
	@Test
	void testCase1_1() {

		// 指定のURLの画面を開く
		//WebDriverUtils.goTo("http://www.google.co.jp");
		WebDriverUtils.goTo("http://localhost:8080/lms/");

		WebDriverUtils.getEvidence(WebDriverUtils.webDriver);

		// セッション情報を事前に設定する場合、MockHttpSessionを使用する
		MockHttpSession session = new MockHttpSession();
		ReflectionTestUtils.setField(loginController, "session", session);

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(false);
		when(infoService.getInfo()).thenReturn(new InfoDto());

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");

		try {
			// 試験開始
			// getRequestの情報を基にコントローラーを呼び出し検証を行う
			mockMvc.perform(getRequest) // コントローラーの呼び出し
					//.andDo(print()) // リクエスト情報をコンソールにprint(デバッグ用)
					.andExpect(status().isOk()) // 実行結果のHTTPステータスが200(正常終了)かどうか検証する
					.andExpect(view().name("login/index"))// viewをreturnしているか検証する
					.andReturn();

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}

	}
}
