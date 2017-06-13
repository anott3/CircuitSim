package com.ra4king.circuitsimulator.gui.peers.io;

import java.util.ArrayList;
import java.util.List;

import com.ra4king.circuitsimulator.gui.ComponentManager.ComponentManagerInterface;
import com.ra4king.circuitsimulator.gui.ComponentPeer;
import com.ra4king.circuitsimulator.gui.Connection.PortConnection;
import com.ra4king.circuitsimulator.gui.Properties;
import com.ra4king.circuitsimulator.gui.peers.wiring.ClockPeer;
import com.ra4king.circuitsimulator.simulator.CircuitState;
import com.ra4king.circuitsimulator.simulator.Component;
import com.ra4king.circuitsimulator.simulator.WireValue;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * @author Roi Atalla
 */
public class Button extends ComponentPeer<Component> {
	public static void installComponent(ComponentManagerInterface manager) {
		manager.addComponent(new Pair<>("Input/Output", "Button"),
		                     new Image(ClockPeer.class.getResourceAsStream("/resources/Button.png")),
		                     new Properties());
	}
	
	private boolean isPressed = false;
	
	public Button(Properties props, int x, int y) {
		super(x, y, 2, 2);
		
		Properties properties = new Properties();
		properties.ensureProperty(Properties.LABEL);
		properties.ensureProperty(Properties.LABEL_LOCATION);
		properties.mergeIfExists(props);
		
		Component component = new Component(properties.getValue(Properties.LABEL), new int[] { 1 }) {
			@Override
			public void init(CircuitState state, Object lastProperty) {
				state.pushValue(getPort(0), WireValue.of(0, 1));
			}
			
			@Override
			public void valueChanged(CircuitState state, WireValue value, int portIndex) {}
		};
		
		List<PortConnection> connections = new ArrayList<>();
		connections.add(new PortConnection(this, component.getPort(0), getWidth(), getHeight() / 2));
		
		init(component, properties, connections);
	}
	
	@Override
	public void mousePressed(CircuitState state, double x, double y) {
		isPressed = true;
		state.pushValue(getComponent().getPort(0), WireValue.of(1, 1));
	}
	
	@Override
	public void mouseReleased(CircuitState state, double x, double y) {
		isPressed = false;
		state.pushValue(getComponent().getPort(0), WireValue.of(0, 1));
	}
	
	@Override
	public void keyPressed(CircuitState state, KeyCode keyCode, String text) {
		if(keyCode == KeyCode.SPACE) {
			mousePressed(state, 0, 0);
		}
	}
	
	@Override
	public void keyReleased(CircuitState state, KeyCode keyCode, String text) {
		if(keyCode == KeyCode.SPACE) {
			mouseReleased(state, 0, 0);
		}
	}
	
	@Override
	public void paint(GraphicsContext graphics, CircuitState state) {
		int x = getScreenX();
		int y = getScreenY();
		int width = getScreenWidth();
		int height = getScreenHeight();
		int offset = 3;
		
		graphics.setStroke(Color.BLACK);
		
		if(isPressed) {
			graphics.setFill(Color.WHITE);
		} else {
			graphics.setFill(Color.DARKGRAY);
		}
		
		graphics.fillRect(x + offset, y + offset, width - offset, height - offset);
		graphics.strokeRect(x + offset, y + offset, width - offset, height - offset);
		
		if(!isPressed) {
			graphics.setFill(Color.WHITE);
			
			graphics.fillRect(x, y, width - offset, height - offset);
			graphics.strokeRect(x, y, width - offset, height - offset);
			
			graphics.strokeLine(x, y + height - offset, x + offset, y + height);
			graphics.strokeLine(x + width - offset, y, x + width, y + offset);
			graphics.strokeLine(x + width - offset, y + height - offset, x + width, y + height);
		}
	}
}
