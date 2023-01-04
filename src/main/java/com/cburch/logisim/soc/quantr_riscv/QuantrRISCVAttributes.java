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

import com.cburch.logisim.data.AbstractAttributeSet;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.util.StringUtil;
import java.awt.Font;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QuantrRISCVAttributes extends AbstractAttributeSet {

	public static final Attribute<BitWidth> NrOfTracesAttr = Attributes.forBitWidth("TraceSize", S.getter("SocBusTraceSize"));
	public static final Attribute<QuantrRISCVInfo> QUANTR_RISCV_BUS_ID = new QuantrRISCVIdAttribute();
	public static final Attribute<Boolean> QUANTR_RISCV_TRACE_VISIBLE = Attributes.forBoolean("TraceVisible", S.getter("SocBusTraceVisible"));
	private static final List<Attribute<?>> ATTRIBUTES = Arrays.asList(
			NrOfTracesAttr,
			QUANTR_RISCV_TRACE_VISIBLE,
			StdAttr.LABEL,
			StdAttr.LABEL_FONT,
			StdAttr.LABEL_VISIBILITY,
			QUANTR_RISCV_BUS_ID);
	private Font labelfont = StdAttr.DEFAULT_LABEL_FONT;
	private Boolean labelVisible = true;
	private BitWidth traceSize = BitWidth.create(5);
	private String label = "";
	private QuantrRISCVInfo id = new QuantrRISCVInfo(null);
	private Boolean traceVisible = true;

	@Override
	protected void copyInto(AbstractAttributeSet dest) {
		final var d = (QuantrRISCVAttributes) dest;
		d.labelfont = labelfont;
		d.labelVisible = labelVisible;
		d.traceSize = traceSize;
		d.label = label;
		d.traceVisible = traceVisible;
		d.id = new QuantrRISCVInfo(null);
	}

	@Override
	public List<Attribute<?>> getAttributes() {
		return ATTRIBUTES;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue(Attribute<V> attr) {
		if (attr == NrOfTracesAttr) {
			return (V) traceSize;
		}
		if (attr == StdAttr.LABEL) {
			return (V) label;
		}
		if (attr == StdAttr.LABEL_FONT) {
			return (V) labelfont;
		}
		if (attr == StdAttr.LABEL_VISIBILITY) {
			return (V) labelVisible;
		}
		if (attr == QUANTR_RISCV_BUS_ID) {
			if (StringUtil.isNullOrEmpty(id.getBusId())) {
				final var date = new Date();
				final var names = this.toString().split("@");
				id.setBusId(String.format("0x%016X%s", date.getTime(), names[names.length - 1]));
			}
			return (V) id;
		}
		if (attr == QUANTR_RISCV_TRACE_VISIBLE) {
			return (V) traceVisible;
		}
		return null;
	}

	@Override
	public boolean isReadOnly(Attribute<?> attr) {
		return attr == QUANTR_RISCV_BUS_ID;
	}

	@Override
	public <V> void setValue(Attribute<V> attr, V value) {
		final V oldValue = getValue(attr);
		if (attr == NrOfTracesAttr) {
			final var v = (BitWidth) value;
			if (!traceSize.equals(v)) {
				traceSize = v;
				fireAttributeValueChanged(attr, value, oldValue);
			}
			return;
		}
		if (attr == StdAttr.LABEL) {
			final var v = (String) value;
			if (!label.equals(v)) {
				label = v;
				fireAttributeValueChanged(attr, value, oldValue);
			}
			return;
		}
		if (attr == StdAttr.LABEL_FONT) {
			final var f = (Font) value;
			if (!labelfont.equals(f)) {
				labelfont = f;
				fireAttributeValueChanged(attr, value, oldValue);
			}
			return;
		}
		if (attr == StdAttr.LABEL_VISIBILITY) {
			final var v = (Boolean) value;
			if (labelVisible != v) {
				labelVisible = v;
				fireAttributeValueChanged(attr, value, oldValue);
			}
			return;
		}
		if (attr == QUANTR_RISCV_BUS_ID) {
			id.setBusId(((QuantrRISCVInfo) value).getBusId());
			return;
		}
		if (attr == QUANTR_RISCV_TRACE_VISIBLE) {
			final var v = (Boolean) value;
			if (traceVisible != v) {
				traceVisible = v;
				fireAttributeValueChanged(attr, value, oldValue);
			}
			return;
		}
	}

	public static class QuantrRISCVIdAttribute extends Attribute<QuantrRISCVInfo> {

		public QuantrRISCVIdAttribute() {
			super("SocBusIdentifier", null);
		}

		@Override
		public QuantrRISCVInfo parse(String value) {
			return new QuantrRISCVInfo(value);
		}

		@Override
		public boolean isHidden() {
			return true;
		}

		@Override
		public String toStandardString(QuantrRISCVInfo value) {
			return value.getBusId();
		}
	}
}
