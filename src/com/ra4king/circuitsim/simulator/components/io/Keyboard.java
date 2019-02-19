package com.ra4king.circuitsim.simulator.components.io;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.ra4king.circuitsim.simulator.CircuitState;
import com.ra4king.circuitsim.simulator.Component;
import com.ra4king.circuitsim.simulator.WireValue;
import com.ra4king.circuitsim.simulator.WireValue.State;

/**
 * @author Adithya Nott
 */
public class Keyboard extends Component {
	public static final int PORT_CLK = 0;
	public static final int PORT_ENABLE = 1;
	public static final int PORT_CLEAR = 2;
	public static final int PORT_DATA = 3;
	public static final int PORT_AVAILABLE = 4;

	private final int bufferMaxLength;

	private int bufferLength;

	public Keyboard(String name, int bufferMaxLength) {
		super(name, new int[] { 1, 1, 1, 1, 7, 1 });
		
		if(bufferLength > 256 || bufferLength <= 0) {
			throw new IllegalArgumentException("Buffer length cannot exceed 256.");
		}
		
		this.bufferMaxLength = bufferMaxLength;
		this.bufferLength = 0;
	}
	
	public int getBufferLength() {
		return bufferLength;
	}
	
	
	


	public void bufferPush(CircuitState state, char value) {
		if (bufferLength < bufferMaxLength) {
			getBufferContents(state)[bufferLength] = value;
			bufferLength++;
		}
	}

	public void keyboardOutputUpdate(CircuitState state) {
		if (bufferLength > 0) {
			state.pushValue(getPort(PORT_DATA), WireValue.of(getBufferContents(state)[0], 7));
			state.pushValue(getPort(PORT_AVAILABLE), WireValue.of(1, 1));
		} else {
			state.pushValue(getPort(PORT_DATA), WireValue.of(0, 7));
			state.pushValue(getPort(PORT_AVAILABLE), WireValue.of(0, 1));
		}

	}

	public void bufferPop(CircuitState state) {
		if (bufferLength > 0) {
			for (int i = 0; i < bufferMaxLength; i++) {
				getBufferContents(state)[i] = getBufferContents(state)[i+1];
			}
			bufferLength--;
		}
	}


	public char[] getBufferContents(CircuitState state) {
		return (char[])state.getComponentProperty(this);
	}
	
	@Override
	public void init(CircuitState state, Object lastProperty) {
		state.putComponentProperty(this, new char[bufferLength]);
	}
	
	@Override
	public void valueChanged(CircuitState state, WireValue value, int portIndex) {
		char[] memory = getBufferContents(state);
		
		boolean enabled = state.getLastReceived(getPort(PORT_ENABLE)).getBit(0) != State.ZERO;
		boolean clear = state.getLastReceived(getPort(PORT_CLEAR)).getBit(0) == State.ONE;
		
		
		switch(portIndex) {
			case PORT_ENABLE:
			case PORT_CLK:
				if(enabled && value.getBit(0) == State.ONE) {
					bufferPop(state);
				}
				break;
			case PORT_CLEAR:
				if(clear) {
					bufferLength = 0;
				}
				break;
		}
	}
}
