/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.erb.common;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class ERBEditorTestCase
{

	private ITextEditor editor;

//	@Override
	@After
	public void tearDown() throws Exception
	{
		if (editor != null)
		{
			editor.close(false);
		}
	}

	@Test
	public void testExecute() throws Exception
	{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		editor = (ITextEditor) page.openEditor(new FileStoreEditorInput(getFileStore()), getEditorId(), false,
				IWorkbenchPage.MATCH_INPUT);
		assertNotNull(editor);
		assertEquals(getClassName(), editor.getClass().getName());
	}

	protected abstract IFileStore getFileStore() throws Exception;

	protected abstract String getEditorId();

	protected abstract String getClassName();
}
