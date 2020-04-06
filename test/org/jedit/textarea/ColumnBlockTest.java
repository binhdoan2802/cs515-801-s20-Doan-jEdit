package org.jedit.textarea;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Vector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.gjt.sp.jedit.textarea.ColumnBlock;
import org.gjt.sp.jedit.textarea.Node;

public class ColumnBlockTest {

	void setUp() throws Exception {
	}
	
	void tearDown() throws Exception {
	}

	@Test
	public final void testSetTabSizeDirtyStatus() {
		ColumnBlock blockA = new ColumnBlock();
		ColumnBlock blockB = new ColumnBlock();
		ColumnBlock blockB1 = new ColumnBlock();
		ColumnBlock blockB2 = new ColumnBlock();
		
		
		Vector<Node> children = new Vector<Node>();
		children.add(blockA);
		children.add(blockA);
		children.elementAt(0).addChild(blockB1);
		blockB1.addChild(blockB2);

		((ColumnBlock)children.elementAt(0)).setTabSizeDirtyStatus(false,
				true);
		
		assertEquals(false, ((ColumnBlock)children.elementAt(0)).areTabSizesDirty());
		assertEquals(false, ((ColumnBlock)children.elementAt(1)).areTabSizesDirty());
		assertEquals(true, ((ColumnBlock)children.elementAt(1).getChildren().elementAt(0)).areTabSizesDirty());
		assertEquals(true, ((ColumnBlock)
				((ColumnBlock)children.elementAt(1).getChildren().elementAt(0)).
				getChildren().elementAt(0)).areTabSizesDirty());
	}

}
