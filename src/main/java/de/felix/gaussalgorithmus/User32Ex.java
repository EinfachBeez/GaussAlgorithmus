package de.felix.gaussalgorithmus;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;

/**
 * @author EinfachBeez | https://github.com/EinfachBeez
 */
public interface User32Ex extends User32 {

    User32Ex INSTANCE = Native.load("user32", User32Ex.class, W32APIOptions.DEFAULT_OPTIONS);

    LONG_PTR SetWindowLongPtr(HWND hwnd, int nIndex, LONG_PTR dwNewLong);

}
