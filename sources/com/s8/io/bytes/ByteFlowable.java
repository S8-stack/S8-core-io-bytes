package com.s8.io.bytes;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.bytes.api.ByteInflow;
import com.s8.io.bytes.api.ByteOutflow;


/**
 * Can parse from a <code>ByteInflow</code> and compose to a <code>ByteOutflow</code>.
 * @author pc
 *
 */
public interface ByteFlowable {

	public final static String FACTORY_KEYWORD = "BYTE_FACTORY";

	/**
	 * <p>
	 * <b>Important explanation on why it's coded that way</b>.
	 * </p>
	 * <p>
	 * Assume you have a -slightly- complex ByteOutflowable object: it's called
	 * <code>ThFluid</code> and it contains all properties of a given fluid. Let's
	 * ay that they are limite nb of fluids (air, water, methane, etc.) so that you
	 * can simply assign a number to them (CAS number in real life). The trick is
	 * that there is multiple types of fluids (like for instance
	 * <code>ImcompressibleFluid</code> or <code>PerfetGasThFluid</code>, eah
	 * bringing it's own formula, equation of state and so on. So when reading the
	 * encoding of the fluid, there is a rot switch that will call sub-factories (on
	 * specialized on perfect gas, another one in incompressible fluid, etc.).
	 * </p>
	 * <p>
	 * That being said, say you are now building a parsing/composing engine for
	 * serilization/persistency or whatever. You stumble on a field which has a type
	 * that actually implementts the <code>ByteOutflowable</code> interface, but it
	 * MUST have a method to indicate the PROPER factory to be used for
	 * deserializing this type of field. In the case described in previous
	 * paragraph, the factory cannot be a constant of for instance
	 * <code>ImcompressibleFluid</code> since type parsing may need to be done at an
	 * higher level (for instance <code>ThFluid</code>) to enable the possibility to
	 * switch dynamically between differnt types of fluid with a unified encoding
	 * (for instance first type gives the type of fluid and allows to switch to a
	 * give factory, then each factory will continue to read bytes, each one havong
	 * it's own byte pattern). If relying on constant fields, override is extremly
	 * painful since we cannot select from a specific class the right factory. For
	 * instance tf the type of the fiekd is simply Impcoressible, we might want to
	 * restrain encoding to simplythe one give by this specific factory.
	 * </p>
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static ByteFactory getFactory(Class<?> type) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = type.getField(FACTORY_KEYWORD);
		return (ByteFactory) field.get(null);

	}

	/**
	 * <b>OPTIONAL</b>
	 * MUST be implemented as a class constant with name = PROTOTYPE_KEYWORD (for reflection purposes)
	 *
	 */
	public interface ByteFactory {

		/**
		 * parse from inflow
		 * 
		 * @param inflow
		 * @throws IOException
		 */
		public ByteFlowable pull(ByteInflow inflow, ByteCount count) throws IOException;	
	}


	public static enum Mode {

		/**
		 * In this mode, implementation must guarantee that multiple read/write do not alter data accuracy.
		 */
		ISO,

		/**
		 * In this mode, all relevant compressions (like converting double to float) are enabled to save byte-count on the network
		 */
		NETWORK
	}


	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void push(Mode mode, ByteOutflow outflow) throws IOException;

}
