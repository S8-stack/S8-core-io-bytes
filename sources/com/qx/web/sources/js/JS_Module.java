package com.qx.web.sources.js;

import java.util.ArrayList;
import java.util.List;

public class JS_Module {
	
	private String JSON_catalog;
	
	private List<JS_Source> sources;
	
	public JS_Module() {
		super();
		this.sources = new ArrayList<>();
	}

	public void add(JS_Source source) {
		sources.add(source);
	}
	
	public String getCatalog() {
		if(JSON_catalog==null) {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			boolean hasItem = false;
			for(JS_Source source : sources) {
				if(hasItem) { builder.append(','); } else { hasItem = true; }
				builder.append("\n\"");
				builder.append(source.getWebPathname());
				builder.append('"');
			}
			builder.append("\n]");
			JSON_catalog = builder.toString();
		}
		return JSON_catalog;
	}

}
