package com.cburch.logisim.soc.quantr_riscv;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.prefs.AppPreferences;
import static com.cburch.logisim.soc.Strings.S;
import static com.cburch.logisim.soc.bus.SocBus.MENU_PROVIDER;
import com.cburch.logisim.soc.bus.SocBusAttributes;
import com.cburch.logisim.soc.data.SocBusSlaveInterface;
import com.cburch.logisim.soc.data.SocBusSnifferInterface;
import com.cburch.logisim.soc.data.SocBusStateInfo;
import com.cburch.logisim.soc.data.SocInstanceFactory;
import static com.cburch.logisim.soc.data.SocInstanceFactory.SOC_MASTER;
import com.cburch.logisim.soc.data.SocProcessorInterface;
import static com.cburch.logisim.soc.gui.CpuDrawSupport.getBlockWidth;
import static com.cburch.logisim.soc.gui.CpuDrawSupport.getBounds;
import com.cburch.logisim.tools.MenuExtender;
import com.cburch.logisim.util.GraphicsUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
public class QuantrRISCV extends SocInstanceFactory {

	public static final String _ID = "QuantrRISCV";

	public QuantrRISCV() {
		super(_ID, S.getter("QuantrRISCVComponent"), SOC_MASTER);
		System.out.println("hi");
	}

	@Override
	public AttributeSet createAttributeSet() {
		return new QuantrRISCVAttributes();
	}

	@Override
	public Bounds getOffsetBounds(AttributeSet attrs) {
		return Bounds.create(
				0,
				0,
				640,
				(attrs.getValue(QuantrRISCVAttributes.NrOfTracesAttr).getWidth() + 1)
				* SocBusStateInfo.TRACE_HEIGHT);
	}

	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
		super.instanceAttributeChanged(instance, attr);
		if (attr.equals(SocBusAttributes.NrOfTracesAttr)) {
			instance.recomputeBounds();
		}
	}

	@Override
	protected void configureNewInstance(Instance instance) {
		instance.addAttributeListener();
		Port[] ps = new Port[1];
		ps[0] = new Port(0, 10, Port.INPUT, 1);
		ps[0].setToolTip(S.getter("Rv32imResetInput"));
		instance.setPorts(ps);
		Bounds bds = instance.getBounds();
		instance.setTextField(
				StdAttr.LABEL,
				StdAttr.LABEL_FONT,
				bds.getX() + bds.getWidth() / 2,
				bds.getY() - 3,
				GraphicsUtil.H_CENTER,
				GraphicsUtil.V_BASELINE);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawBounds();
		painter.drawLabel();
//		painter.drawPort(0, "Reset", Direction.EAST);
		Graphics2D g2 = (Graphics2D) painter.getGraphics();
		Location loc = painter.getLocation();
//		Font f = g2.getFont();
		g2.setFont(StdAttr.DEFAULT_LABEL_FONT);
		GraphicsUtil.drawCenteredText(g2, "Quantr RISC-V", loc.getX() + 320, loc.getY() + 10);
//		g2.setFont(f);
		Bounds bds;
		boolean scale = true;
//		if (scale) {
//			g2.setFont(AppPreferences.getScaledFont(g.getFont()));
//		}
		g2.translate(loc.getX(), loc.getY() + 50);
		int blockWidth = getBlockWidth(g2, scale);
		int blockX = ((scale ? AppPreferences.getScaled(160) : 160) - blockWidth) / 2;
		if (scale) {
			blockWidth = AppPreferences.getDownScaled(blockWidth);
			blockX = AppPreferences.getDownScaled(blockX);
		}
		g2.setColor(Color.decode("68A691"));
		bds = getBounds(0, 0, 160, 495, scale);
		g2.fillRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
		g2.setColor(Color.getColor("68A691"));
		bds = getBounds(0, 0, 160, 15, scale);
		g2.fillRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());

		g2.setColor(Color.black);
		bds = getBounds(80, 6, 0, 0, scale);
		GraphicsUtil.drawCenteredText(g2, S.get("Rv32imRegisterFile"), bds.getX(), bds.getY());

		g2.setColor(Color.BLACK);
		bds = getBounds(0, 0, 160, 495, scale);
		g2.drawRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
		for (int i = 0; i < 32; i++) {
			bds = getBounds(20, 21 + i * 15, 0, 0, scale);
			GraphicsUtil.drawCenteredText(g2, "x" + i, bds.getX(), bds.getY());
			g2.setColor(Color.BLUE);
			bds = getBounds(blockX, 16 + i * 15, blockWidth, 13, scale);
			g2.fillRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
			g2.setColor(Color.BLACK);
			g2.drawRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
			g2.setColor(Color.BLUE);
			bds = getBounds(blockX + blockWidth / 2, 21 + i * 15, 0, 0, scale);
			GraphicsUtil.drawCenteredText(g2, "c" + i, bds.getX(), bds.getY());
			g2.setColor(Color.darkGray);
			bds = getBounds(140, 21 + i * 15, 0, 0, scale);
			GraphicsUtil.drawCenteredText(g2, "a" + i, bds.getX(), bds.getY());
			g2.setColor(Color.BLACK);
		}
		g2.dispose();
	}

	@Override
	public void propagate(InstanceState state) {
//		QuantrRISCVInfo info = state.getAttributeValue(QuantrRISCVAttributes.QUANTR_RISCV_BUS_ID);
//		SocBusStateInfo data = info.getSimulationManager().getSocBusState(info.getBusId());
//		SocBusStateInfo.SocBusState dat = (SocBusStateInfo.SocBusState) state.getData();
//		if (dat == null) {
//			state.setData(data.getNewState(state.getInstance()));
//		}
//		if (state.getPortValue(0) == Value.TRUE) {
//			dat.clear();
//		}
	}

	@Override
	public boolean providesSubCircuitMenu() {
		return true;
	}

	@Override
	protected Object getInstanceFeature(Instance instance, Object key) {
		if (key == MenuExtender.class) {
			return MENU_PROVIDER.getMenu(instance);
		}
		return super.getInstanceFeature(instance, key);
	}

	@Override
	public SocBusSlaveInterface getSlaveInterface(AttributeSet attrs) {
		return null;
	}

	@Override
	public SocBusSnifferInterface getSnifferInterface(AttributeSet attrs) {
		return null;
	}

	@Override
	public SocProcessorInterface getProcessorInterface(AttributeSet attrs) {
		return null;
	}

//	@Override
//	public DynamicElement createDynamicElement(int x, int y, DynamicElement.Path path) {
//		return new SocCpuShape(x, y, path);
//	}
}
