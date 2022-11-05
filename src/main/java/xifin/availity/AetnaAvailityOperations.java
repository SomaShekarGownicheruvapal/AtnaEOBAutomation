package xifin.availity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.hotkey.Keys;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import org.sikuli.script.TextRecognizer;

public class AetnaAvailityOperations extends Base {
	static String DOB;

	static String[] flnames;

	static boolean maximized = true;

	public static String availityErrorMessage;

	Keyboard keyboard = (Keyboard) new DesktopKeyboard();

	static Screen screen = new Screen();

	Screen screenImage = new Screen();

	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	WebDriverWait wait = new WebDriverWait(driver, 20L);

	static String trimmedFName = null;

	static String trimmedLName = null;

	ArrayList<String> tabs;

	private static final Logger logger = LogManager.getLogger(AetnaAvailityOperations.class.getName());

	public static void waitForElement(String target, float scanrate, float maxtimeout)
			throws FindFailed, InterruptedException {
		try {
			screen.setWaitScanRate(scanrate);
			screen.wait(target, maxtimeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage getBufferedImageFromImage(Image image) {
		Image temp = (new ImageIcon(image)).getImage();
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), 1);
		Graphics g = bufferedImage.createGraphics();
		g.drawImage(temp, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	public void openXifin(String vdiUsername, String vdiPassword, String xifinUsername, String xifinPassword)
			throws Exception {
		open(getPropertyValue("xifinurl"));
		sleep(10000);
		wait.until((Function) ExpectedConditions.visibilityOfElementLocated(By.id("acceptDisclaimerBtn")));
		driver.findElement(By.id("acceptDisclaimerBtn")).click();
		sleep(5000);
		driver.findElement(By.id("username")).sendKeys(new CharSequence[] { vdiUsername });
		driver.findElement(By.id("password")).sendKeys(new CharSequence[] { vdiPassword });
		driver.findElement(By.id("loginButton")).click();
		sleep(8000);
		try {
			while (true) {
				driver.findElement(By.className("ui-desktop-icon")).click();
				sleep(15000);
				screen.click(new Pattern(inputFilePath + "error_tharpool.png"));
				sleep(5000);
			}
		} catch (Exception e) {
			try {
				while (true) {
					driver.findElement(By.className("ui-desktop-icon")).click();
					sleep(15000);
					screen.click(new Pattern(inputFilePath + "disconnect.png"));
					sleep(5000);
				}
			} catch (Exception exception) {
				try {
					while (true) {
						driver.findElement(By.className("ui-desktop-icon")).click();
						sleep(15000);
						screen.click(new Pattern(inputFilePath + "disconnect_close.png"));
						sleep(5000);
					}
				} catch (Exception exception1) {
					try {
						Pattern ok = new Pattern(inputFilePath + "xifin_windows_ok.png");
						screen.click(ok);
						sleep(2000);
						Pattern xifinSso = new Pattern(inputFilePath + "xifin_sso.png");
						while (true) {
							try {
								screen.doubleClick(xifinSso);
								maximized = false;
								sleep(10000);
								break;
							} catch (Exception exception2) {
								sleep(10000);
							}
						}
					} catch (Exception exception2) {
					}
					try {
						screen.click(new Pattern(inputFilePath + "information_message.png"));
						sleep(1000);
						screen.click(new Pattern(inputFilePath + "information_message.png"));
					} catch (Exception exception2) {
					}
					try {
						Pattern xifinSso = new Pattern(inputFilePath + "xifin_sso.png");
						screen.doubleClick(xifinSso);
						sleep(5000);
					} catch (Exception exception2) {
					}
					Utils.f11();
					sleep(3000);
					try {
						Utils.backspace();
						keyboard.type(xifinUsername);
						Utils.tab();
						Utils.backspace();
						sleep(2000);
						keyboard.type(xifinPassword);
						Utils.tab();
					} catch (Exception exception2) {
					}
					try {
						Utils.enter();
					} catch (Exception exception2) {
					}
					sleep(5000);
					try {
						screen.click(new Pattern(inputFilePath + "login_continue.png"));
						sleep(5000);
					} catch (Exception exception2) {
					}
					while (true) {
						try {
							screen.wait(new Pattern(inputFilePath + "brli.png"), 10.0D);
							screen.click(new Pattern(inputFilePath + "brli.png"));
							sleep(3000);
							break;
						} catch (Exception exception2) {
						}
					}
					screen.mouseMove(new Pattern(inputFilePath + "accerssion.png"));
					sleep(2000);
					while (true) {
						try {
							screen.wait(new Pattern(inputFilePath + "details.png"), 10.0D);
							screen.click(new Pattern(inputFilePath + "details.png"));
							sleep(3000);
							break;
						} catch (Exception exception2) {
						}
					}
					((JavascriptExecutor) driver).executeScript("window.open()", new Object[0]);
					tabs = new ArrayList<>(driver.getWindowHandles());
					driver.switchTo().window(tabs.get(1));
					return;
				}
			}
		}
	}

	public void availityLoginPatient(String insurancePortalUsername, String insurancePortalPassword)
			throws InterruptedException, IOException, FindFailed {
		driver.get(getPropertyValue("availityurl"));
		while (true) {
			try {
				sleep(2000);
				screen.click(new Pattern(inputFilePath + "availityLoginButton.png"));
				Thread.sleep(3000L);
				System.err.println("Availity Login Page apperared.");
				break;
			} catch (Exception e) {
				Utils.f5();
				sleep(10000);
			}
		}
		keyboard.type(insurancePortalUsername);
		keyboard.keyDown(Keys.TAB);
		keyboard.keyUp(Keys.TAB);
		keyboard.type(insurancePortalPassword);
		int i;
		for (i = 0; i < 4; i++) {
			keyboard.keyDown(Keys.TAB);
			keyboard.keyUp(Keys.TAB);
		}
		keyboard.keyDown(Keys.ENTER);
		keyboard.keyUp(Keys.ENTER);
		while (true) {
			try {
				screen.click(inputFilePath + "mydashboard.png");
				sleep(1000);
				System.err.println("Dash board apperared.");
				break;
			} catch (Exception e) {
			}
		}
		try {
			screen.doubleClick(new Pattern(inputFilePath + "georgia.png"));
			System.err.println("Default location Georgia already set.");
			sleep(1000);
		} catch (Exception e) {
			driver.findElement(By.xpath("//li[@position='4']")).click();
			sleep(2000);
			keyboard.type("Georgia");
			keyboard.keyDown(Keys.ENTER);
			keyboard.keyUp(Keys.ENTER);
			sleep(10000);
			wait.until((Function) ExpectedConditions.presenceOfElementLocated(By.className("img-responsive")));
			System.err.println("Element loaded. avatar- After entering location");
			screen.click(new Pattern(inputFilePath + "mydashboard.png"));
		}
		wait.until((Function) ExpectedConditions.presenceOfElementLocated(By.className("img-responsive")));
		System.err.println("Element loaded. avatar- After entering location");
		sleep(1000);
		screen.click(new Pattern(inputFilePath + "mydashboard.png"));
		sleep(1000);
		for (i = 0; i < 6; i++) {
			keyboard.keyDown(Keys.DOWN);
			keyboard.keyUp(Keys.DOWN);
			sleep(100);
			sleep(100);
		}
		sleep(1000);
		sleep(1000);
		screen.click(new Pattern(inputFilePath + "EB_orange.png"));
		sleep(1000);
		while (true) {
			try {
				sleep(5000);
				screen.click(new Pattern(inputFilePath + "new_request.png"));
				System.err.println("New Request page apperared.");
				break;
			} catch (Exception e) {
			}
		}
		System.err.println("submit clicked. Swithing to xifin portal.");
		sleep(2000);
		driver.switchTo().window(tabs.get(0));
		sleep(1000);
	}

	public String fromAccessionGetDOB(String AID) throws FindFailed, InterruptedException, IOException {
		try {
			Utils.f5();
			while (true) {
				try {
					sleep(2000);
					screen.click(new Pattern(inputFilePath + "AccessionIDblue.png"));
					sleep(2000);
					break;
				} catch (Exception e) {
					sleep(2000);
				}
			}
			keyboard.type(AID);
			System.err.println("AID is: " + AID);
			Utils.tab();
		} catch (Exception e) {
			System.err.println("Accession click failed.");
		}
		sleep(3000);
		Utils.switchToDetailView();
		while (true) {
			try {
				sleep(4000);
				screen.click(new Pattern(inputFilePath + "force_ep.png"));
				sleep(2000);
				System.err.println("force_ep clicked .. breaking while loop.");
				break;
			} catch (Exception e) {
				sleep(2000);
			}
		}
		sleep(5000);
		ScreenImage image = screenImage.capture(30, 30, 600, 450);
		sleep(1000);
		String tempFilePath = getPropertyValue("temporaryPath");
		String absPath = image.getFile(tempFilePath);
		Image img = Toolkit.getDefaultToolkit().createImage(absPath);
		BufferedImage brImage = getBufferedImageFromImage(img);
		TextRecognizer tr = TextRecognizer.start();
		sleep(1000);
		String s = tr.doOCR(brImage);
		java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}|\\d{1,2}/\\d{1,2})", 2)
				.matcher(s);
		while (m.find())
			DOB = m.group(1);
		System.err.println("Date of Birth:-- " + DOB);
		if (DOB == null) {
			sleep(2000);
			keyboard.keyDown(Keys.ALT);
			keyboard.keyDown(KeyEvent.VK_R);
			keyboard.keyUp(KeyEvent.VK_R);
			keyboard.keyUp(Keys.ALT);
			driver.switchTo().window(tabs.get(1));
			sleep(5000);
			return null;
		}
		driver.switchTo().window(tabs.get(1));
		return DOB;
	}

	public PatientDetails availityDetailsProvider(String DOS, String PSID, String DOB, String AID)
			throws InterruptedException, FindFailed {
		Utils.f5();
		while (true) {
			try {
				sleep(5000);
				screen.click(new Pattern(inputFilePath + "new_request.png"));
				System.err.println("New Request page apperared.");
				break;
			} catch (Exception e) {
			}
		}
		driver.switchTo().frame("newBodyFrame");
		sleep(2000);
		driver.findElement(By.xpath("//span[text()='Please Select a Payer']")).click();
		sleep(2000);
		try {
			driver.switchTo().frame("newBodyFrame");
		} catch (Exception exception) {
		}
		driver.findElement(By.xpath("//span[text()='Please Select a Payer']/following::input"))
				.sendKeys(new CharSequence[] { "OTHER" });
		sleep(1000);
		Utils.enterWithoutWait();
		sleep(8000);
		Utils.tabWithoutWait();
		keyboard.type("BIO");
		sleep(8000);
		Utils.enterWithoutWait();
		WebElement asOfDate = driver.findElement(By.id("asOfDateInput"));
		sleep(5000);
		js.executeScript("arguments[0].scrollIntoView();", new Object[] { asOfDate });
		driver.findElement(By.id("asOfDateInput")).clear();
		sleep(1000);
		String dateOfService = "";
		try {
			driver.findElement(By.id("asOfDateInput")).sendKeys(new CharSequence[] { DOS });
			sleep(1000);
			screen.click(new Pattern(inputFilePath + "serviceinformation.png"));
			sleep(1000);
			screen.click(new Pattern(inputFilePath + "asofdate.png"));
			sleep(1000);
			js.executeScript("arguments[0].scrollIntoView();", new Object[] { asOfDate });
			driver.findElement(By.id("asOfDateInput")).clear();
			String date = "";
			String[] dataSegment = DOS.trim().split("/");
			for (int i = 0; i < dataSegment.length; i++)
				System.err.println("Date split:" + dataSegment[i]);
			System.err.println("x:" + dataSegment[0]);
			System.err.println("y:" + dataSegment[1]);
			System.err.println("z:" + dataSegment[2]);
			date = dataSegment[1] + "/" + dataSegment[0] + "/" + dataSegment[2];
			System.err.println("Final Date:" + date);
			driver.findElement(By.id("asOfDateInput")).sendKeys(new CharSequence[] { dataSegment[1] });
			sleep(500);
			driver.findElement(By.id("asOfDateInput")).sendKeys(new CharSequence[] { dataSegment[0] });
			sleep(500);
			driver.findElement(By.id("asOfDateInput")).sendKeys(new CharSequence[] { dataSegment[2] });
			sleep(1000);
		} catch (Exception e) {
			System.err.println("Date is in correct format.");
		}
		sleep(1000);
		driver.findElement(By.xpath("//span[text()='Please Select a Benefit/Service Type']")).click();
		sleep(1000);
		driver.findElement(By.xpath("//span[text()='Please Select a Benefit/Service Type']/following::input"))
				.sendKeys(new CharSequence[] { "HEALTH" });
		sleep(2000);
		Utils.enterWithoutWait();
		sleep(5000);
		System.out.println("ExcelTest.availityDetailsProvider()");
		System.out.println(PSID);
		sleep(2000);
		WebElement memberId = driver.findElement(By.id("memberIdInput"));
		js.executeScript("arguments[0].scrollIntoView();", new Object[] { memberId });
		sleep(1000);
		driver.findElement(By.id("memberIdInput")).sendKeys(new CharSequence[] { PSID });
		sleep(3000);
		try {
			screen.click(new Pattern(inputFilePath + "dob.png"));
			sleep(2000);
			screen.click(new Pattern(inputFilePath + "patient_id_error.png"));
			System.out.println("Entered If Condition for the availity error");
			Utils.f5();

			sleep(2000);
			driver.switchTo().window(tabs.get(0));
			sleep(2000);
			keyboard.keyDown(Keys.ALT);
			keyboard.keyDown(KeyEvent.VK_R);
			keyboard.keyUp(KeyEvent.VK_R);
			keyboard.keyUp(Keys.ALT);
			sleep(5000);
			Utils.f5();
			sleep(8000);

			return null;
		} catch (Exception e) {
			System.out.println("Patient ID is correct.");
			sleep(200);
			WebElement dob = driver.findElement(By.id("dateOfBirthInput"));
			js.executeScript("arguments[0].scrollIntoView();", new Object[] { dob });
			driver.findElement(By.id("dateOfBirthInput")).sendKeys(new CharSequence[] { DOB });
			sleep(1000);
			Utils.enterWithoutWait();
			sleep(8000);
			js.executeScript("window.scrollBy(0,100)", new Object[0]);
			sleep(1300);
			boolean flag = false;
			boolean recordsuccess = false;
			int counter = 0;
			try {
				while (true) {
					counter++;
					try {
						sleep(5000);
						screen.click(new Pattern(inputFilePath + "patient_information.png"));
						flag = true;
						recordsuccess = true;
						System.err.println("Patient info retrieved.");
						break;
					} catch (Exception exception) {
						if (counter == 2)
							break;
					}
				}
			} catch (Exception exception) {
				System.err.println("Invalid / Missing subscriber ID");
				flag = false;
			}
			if (flag) {
				String[] relation = driver.findElement(By.xpath("//h4[@class='condensed ng-binding']//small[1]"))
						.getText().split(" ");
				String patientName = driver.findElement(By.xpath("//h5[@class='condensed ng-binding']")).getText();
				String[] flnames = patientName.trim().split(",");
				String trimmedFName = flnames[1];
				String trimmedLName = flnames[0];
				try {
					String[] avoiddquotes = trimmedLName.split("\"");
					trimmedLName = avoiddquotes[0] + " " + avoiddquotes[1];
				} catch (Exception exception) {
				}
				PatientDetails details = new PatientDetails();
				trimmedFName = trimmedFName.replaceAll("[^a-zA-Z0-9]", " ");
				trimmedLName = trimmedLName.replaceAll("[^a-zA-Z0-9]", " ");
				details.setFirstName(trimmedFName);
				details.setLastName(trimmedLName);
				String relString = relation[0].toLowerCase();
				details.setRelation(relString);
				System.err.println("Patient relation is:" + relString);
				try {
					screen.click(new Pattern(inputFilePath + "inactive_patient.png"));
					System.err.println("Patient is Inactive");
					logger.error(
							"\"Accession ID: {}, {}, Subscriber ID not found in Availity for {}, Error Message : The Patient is Inactive\"",
							AID, "Manual", PSID);
					Utils.f5();

					sleep(2000);
					driver.switchTo().window(tabs.get(0));
					sleep(2000);
					keyboard.keyDown(Keys.ALT);
					keyboard.keyDown(KeyEvent.VK_R);
					keyboard.keyUp(KeyEvent.VK_R);
					keyboard.keyUp(Keys.ALT);
					sleep(5000);

					Utils.f5();
					sleep(8000);
					return null;
				} catch (Exception exception) {
					sleep(1500);
					driver.switchTo().window(tabs.get(0));
					return details;
				}
			}
			try {
				if (!recordsuccess) {
					driver.findElement(By.xpath("//div[@title='Error']")).click();
					sleep(3000);
					availityErrorMessage = driver.findElement(By.xpath("//ul[@class='unstyled error-color']//li[1]"))
							.getText();
					Utils.tab();
					Utils.enter();
					sleep(2000);
					driver.switchTo().window(tabs.get(0));
					sleep(2000);
					// screen.click(new Pattern(inputFilePath + "reset.png"));
					keyboard.keyDown(Keys.ALT);
					keyboard.keyDown(KeyEvent.VK_R);
					keyboard.keyUp(KeyEvent.VK_R);
					keyboard.keyUp(Keys.ALT);
					sleep(5000);
					Utils.f5();
					sleep(8000);

					return null;
				}
			} catch (Exception exception) {
				try {
					screen.click(new Pattern(inputFilePath + "unable_to_process_availity.png"));
					System.err.println("Availity is slow, or Down.... Process on hold..!! pausing for 60 hours");
					sleep(3600000);
				} catch (Exception e2) {
					return null;
				}
			}
			return null;
		}
	}

	public void updateNameInXifin(String firstName, String lastName, String fromFirstName, String fromLastName,
			String AID, String PSID, String relation, String vdiPassword) throws InterruptedException, FindFailed {
		try {
			Pattern ctrlaltdel = new Pattern(inputFilePath + "ctrlaltdel.png");
			screen.click(ctrlaltdel);
			sleep(3000);
			Pattern brwsr = new Pattern(inputFilePath + "trnsfercontrolmenu.png");
			screen.mouseMove(brwsr);
			sleep(1000);
			screen.click(brwsr);
			sleep(2000);
			Pattern openmenu = new Pattern(inputFilePath + "openmenu.png");
			screen.mouseMove(openmenu);
			sleep(2000);
			screen.click(openmenu);
			sleep(2000);
			Pattern availablePattern = new Pattern(inputFilePath + "available.png");
			screen.click(availablePattern);
			sleep(1000);
			Pattern sendctrlaltdel = new Pattern(inputFilePath + "sendctrlaltdel.png");
			screen.click(sendctrlaltdel);
			sleep(5000);
			Pattern horizonpassword = new Pattern(inputFilePath + "horizonpassword.png");
			screen.click(horizonpassword);
			sleep(2000);
			keyboard.type(vdiPassword);
			keyboard.keyDown(Keys.ENTER);
			keyboard.keyUp(Keys.ENTER);
			sleep(6000);
			Pattern closemenu = new Pattern(inputFilePath + "openmenu.png");
			screen.click(closemenu);
			sleep(2000);
			System.err.println("Windows unlocked.");
		} catch (Exception e) {
			System.err.println("Windows is already unlocked.");
		}
		Utils.f5();
		sleep(5000);
		Utils.switchToDetailView();
		while (true) {
			try {
				sleep(5000);
				screen.click(new Pattern(inputFilePath + "force_ep.png"));
				break;
			} catch (Exception e) {
				Utils.f5();
				sleep(5000);
				Utils.switchToDetailView();
			}
		}
		while (true) {
			try {
				sleep(5000);
				screen.click(new Pattern(inputFilePath + "patient_name.png"));
				sleep(8000);
				screen.click(new Pattern(inputFilePath + "patient_first_name.png"));
				sleep(2000);
				screen.click(new Pattern(inputFilePath + "patient_name.png"));
				sleep(2000);
				System.err.println("Patient appeared for edit");
				break;
			} catch (Exception e) {
			}
		}
		int count1 = 0;
		while (count1 < 50) {
			count1++;
			try {
				sleep(10);
				keyboard.keyDown(Keys.BACKSPACE);
				keyboard.keyUp(Keys.BACKSPACE);
			} catch (Exception exception) {
			}
		}
		try {
			keyboard.keyDown(Keys.DELETE);
			keyboard.keyUp(Keys.DELETE);
			sleep(10);
		} catch (Exception exception) {
		}
		sleep(1000);
		System.out.println("First name" + firstName);
		keyboard.type(firstName);
		Utils.tabWithoutWait();
		sleep(1000);
		int count2 = 0;
		while (count2 < 20) {
			count2++;
			try {
				sleep(10);
				keyboard.keyDown(Keys.BACKSPACE);
				keyboard.keyUp(Keys.BACKSPACE);
			} catch (Exception exception) {
			}
		}
		try {
			keyboard.keyDown(Keys.DELETE);
			keyboard.keyUp(Keys.DELETE);
			sleep(10);
		} catch (Exception exception) {
		}
		sleep(1000);
		keyboard.type(lastName);
		sleep(2000);
		try {
			screen.click(new Pattern(inputFilePath + "save_updatenames.png"));
			sleep(15000);
			try {
				screen.click(new Pattern(inputFilePath + "do_you_want_to_reset_or_save.png"));
				sleep(3000);
			} catch (Exception e) {
				System.err.println("No save error occured.");
			}
		} catch (Exception e) {
			System.err.println("Record update failed.");
		}
		try {
			sleep(5000);
			screen.click(new Pattern(inputFilePath + "note.png"));
			sleep(2000);
		} catch (Exception e) {
			System.err.println("Start clicking on fix error check box...");
			screen.click(new Pattern(inputFilePath + "acc_errors.png"));
		}
		try {
			try {
				Pattern fixerPattern = new Pattern(inputFilePath + "fix_error.png");
				screen.click(fixerPattern);
				System.err.println("fix error checked.");
			} catch (Exception e) {
				try {
					Pattern fixerPattern = new Pattern(inputFilePath + "fix_error_checkbox.png");
					screen.click(fixerPattern);
					System.err.println("fix error checked.");
				} catch (Exception e2) {
					System.err.println("Fix error checkbox action failed.");
				}
			}
			sleep(2000);
			Pattern collapse = new Pattern(inputFilePath + "collapse.png");
			screen.click(collapse);
			sleep(5000);
			Pattern insuranceInfo = new Pattern(inputFilePath + "insurenceinfo.png");
			screen.doubleClick(insuranceInfo);
			sleep(7000);
			try {
				Pattern payorinfo = new Pattern(inputFilePath + "payorinfo.png");
				screen.click(payorinfo);
				sleep(2000);
				Pattern section = new Pattern(inputFilePath + "section_search.png");
				screen.click(section);
				sleep(2000);
				keyboard.type("Payor Info");
				keyboard.keyDown(Keys.ENTER);
				keyboard.keyUp(Keys.ENTER);
				sleep(5000);
			} catch (Exception e) {
				System.err.println("scroll failed for eligibility check.");
			}
			try {
				Pattern clicksubsciberId = new Pattern(inputFilePath + "subscriber.png");
				screen.doubleClick(clicksubsciberId);
				sleep(1000);
			} catch (Exception e) {
				System.out.println("SubscriberId click failed");
			}
			int counter = 0;
			while (counter != 2) {
				try {
					Pattern checkEligibility = new Pattern(inputFilePath + "check_eligibility.png");
					sleep(400);
					Pattern checkedbox = new Pattern(inputFilePath + "checkedbox.png");
					sleep(400);
					screen.click(checkEligibility);
					sleep(2000);
					screen.wait(checkedbox, 30.0D);
					break;
				} catch (Exception e) {
					sleep(2000);
					counter++;
				}
			}
			sleep(2000);
			try {
				screen.click(new Pattern(inputFilePath + "save_updatenames.png"));
				sleep(10000);
			} catch (Exception e) {
				System.err.println("Save failed after checking eligibility");
			}
			try {
				Pattern section = new Pattern(inputFilePath + "section_search.png");
				screen.click(section);
				sleep(2000);
				Utils.backspace();
				sleep(2000);
				keyboard.type("Insured Info");
				keyboard.keyDown(Keys.ENTER);
				keyboard.keyUp(Keys.ENTER);
				sleep(5000);
			} catch (Exception e) {
				System.err.println("Section search failed.");
			}
			while (true) {
				try {
					screen.click(new Pattern(inputFilePath + "relationship.png"));
					sleep(2000);
					break;
				} catch (Exception e) {
					System.err.println("Failed relationship click");
				}
			}
			try {
				System.err.println("Relationship: " + relation.equalsIgnoreCase("child"));
				if (relation.equalsIgnoreCase("child")) {
					keyboard.keyDown(KeyEvent.VK_C);
					keyboard.keyUp(KeyEvent.VK_C);
					sleep(1000);

				} else if (relation.equalsIgnoreCase("spouse")) {
					keyboard.keyDown(KeyEvent.VK_S);
					keyboard.keyUp(KeyEvent.VK_S);
					sleep(1000);
				} else if (relation.equalsIgnoreCase("subscriber")) {

				} else {

					keyboard.keyDown(KeyEvent.VK_O);
					keyboard.keyUp(KeyEvent.VK_O);
					sleep(1000);

				}
			} catch (Exception e) {
				System.err.println("Failed while selecting drop down");
			}
			try {
				sleep(3000);
				Pattern insuranceControl = new Pattern(inputFilePath + "email.png");
				screen.click(insuranceControl);
				sleep(400);
			} catch (Exception e) {
				System.err.println("insured info_email.png failed");
			}
			sleep(2000);
			try {
				screen.click(new Pattern(inputFilePath + "save_updatenames.png"));
				sleep(10000);
			} catch (Exception e) {
				System.err.println("Save update failed.");
			}
			try {
				screen.click(new Pattern(inputFilePath + "do_you_want_to_reset_or_save.png"));
				sleep(3000);
			} catch (Exception exception) {
			}
			try {
				Pattern no_error_generated = new Pattern(inputFilePath + "new_error_gen_eli_check.png");
				screen.click(no_error_generated);
				sleep(1000);
				Pattern reset = new Pattern(inputFilePath + "eli_check_reset.png");
				screen.click(reset);
				sleep(5000);
				logger.error("Accession ID: {} ,{}, \"There are errors for this, subscriber ID : {}\"", AID, "Manual",
						PSID);
				sleep(1000);
			} catch (Exception e) {
				System.err.println("Eligibility check completed without error and reset the page.");
			}
			try {
				Pattern eligibility_check = new Pattern(inputFilePath + "eli_check_header.png");
				screen.click(eligibility_check);
				sleep(500);
				Pattern remain = new Pattern(inputFilePath + "remain_on_same_page.png");
				screen.click(remain);
				sleep(500);
				Pattern remain_ok = new Pattern(inputFilePath + "eli_check_ok.png");
				screen.click(remain_ok);
				sleep(5000);
			} catch (Exception e) {
				System.err.println("Eligibility check completed without error.");
			}
			sleep(2000);
			screen.click(new Pattern(inputFilePath + "add_note.png"));
			try {
				screen.wait(new Pattern(inputFilePath + "add_record.png"), 5.0D);
				Utils.tabWithoutWait();
				sleep(1000);
				keyboard.type("Patient details updated successfully for the, " + fromLastName + "," + fromFirstName
						+ " with " + lastName + "," + firstName + " as per availity.");
				System.err.println("Note added successfully.");
				for (int i = 0; i < 6; i++) {
					Utils.tabWithoutWait();
					sleep(100);
				}
				Utils.enterWithoutWait();
				sleep(1000);
			} catch (Exception e) {
				System.err.println("Add note failed.");
			}
			keyboard.keyDown(Keys.ALT);
			keyboard.keyDown(KeyEvent.VK_S);
			keyboard.keyUp(KeyEvent.VK_S);
			keyboard.keyUp(Keys.ALT);
			sleep(10000);
			try {
				sleep(3000);
				screen.click(new Pattern(inputFilePath + "do_you_want_to_reset_or_save.png"));
			} catch (Exception exception) {
			}
		} catch (Exception e) {
			logger.error("Accession ID: {} ,{}, \"There are errors for this, subscriber ID : {}\"", AID, "Manual",
					PSID);
			sleep(1000);
			keyboard.keyDown(Keys.ALT);
			keyboard.keyDown(KeyEvent.VK_R);
			keyboard.keyUp(KeyEvent.VK_R);
			keyboard.keyUp(Keys.ALT);

			sleep(6000);
		}
	}

	public void terminate() throws InterruptedException, FindFailed {
		try {
			screen.click(new Pattern(inputFilePath + "services_90.png"));
		} catch (Exception e) {
			screen.click(new Pattern(inputFilePath + "services.png"));
		}
		sleep(6000);
		screen.click(new Pattern(inputFilePath + "logout.png"));
		sleep(8000);
		Utils.f11();
		sleep(3000);
		screen.rightClick(new Pattern(inputFilePath + "xifin_logout.png"));
		sleep(1000);
		keyboard.keyDown(KeyEvent.VK_C);
		keyboard.keyUp(KeyEvent.VK_C);
		sleep(8000);
	}

	public void exportLogToCSV(String logfile, String csv_file_path)
			throws FindFailed, InterruptedException, IOException {
		Utils.f5();
		sleep(5000);
		screen.doubleClick(new Pattern(inputFilePath + "notepad_icon.png"));
		sleep(5000);
		while (true) {
			try {
				screen.doubleClick(new Pattern(inputFilePath + "notepad_fullscreen.png"));
				sleep(3000);
				break;
			} catch (Exception e) {
				sleep(3000);
			}
		}
		File file = new File(logfile);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				keyboard.type(line);
				Utils.enterWithoutWait();
			}
		}
		sleep(2000);
		keyboard.keyDown(Keys.CTRL);
		keyboard.keyDown(KeyEvent.VK_S);
		keyboard.keyUp(KeyEvent.VK_S);
		keyboard.keyUp(Keys.CTRL);

		sleep(6000);
		keyboard.type(csv_file_path);
		sleep(1000);
		Utils.enterWithoutWait();
		sleep(3000);
		try {
			screen.rightClick(new Pattern(inputFilePath + "notepad.png"));
			sleep(3000);
			keyboard.keyDown(KeyEvent.VK_C);
			keyboard.keyUp(KeyEvent.VK_C);
			System.err.println("notepad closed.");
		} catch (Exception e) {
			System.err.println("right click failed details tab");
		}
		System.err.println("file saved.");
		sleep(500);
		keyboard.keyDown(Keys.CTRL);
		keyboard.keyDown(Keys.F4);
		keyboard.keyUp(Keys.F4);
		keyboard.keyUp(Keys.CTRL);
		sleep(2000);
		System.err.println("Xifin closed successfully.");
		keyboard.keyDown(Keys.ALT);
		keyboard.keyDown(Keys.F4);
		keyboard.keyUp(Keys.F4);
		keyboard.keyUp(Keys.ALT);
		System.err.println("Availity closed successfully.");
		sleep(2000);
	}

	public static void sleep(int sleepTime) throws InterruptedException {
		Thread.sleep(sleepTime);
	}
}
