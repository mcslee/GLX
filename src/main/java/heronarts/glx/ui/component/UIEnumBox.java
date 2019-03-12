/**
 * Copyright 2017- Mark C. Slee, Heron Arts LLC
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

package heronarts.glx.ui.component;

import heronarts.glx.event.KeyEvent;

public class UIEnumBox extends UIIntegerBox {

  public UIEnumBox() {
    this(0, 0, 0, 0);
  }

  public UIEnumBox(float x, float y, float w, float h) {
    super(x, y, w, h);
    enableImmediateEdit(false);
  }

  @Override
  public String getValueString() {
    if (this.parameter != null) {
      return this.parameter.getOption();
    }
    return super.getValueString();
  }

  @Override
  public void onKeyPressed(KeyEvent keyEvent, char keyChar, int keyCode) {
    if (this.enabled && (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE)) {
      keyEvent.consume();
      incrementValue(keyEvent);
    } else {
      super.onKeyPressed(keyEvent, keyChar, keyCode);
    }
  }
}
