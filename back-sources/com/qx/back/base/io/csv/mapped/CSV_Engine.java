package com.qx.back.base.io.csv.mapped;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.qx.back.base.io.csv.mapped.type.CSV_TypeHandler;
import com.qx.back.base.io.csv.mapped.type.Setter;
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

	private CSV_TypeHandler typeHandler;

	/**
	 * 
	 * @param type
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 */
	public CSV_Engine(Class<T> type) 
			throws 
			NoSuchMethodException, 
			SecurityException, 
			NoSuchFieldException, 
			IllegalArgumentException, 
			IllegalAccessException 
	{
		super();

		constructor = type.getConstructor(new Class<?>[]{});

		typeHandler = new CSV_TypeHandler(type);

		// compile tag regex in a pattern
		tagPattern = Pattern.compile(TAG_REGEX);
	}





	public T[] toArray(Class<?> type, InputStream inputStream) throws IOException {
		List<T> list = new ArrayList<T>();
		for(T object : read(inputStream)) {
			list.add(object);
		}
		int length = list.size();

		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(type, length);
		list.toArray(array);
		return array;
	}


	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public Iterable<T> read(InputStream inputStream, Object... nonSerializedParameters) throws IOException{

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
						structure[i] = typeHandler.getSetter(tag);
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

