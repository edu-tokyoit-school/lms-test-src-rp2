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
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
		Case09 instance = new Case09();

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
		Case09 instance = new Case09();
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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() throws InterruptedException {
		// TODO ここに追加

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

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
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws InterruptedException {
		// TODO ここに追加

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		String reportdate = "2022年10月2日(日)";
		String reportname = "週報【デモ】";
		WebElement applicablereportcorrectionBtn = WebDriverUtils.getApplicableReportCorrectionBtn(reportdate,
				reportname);
		applicablereportcorrectionBtn.click();

		//scrollTo(250);
		//scrollTo(0);

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "01_レポート詳細画面遷移";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() throws InterruptedException {
		// TODO ここに追加

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		//scrollTo(250);
		//scrollTo(0);

		// 学習項目を入力
		WebElement learningtopic = getLearningTopic();
		learningtopic.clear();
		learningtopic.sendKeys("");

		Select comprehensionlevel = getComprehensionLevel();
		//comprehensionlevel.clear();
		//comprehensionlevel.sendKeys("3");
		comprehensionlevel.selectByValue("3");

		WebElement goalachievement = getGoalAchievement();

		suffix = "01_報告内容を入力する(学習項目が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(300);

		goalachievement.clear();
		goalachievement.sendKeys("4");

		WebElement impressions = getImpressions();
		impressions.clear();
		impressions.sendKeys("週報のサンプルです。2");

		WebElement reviewoftheweek = getReviewOfTheWeek();
		reviewoftheweek.clear();
		reviewoftheweek.sendKeys(
				"日報・週報などの入力項目は管理者権限で作成・変更することが可能です。「週報」の入力項目は管理画面のレポート作成機能を用いて設定し、登録されたレポートのフォーマットはデータベースの「m_daily_report」テーブルと「m_daily_report_detail」テーブルに登録されています2。");

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(450);

		suffix = "02_報告内容を入力する(学習項目が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		WebElement weeklyreportsubmitBtn = WebDriverUtils.getWeeklyReportSubmitBtn();
		weeklyreportsubmitBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "03_報告内容を提出する(学習項目が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() throws InterruptedException {
		// TODO ここに追加

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		//scrollTo(250);
		//scrollTo(0);

		// 学習項目を入力
		WebElement learningtopic = getLearningTopic();
		learningtopic.clear();
		learningtopic.sendKeys("ITリテラシー①7");

		Select comprehensionlevel = getComprehensionLevel();
		//comprehensionlevel.clear();
		//comprehensionlevel.sendKeys("3");
		comprehensionlevel.selectByValue("");

		WebElement goalachievement = getGoalAchievement();

		suffix = "01_報告内容を入力する(理解度が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(300);

		goalachievement.clear();
		goalachievement.sendKeys("4");

		WebElement impressions = getImpressions();
		impressions.clear();
		impressions.sendKeys("週報のサンプルです。2");

		WebElement reviewoftheweek = getReviewOfTheWeek();
		reviewoftheweek.clear();
		reviewoftheweek.sendKeys(
				"日報・週報などの入力項目は管理者権限で作成・変更することが可能です。「週報」の入力項目は管理画面のレポート作成機能を用いて設定し、登録されたレポートのフォーマットはデータベースの「m_daily_report」テーブルと「m_daily_report_detail」テーブルに登録されています2。");

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(450);

		suffix = "02_報告内容を入力する(理解度が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		WebElement weeklyreportsubmitBtn = WebDriverUtils.getWeeklyReportSubmitBtn();
		weeklyreportsubmitBtn.click();

		// 5秒待つ 
		Thread.sleep(5000);

		suffix = "03_報告内容を提出する(理解度が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		//scrollTo(250);
		//scrollTo(0);

		// 学習項目を入力
		WebElement learningtopic = getLearningTopic();
		learningtopic.clear();
		learningtopic.sendKeys("ITリテラシー①7");

		Select comprehensionlevel = getComprehensionLevel();
		//comprehensionlevel.clear();
		//comprehensionlevel.sendKeys("3");
		comprehensionlevel.selectByValue("3");

		WebElement goalachievement = getGoalAchievement();

		suffix = "01_報告内容を入力する(目標の達成度が数値以外)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(300);

		goalachievement.clear();
		goalachievement.sendKeys("文字");

		WebElement impressions = getImpressions();
		impressions.clear();
		impressions.sendKeys("週報のサンプルです。2");

		WebElement reviewoftheweek = getReviewOfTheWeek();
		reviewoftheweek.clear();
		reviewoftheweek.sendKeys(
				"日報・週報などの入力項目は管理者権限で作成・変更することが可能です。「週報」の入力項目は管理画面のレポート作成機能を用いて設定し、登録されたレポートのフォーマットはデータベースの「m_daily_report」テーブルと「m_daily_report_detail」テーブルに登録されています2。");

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(450);

		suffix = "02_報告内容を入力する(目標の達成度が数値以外)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		WebElement weeklyreportsubmitBtn = WebDriverUtils.getWeeklyReportSubmitBtn();
		weeklyreportsubmitBtn.click();

		// 5秒待つ 
		scrollTo(300);
		Thread.sleep(5000);

		suffix = "03_報告内容を提出する(目標の達成度が数値以外)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		//scrollTo(250);
		//scrollTo(0);

		// 学習項目を入力
		WebElement learningtopic = getLearningTopic();
		learningtopic.clear();
		learningtopic.sendKeys("ITリテラシー①7");

		Select comprehensionlevel = getComprehensionLevel();
		//comprehensionlevel.clear();
		//comprehensionlevel.sendKeys("3");
		comprehensionlevel.selectByValue("3");

		WebElement goalachievement = getGoalAchievement();

		suffix = "01_報告内容を入力する(目標の達成度が範囲外)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(300);

		goalachievement.clear();
		goalachievement.sendKeys("100");

		WebElement impressions = getImpressions();
		impressions.clear();
		impressions.sendKeys("週報のサンプルです。2");

		WebElement reviewoftheweek = getReviewOfTheWeek();
		reviewoftheweek.clear();
		reviewoftheweek.sendKeys(
				"日報・週報などの入力項目は管理者権限で作成・変更することが可能です。「週報」の入力項目は管理画面のレポート作成機能を用いて設定し、登録されたレポートのフォーマットはデータベースの「m_daily_report」テーブルと「m_daily_report_detail」テーブルに登録されています2。");

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(450);

		suffix = "02_報告内容を入力する(目標の達成度が範囲外)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		WebElement weeklyreportsubmitBtn = WebDriverUtils.getWeeklyReportSubmitBtn();
		weeklyreportsubmitBtn.click();

		// 5秒待つ 
		scrollTo(300);
		Thread.sleep(5000);

		suffix = "03_報告内容を提出する(目標の達成度が範囲外)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() throws InterruptedException {
		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		//scrollTo(250);
		//scrollTo(0);

		// 学習項目を入力
		WebElement learningtopic = getLearningTopic();
		learningtopic.clear();
		learningtopic.sendKeys("ITリテラシー①7");

		Select comprehensionlevel = getComprehensionLevel();
		//comprehensionlevel.clear();
		//comprehensionlevel.sendKeys("3");
		comprehensionlevel.selectByValue("3");

		WebElement goalachievement = getGoalAchievement();

		suffix = "01_報告内容を入力する(目標の達成度・所感が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(300);

		goalachievement.clear();
		goalachievement.sendKeys("");

		WebElement impressions = getImpressions();
		impressions.clear();
		impressions.sendKeys("");

		WebElement reviewoftheweek = getReviewOfTheWeek();
		reviewoftheweek.clear();
		reviewoftheweek.sendKeys(
				"日報・週報などの入力項目は管理者権限で作成・変更することが可能です。「週報」の入力項目は管理画面のレポート作成機能を用いて設定し、登録されたレポートのフォーマットはデータベースの「m_daily_report」テーブルと「m_daily_report_detail」テーブルに登録されています2。");

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(450);

		suffix = "02_報告内容を入力する(目標の達成度・所感が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		WebElement weeklyreportsubmitBtn = WebDriverUtils.getWeeklyReportSubmitBtn();
		weeklyreportsubmitBtn.click();

		// 5秒待つ 
		scrollTo(300);
		Thread.sleep(5000);

		suffix = "03_報告内容を提出する(目標の達成度・所感が未入力)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() throws InterruptedException {

		String suffix = null;

		//自分自身をインスタンス化して渡す
		Case09 instance = new Case09();

		//scrollTo(250);
		//scrollTo(0);

		// 学習項目を入力
		WebElement learningtopic = getLearningTopic();
		learningtopic.clear();
		learningtopic.sendKeys("ITリテラシー①7");

		Select comprehensionlevel = getComprehensionLevel();
		//comprehensionlevel.clear();
		//comprehensionlevel.sendKeys("3");
		comprehensionlevel.selectByValue("3");

		WebElement goalachievement = getGoalAchievement();

		suffix = "01_報告内容を入力する(所感・一週間の振り返りが2000文字超)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		// 5秒待つ 
		scrollTo(300);
		Thread.sleep(5000);

		goalachievement.clear();
		goalachievement.sendKeys("4");

		WebElement impressions = getImpressions();
		impressions.clear();
		impressions.sendKeys(
				"ようやく病院の敷地を抜けると、ぐっとつまっていた肩の力がようやく抜けた気がした。風に吹かれる髪は一日中つけていた帽子のせいでぐちゃぐちゃだし、ビニル手袋のせいで手の皮膚はふやけてところどころ剥けていた。水色のビニール製のガウンは熱がこもるため、２月初旬の寒空だというのに熱中症のように頭が未だにぼうっとしていた。\r\n"
						+ "\r\n"
						+ "　コロナウイルスが蔓延し、世界中に大打撃を与えてからもうすぐ１年も過ぎようというのに、この大狂乱は落ち着く気配もなかった。恵介は研修医として、今日は休日の救急外来当直だったが、お昼すぎに重症のCOVID-19患者が搬送されてきてからまさに戦場と化していた。\r\n"
						+ "\r\n"
						+ "　赤信号に引っかかり、恵介は自転車のブレーキを掛ける。ここの信号は長い。ポケットからスマホを取り出すと、LINEの画面をつける。宣伝アカウントから通知が数件来ている以外は特にメッセージの受信はなかった。トーク相手の選択画面で一番上にピンどめされているのは、付き合って３年以上になる彼女の由佳だった。１行だけ表示されている昨日のやりとりの最後。\r\n"
						+ "\r\n"
						+ "『分かったよ。どうせ私がわがままなだけなんだ』\r\n"
						+ "\r\n"
						+ "喧嘩したままぶつ切りになっていた。スマホを持つ手に力が入る。あんな喧嘩だってしたかったわけじゃないのだけど、このところ由佳とは何となくすれ違ってばかりいると、恵介は感じていた。信号が青に変わる。重いペダルをゆっくりと漕ぎ始める。\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "　院内感染を防ぐために旅行はもちろん禁止、飲み会やカラオケはもちろんのこと、普通の飲食店でも２人以上での食事は病院規約で厳しく規制されていた。帰省が概ね許可されるようになったのも去年の年末頃にようやくだった。そんな調子だったから、由佳と会う機会も自然と減っていた。まだ学生の由佳は、大学がほとんど授業がリモートなこともあり感染リスクの上がるような行動はできる限り避けていた。でもまがりなりにも医師として、家族でない人と接触してCOVID-19を発症するのは良くないし、何より今日のように突然COVID-19患者が運ばれるような救急外来も受け持つ恵介としては、自分という最大のリスク因子を由佳に近づけたくなかった。\r\n"
						+ "\r\n"
						+ "　それにしても、と恵介は思い返す。もう随分由佳の声を聞いていない。もちろん電話は出来るけれど、日々の仕事に追われてしまい最近は帰ると１０時過ぎなんてこともざらで、それから由佳に電話をかける元気がない日も多かった。LINEの文面だけでやり取りすることが増えるにつれ、何となく会話がぎこちないと感じることが増えていた。\r\n"
						+ "\r\n"
						+ "　昔は由佳の考えていることは大体分かったし、そのことは恵介自身のちょっとした自慢でもあった。由佳が素直で良い子なのは間違いなかったけど、それでも直感のような、あるいは波長が自然と合うような、そんな感覚があった。でも最近はそれが薄れているのを感じていた。昨日だって何がきっかけとなったのか分からないうちに、LINEのやり取りは険悪になっていった。\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "　家の前に自転車を止め、3階まで重たい体を引きずって階段を上る。部屋の鍵を回し、ゆっくりと玄関扉を開けると、廊下と部屋を隔てる扉にはめられたガラスの向こうが明るいことに気がついた。\r\n"
						+ "\r\n"
						+ "「え、電気は消したはず……」\r\n"
						+ "\r\n"
						+ "泥棒か？と身構えた数秒後に、その扉をバタンと開けて部屋着姿の由佳が顔を出した。5mの廊下の端と端で次の言葉につまったままの二人。\r\n"
						+ "\r\n"
						+ "「……ごめんね。来ちゃいけないとは思ってたんだけど、どうしても……我慢できなくて」\r\n"
						+ "\r\n"
						+ "やがて由佳は、申し訳無さそうに小さくそう言った。\r\n"
						+ "\r\n"
						+ "「あ、大丈夫だよ部屋に入る前に服は全部着替えたし、手洗いとかもしっかりしたし、とりあえず家の中でも新しいマスクつけたけども……って恵ちゃん、どうしたの？！」\r\n"
						+ "\r\n"
						+ "どうしたのと言われてようやく、恵介は自分が泣いていることに気づいた。久しぶりに見た由佳の顔にほっとしたのか、口元がほころぶのを止められないのに、目からはぼろぼろと涙がこぼれ続けた。\r\n"
						+ "\r\n"
						+ "「恵ちゃん大丈夫？」\r\n"
						+ "\r\n"
						+ "思わず駆け寄りそうになる由佳に恵介は手を突き出して、無言で近づくなのアピールをした。\r\n"
						+ "\r\n"
						+ "「俺、病院から帰ってきたばかりだから。とりあえず風呂に入って、服の洗濯だけしちゃうよ。そしたらゆっくり話そう」\r\n"
						+ "\r\n"
						+ "そう、久々にゆっくりと。\r\n"
						+ "\r\n"
						+ "「会いたかったよ、由佳」\r\n"
						+ "\r\n"
						+ "恵介の言葉に由佳はうんとうなずく。\r\n"
						+ "\r\n"
						+ "「わたしも、恵ちゃん。じゃあ向こうでおとなしく待ってるね」\r\n"
						+ "\r\n"
						+ "そう言って部屋に戻ろうとした由佳は足を止め、くるりと振り返りながら言った。\r\n"
						+ "\r\n"
						+ "「恵ちゃん、おかえり」\r\n"
						+ "\r\n"
						+ "その笑顔に恵介は救われたような気持ちになった。毎日無人の家に帰る寂しさ。擦り切れそうな日々。\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "　『おかえり』。それがこんなに温かい言葉だったと恵介は噛み締めていた。");

		WebElement reviewoftheweek = getReviewOfTheWeek();
		reviewoftheweek.clear();
		reviewoftheweek.sendKeys(
				"ようやく病院の敷地を抜けると、ぐっとつまっていた肩の力がようやく抜けた気がした。風に吹かれる髪は一日中つけていた帽子のせいでぐちゃぐちゃだし、ビニル手袋のせいで手の皮膚はふやけてところどころ剥けていた。水色のビニール製のガウンは熱がこもるため、２月初旬の寒空だというのに熱中症のように頭が未だにぼうっとしていた。\r\n"
						+ "\r\n"
						+ "　コロナウイルスが蔓延し、世界中に大打撃を与えてからもうすぐ１年も過ぎようというのに、この大狂乱は落ち着く気配もなかった。恵介は研修医として、今日は休日の救急外来当直だったが、お昼すぎに重症のCOVID-19患者が搬送されてきてからまさに戦場と化していた。\r\n"
						+ "\r\n"
						+ "　赤信号に引っかかり、恵介は自転車のブレーキを掛ける。ここの信号は長い。ポケットからスマホを取り出すと、LINEの画面をつける。宣伝アカウントから通知が数件来ている以外は特にメッセージの受信はなかった。トーク相手の選択画面で一番上にピンどめされているのは、付き合って３年以上になる彼女の由佳だった。１行だけ表示されている昨日のやりとりの最後。\r\n"
						+ "\r\n"
						+ "『分かったよ。どうせ私がわがままなだけなんだ』\r\n"
						+ "\r\n"
						+ "喧嘩したままぶつ切りになっていた。スマホを持つ手に力が入る。あんな喧嘩だってしたかったわけじゃないのだけど、このところ由佳とは何となくすれ違ってばかりいると、恵介は感じていた。信号が青に変わる。重いペダルをゆっくりと漕ぎ始める。\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "　院内感染を防ぐために旅行はもちろん禁止、飲み会やカラオケはもちろんのこと、普通の飲食店でも２人以上での食事は病院規約で厳しく規制されていた。帰省が概ね許可されるようになったのも去年の年末頃にようやくだった。そんな調子だったから、由佳と会う機会も自然と減っていた。まだ学生の由佳は、大学がほとんど授業がリモートなこともあり感染リスクの上がるような行動はできる限り避けていた。でもまがりなりにも医師として、家族でない人と接触してCOVID-19を発症するのは良くないし、何より今日のように突然COVID-19患者が運ばれるような救急外来も受け持つ恵介としては、自分という最大のリスク因子を由佳に近づけたくなかった。\r\n"
						+ "\r\n"
						+ "　それにしても、と恵介は思い返す。もう随分由佳の声を聞いていない。もちろん電話は出来るけれど、日々の仕事に追われてしまい最近は帰ると１０時過ぎなんてこともざらで、それから由佳に電話をかける元気がない日も多かった。LINEの文面だけでやり取りすることが増えるにつれ、何となく会話がぎこちないと感じることが増えていた。\r\n"
						+ "\r\n"
						+ "　昔は由佳の考えていることは大体分かったし、そのことは恵介自身のちょっとした自慢でもあった。由佳が素直で良い子なのは間違いなかったけど、それでも直感のような、あるいは波長が自然と合うような、そんな感覚があった。でも最近はそれが薄れているのを感じていた。昨日だって何がきっかけとなったのか分からないうちに、LINEのやり取りは険悪になっていった。\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "　家の前に自転車を止め、3階まで重たい体を引きずって階段を上る。部屋の鍵を回し、ゆっくりと玄関扉を開けると、廊下と部屋を隔てる扉にはめられたガラスの向こうが明るいことに気がついた。\r\n"
						+ "\r\n"
						+ "「え、電気は消したはず……」\r\n"
						+ "\r\n"
						+ "泥棒か？と身構えた数秒後に、その扉をバタンと開けて部屋着姿の由佳が顔を出した。5mの廊下の端と端で次の言葉につまったままの二人。\r\n"
						+ "\r\n"
						+ "「……ごめんね。来ちゃいけないとは思ってたんだけど、どうしても……我慢できなくて」\r\n"
						+ "\r\n"
						+ "やがて由佳は、申し訳無さそうに小さくそう言った。\r\n"
						+ "\r\n"
						+ "「あ、大丈夫だよ部屋に入る前に服は全部着替えたし、手洗いとかもしっかりしたし、とりあえず家の中でも新しいマスクつけたけども……って恵ちゃん、どうしたの？！」\r\n"
						+ "\r\n"
						+ "どうしたのと言われてようやく、恵介は自分が泣いていることに気づいた。久しぶりに見た由佳の顔にほっとしたのか、口元がほころぶのを止められないのに、目からはぼろぼろと涙がこぼれ続けた。\r\n"
						+ "\r\n"
						+ "「恵ちゃん大丈夫？」\r\n"
						+ "\r\n"
						+ "思わず駆け寄りそうになる由佳に恵介は手を突き出して、無言で近づくなのアピールをした。\r\n"
						+ "\r\n"
						+ "「俺、病院から帰ってきたばかりだから。とりあえず風呂に入って、服の洗濯だけしちゃうよ。そしたらゆっくり話そう」\r\n"
						+ "\r\n"
						+ "そう、久々にゆっくりと。\r\n"
						+ "\r\n"
						+ "「会いたかったよ、由佳」\r\n"
						+ "\r\n"
						+ "恵介の言葉に由佳はうんとうなずく。\r\n"
						+ "\r\n"
						+ "「わたしも、恵ちゃん。じゃあ向こうでおとなしく待ってるね」\r\n"
						+ "\r\n"
						+ "そう言って部屋に戻ろうとした由佳は足を止め、くるりと振り返りながら言った。\r\n"
						+ "\r\n"
						+ "「恵ちゃん、おかえり」\r\n"
						+ "\r\n"
						+ "その笑顔に恵介は救われたような気持ちになった。毎日無人の家に帰る寂しさ。擦り切れそうな日々。\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "　『おかえり』。それがこんなに温かい言葉だったと恵介は噛み締めていた。");

		// 5秒待つ 
		Thread.sleep(5000);
		scrollTo(450);

		suffix = "02_報告内容を入力する(所感・一週間の振り返りが2000文字超)";

		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());

		WebElement weeklyreportsubmitBtn = WebDriverUtils.getWeeklyReportSubmitBtn();
		weeklyreportsubmitBtn.click();

		// 5秒待つ 
		scrollTo(450);
		Thread.sleep(5000);

		suffix = "03_報告内容を提出する(所感・一週間の振り返りが2000文字超)";
		scrollTo(450);
		Thread.sleep(5000);
		getEvidence(instance, suffix);

		assertEquals("レポート登録 | LMS", getTitle());
	}

}
