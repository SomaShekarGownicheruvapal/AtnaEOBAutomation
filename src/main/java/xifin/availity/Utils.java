package xifin.availity;

import java.awt.event.KeyEvent;

import org.sikuli.api.robot.Keyboard;
import org.sikuli.api.robot.desktop.DesktopKeyboard;
import org.sikuli.hotkey.Keys;

public class Utils {
	static Keyboard keyboard = new DesktopKeyboard();

	public static void backspace() throws InterruptedException {
		Thread.sleep(2000);
		int keyCounter = 0;
		while (true) {
			keyCounter++;
			keyboard.keyDown(Keys.BACKSPACE);
			keyboard.keyUp(Keys.BACKSPACE);
			if (keyCounter == 50)
				break;
		}
		Thread.sleep(2000);
	}

	public static void switchToDetailView() throws InterruptedException {

		keyboard.keyDown(Keys.C_NUM_LOCK);
		keyboard.keyUp(Keys.C_NUM_LOCK);
		Thread.sleep(1000);
		keyboard.keyDown(Keys.CTRL);
		keyboard.keyDown(Keys.ALT);
		keyboard.keyDown(Keys.NUM3);
		keyboard.keyUp(Keys.NUM3);
		keyboard.keyUp(Keys.ALT);
		keyboard.keyUp(Keys.CTRL);
		Thread.sleep(3000);

		System.err.println("Detail view started");

		keyboard.keyDown(KeyEvent.VK_NUM_LOCK);
		keyboard.keyUp(KeyEvent.VK_NUM_LOCK);
		Thread.sleep(1000);
		keyboard.keyDown(KeyEvent.VK_CONTROL);
		keyboard.keyDown(KeyEvent.VK_ALT);
		keyboard.keyDown(KeyEvent.VK_NUMPAD3);
		keyboard.keyUp(KeyEvent.VK_NUMPAD3);
		keyboard.keyUp(KeyEvent.VK_ALT);
		keyboard.keyUp(KeyEvent.VK_CONTROL);
		Thread.sleep(3000);
	}

	public static void tab() throws InterruptedException {
		Thread.sleep(2000);
		keyboard.keyDown(Keys.TAB);
		keyboard.keyUp(Keys.TAB);
	}

	public static void tabWithoutWait() throws InterruptedException {
		keyboard.keyDown(Keys.TAB);
		keyboard.keyUp(Keys.TAB);
	}

	public static void enter() throws InterruptedException {
		Thread.sleep(5000);
		keyboard.keyDown(Keys.ENTER);
		keyboard.keyUp(Keys.ENTER);
		Thread.sleep(8000);
	}

	public static void enterWithoutWait() throws InterruptedException {
		keyboard.keyDown(Keys.ENTER);
		keyboard.keyUp(Keys.ENTER);
	}

	public static void f11() throws InterruptedException {
		keyboard.keyDown(Keys.F11);
		keyboard.keyUp(Keys.F11);
	}

	public static void f5() throws InterruptedException {
		keyboard.keyDown(Keys.F5);
		keyboard.keyUp(Keys.F5);
	}

	public static void down() throws InterruptedException {
		keyboard.keyDown(Keys.DOWN);
		keyboard.keyUp(Keys.DOWN);
	}
}