package com.qx.back.base.io.csv.mapped;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qx.back.base.io.units.QxScientificUnit;

/**
 * 
 * @author pc
 *
 * @param <T>
 */
public class CSV_Engine<T> {

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
	public CSV_Engine(Class<T> type) throws NoSuchMethodException, SecurityException {
		super();
		constructor = type.getConstructor(new Class<?>[]{});
		CSV_Mapping mappingAnnotation;

		getters = new HashMap<>();
		setters = new HashMap<>();

		// screen fields
		for(Field field : type.getFields()){
			mappingAnnotation = field.getAnnotation(CSV_Mapping.class);
			if(mappingAnnotation!=null){
				String column = mappingAnnotation.tag();
				FieldMapping mapping = FieldMapping.build(field); 
				getters.put(column, mapping);
				setters.put(column, mapping);
			}
		}

		// screen setters
		for(Method method : type.getMethods()){
			mappingAnnotation = method.getAnnotation(CSV_Mapping.class);
			if(mappingAnnotation!=null){
				String column = mappingAnnotation.tag();
				MethodSetter setter = MethodSetter.build(method);
				if(setter!=null){
					setters.put(column, setter);
				}
				// TODO same things for getters
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
					QxScientificUnit[] units = new QxScientificUnit[n];

					String tag, unit;
					for(int i=0; i<n; i++){
						Matcher matcher = tagPattern.matcher(headers[i]);
						matcher.find();
						tag = matcher.group(1);
						unit = matcher.group(3);
						structure[i] = setters.get(tag);
						if(unit!=null){
							units[i] = new QxScientificUnit(unit);	
						}
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

