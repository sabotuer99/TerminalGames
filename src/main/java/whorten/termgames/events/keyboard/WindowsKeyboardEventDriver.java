package whorten.termgames.events.keyboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import whorten.termgames.events.EventDriver;
import whorten.termgames.events.EventListener;
import whorten.termgames.utils.Keys;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class WindowsKeyboardEventDriver implements KeyboardEventDriver {

	private final static Logger logger = LogManager.getLogger(WindowsKeyboardEventDriver.class);
	private List<String> stops;
	private List<KeyboardEventListener> listeners = new ArrayList<>();
	private int repeatThreshold;
	private volatile boolean isListening = false;


	private static Msvcrt msvcrt;
	private static Kernel32 kernel32;
	private static Pointer consoleHandle;
	private static int originalConsoleMode;
	private static final int invalidKey = 0xFFFE;
	private static final String invalidKeyStr = String.valueOf((char) invalidKey);

	private static boolean initDone;
	private static boolean stdinIsConsole;
	private static boolean consoleModeAltered;
	
	
	
	
	
	private WindowsKeyboardEventDriver() {}

	@Override
	public void subscribe(KeyboardEventListener listener) {
		logger.debug("Adding subscriber to Keyboard Events.");
		listeners.add(listener);
	}

	@Override
	public void unsubscribe(KeyboardEventListener listener) {
		logger.debug("Removing subscriber to Keyboard Events.");
		listeners.remove(listener);
	}

	@Override
	public boolean isListening() {
		return isListening;
	}

	@Override
	public void listen() throws IOException {
		initWindows();
		isListening = true;
		String last = null;
		try {
			while (!stops.contains(last) && !die) {

				Thread.sleep(10);

				if (msvcrt._kbhit() != 0) {
					logger.debug("New input being processed...");
					int c = msvcrt._getwch();

					if (c == 0 || c == 0xE0) { // Function key or arrow key
						// escape character requires special handling
						c = msvcrt._getwch();
						switch (c) {
						case 'B':
						case Keys.DOWN_ARROW_BYTE:
							last = Keys.DOWN_ARROW;
							break;
						case 'A':
						case Keys.UP_ARROW_BYTE:
							last = Keys.UP_ARROW;
							break;
						case 'C':
						case Keys.RIGHT_ARROW_BYTE:
							last = Keys.RIGHT_ARROW;
							break;
						case 'D':
						case Keys.LEFT_ARROW_BYTE:
							last = Keys.LEFT_ARROW;
							break;
						case 13:
							last = Keys.ENTER;
							break;
						default:
							last = String.valueOf((char) c);
							break;
						}
					} else {
						// every other character, uppered
						last = Character.toString((char) c);
					}

					KeyEvent k = new KeyDownEvent(last.toString().toUpperCase());
					fire(k);
					checkForKeyUp(last);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		isListening = false;
	}

	private Map<String, Long> lastSeen = new HashMap<>();
	private volatile boolean die;

	private void checkForKeyUp(String last) {
		Long timeSeen = System.currentTimeMillis();
		for (String key : new HashSet<String>(lastSeen.keySet())) {
			Long diff = timeSeen - lastSeen.get(key);
			if (diff > repeatThreshold) {
				KeyEvent ku = new KeyUpEvent(key);
				fire(ku);
				lastSeen.remove(key);
			}
		}
		lastSeen.put(last, timeSeen);
	}

	private void fire(KeyEvent k) {
		logger.debug(String.format("Firing keyboard event: %s", k));
		for (EventListener<KeyEvent> listener : listeners) {
			listener.handleEvent(k);
		}
	}

	public static class Builder {
		private List<String> stops = new ArrayList<>();
		private List<KeyboardEventListener> listeners = new ArrayList<>();
		private int threshold = 100;

		public Builder withListener(KeyboardEventListener listener) {
			listeners.add(listener);
			return this;
		}

		public Builder withStopCharacter(Character stopCharacter) {
			stops.add(stopCharacter.toString());
			return this;
		}

		public Builder withStopSequence(String stopSequence) {
			stops.add(stopSequence);
			return this;
		}

		public WindowsKeyboardEventDriver build() {
			WindowsKeyboardEventDriver instance = new WindowsKeyboardEventDriver();
			//instance.in = this.in;
			instance.stops = new ArrayList<>(stops);
			instance.listeners = new ArrayList<>(listeners);
			instance.repeatThreshold = threshold;
			return instance;
		}

		public Builder withRepeatThreshold(int threshold) {
			this.threshold = threshold;
			return this;
		}
	}

	public void die() {
		die = true;
	}

	// MODIFIED version of RawConsoleInput class:
	// Copyright 2015 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
	// www.source-code.biz, www.inventec.ch/chdh
	//
	// This module is multi-licensed and may be used under the terms of any of the following licenses:
	//
	//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
	//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
	//
	// Please contact the author if you need another license.
	// This module is provided "as is", without warranties of any kind.
	//
	// Home page: http://www.source-code.biz/snippets/java/RawConsoleInput
	
	// --- Windows
	// ------------------------------------------------------------------

	// The Windows version uses _kbhit() and _getwch() from msvcrt.dll.


	private static int readWindows(boolean wait) throws IOException {
		initWindows();
		if (!stdinIsConsole) {
			int c = msvcrt.getwchar();
			if (c == 0xFFFF) {
				c = -1;
			}
			return c;
		}
		consoleModeAltered = true;
		setConsoleMode(consoleHandle, originalConsoleMode & ~Kernel32Defs.ENABLE_PROCESSED_INPUT);
		// ENABLE_PROCESSED_INPUT must remain off to prevent Ctrl-C from being
		// processed by the system
		// while the program is not within getwch().
		if (!wait && msvcrt._kbhit() == 0) {
			return -2;
		} // no key available
		return getwch();
	}

	private static int getwch() {
		int c = msvcrt._getwch();
		if (c == 0 || c == 0xE0) { // Function key or arrow key
			c = msvcrt._getwch();
			if (c >= 0 && c <= 0x18FF) {
				return 0xE000 + c; // construct key code in private Unicode
									// range
			}
			return invalidKey;
		}
		if (c < 0 || c > 0xFFFF) {
			return invalidKey;
		}
		return c; // normal key
	}

	private static synchronized void initWindows() throws IOException {
		if (initDone) {
			return;
		}

		msvcrt = (Msvcrt) Native.load("msvcrt", Msvcrt.class);
		kernel32 = (Kernel32) Native.load("kernel32", Kernel32.class);
		try {
			consoleHandle = getStdInputHandle();
			originalConsoleMode = getConsoleMode(consoleHandle);
			stdinIsConsole = true;
		} catch (IOException e) {
			stdinIsConsole = false;
		}
		if (stdinIsConsole) {
			registerShutdownHook();
		}
		initDone = true;
	}

	private static Pointer getStdInputHandle() throws IOException {
		Pointer handle = kernel32.GetStdHandle(Kernel32Defs.STD_INPUT_HANDLE);
		if (Pointer.nativeValue(handle) == 0 || Pointer.nativeValue(handle) == Kernel32Defs.INVALID_HANDLE_VALUE) {
			throw new IOException("GetStdHandle(STD_INPUT_HANDLE) failed.");
		}
		return handle;
	}

	private static int getConsoleMode(Pointer handle) throws IOException {
		IntByReference mode = new IntByReference();
		int rc = kernel32.GetConsoleMode(handle, mode);
		if (rc == 0) {
			throw new IOException("GetConsoleMode() failed.");
		}
		return mode.getValue();
	}

	private static void setConsoleMode(Pointer handle, int mode) throws IOException {
		int rc = kernel32.SetConsoleMode(handle, mode);
		if (rc == 0) {
			throw new IOException("SetConsoleMode() failed.");
		}
	}

	private static void resetConsoleModeWindows() throws IOException {
		if (!initDone || !stdinIsConsole || !consoleModeAltered) {
			return;
		}
		setConsoleMode(consoleHandle, originalConsoleMode);
		consoleModeAltered = false;
	}

	private static interface Msvcrt extends Library {
		int _kbhit();
		int _getwch();
		int getwchar();
	}

	private static class Kernel32Defs {
		static final int STD_INPUT_HANDLE = -10;
		static final long INVALID_HANDLE_VALUE = (Native.POINTER_SIZE == 8) ? -1 : 0xFFFFFFFFL;
		static final int ENABLE_PROCESSED_INPUT = 0x0001;
	}

	private static interface Kernel32 extends Library {
		int GetConsoleMode(Pointer hConsoleHandle, IntByReference lpMode);
		int SetConsoleMode(Pointer hConsoleHandle, int dwMode);
		Pointer GetStdHandle(int nStdHandle);
	}

	private static void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdownHook();
			}
		});
	}

	private static void shutdownHook() {
		try {
			resetConsoleModeWindows();
		} catch (Exception e) {
		}
	}

}
