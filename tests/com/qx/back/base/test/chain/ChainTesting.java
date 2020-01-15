package com.qx.back.base.test.chain;

import com.qx.level0.utilities.chain.QxChain;
import com.qx.level0.utilities.chain.QxChainLinkHandle;
import com.qx.level0.utilities.chain.QxChainable;
import com.qx.level0.utilities.chain.QxChain.LinkConsumer;

public class ChainTesting {

	public static class MyProject implements QxChainable<MyProject> {

		private QxChainLinkHandle<MyProject> handle;
		
		private int index;
		
		public MyProject(int index) {
			super();
			this.index = index;
		}
		
		@Override
		public void setChainLinkHandle(QxChainLinkHandle<MyProject> handle) {
			this.handle = handle;
		}

		public QxChainLinkHandle<MyProject> getChainLinkHandle() {
			return handle;
		}

		@Override
		public void onChainLinkDetached() {
			System.out.println("I'm detached! Help!!");
		}
		
		@Override
		public String toString() {
			return Integer.toString(index);
		}
	}
	
	public static void main(String[] args) {
		
		QxChain<MyProject> chain = new QxChain<MyProject>();
		
		MyProject chainable, special0=null, special1=null, special2=null;
		
		for(int i=0; i<100; i++) {
			chainable = new MyProject(i);
			if(i==22) {
				special0 = chainable;
			}
			else if(i==23) {
				special1 = chainable;
			}
			else if(i==77) {
				special2 = chainable;
			}
			chain.pushLast(chainable);
		}
		chain.traverse(new LinkConsumer<MyProject>() {
			public @Override boolean consume(MyProject object) {
				System.out.print(object+", ");
				return false;
			}
		});
		System.out.println();
		
		special0.getChainLinkHandle().detach();
		chain.traverse(new LinkConsumer<MyProject>() {
			public @Override boolean consume(MyProject object) {
				System.out.print(object+", ");
				return false;
			}
		});
		System.out.println();
		
		special1.getChainLinkHandle().detach();
		chain.traverse(new LinkConsumer<MyProject>() {
			public @Override boolean consume(MyProject object) {
				System.out.print(object+", ");
				return false;
			}
		});
		System.out.println();
		
		special2.getChainLinkHandle().moveFirst();
		chain.traverse(new LinkConsumer<MyProject>() {
			public @Override boolean consume(MyProject object) {
				System.out.print(object+", ");
				return false;
			}
		});
		System.out.println();
	}
}
