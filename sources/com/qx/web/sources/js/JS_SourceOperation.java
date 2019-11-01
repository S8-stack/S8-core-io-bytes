package com.qx.web.sources.js;

public class JS_SourceOperation {


	/*
	public static String compress(String source) {
		JS_SourceOperation operation = new JS_SourceOperation(source);
		return operation.run(operation.new CodeScope());
	}


	private String source;

	private int index;

	private int length;

	private StringBuilder buffer;
	
	private char c1;
	
	private char c2;
	
	private boolean hasNext;
	



	private JS_SourceOperation(String source) {
		this.source = source;
		this.index = 0;
		this.length = source.length();
		this.buffer = new StringBuilder();
	}

	private String run(State state) {
		while(state!=null) {
			state = state.read();	
			
		}
		return buffer.toString();
	}

	private void next(boolean isAppended) {
		
	}
	
	

	private interface State {

		public State read();

	}

	private class CodeScope implements State {

		@Override
		public State read() {
			
			if(!hasNext) {
				return null;
			}
			char c1 = source.charAt(index++);
			
			if(!hasNext) {
				buffer.append(c1);
				return null;
			}
			c2 = source.charAt(index++);

			while(index<length) {
				

				if(c1=='/' && c2=='*') {
					return new MultiLinesCommentScope();
				}
					else if(c1=='/' && c2=='/') {
						return new EndOfLineCommentScope();
					}
				}
				else if(c2=='"'){
					buffer.append(c1);
					return new StringScope();
				}
				// skip spaces, tabs and line breaks
				else if(c2!=' ' && c2!='\t' && c2!='\n'){
					buffer.append(c1);
				}
			}
			return null;
		}
	}


	private class MultiLinesCommentScope implements State {

		@Override
		public State read() {
			char c1=next(), c2 = '?';
			while(hasNext()) {
				c1 = c2; c2 = next();
				if(c1=='*' && c2=='/') {
					return new CodeScope();
				}
				// skip everything else
			}
			return null;
		}
	}

	private class EndOfLineCommentScope implements State {

		@Override
		public State read() {
			char c;
			while(hasNext()) {
				c = next();	
				if(c=='\n') {
					return new CodeScope();
				}
				// skip everything else
			}
			return null;
		}
	}

	private class StringScope implements State {

		@Override
		public State read() {
			buffer.append('"');
			char c;
			while(hasNext()) {
				c = next();	

				if(c=='"') {
					buffer.append(c);
					return new CodeScope();
				}
				else { // append everything else
					buffer.append(c);
				}
			}
			return null;
		}
	}
	*/
}
