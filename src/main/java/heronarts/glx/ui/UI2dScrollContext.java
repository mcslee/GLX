/**
 * Copyright 2013- Mark C. Slee, Heron Arts LLC
 *
 * This file is part of the LX Studio software library. By using
 * LX, you agree to the terms of the LX Studio Software License
 * and Distribution Agreement, available at: http://lx.studio/license
 *
 * Please note that the LX license is not open-source. The license
 * allows for free, non-commercial use.
 *
 * HERON ARTS MAKES NO WARRANTY, EXPRESS, IMPLIED, STATUTORY, OR
 * OTHERWISE, AND SPECIFICALLY DISCLAIMS ANY WARRANTY OF
 * MERCHANTABILITY, NON-INFRINGEMENT, OR FITNESS FOR A PARTICULAR
 * PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package heronarts.glx.ui;

import heronarts.glx.event.MouseEvent;
import heronarts.lx.utils.LXUtils;

public class UI2dScrollContext extends UI2dContext {

  private boolean dynamicHeight = false;
  private float maxHeight = -1;

  private float scrollWidth;
  private float scrollHeight;

  private boolean horizontalScrollingEnabled = false;
  private boolean verticalScrollingEnabled = true;

  public UI2dScrollContext(UI ui, int x, int y, int w, int h) {
    super(ui, x, y, w, h);
    this.scrollWidth = w;
    this.scrollHeight = h;
  }

  /**
   * Sets a maximum height on the scroll container. Resize or dynamic layout operations
   * up to this size will actually resize the container and texture itself. But past that point,
   * scroll operation occurs.
   *
   * @param maxHeight Maximum height before scrolling kicks in
   * @return this
   */
  public UI2dScrollContext setMaxHeight(float maxHeight) {
    this.dynamicHeight = true;
    this.maxHeight = maxHeight;
    return this;
  }


  @Override
  public UI2dContainer setContentSize(float w, float h) {
    // Explicitly do not invoke super here!
    if (this.dynamicHeight) {
      setSize(this.width, Math.min(this.maxHeight, h));
    }
    return setScrollSize(w, h);
  }

  public UI2dScrollContext setScrollSize(float scrollWidth, float scrollHeight) {
    if ((this.scrollWidth != scrollWidth) || (this.scrollHeight != scrollHeight)) {
      this.scrollWidth = scrollWidth;
      this.scrollHeight = scrollHeight;
      rescroll();
    }
    return this;
  }

  public float getScrollHeight() {
    return this.scrollHeight;
  }

  public UI2dScrollContext setScrollHeight(float scrollHeight) {
    if (this.scrollHeight != scrollHeight) {
      this.scrollHeight = scrollHeight;
      rescroll();
    }
    return this;
  }

  public float getScrollWidth() {
    return this.scrollWidth;
  }

  public UI2dScrollContext setScrollWidth(float scrollWidth) {
    if (this.scrollWidth != scrollWidth) {
      this.scrollWidth = scrollWidth;
      rescroll();
    }
    return this;
  }

  public UI2dScrollContext setHorizontalScrollingEnabled(boolean horizontalScrollingEnabled) {
    this.horizontalScrollingEnabled = horizontalScrollingEnabled;
    return this;
  }

  public UI2dScrollContext setVerticalScrollingEnabled(boolean verticalScrollingEnabled) {
    this.verticalScrollingEnabled = verticalScrollingEnabled;
    return this;
  }

  @Override
  protected void onResize() {
    super.onResize();
    rescroll();
  }

  private float minScrollX() {
    return Math.min(0, this.width - this.scrollWidth);
  }

  private float minScrollY() {
    return Math.min(0, this.height - this.scrollHeight);
  }

  public float getScrollX() {
    return this.scrollX;
  }

  public float getScrollY() {
    return this.scrollY;
  }

  public UI2dScrollContext setScrollX(float scrollX) {
    scrollX = LXUtils.constrainf(scrollX, minScrollX(), 0);
    if (this.scrollX != scrollX) {
      this.scrollX = scrollX;
      redraw();
    }
    return this;
  }

  public UI2dScrollContext setScrollY(float scrollY) {
    scrollY = LXUtils.constrainf(scrollY, minScrollY(), 0);
    if (this.scrollY != scrollY) {
      this.scrollY = scrollY;
      redraw();
    }
    return this;
  }

  private void rescroll() {
    float minScrollX = minScrollX();
    float minScrollY = minScrollY();
    if ((this.scrollX < minScrollX) || (this.scrollY < minScrollY)) {
      this.scrollX = Math.max(this.scrollX, minScrollX);
      this.scrollY = Math.max(this.scrollY, minScrollY);
      redraw();
    }
  }

  @Override
  void mousePressed(MouseEvent mouseEvent, float mx, float my) {
    super.mousePressed(mouseEvent, mx - this.scrollX, my - this.scrollY);
  }

  @Override
  void mouseReleased(MouseEvent mouseEvent, float mx, float my) {
    super.mouseReleased(mouseEvent, mx - this.scrollX, my - this.scrollY);
  }

  @Override
  void mouseDragged(MouseEvent mouseEvent, float mx, float my, float dx, float dy) {
    super.mouseDragged(mouseEvent, mx - this.scrollX, my - this.scrollY, dx, dy);
  }

  @Override
  void mouseMoved(MouseEvent mouseEvent, float mx, float my) {
    super.mouseMoved(mouseEvent, mx - this.scrollX, my - this.scrollY);
  }

  @Override
  void mouseScroll(MouseEvent mouseEvent, float mx, float my, float dx, float dy) {
    super.mouseScroll(mouseEvent, mx - this.scrollX, my - this.scrollY, dx, dy);
  }

  @Override
  protected void onMouseDragged(MouseEvent mouseEvent, float mx, float my, float dx, float dy) {
    if (this.verticalScrollingEnabled) {
      mouseEvent.consume();
      setScrollY(this.scrollY + dy);
    }
    if (this.horizontalScrollingEnabled) {
      mouseEvent.consume();
      setScrollX(this.scrollX + dx);
    }
  }

  @Override
  protected void onMouseScroll(MouseEvent mouseEvent, float mx, float my, float dx, float dy) {
    if (this.horizontalScrollingEnabled) {
      if (this.scrollWidth > this.width) {
        mouseEvent.consume();
        setScrollX(this.scrollX + (mouseEvent.isShiftDown() ? dy : -dx));
      }
    }
    if (this.verticalScrollingEnabled) {
      if (this.scrollHeight > this.height) {
        mouseEvent.consume();
        setScrollY(this.scrollY + dy);
      }
    }
  }
}
