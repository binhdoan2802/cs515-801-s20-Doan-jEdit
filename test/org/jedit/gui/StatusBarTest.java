package org.jedit.gui;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.*;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyBoolean;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.text.Segment;
import javax.swing.JLabel;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.gui.StatusBar;
import org.gjt.sp.jedit.gui.statusbar.ToolTipLabel;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.util.StandardUtilities;

@RunWith(PowerMockRunner.class)
@PrepareForTest({jEdit.class,StandardUtilities.class})
public class StatusBarTest {

	void setUp() throws Exception {
	}
	
	void tearDown() throws Exception {
	}

	@Test
	public final void testUpdateCaretStatus() {
		//Setup values used in the method updateCaretStatus
		int caretPosition = 99;
		int caretLine = 88;			// current line
		int bufferLineCnt = 99;		// total number of lines
		int lineStart = 5;
		int bufferLength = 50;
		int virtualPosition = 0;
		int start = 0;
	
		//Setup classes used in the method updateCaretStatus
		JEditTextArea textarea = Mockito.mock(JEditTextArea.class);
		Buffer buffer = Mockito.mock(Buffer.class);
		View view = Mockito.mock(View.class);
		StatusBar statusbar =  Mockito.mock(StatusBar.class);//cannot instantiate StatusBar class for some reason -> cannot use spy here
		Segment seg = new Segment();
		StringBuilder buf = new StringBuilder();
		ToolTipLabel caretStatus = Mockito.mock(ToolTipLabel.class);
		
		//Mock methods used in the method updateCaretStatus to return predictable values
		Mockito.when(buffer.getLineCount()).thenReturn(bufferLineCnt);
		Mockito.when(buffer.isLoaded()).thenReturn(true);
		Mockito.when(buffer.getLength()).thenReturn(bufferLength);
		Mockito.when(view.getBuffer()).thenReturn(buffer);
		
		Mockito.when(textarea.getLineStartOffset(caretLine)).thenReturn(lineStart);
		Mockito.when(textarea.getCaretPosition()).thenReturn(caretPosition);
		Mockito.when(textarea.getCaretLine()).thenReturn(caretLine);
		Mockito.when(textarea.getLineStartOffset(anyInt())).thenReturn(start);
		Mockito.when(view.getTextArea()).thenReturn(textarea);
		
		//Mock static methods used in the method updateCaretStatus
		PowerMockito.mockStatic(jEdit.class);
		Mockito.when(jEdit.getBooleanProperty(anyString(), anyBoolean())).thenReturn(true);
		
		PowerMockito.mockStatic(StandardUtilities.class);
		Mockito.when(StandardUtilities.getVirtualWidth(any(Segment.class),anyInt())).thenReturn(virtualPosition);

		//Set class member variables for a nominal run
		Whitebox.setInternalState(statusbar, "showCaretStatus", true);
		Whitebox.setInternalState(statusbar, "view", view);
		Whitebox.setInternalState(statusbar, "seg", seg);
		Whitebox.setInternalState(statusbar, "buf", buf);
		Whitebox.setInternalState(statusbar, "caretStatus", caretStatus);
		Whitebox.setInternalState(textarea, "buffer", buffer);
		
		
		//NOTE: this section is for my personal learning purpose
		//Mockito.when(statusbar.updateCaretStatus()).thenCallRealMethod();
		//The above method is for getValue() that returns something
		
		
		//since this is a mocked class (not spy), force these methods to actually execute when being called
		int dot = caretPosition - start;
		Mockito.doCallRealMethod().when(statusbar).updateCaretStatus();
		Mockito.doCallRealMethod().when(statusbar).showStatusBarInfo(caretLine, dot, virtualPosition);
		Mockito.doCallRealMethod().when(statusbar).showMoreStatusBarInfo(caretPosition, bufferLength);
		Mockito.doCallRealMethod().when(caretStatus).setText(anyString());
		
		//run
		statusbar.updateCaretStatus();
		
		//output
		String str =(String)Whitebox.getInternalState(caretStatus, "text");
		
		//this string is what will appear on the status bar based on this test configuration
		assertEquals("89,100-1 (99/50)", str);
	}

}
