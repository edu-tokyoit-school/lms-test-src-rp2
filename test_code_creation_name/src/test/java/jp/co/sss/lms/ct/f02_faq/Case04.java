package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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

	@BeforeEach
	void setup() {
		//Spring MVCにテスト対象のコントローラを設定する
		this.mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

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
		Case04 instance = new Case04();

		getEvidence(instance);

		assertEquals("ログイン | LMS", getTitle());
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

		assertEquals("ログイン | LMS", getTitle());

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

		assertEquals("コース詳細 | LMS", getTitle());
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

		assertEquals("ヘルプ | LMS", getTitle());
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

		assertEquals("ヘルプ | LMS", getTitle());

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/faq");
		scrollTo(0);
		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "04-2_よくある質問画面遷移(絶対パスでクリック)";

		WebDriverUtils.getEvidence(instance, suffix);

		assertEquals("よくある質問 | LMS", getTitle());

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(true);
		when(loginUserUtil.isStudent()).thenReturn(true);
		when(infoService.getInfo()).thenReturn(new InfoDto());
		when(loginUserUtil.sendDisp()).thenReturn("redirect:/course/detail");

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");
		try {
			// 試験開始
			// getRequestの情報を基にコントローラーを呼び出す。Case1_1のMvcResultは使用しない場合、省略可能
			mockMvc.perform(getRequest) // コントローラーの呼び出し
					//.andDo(print()) // リクエスト情報をコンソールにprintする（デバッグ用）
					.andExpect(status().isFound()) // 実行結果のHTTPステータスが302(リダイレクト)かどうか検証する
					.andExpect(redirectedUrl("/course/detail")); // リダイレクトパスが正しいか検証する(ログイン機能のみ検証する)

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

}
