/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
 
public class TableColumn extends Item {
	Table parent;
	String displayText;
	int width;
	boolean moveable, resizable = true;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style) {
	this (parent, style, checkNull (parent).columns.length);
}
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style, int index) {
	super (parent, checkStyle (style), index);
	if (!(0 <= index && index <= parent.columns.length)) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	parent.createItem (this, index);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize, typedListener);
	addListener (SWT.Move, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}
static Table checkNull (Table table) {
	if (table == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return table;
}
static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void computeDisplayText (GC gc) {
	int availableWidth = width - 2 * parent.getHeaderPadding (); 
	if (image != null) {
		availableWidth -= image.getBounds ().width;
		availableWidth -= Table.MARGIN_IMAGE;
	}
	String text = this.text;
	int textWidth = gc.textExtent (text).x;
	if (textWidth <= availableWidth) {
		displayText = text;
		return;
	}
	
	/* Ellipsis will be needed, so subtract their width from the available text width */
	int ellipsisWidth = gc.textExtent (Table.ELLIPSIS).x;
	availableWidth -= ellipsisWidth;
	if (availableWidth <= 0) {
		displayText = Table.ELLIPSIS;
		return;
	}
	
	/* Make initial guess. */
	int index = availableWidth / gc.getFontMetrics ().getAverageCharWidth ();
	textWidth = gc.textExtent (text.substring (0, index)).x;

	/* Initial guess is correct. */
	if (availableWidth == textWidth) {
		displayText = text.substring (0, index) + Table.ELLIPSIS;
		return;
	}

	/* Initial guess is too high, so reduce until fit is found. */
	if (availableWidth < textWidth) {
		do {
			index--;
			if (index < 0) {
				displayText = Table.ELLIPSIS;
				return;
			}
			text = text.substring (0, index);
			textWidth = gc.textExtent (text).x;
		} while (availableWidth < textWidth);
		displayText = text + Table.ELLIPSIS;
		return;
	}
	
	/* Initial guess is too low, so increase until overrun is found. */
	while (textWidth < availableWidth) {
		index++;
		textWidth = gc.textExtent (text.substring (0, index)).x;
	}
	displayText = text.substring (0, index - 1) + Table.ELLIPSIS;
}
public void dispose () {
	if (isDisposed ()) return;
	Rectangle parentBounds = parent.getClientArea ();
	int x = getX ();
	int index = getIndex ();
	int orderIndex = getOrderIndex ();
	Table parent = this.parent;
	dispose (true);

	int width = parentBounds.width - x;
	parent.redraw (x, 0, width, parentBounds.height, false);
	/* 
	 * If column 0 was disposed and if the parent has style CHECK then
	 * the new column 0 will change, so explicitly redraw it if it appears to
	 * the left of the disposed column in the column order.
	 */
	if ((parent.style & SWT.CHECK) != 0 && index == 0) {
		if (parent.columns.length > 0) {
			TableColumn newColumn0 = parent.columns [0];
			if (newColumn0.getOrderIndex () < orderIndex) {
				parent.redraw (newColumn0.getX (), 0, newColumn0.width, parentBounds.height, false);
			}
		}
	}
	if (parent.getHeaderVisible ()) {
		parent.header.redraw (x, 0, width, parent.getHeaderHeight (), false);
	}
}
void dispose (boolean notifyParent) {
	super.dispose ();	/* the use of super is intentional here */
	if (notifyParent) parent.destroyItem (this);
	parent = null;
}
/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}
/*
 * Returns the width of the header's content (image + text + margin widths)
 */
int getContentWidth (GC gc, boolean useDisplayText) {
	int contentWidth = 0;
	String text = useDisplayText ? displayText : this.text;
	if (text.length () > 0) {
		contentWidth += gc.textExtent (text, SWT.DRAW_MNEMONIC).x;
	}
	if (image != null) {
		contentWidth += image.getBounds ().width;
		if (text.length () > 0) contentWidth += Table.MARGIN_IMAGE;
	}
	return contentWidth;
}
int getIndex () {
	TableColumn[] columns = parent.columns;
	for (int i = 0; i < columns.length; i++) {
		if (columns [i] == this) return i;
	}
	return -1;
}
/**
 * Gets the moveable attribute. A column that is
 * not moveable cannot be reordered by the user 
 * by dragging the header but may be reordered 
 * by the programmer.
 *
 * @return the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public boolean getMoveable () {
	checkWidget ();
	return moveable;
}
int getOrderIndex () {
	TableColumn[] orderedColumns = parent.orderedColumns; 
	if (orderedColumns == null) return getIndex ();
	for (int i = 0; i < orderedColumns.length; i++) {
		if (orderedColumns [i] == this) return i;
	}
	return -1;
}
/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent () {
	checkWidget ();
	return parent;
}
int getPreferredWidth () {
	if (!parent.getHeaderVisible ()) return 0;
	GC gc = new GC (parent);
	int result = getContentWidth (gc, false);
	gc.dispose ();
	return result + 2 * parent.getHeaderPadding ();
}
/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable () {
	checkWidget ();
	return resizable;
}
/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget ();
	return width;
}
int getX () {
	TableColumn[] columns = parent.getOrderedColumns ();
	int index = getOrderIndex ();
	int result = -parent.horizontalOffset;
	for (int i = 0; i < index; i++) {
		result += columns [i].width;
	}
	return result;
}
/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void pack () {
	checkWidget ();
	TableItem[] availableItems = parent.items;
	if (availableItems.length == 0) return;
	int index = getIndex ();
	int newWidth = getPreferredWidth ();
	for (int i = 0; i < availableItems.length; i++) {
		newWidth = Math.max (newWidth, availableItems [i].getPreferredWidth (index));
	}
	if (newWidth != width) parent.updateColumnWidth (this, newWidth);
}
void paint (GC gc) {
	int padding = parent.getHeaderPadding ();
	
	int x = getX ();
	int startX = x + padding;
	if ((style & SWT.LEFT) == 0) {
		int contentWidth = getContentWidth (gc, true);
		if ((style & SWT.RIGHT) != 0) {
			startX = Math.max (startX, x + width - padding - contentWidth);	
		} else {	/* SWT.CENTER */
			startX = Math.max (startX, x + (width - contentWidth) / 2);	
		}
	}
	int headerHeight = parent.getHeaderHeight ();

	/* restrict the clipping region to the header cell */
	gc.setClipping (
		x + padding,
		padding,
		width - 2 * padding,
		headerHeight - 2 * padding);
	
	if (image != null) {
		Rectangle imageBounds = image.getBounds ();
		int drawHeight = Math.min (imageBounds.height, headerHeight - 2 * padding);
		gc.drawImage (
			image,
			0, 0,
			imageBounds.width, imageBounds.height,
			startX, (headerHeight - drawHeight) / 2,
			imageBounds.width, drawHeight); 
		startX += imageBounds.width;
		if (displayText.length () > 0) startX += Table.MARGIN_IMAGE; 
	}
	if (displayText.length () > 0) {
		int fontHeight = parent.fontHeight;
		gc.drawText (displayText, startX, (headerHeight - fontHeight) / 2, SWT.DRAW_MNEMONIC);
	}
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);
}
/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = getIndex ();
	if (index == -1 || index == 0) return;	/* column 0 can only have left-alignment */
	alignment = checkBits (alignment, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & alignment) != 0) return;	/* same value */
	style &= ~(SWT.LEFT | SWT.CENTER | SWT.RIGHT);
	style |= alignment;
	int x = getX ();
	parent.redraw (x, 0, width, parent.getClientArea ().height, false);
	if (parent.getHeaderVisible ()) {
		parent.header.redraw (x, 0, width, parent.getHeaderHeight (), false);		
	}
}
public void setImage (Image value) {
	checkWidget ();
	if (value == image) return;
	if (value != null && value.equals (image)) return;	/* same value */
	super.setImage (value);
	
	/* An image width change may affect the space available for the column's displayText. */
	GC gc = new GC (parent);
	computeDisplayText (gc);
	gc.dispose ();
	
	/*
	 * If this is the first image being put into the header then the header
	 * height may be adjusted, in which case a full redraw is needed.
	 */
	if (parent.headerImageHeight == 0) {
		int oldHeaderHeight = parent.getHeaderHeight ();
		parent.setHeaderImageHeight (value.getBounds ().height);
		if (oldHeaderHeight != parent.getHeaderHeight ()) {
			/* parent header height changed */
			parent.header.redraw ();
			parent.redraw ();
			return;
		}
	}
	
	parent.header.redraw (getX (), 0, width, parent.getHeaderHeight (), false);
}
/**
 * Sets the moveable attribute.  A column that is
 * moveable can be reordered by the user by dragging
 * the header. A column that is not moveable cannot be 
 * dragged by the user but may be reordered 
 * by the programmer.
 *
 * @param moveable the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#setColumnOrder(int[])
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see SWT#Move
 * 
 * @since 3.1
 */
public void setMoveable (boolean moveable) {
	checkWidget ();
	this.moveable = moveable;
}
/**
 * Sets the resizable attribute.  A column that is
 * resizable can be resized by the user dragging the
 * edge of the header.  A column that is not resizable 
 * cannot be dragged by the user but may be resized 
 * by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean value) {
	checkWidget ();
	resizable = value;
}
public void setText (String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (value.equals (text)) return;					/* same value */
	super.setText (value);
	GC gc = new GC (parent);
	computeDisplayText (gc);
	gc.dispose ();
	parent.header.redraw (getX (), 0, width, parent.getHeaderHeight (), false);
}
/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int value) {
	checkWidget ();
	value = Math.max (value, 0);
	if (width == value) return;							/* same value */
	parent.updateColumnWidth (this, value);
}
void updateFont (GC gc) {
	computeDisplayText (gc);
}
}
