package com.qx.io.csv.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qx.io.units.Unit;

/**
 * 
 * @author pc
 *
 * @param <T>
 */
public class CSV_BasicReader<T> {


	public interface Getter {

		public abstract String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException;
	}

	public interface Setter {
		
		public abstract void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;

	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public static FieldMapping buildFieldMapping(Field field){
		Class<?> type = field.getType();
		if(type==boolean.class){
			return new BooleanFieldMapping(field);
		}
		else if(type==short.class){
			return new ShortFieldMapping(field);
		}
		else if(type==int.class){
			return new IntegerFieldMapping(field);
		}
		else if(type==long.class){
			return new LongFieldMapping(field);
		}
		else if(type==float.class){
			return new FloatFieldMapping(field);
		}
		else if(type==double.class){
			return new DoubleFieldMapping(field);
		}
		else if(type==String.class){
			return new StringFieldMapping(field);
		}
		else {
			throw new RuntimeException("Type is not primitive : "+type.getName());
		}
	}

	public static abstract class FieldMapping implements Getter, Setter {

		protected Field field;

		private FieldMapping(Field field) {
			super();
			this.field = field;
		}
	}

	public static class DoubleFieldMapping extends FieldMapping {


		public DoubleFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setDouble(object, unit.toIS(Double.valueOf(value)));
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return Double.toString(unit.fromIS(field.getDouble(object)));
		}
	}


	public static class FloatFieldMapping extends FieldMapping {

		public FloatFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setFloat(object, (float) unit.toIS(Float.valueOf(value)));
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return Double.toString(unit.fromIS(field.getFloat(object)));
		}
	}

	public static class IntegerFieldMapping extends FieldMapping {

		public IntegerFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setInt(object, Integer.valueOf(value));
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return Integer.toString(field.getInt(object));
		}
	}

	public static class LongFieldMapping extends FieldMapping {

		public LongFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setLong(object, Long.valueOf(value));
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return Long.toString(field.getLong(object));
		}
	}

	public static class ShortFieldMapping extends FieldMapping {

		public ShortFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setShort(object, Short.valueOf(value));
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return Short.toString(field.getShort(object));
		}
	}

	public static class BooleanFieldMapping extends FieldMapping {

		public BooleanFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.setBoolean(object, Boolean.valueOf(value));
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return Boolean.toString(field.getBoolean(object));
		}
	}

	public static class StringFieldMapping extends FieldMapping {

		public StringFieldMapping(Field field) {
			super(field);
		}

		@Override
		public void set(String value, Object object, Unit unit)
				throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
			field.set(object, value);
		}

		@Override
		public String get(Object object, Unit unit) throws IllegalArgumentException, IllegalAccessException {
			return (String) field.get(object);
		}
	}




	public final static String DELIMITERS = "[ ]*,[ ]*";

	public final static String TAG_REGEX = "([a-zA-Z0-0\\-_]+) *(\\[([a-zA-Z0-9\\.\\-]+)\\])?";

	private Pattern tagPattern;


	private Constructor<T> constructor;

	private Map<String, Getter> getters;

	private Map<String, Setter> setters;

	/**
	 * 
	 * @param type
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public CSV_BasicReader(Class<T> type) throws NoSuchMethodException, SecurityException {
		super();
		constructor = type.getConstructor(new Class<?>[]{});
		CSV_Streamable mappingAnnotation;

		getters = new HashMap<>();
		setters = new HashMap<>();

		// screen fields
		for(Field field : type.getDeclaredFields()){
			field.setAccessible(true);
			mappingAnnotation = field.getAnnotation(CSV_Streamable.class);
			if(mappingAnnotation!=null){
				String column = mappingAnnotation.tag();
				FieldMapping mapping = buildFieldMapping(field); 
				getters.put(column, mapping);
				setters.put(column, mapping);
			}
		}

		// compile tag regex in a pattern
		tagPattern = Pattern.compile(TAG_REGEX);
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public Iterable<T> read(InputStream inputStream) throws IOException{

		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {

				try {
					// read headers

					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

					String headerLine = reader.readLine();
					String[] headers = headerLine.split(DELIMITERS);
					int n = headers.length;

					Setter[] structure = new Setter[n];
					Unit[] units = new Unit[n];

					String tag, unit;
					for(int i=0; i<n; i++){
						Matcher matcher = tagPattern.matcher(headers[i]);
						matcher.find();
						tag = matcher.group(1);
						unit = matcher.group(3);
						structure[i] = setters.get(tag);
						units[i] = Unit.get(unit);
					}


					return new Iterator<T>() {

						private String line = reader.readLine();


						@Override
						public boolean hasNext() {
							return line!=null;
						}

						@Override
						public T next() {
							T object = null;
							try {
								String[] values = line.split(DELIMITERS);
								object = constructor.newInstance(new Object[]{});
								for(int i=0; i<n; i++){
									structure[i].set(values[i], object, units[i]);
								}
							}
							catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException e) {
								e.printStackTrace();
							}
							try {
								line = reader.readLine();
							} catch (IOException e) {
								line = null;
							}
							return object;
						}

					};

				} catch (IOException e1) {

					e1.printStackTrace();
					return null;
				}
			}
		};
	}
}

