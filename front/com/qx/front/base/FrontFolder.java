package com.qx.front.base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class FrontFolder extends FrontResourceDescriptor {

	private String path;
	
	public FrontFolder(String path) {
		super();
		this.path = path;
	}

	@Override
	public void register(FrontResourceLoader loader, List<FrontResourceHandle> list) {
		Path folderPath = loader.rootPath.resolve(path);
		walk(loader, folderPath, list);
	}

	private void walk(FrontResourceLoader loader, Path folderPath, List<FrontResourceHandle> list) {
		try {
			Files.list(folderPath).forEach(path -> {
				if(path.toFile().isDirectory()) {
					walk(loader, path, list);
				}
				else {
					String key = loader.rootKey+'/'+loader.rootPath.relativize(path).toString();
					list.add(new FrontResourceHandle(key, path));
				}
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
