/*******************************************************************************
 * Copyright (c) 2001, 2006 Mathew Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.robocode.net/license/CPLv1.0.html
 * 
 * Contributors:
 *     Mathew Nelson - initial API and implementation
 *******************************************************************************/
package robocode.util;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


/**
 * Insert the type's description here.
 * Creation date: (10/23/2001 1:09:54 PM)
 * @author: Administrator
 */
public class WindowPositionManager implements ComponentListener {
	private Properties windowPositions = null;

	/**
	 * WindowPositionManager constructor comment.
	 */
	public WindowPositionManager() {
		super();
	}

	public Properties getWindowPositions() {
		if (windowPositions == null) {
			windowPositions = new Properties();
			try {
				FileInputStream in = new FileInputStream(new File(Constants.cwd(), "window.properties"));

				windowPositions.load(in);
			} catch (FileNotFoundException e) {
				log("Creating window.properties file");
			} catch (Exception e) {
				log(e);
			}
		}
		return windowPositions;
	}

	public void componentHidden(ComponentEvent e) {}

	public void componentMoved(ComponentEvent e) {
		// Hack, because we cannot detect maximized frame in Java 1.3
		if (e.getComponent().getBounds().getWidth() >= Toolkit.getDefaultToolkit().getScreenSize().width
				|| e.getComponent().getBounds().getHeight() >= Toolkit.getDefaultToolkit().getScreenSize().height) {
			return;
		}
		setWindowRect(true, (Window) e.getComponent(), e.getComponent().getBounds());
	}

	public void componentResized(ComponentEvent e) {
		// Hack, because we cannot detect maximized frame in Java 1.3
		if (e.getComponent().getBounds().getWidth() >= Toolkit.getDefaultToolkit().getScreenSize().width
				|| e.getComponent().getBounds().getHeight() >= Toolkit.getDefaultToolkit().getScreenSize().height) {
			return;
		}
		setWindowRect(false, (Window) e.getComponent(), e.getComponent().getBounds());
	}

	public void componentShown(ComponentEvent e) {}

	/**
	 * Insert the method's description here.
	 * Creation date: (10/23/2001 1:21:55 PM)
	 * @param windowName java.lang.String
	 */
	public Rectangle getWindowRect(Window window) {
		window.addComponentListener(this);
		String rString = (String) getWindowPositions().get(
				window.getName() + "(" + window.getPreferredSize().width / 10 + "-" + window.getPreferredSize().height / 10
				+ ")");

		if (rString == null) {
			// System.out.println("Could not find " + window.getName());
			return null;
		} else {
			StringTokenizer tokenizer = new StringTokenizer(rString, ",");
			int x = Integer.parseInt(tokenizer.nextToken());
			int y = Integer.parseInt(tokenizer.nextToken());
			int width = Integer.parseInt(tokenizer.nextToken());
			int height = Integer.parseInt(tokenizer.nextToken());
			Rectangle r = new Rectangle(x, y, width, height);
		
			// System.out.println(window.getName() + " returning " + r.x + "," + r.y);
		
			return r;
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (10/23/2001 5:24:19 PM)
	 * @param s java.lang.String
	 */
	public void log(String s) {
		Utils.log(s);
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (10/23/2001 5:24:19 PM)
	 * @param s java.lang.String
	 */
	public void log(Throwable e) {
		Utils.log(e);
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (10/23/2001 1:34:46 PM)
	 */
	public void saveWindowPositions() {
		try {
			FileOutputStream out = new FileOutputStream(new File(Constants.cwd(), "window.properties"));

			getWindowPositions().store(out, "Robocode window sizes");
		} catch (IOException e) {
			log("Warning:  Unable to save window positions: " + e);
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (10/23/2001 1:21:55 PM)
	 * @param windowName java.lang.String
	 */
	public void setWindowRect(boolean move, Window w, Rectangle rect) {
		// System.out.println(w.getName() + " moved? " + move + " to " + rect.x + "," + rect.y);
		String rString = new String(rect.x + "," + rect.y + "-" + rect.width + "," + rect.height);

		getWindowPositions().put(
				w.getName() + "(" + w.getPreferredSize().width / 10 + "," + w.getPreferredSize().height / 10 + ")", rString);
	}
}
