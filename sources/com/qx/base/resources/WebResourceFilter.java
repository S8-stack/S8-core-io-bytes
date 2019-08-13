package com.qx.base.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import com.qx.lang.xml.annotation.XML_SetElement;
import com.qx.lang.xml.annotation.XML_Type;

@XML_Type(name="filtered", sub={})
public class WebResourceFilter extends WebResourceDescriptor {

	private Pattern pattern;

	private String filter;

	private Path packagePath;

	private String filterWebPathname;

	@XML_SetElement(name="filter")
	public void setFilter(String filter) {
		this.filter = filter;
	}

	@XML_SetElement(name="package")
	public void setPackagePath(String packagePath) {
		if(packagePath==null) {
			packagePath = "";
		}
		// package
		this.packagePath = Paths.get(packagePath);
	}

	@XML_SetElement(name="web")
	public void setWebPathname(String pathname) {
		if(pathname==null) {
			pathname = "";
		}
		this.filterWebPathname = pathname;
	}


	@Override
	public void load(WebResourcesBase base, Path rootPath, String moduleWebPathname, boolean isVerbose) {
		// pattern
		String preprocessedRegex = preprocessRegex(filter);
		pattern = Pattern.compile(preprocessedRegex);	
		loadFolderContent(base, rootPath.resolve(packagePath), "", moduleWebPathname+filterWebPathname, isVerbose);
	}


	/**
	 * 
	 * @param base
	 * @param folderPath
	 * @param localFolderPathname EMPTY or always ending with '/'
	 * @param webFolderPathname EMPTY or always ending with '/'
	 * @param verbosity
	 */
	private void loadFolderContent(
			WebResourcesBase base, 
			Path folderPath, 
			String localFolderPathname, 
			String webFolderPathname, 
			boolean isVerbose) {
		try {

			Files.list(folderPath).forEach(path -> {
				if(path.toFile().isDirectory()) {
					loadFolderContent(
							base, 
							path, 
							localFolderPathname+path.getFileName()+'/', 
							webFolderPathname+path.getFileName()+'/',
							isVerbose);
				}
				else {
					String filePathname = path.getFileName().toString();
					String localPathname = localFolderPathname+filePathname;
					boolean isMatched = pattern.matcher(localPathname).matches();
					if(isVerbose && !isMatched){
						System.out.println("[WebResourceFilter] REJECTED: "+localPathname);
					}
					
					if(isMatched) {
						String webFilePathname = webFolderPathname+filePathname;
						base.add(new WebResource(path, webFilePathname, fragmentLength, isCaching), isVerbose);		
					}
					
				}
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}


	public final static String preprocessRegex(String regex) {
		StringBuilder builder = new StringBuilder();		
		int i=0, n = regex.length();
		char c;
		while(i<n) {
			c = regex.charAt(i);
			switch(c) {
			
			case ' ': 
				// do nothing -> skip white space
				break;
				
			case '\n': 
				// do nothing -> skip end of line
				break;
				
			case '\t': 
				// do nothing -> skip tab
				break;
				
			case '$':
				// replace all * by filename regex
				builder.append("[\\w-]*");
				break;
				
			case '*':
				// replace all + by pathname regex
					builder.append("[/\\w-]*");
				break;
				
			case '.':
				// . is always used escaped
				builder.append("\\.");
				break;
			
				// change options group
			case '{': 
				builder.append('(');
				break;
				
			case ',': 
				builder.append('|'); 
				break;
				
			case '}': 
				builder.append(')'); 
				break;
			
			default:
				builder.append(c); 
				break;
			}
			i++;
		}
		
		return builder.toString();
	}


}
