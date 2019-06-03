package com.qx.back.base.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FrontFolder extends FrontResourceDescriptor {

	private String path;
	
	private int fragmentLength;
	
	private boolean isCaching;
	
	public FrontFolder(String path, int fragmentLength, boolean isCaching) {
		super();
		this.path = path;
		this.fragmentLength = fragmentLength;
		this.isCaching = isCaching;
	}
	
	public FrontFolder(String path) {
		super();
		this.path = path;
		this.fragmentLength = 8192;
		this.isCaching = true;
	}

	@Override
	public void register(FrontResourceBase base, Path rootPath, String rootPathname) {
		Path folderPath = rootPath.resolve(path);
		walk(base, rootPath, rootPathname, folderPath);
	}

	private void walk(FrontResourceBase base, Path rootPath, String rootPathname, Path folderPath) {
		try {
			Files.list(folderPath).forEach(path -> {
				if(path.toFile().isDirectory()) {
					walk(base, rootPath, rootPathname, path);
				}
				else {
					String pathname = rootPathname+'/'+rootPath.relativize(path).toString();
					base.add(new FrontResource(path, pathname, fragmentLength, isCaching));
				}
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
