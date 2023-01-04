/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.soc.quantr_riscv;

import static com.cburch.logisim.soc.Strings.S;

import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.util.GraphicsUtil;
import java.awt.Color;
import java.awt.Graphics;

public class QuantrRISCVInfo {
  private String busId;
  private QuantrRISCVSimulationManager simulationManager;
  private Component myComp;

  public QuantrRISCVInfo(String id) {
    busId = id;
    simulationManager = null;
  }

  public void setBusId(String value) {
    busId = value;
  }

  public String getBusId() {
    return busId;
  }

  public void setSimulationManager(QuantrRISCVSimulationManager man, Component comp) {
    simulationManager = man;
    myComp = comp;
  }

  public QuantrRISCVSimulationManager getSimulationManager() {
    return simulationManager;
  }

  public Component getComponent() {
    return myComp;
  }

  public void paint(Graphics g, Bounds b) {
//    final var ident = simulationManager == null ? null : simulationManager.getSocBusDisplayString(busId);
//    final var color = (ident == null) ? Color.RED : Color.GREEN;
//    g.setColor(color);
//    g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
//    g.setColor(Color.BLACK);
//    GraphicsUtil.drawCenteredText(g, ident == null ? S.get("SocBusNotConnected") : ident, b.getCenterX(), b.getCenterY());
  }
}
