package jp.co.sss.lms.ct.f01_login1;

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
import jp.co.sss.lms.service.InfoService;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
	void test01() throws Exception {
		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");

		scrollTo(0);

		//自分自身をインスタンス化して渡す
		Case02 instance = new Case02();

		getEvidence(instance);

		assertEquals("ログイン | LMS", getTitle());

		//		// URLが「/」のgetメソッドを実行する
		//		ResultActions results = mockMvc.perform(get("/"));
		//
		//		results.andExpect(view().name("login/index"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() throws Exception {
		// TODO ここに追加
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case02 instance = new Case02();

		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		scrollTo(0);

		suffix = "01_ログイン前(未登録ユーザー)";

		getEvidence(instance, suffix);

		// ユーザー名とパスワードを入力

		WebElement username = getUserName();
		WebElement password = getPassword();

		username.sendKeys("StudentZZ001");
		password.sendKeys("StudentZZ001");

		// ログインボタンをクリック
		WebElement loginBtn = getLoginBtn();

		loginBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "02_ログイン後(未登録ユーザー)";

		getEvidence(instance, suffix);

		assertEquals("ログイン | LMS", getTitle());

		// ログイン情報
		String loginuserId = "noUser";
		String loginpassword = "test";

		// モック対象メソッドの返却値を設定
		when(loginService.getLoginInfo(loginuserId, loginpassword)).thenReturn("ログインに失敗しました");

		getRequest = MockMvcRequestBuilders.post("/login").param("loginId", loginuserId).param("password",
				loginpassword);

		try {
			mockMvc.perform(getRequest)
					//.andDo(print()) 
					.andExpect(status().isOk())
					.andExpect(view().name("login/index"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

}
