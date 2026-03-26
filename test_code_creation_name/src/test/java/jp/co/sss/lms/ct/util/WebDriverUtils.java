package jp.co.sss.lms.ct.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
//import com.amazonaws.services.dynamodbv2.model.Select;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

/**
 * Webドライバーユーティリティ
 * @author holy
 */
public class WebDriverUtils {

	/** Webドライバ */
	public static WebDriver webDriver;

	/**
	 * インスタンス取得
	 * @return Webドライバ
	 */
	public static void createDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
		webDriver = new ChromeDriver();
	}

	/**
	 * インスタンス終了
	 */
	public static void closeDriver() {
		webDriver.quit();
	}

	/**
	 * 画面遷移
	 * @param url
	 */
	public static void goTo(String url) {
		webDriver.get(url);
		pageLoadTimeout(5);
	}

	/**
	 * ページロードタイムアウト設定
	 * @param second
	 */
	public static void pageLoadTimeout(int second) {
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(second));
	}

	/**
	 * 要素の可視性タイムアウト設定
	 * @param locater
	 * @param second
	 */
	public static void visibilityTimeout(By locater, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locater));
	}

	/**
	 * 指定ピクセル分だけスクロール
	 * @param pixel
	 */
	public static void scrollBy(int pixel) {
		//((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
		((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
	}

	/**
	 * 指定位置までスクロール
	 * @param pixel
	 */
	public static void scrollTo(int pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + pixel + ");");
	}

	/**
	 * エビデンス取得
	 * @param instance
	 */
	public static void getEvidence(Object instance) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getSimpleName();
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エビデンス取得（サフィックスあり）
	 * @param instance
	 * @param suffix
	 */
	public static void getEvidence(Object instance, String suffix) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getSimpleName();
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			Files.move(tempFile, new File("evidence\\" + className + "_" + methodName + "_" + suffix + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * タイトル取得
	 * @return 
	 */
	public static String getTitle() {
		String title = webDriver.getTitle();
		return title;
	}

	/**
	 * ログインID取得
	 */
	public static WebElement getUserName() {
		WebElement username = webDriver.findElement(By.id("loginId"));
		return username;
	}

	/**
	 * パスワード取得
	 */
	public static WebElement getPassword() {
		WebElement password = webDriver.findElement(By.id("password"));
		return password;
	}

	/**
	 * ログインボタン取得
	 */
	public static WebElement getLoginBtn() {
		WebElement loginBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return loginBtn;
	}

	/**
	 * 機能タブ取得
	 */
	public static WebElement getFunctionTab() {
		WebElement functionTab = webDriver.findElement(By.cssSelector(".dropdown-toggle"));
		return functionTab;
	}

	/**
	 * ヘルプリンク取得
	 */
	public static WebElement getHelpLink() {
		WebElement helpLink = webDriver.findElement(By.linkText("ヘルプ"));
		return helpLink;
	}

	/**
	 * よくある質問リンク取得
	 */
	public static WebElement getFAQ() {
		WebElement FAQ = webDriver.findElement(By.linkText("よくある質問"));
		return FAQ;
	}

	/**
	 * キーワード検索取得
	 */
	public static WebElement getKeyword() {
		WebElement keyword = webDriver.findElement(By.cssSelector(".form-control"));
		return keyword;
	}

	/**
	 * 検索ボタン取得
	 */
	public static WebElement getSearchBtn() {
		WebElement searchBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return searchBtn;
	}

	/**
	 * クリアボタン取得
	 */
	public static void getClearBtn() {

		webDriver.findElement(By.xpath("//input[@value='クリア']")).click();
	}

	/**
	 * カテゴリー検索の「【研修関係】」情報取得
	 */
	public static WebElement getTrainingRelated() {
		WebElement trainingRelated = webDriver.findElement(By.partialLinkText("【研修関係】"));
		return trainingRelated;
	}

	/**
	 * 「キャンセル料・途中退校について」情報取得
	 */
	public static WebElement getSearchResultFAQ() {
		WebElement searchresultFAQ = webDriver.findElement(By.xpath("//span[text()='キャンセル料・途中退校について']"));
		return searchresultFAQ;
	}

	/**
	 * 「未提出について」情報取得 ★使用している
	 */
	public static WebElement getNotSubmitted() {
		List<WebElement> elements = webDriver.findElements(By.xpath("//table/tbody"));

		int count = 1;
		for (WebElement els : elements) {
			//int els2 = count;
			String innerText = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[3]")).getText(); // テキストを取得
			String innerText2 = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[2]")).getText(); // テキストを取得
			if (innerText.equals("未提出")) {
				WebElement notSubmitted = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[5]"));
				//notSubmitted.click();

				return notSubmitted;
			}
			count++;
		}

		//		String innerText = webDriver.findElement(By.xpath("//table/tbody/tr[1]/td[1]")).getText();
		//		String innerText2 = webDriver.findElement(By.xpath("//table/tbody/tr[1]/td[2]")).getText();
		//		String innerText3 = webDriver.findElement(By.xpath("//table/tbody/tr[1]/td[3]")).getText();
		//		String innerText4 = webDriver.findElement(By.xpath("//table/tbody/tr[1]/td[4]")).getText();
		//		String innerText5 = webDriver.findElement(By.xpath("//table/tbody/tr[1]/td[5]")).getText();
		//		String innerText6 = webDriver.findElement(By.xpath("//table/tbody/tr[2]/td[1]")).getText();
		//		String innerText7 = webDriver.findElement(By.xpath("//table/tbody/tr[3]/td[1]")).getText();
		//
		//		for (WebElement els : elements) {
		//			String mozi = els.getText(); // テキストを取得
		//			if (mozi == "未提出") {
		//				//WebElement count = els;
		//				//return count;
		//				if (elements2.size() > 1) {
		//					WebElement notSubmitted = elements2.get(0); // 0が最初の要素、1が2番目の要素
		//					// secondElementに対して操作を行う
		//					return notSubmitted;
		//				}
		//			}
		//		}

		return null;
	}

	/**
	 * 「試験有」情報取得 ★使用している
	 */
	public static WebElement getExam() {
		List<WebElement> elements = webDriver.findElements(By.xpath("//table/tbody"));

		int count = 1;
		for (WebElement els : elements) {
			String innerText = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[4]")).getText(); // テキストを取得
			//String innerText2 = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[2]")).getText(); // テキストを取得
			if (innerText.equals("試験有")) {
				WebElement exam = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[5]"));
				//notSubmitted.click();

				return exam;
			}
			count++;
		}
		return null;
	}

	/**
	 * セクション詳細画面の詳細情報取得 ★使用している
	 */
	public static WebElement getExamDetailBtn() {
		WebElement examdetailBtn = webDriver
				.findElement(By.xpath("//input[@value='詳細']"));
		return examdetailBtn;
	}

	/**
	 * 試験開始画面の試験開始情報取得 ★使用している
	 */
	public static WebElement getExamStartBtn() {
		WebElement examstartBtn = webDriver
				.findElement(By.xpath("//input[@value='試験を開始する']"));
		return examstartBtn;
	}

	/**
	 * 画面の下までスクロール
	 */
	public static void getDisplayDown() {

		// JavaScriptExecutorを取得
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		// ページ最下部までスクロール
		// ページ最下部までスクロール
		js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		// 最下部前スクロールしたければPAGE_DOWNを繰り返す
		//webDriver.findElement(By.tagName("body")).click();
		//webDriver.findElement(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
	}

	/**
	 * 試験画面の問題情報取得
	 * @throws InterruptedException 
	 */
	public static void getExamQuestion() throws InterruptedException {
		//List<WebElement> elements = webDriver.findElements(By.xpath("//table[3]/tbody/tr"));
		//List<WebElement> elements = webDriver.findElements(By.xpath("//form/div[1]"));

		//String innerText = webDriver.findElement(By.xpath("//form/div[1]/div/ul/li[3]")).getText();
		//WebElement examquestion = webDriver
		//		.findElement(By.xpath("//form/div[1]/div/ul/li[3]"));
		//examquestion.click();

		//		int questionselect[] = new int[12];
		//		questionselect[0] = 3;
		//		questionselect[1] = 3;
		//		questionselect[2] = 1;
		//		questionselect[3] = 1;
		//		questionselect[4] = 2;
		//		questionselect[5] = 2;
		//		questionselect[6] = 4;
		//		questionselect[7] = 3;
		//		questionselect[8] = 2;
		//		questionselect[9] = 2;
		//		questionselect[10] = 2;
		//		questionselect[11] = 3;

		//全問正解
		//int[] questionanswer = { 3, 3, 1, 1, 2, 2, 4, 3, 2, 2, 2, 3 };
		//正答と誤答が半々
		int[] questionanswer = { 3, 3, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1 };
		//List<WebElement> elements = webDriver.findElements(By.xpath("//form/div"));
		//String innerText = webDriver.findElement(By.xpath("//form/div")).getText();
		int count = 0;
		int options = 0;

		for (int i = 0; i < questionanswer.length; i++) {
			scrollTo(options);
			//Thread.sleep(3000);
			int mozi = count + 1;
			int mozi2 = questionanswer[count];
			WebElement examquestion = webDriver
					.findElement(By.xpath("//form/div[" + mozi + "]/div/ul/li[" + mozi2 + "]/input"));
			examquestion.click();
			Thread.sleep(3000);

			count++;
			//options = options + 400;
			//options = options + 380;
			options = options + 370;
		}

		//		scrollTo(0);
		//		Thread.sleep(3000);
		//		int mozi = questionanswer[0];
		//		//String innerText = webDriver.findElement(By.xpath("//form/div[1]/div/ul/li[ " + mozi + " ]")).getText();
		//		WebElement examquestion = webDriver.findElement(By.xpath("//form/div[1]/div/ul/li[ " + mozi + " ]/input"));
		//		examquestion.click();
		//
		//		scrollTo(400);
		//		Thread.sleep(3000);
		//		int mozi2 = questionanswer[1];
		//		//String innerText2 = webDriver.findElement(By.xpath("//form/div[2]/div/ul/li[ " + mozi2 + " ]")).getText();
		//		WebElement examquestion2 = webDriver.findElement(By.xpath("//form/div[2]/div/ul/li[ " + mozi2 + " ]/input"));
		//		examquestion2.click();
		//
		//		scrollTo(800);
		//		Thread.sleep(3000);
		//		int mozi3 = questionanswer[2];
		//		//String innerText3 = webDriver.findElement(By.xpath("//form/div[3]/div/ul/li[ " + mozi3 + " ]")).getText();
		//		WebElement examquestion3 = webDriver.findElement(By.xpath("//form/div[3]/div/ul/li[ " + mozi3 + " ]/input"));
		//		examquestion3.click();

		//return examquestion;

		//int count = 2;
		//		for (WebElement els : elements) {
		//			String innerText = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[1]")).getText(); // テキストを取得
		//			String innerText2 = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[2]")).getText(); // テキストを取得
		//			if (innerText.equals(reportdate) && innerText2.equals(reportname)) {
		//				WebElement applicablereportcorrectionBtn = webDriver
		//						.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[5]/form[2]"));
		//				return applicablereportcorrectionBtn;
		//			}
		//			count++;
		//		}
		//		return null;
	}

	/**
	 * 試験画面の試験確認情報取得 ★使用している
	 */
	public static WebElement getExamConfirmationBtn() {
		WebElement examconfirmationBtn = webDriver
				.findElement(By.xpath("//input[@value='確認画面へ進む']"));
		return examconfirmationBtn;
	}

	/**
	 * 試験確認画面の試験回答送信情報取得 ★使用している
	 */
	public static void getExamAnswerSubmitBtn() {
		//WebElement examanswersubmitBtn = webDriver.findElement(By.id("sendButton"));
		webDriver.findElement(By.id("sendButton")).click();

		//アラートが出るまで待つ
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.alertIsPresent());

		//アラートに切り替える
		Alert alert = webDriver.switchTo().alert();

		//OKをクリック
		alert.accept();

		//return examanswersubmitBtn;
	}

	/**
	 * 試験結果画面の戻る情報取得 ★使用している
	 */
	public static WebElement getExamBackBtn() {
		WebElement exambackBtn = webDriver
				.findElement(By.xpath("//input[@value='戻る']"));
		return exambackBtn;
	}

	/**
	 * 「提出について」情報取得 ★使用している
	 */
	public static WebElement getSubmitted() {
		List<WebElement> elements = webDriver.findElements(By.xpath("//table/tbody"));
		int count = 1;
		for (WebElement els : elements) {
			String innerText = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[3]")).getText(); // テキストを取得
			String innerText2 = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[2]")).getText(); // テキストを取得
			if (innerText.equals("提出済み")) {
				WebElement Submitted = webDriver.findElement(By.xpath("//table/tbody/tr[ " + count + "]/td[5]"));
				//Submitted.click();
				return Submitted;
			}
			count++;
		}
		return null;
	}

	/**
	 * 「未提出について」情報取得 ★使用していない
	 */
	public static WebElement getDetailBtn() {
		//WebElement notSubmitted = webDriver.findElement(By.xpath("//span[text()='未提出']"));
		//WebElement detailBtn = webDriver
		//		.findElement(By.xpath("//input[@value='詳細']"));

		List<WebElement> elements = webDriver.findElements(By.xpath("//*[@class='btn btn-default']"));
		if (elements.size() > 1) {
			WebElement detailBtn = elements.get(3); // 0が最初の要素、1が2番目の要素
			// secondElementに対して操作を行う
			return detailBtn;
		}

		return null;
	}

	/**
	 * 「日報【デモ】を提出する」情報取得
	 */
	public static WebElement getDailyReportBtn() {
		WebElement dailyreportBtn = webDriver
				.findElement(By.xpath("//input[@value='日報【デモ】を提出する']"));
		//WebElement dailyreportBtn = webDriver
		//		.findElement(By.xpath("//input[@value='提出済み日報【デモ】を確認する']"));

		return dailyreportBtn;
	}

	/**
	 * 「提出済み日報【デモ】を確認する」情報取得
	 */
	public static WebElement getDailyReportSubmittedBtn() {
		WebElement dailyreportsubmittedBtn = webDriver
				.findElement(By.xpath("//input[@value='提出済み日報【デモ】を確認する']"));
		//WebElement dailyreportBtn = webDriver
		//		.findElement(By.xpath("//input[@value='提出済み日報【デモ】を確認する']"));

		return dailyreportsubmittedBtn;
	}

	/**
	 * 報告内容情報取得
	 */
	public static WebElement getReportContent() {
		WebElement reportContent = webDriver.findElement(By.id("content_0"));
		return reportContent;
	}

	/**
	 * 報告内容提出ボタン取得
	 */
	public static WebElement getSubmitBtn() {
		WebElement submitBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return submitBtn;
	}

	/**
	 * 「ようこそ受験生○○さん」情報取得
	 */
	public static WebElement getWelcomeExaminee(String examinee) {
		//WebElement welcomeexaminee = webDriver.findElement(By.partialLinkText("ようこそ受講生ＡＡ１さん"));
		WebElement welcomeexaminee = webDriver.findElement(By.partialLinkText(examinee));
		return welcomeexaminee;
	}

	/**
	 * 該当レポート情報取得(詳細ボタン) ★使用している
	 */
	public static WebElement getApplicableReportDetailBtn(String reportdate) {
		//List<WebElement> elements = webDriver.findElements(By.xpath("//table[3]/tbody"));
		List<WebElement> elements = webDriver.findElements(By.xpath("//table[3]/tbody/tr"));
		int count = 2;
		//		int kazu1 = webDriver.findElement(By.xpath("//table[1]/tbody")).findElements(By.tagName("tr")).size();
		//		int kazu2 = webDriver.findElement(By.xpath("//table[2]/tbody")).findElements(By.tagName("tr")).size();
		//		int kazu3 = webDriver.findElement(By.xpath("//table[3]/tbody")).findElements(By.tagName("tr")).size();
		for (WebElement els : elements) {
			//String innerText0 = webDriver.findElement(By.xpath("//table[3]/tbody/tr[2]/td[1]")).getText();
			String innerText = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[1]")).getText(); // テキストを取得
			//String innerText2 = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[4]")).getText(); // テキストを取得
			if (innerText.equals(reportdate)) {
				//if (innerText.equals("2022年10月5日(水)")) {
				//WebElement applicablereport = webDriver
				//		.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[5]"));
				WebElement applicablereportdetail = webDriver
						.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[5]/form[1]"));
				//Submitted.click();
				return applicablereportdetail;
			}
			count++;
		}
		return null;
	}

	/**
	 * 該当レポート情報取得(修正するボタン) ★使用している
	 */
	public static WebElement getApplicableReportCorrectionBtn(String reportdate, String reportname) {
		List<WebElement> elements = webDriver.findElements(By.xpath("//table[3]/tbody/tr"));
		int count = 2;
		for (WebElement els : elements) {
			String innerText = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[1]")).getText(); // テキストを取得
			String innerText2 = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[2]")).getText(); // テキストを取得
			if (innerText.equals(reportdate) && innerText2.equals(reportname)) {
				WebElement applicablereportcorrectionBtn = webDriver
						.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[5]/form[2]"));
				return applicablereportcorrectionBtn;
			}
			count++;
		}
		return null;
	}

	/**
	 * 学習サイズmax情報取得
	 */
	public static void getDisplayMax() {
		webDriver.manage().window().maximize();
	}

	/**
	 * 学習項目情報取得
	 */
	public static WebElement getLearningTopic() {
		WebElement learningtopic = webDriver.findElement(By.id("intFieldName_0"));
		//webDriver.manage().window().maximize();
		return learningtopic;
	}

	/**
	 * 理解度の達成度情報取得
	 */
	public static Select getComprehensionLevel() {
		//WebElement element = webDriver.findElement(By.xpath("xpath"));
		//Select(element).selectByValue("value"); // valueの値

		//Select drpCountry = new Select(webDriver.findElement(By.name("country")));
		//drpCountry.selectByVisibleText("ANTARCTICA");

		//WebElement comprehensionlevel = webDriver.findElement(By.xpath("xpath"));
		//Select(comprehensionlevel).selectByValue("value");

		//Select comprehensionlevel = webDriver.findElement(By.id("intFieldValue_0"));
		Select comprehensionlevel = new Select(webDriver.findElement(By.id("intFieldValue_0")));

		//Select select = new Select(comprehensionlevel);
		//Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		//select.selectByValue("3");

		//comprehensionlevel.selectByValue("3");

		return comprehensionlevel;
	}

	/**
	 * 目標の達成度情報取得
	 */
	public static WebElement getGoalAchievement() {
		WebElement goalachievement = webDriver.findElement(By.id("content_0"));
		return goalachievement;
	}

	/**
	 * 所感情報取得
	 */
	public static WebElement getImpressions() {
		WebElement impressions = webDriver.findElement(By.id("content_1"));
		return impressions;
	}

	/**
	 * 一週間の振り返り情報取得
	 */
	public static WebElement getReviewOfTheWeek() {
		WebElement reviewoftheweek = webDriver.findElement(By.id("content_2"));
		return reviewoftheweek;
	}

	/**
	 * 週報レポートの提出ボタン取得
	 */
	public static WebElement getWeeklyReportSubmitBtn() {
		WebElement weeklyreportsubmitBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return weeklyreportsubmitBtn;
	}

	/**
	 * 次へボタン取得
	 */
	public static WebElement getNextBtn() {
		WebElement nextBtn = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		return nextBtn;
	}

	/**
	 * セキュリティ規約エラーメッセージ取得
	 */
	public static String getSecurityConsentErorrMessage() {

		WebElement element = webDriver.findElement(By.xpath("//div[text()='セキュリティ規約への同意は必須です。']"));
		String securityconsentErorrMessage = element.getText(); // テキストを取得
		return securityconsentErorrMessage;
	}

	/**
	 * ラジオボタン取得(非選択)
	 */
	public static WebElement getRadioNegativeBtn() {
		//WebElement radioBtn = webDriver.findElement(By.xpath("//input[value()='1']"));
		WebElement radionegativeBtn = webDriver.findElement(By.name("securityFlg"));

		//radionegativeBtn.click();
		Boolean isSelected = radionegativeBtn.isSelected();
		if (isSelected) {
			return null;
		}
		return radionegativeBtn;
	}

	/**
	 * ラジオボタン取得(選択)
	 */
	public static WebElement getRadioAcitveBtn() {
		//WebElement radioBtn = webDriver.findElement(By.xpath("//input[value()='1']"));
		WebElement radioacitveBtn = webDriver.findElement(By.name("securityFlg"));

		radioacitveBtn.click();
		Boolean isSelected = radioacitveBtn.isSelected();
		if (isSelected) {
			return radioacitveBtn;
		}

		return null;
	}

	/**
	 * 変更ボタン情報取得
	 */
	public static WebElement getChangeBtn() {
		List<WebElement> elements = webDriver.findElements(By.xpath("//*[@class='btn btn-primary']"));
		if (elements.size() > 1) {
			WebElement changeBtn = elements.get(1); // 0が最初の要素、1が2番目の要素
			// secondElementに対して操作を行う
			return changeBtn;
		}

		return null;
	}

	/**
	 * モーダル表示の変更ボタン情報取得
	 */
	public static WebElement getChangeModalBtn() {
		//		List<WebElement> elements = webDriver.findElements(By.xpath("//*[@class='btn btn-primary']"));
		//		if (elements.size() > 1) {
		//			WebElement changeBtn = elements.get(1); // 0が最初の要素、1が2番目の要素
		//			// secondElementに対して操作を行う
		//			return changeBtn;
		//		}
		//
		//		return null;

		WebElement changemodalBtn = webDriver.findElement(By.id("upd-btn"));
		return changemodalBtn;

	}

	/**
	 * 現在のパスワード取得
	 */
	public static WebElement getCurrentPassword() {
		WebElement currentpassword = webDriver.findElement(By.id("currentPassword"));
		return currentpassword;
	}

	/**
	 * 新しいパスワード取得
	 */
	public static WebElement getNewPassword() {
		WebElement newpassword = webDriver.findElement(By.id("password"));
		return newpassword;
	}

	/**
	 * 確認パスワード取得
	 */
	public static WebElement getCheckPassword() {
		WebElement checkpassword = webDriver.findElement(By.id("passwordConfirm"));
		return checkpassword;
	}

	/**
	 * 長いパスワードエラーメッセージ取得
	 */
	public static String getLongPasswordErorrMessage() {

		WebElement element = webDriver.findElement(By.xpath("//span[text()='パスワードの長さが最大値(20)を超えています。']"));
		String longpasswordErorrMessage = element.getText(); // テキストを取得
		return longpasswordErorrMessage;
	}

	/**
	 * ポリシーに合わないパスワードエラーメッセージ取得
	 */
	public static String getNotMatchPolicyErorrMessage() {

		WebElement element = webDriver.findElement(
				By.xpath("//span[text()='「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。']"));
		String notmatchpolicyErorrMessage = element.getText(); // テキストを取得
		return notmatchpolicyErorrMessage;
	}

	/**
	 * 一致しない確認パスワードエラーメッセージ取得
	 */
	public static String getNotMatchCheckErorrMessage() {

		WebElement element = webDriver.findElement(
				By.xpath("//span[text()='パスワードは英大文字、英小文字、数字の3文字種を混合させた8文字以上を入力してください。']"));
		String notmatchCheckErorrMessage = element.getText(); // テキストを取得
		return notmatchCheckErorrMessage;
	}

	/**
	 * 勤怠タブ取得
	 */
	public static WebElement getAttendance() {
		WebElement attendance = webDriver.findElement(By.linkText("勤怠"));
		return attendance;
	}

	/**
	 * 打刻の出勤情報取得
	 */
	public static void getAttendanceAtWorkBtn() {
		//WebElement attendanceatworkBtn = webDriver.findElement(By.name("punchIn"));

		//return attendanceatworkBtn;
		//WebElement examanswersubmitBtn = webDriver.findElement(By.id("sendButton"));
		webDriver.findElement(By.name("punchIn")).click();

		//アラートが出るまで待つ
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.alertIsPresent());

		//アラートに切り替える
		Alert alert = webDriver.switchTo().alert();

		//OKをクリック
		alert.accept();

	}

	/**
	 * 打刻の退勤情報取得
	 */
	public static void getLeavingWorkBtn() {
		//WebElement leavingworkBtn = webDriver.findElement(By.name("punchOut"));
		//return leavingworkBtn;
		webDriver.findElement(By.name("punchOut")).click();

		//アラートが出るまで待つ
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.alertIsPresent());

		//アラートに切り替える
		Alert alert = webDriver.switchTo().alert();

		//OKをクリック
		alert.accept();
	}

	/**
	 * 勤怠情報を直接編集するリンク取得
	 */
	public static WebElement getAttendanceDirectEditing() {
		WebElement attendancedirectediting = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		return attendancedirectediting;
	}

	/**
	 * 勤怠情報を更新する ★使用している
	 */
	public static void getAttendanceUpdateBtn() {
		//WebElement attendanceupdateBtn = webDriver
		//		.findElement(By.xpath("//input[@value='更新']"));
		webDriver.findElement(By.xpath("//input[@value='更新']")).click();
		//アラートが出るまで待つ
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.alertIsPresent());

		//アラートに切り替える
		Alert alert = webDriver.switchTo().alert();

		//OKをクリック
		alert.accept();
		//return attendanceupdateBtn;
	}

	/**
	 * 出勤の時情報を直接編集するリンク取得
	 */
	public static Select getAttendanceAtWorkHour() {
		Select attendanceatworkhour = new Select(webDriver.findElement(By.id("startHour0")));
		return attendanceatworkhour;
	}

	/**
	 * 出勤の分情報を直接編集するリンク取得
	 */
	public static Select getAttendanceAtWorkMinutes() {
		Select attendanceatworkminutes = new Select(webDriver.findElement(By.id("startMinute0")));
		return attendanceatworkminutes;
	}

	/**
	 * 退勤の時情報を直接編集するリンク取得
	 */
	public static Select getLeavingWorkHour() {
		Select leavingworkHour = new Select(webDriver.findElement(By.id("endHour0")));
		return leavingworkHour;
	}

	/**
	 * 退勤の分情報を直接編集するリンク取得
	 */
	public static Select getLeavingWorkMinutes() {
		Select leavingworkMinutes = new Select(webDriver.findElement(By.id("endMinute0")));
		return leavingworkMinutes;
	}

	/**
	 * 中抜け情報を直接編集するリンク取得
	 */
	public static Select getStepOut() {
		//Select stepout = new Select(webDriver.findElement(By.id("endMinute0")));
		//String innerText = webDriver.findElement(By.xpath("//table[3]/tbody/tr[ " + count + "]/td[1]")).getText(); // テキストを取得

		Select stepout = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		return stepout;
	}

	/**
	 * 備考情報を直接編集するリンク取得
	 */
	public static WebElement getRemarks() {
		//Select remarks = new Select(webDriver.findElement(By.name("attendanceList[0].note")));
		WebElement remarks = webDriver.findElement(By.name("attendanceList[0].note"));
		return remarks;
	}

	/**
	 * 出勤時間エラーメッセージ取得
	 */
	public static String getAttendanceAtWorkErorrMessage() {

		//WebElement element = webDriver.findElement(By.xpath("//span[text()='*&nbsp;出勤時間が正しく入力されていません。']"));
		//WebElement element = webDriver.findElement(By.xpath("//span[text()='* 出勤時間が正しく入力されていません。']"));
		//WebElement element = webDriver.findElement(By.xpath("//span[text()='* 出勤時間が正しく入力されていません。']"));

		//WebElement element = webDriver.findElement(By.xpath("//span[normalize-space(text())='出勤時間が正しく入力されていません。']"));
		//WebElement element = webDriver.findElement(By.xpath("//span"));
		WebElement element = webDriver.findElement(By.cssSelector(".help-inline.error"));

		String attendanceatworkErorrMessage = element.getText(); // テキストを取得
		return attendanceatworkErorrMessage;
	}

	/**
	 * 出勤時間エラーメッセージ取得
	 */
	public static String getAttendanceAtWork2ErorrMessage() {
		WebElement element = webDriver.findElement(By.cssSelector(".help-inline.error"));
		String attendanceatworkErorrMessage = element.getText(); // テキストを取得
		return attendanceatworkErorrMessage;
	}

	/**
	 * 出勤時間エラーメッセージ取得
	 */
	public static String getAttendanceAtWork3ErorrMessage() {
		WebElement element = webDriver.findElement(By.cssSelector(".help-inline.error"));
		String attendanceatworkErorrMessage = element.getText(); // テキストを取得
		return attendanceatworkErorrMessage;
	}

	/**
	 * 出勤時間エラーメッセージ取得
	 */
	public static String getAttendanceAtWork4ErorrMessage() {
		WebElement element = webDriver.findElement(By.cssSelector(".help-inline.error"));
		String attendanceatworkErorrMessage = element.getText(); // テキストを取得
		return attendanceatworkErorrMessage;
	}

	/**
	 * 出勤時間エラーメッセージ取得
	 */
	public static String getAttendanceAtWork5ErorrMessage() {
		WebElement element = webDriver.findElement(By.cssSelector(".help-inline.error"));
		String attendanceatworkErorrMessage = element.getText(); // テキストを取得
		return attendanceatworkErorrMessage;
	}

}
