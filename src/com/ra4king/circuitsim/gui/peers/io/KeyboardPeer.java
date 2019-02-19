package com.ra4king.circuitsim.gui.peers.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ra4king.circuitsim.gui.CircuitManager;
import com.ra4king.circuitsim.gui.ComponentManager.ComponentManagerInterface;
import com.ra4king.circuitsim.gui.ComponentPeer;
import com.ra4king.circuitsim.gui.Connection.PortConnection;
import com.ra4king.circuitsim.gui.GuiUtils;
import com.ra4king.circuitsim.gui.Properties;
import com.ra4king.circuitsim.simulator.CircuitState;
import com.ra4king.circuitsim.simulator.Component;
import com.ra4king.circuitsim.simulator.WireValue;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * @author Adithya Nott
 */
public class KeyboardPeer extends ComponentPeer<Component> {
	public static void installComponent(ComponentManagerInterface manager) {
		manager.addComponent(new Pair<>("Input/Output", "Keyboard"),
		                     new Image(KeyboardPeer.class.getResourceAsStream("/resources/RAM.png")),
		                     new Properties());
	}

	// private final PortConnection clockConnection;
	
	public KeyboardPeer(Properties props, int x, int y) {
		super(x, y, 12, 5);
		
		Properties properties = new Properties();
		properties.ensureProperty(Properties.LABEL);
		properties.ensureProperty(Properties.LABEL_LOCATION);
		properties.mergeIfExists(props);
		
		Component component = new Component(properties.getValue(Properties.LABEL), new int[] { 4 }) {
			@Override
			public void valueChanged(CircuitState state, WireValue value, int portIndex) {}
		};
		
		List<PortConnection> connections = new ArrayList<>();
		connections.add(new PortConnection(this, component.getPort(0), getWidth() / 2, getHeight()));
		init(component, properties, connections);
	}

	@Override
	public boolean keyPressed(CircuitManager manager, CircuitState state, KeyCode keyCode, String text) {
		switch(keyCode) {
			case DIGIT0:
			case DIGIT1:
			case DIGIT2:
			case DIGIT3:
			case DIGIT4:
			case DIGIT5:
			case DIGIT6:
			case DIGIT7:
			case DIGIT8:
			case DIGIT9:
			case NUMPAD0:
			case NUMPAD1:
			case NUMPAD2:
			case NUMPAD3:
			case NUMPAD4:
			case NUMPAD5:
			case NUMPAD6:
			case NUMPAD7:
			case NUMPAD8:
			case NUMPAD9:
			case A:
			case B:
			case C:
			case D:
			case E:
			case F:
				// char c = text.charAt(0);
				
				// int value;
				// if(c >= '0' && c <= '9') {
				// 	value = c - '0';
				// } else {
				// 	value = Character.toUpperCase(c) - 'A' + 10;
				// }
				
				// WireValue currentValue = state.getLastPushed(getComponent().getPort(Register.PORT_OUT));
				// WireValue typedValue = WireValue.of(value, Math.min(4, currentValue.getBitSize()));
				// if(typedValue.getValue() != value) {
				// 	typedValue.setAllBits(State.ZERO); // to prevent typing '9' on a 3-bit value, producing 1
				// }
				
				// if(currentValue.getBitSize() <= 4) {
				// 	currentValue.set(typedValue);
				// } else {
				// 	for(int i = currentValue.getBitSize() - 1; i >= 4; i--) {
				// 		currentValue.setBit(i, currentValue.getBit(i - 4));
				// 	}
					
				// 	for(int i = 0; i < 4; i++) {
				// 		currentValue.setBit(i, typedValue.getBit(i));
				// 	}
				// }
				
				// state.pushValue(getComponent().getPort(Register.PORT_OUT), currentValue);
				// break;
		}
		
		return false;
	}
	
	@Override
	public void paint(GraphicsContext graphics, CircuitState circuitState) {
		GuiUtils.drawName(graphics, this, getProperties().getValue(Properties.LABEL_LOCATION));
		
		graphics.setFill(Color.WHITE);
		GuiUtils.drawShape(graphics::fillRect, this);
		graphics.setStroke(Color.BLACK);
		GuiUtils.drawShape(graphics::strokeRect, this);
		
		drawDigit(graphics, -1);
		
		WireValue value = circuitState.getLastReceived(getComponent().getPort(0));
		if(value.isValidValue()) {
			drawDigit(graphics, value.getValue());
		}
	}
	
	private void drawDigit(GraphicsContext graphics, int num) {
		GuiUtils.drawName(graphics, this, getProperties().getValue(Properties.LABEL_LOCATION));

		int x = getScreenX();
		int y = getScreenY();
		int width = getScreenWidth();
		int height = getScreenHeight();
		
		int margin = 4;
		int size = 6;
		
		if(top.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + margin + size, y + margin, width - 2 * margin - 2 * size, size);
		
		if(middle.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + margin + size, y + (height - size) / 2, width - 2 * margin - 2 * size, size);
		
		if(bottom.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + margin + size, y + height - margin - size, width - 2 * margin - 2 * size, size);
		
		if(topRight.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + width - margin - size, y + margin + size / 2, size, (height - size) / 2 - margin);
		
		if(topLeft.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + margin, y + margin + size / 2, size, (height - size) / 2 - margin);
		
		if(botRight.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + width - margin - size, y + height / 2, size, (height - size) / 2 - margin);
		
		if(botLeft.contains(num)) {
			graphics.setFill(Color.RED);
		} else {
			graphics.setFill(Color.LIGHTGRAY);
		}
		graphics.fillRect(x + margin, y + height / 2, size, (height - size) / 2 - margin);
	}
	
	private static final Set<Integer> top = new HashSet<>(Arrays.asList(0, 2, 3, 5, 6, 7, 8, 9, 10, 12, 14, 15));
	private static final Set<Integer> topRight = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 7, 8, 9, 10, 13));
	private static final Set<Integer> botRight = new HashSet<>(Arrays.asList(0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13));
	private static final Set<Integer> bottom = new HashSet<>(Arrays.asList(0, 2, 3, 5, 6, 8, 9, 11, 12, 13, 14));
	private static final Set<Integer> botLeft = new HashSet<>(Arrays.asList(0, 2, 6, 8, 10, 11, 12, 13, 14, 15));
	private static final Set<Integer> topLeft = new HashSet<>(Arrays.asList(0, 4, 5, 6, 8, 9, 10, 11, 12, 14, 15));
	private static final Set<Integer> middle = new HashSet<>(Arrays.asList(2, 3, 4, 5, 6, 8, 9, 10, 11, 13, 14, 15));
}
