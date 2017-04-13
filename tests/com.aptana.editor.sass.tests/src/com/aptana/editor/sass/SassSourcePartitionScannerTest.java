/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.sass;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;

import com.aptana.editor.common.ExtendedFastPartitioner;
import com.aptana.editor.common.IExtendedPartitioner;
import com.aptana.editor.common.NullPartitionerSwitchStrategy;
import com.aptana.editor.common.text.rules.CompositePartitionScanner;
import com.aptana.editor.common.text.rules.NullSubPartitionScanner;

/**
 * @author Chris
 * @author Sandip
 */
@SuppressWarnings("nls")
public class SassSourcePartitionScannerTest
{

	private IDocumentPartitioner partitioner;

	private void assertContentType(String contentType, String code, int offset)
	{
		assertEquals("Content type doesn't match expectations for: " + code.charAt(offset), contentType,
				getContentType(code, offset));
	}

//	@Override
	@After
	public void tearDown() throws Exception
	{
		partitioner = null;
//		super.tearDown();
	}

	private String getContentType(String content, int offset)
	{
		if (partitioner == null)
		{
			IDocument document = new Document(content);
			CompositePartitionScanner partitionScanner = new CompositePartitionScanner(SassSourceConfiguration
					.getDefault().createSubPartitionScanner(), new NullSubPartitionScanner(),
					new NullPartitionerSwitchStrategy());
			partitioner = new ExtendedFastPartitioner(partitionScanner, SassSourceConfiguration.getDefault()
					.getContentTypes());
			partitionScanner.setPartitioner((IExtendedPartitioner) partitioner);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return partitioner.getContentType(offset);
	}

	@Test
	public void testPartitioningOfEmittedCommentLine()
	{
		String source =
		// 0123456789012345678901234567890123456 789
		" /* This is Sass comment on one Line */\n";

		assertContentType(SassSourceConfiguration.DEFAULT, source, 0);
		for (int i = 1; i < source.length() - 1; i++)
		{
			assertContentType(SassSourceConfiguration.MULTI_LINE_COMMENT, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, source.length() - 1);
	}

	@Test
	public void testPartitioningOfSilentCommentLine()
	{
		String source =
		// 01234567890123456789012345678901234 5678901234567890
		" // This is Sass comment on one Line\n ";

		assertContentType(SassSourceConfiguration.DEFAULT, source, 0);
		for (int i = 1; i <= 35; i++)
		{
			assertContentType(SassSourceConfiguration.SINGLE_LINE_COMMENT, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 37);
	}

	@Test
	public void testPartitioningOfMultiLineComment()
	{
		String source = "/**\n" + //
				" * This is Sass comment\n" + //
				" * spanning multiple lines\n" + //
				" */\n";

		for (int i = 0; i < source.length() - 1; i++)
		{
			assertContentType(SassSourceConfiguration.MULTI_LINE_COMMENT, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, source.length() - 1);
	}

	@Test
	public void testPartitioningOfSingleQuotedString()
	{
		String source =
		// 012345678901234567890123456789012345678 901234567890
		"' This is a single quoted Sass string'\n";
		for (int i = 0; i <= 37; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_SINGLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 38);
	}

	@Test
	public void testPartitioningOfEmptySingleQuotedString()
	{
		String source =
		// 1 2 3 4 5
		// 01234567890123456789012345678901234567 8901234567890
		"''\n";
		for (int i = 0; i <= 1; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_SINGLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 2);
	}

	@Test
	public void testPartitioningOfSingleQuotedStringWithEscape()
	{
		String source =
		// 012345678901234567890123456789012345678901234567890 1234 56
		"' This is a single quoted Sass string with escape \\' '\n";

		for (int i = 0; i <= 53; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_SINGLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 54);
	}

	@Test
	public void testPartitioningOfSingleQuotedStringWithDoubleQuote()
	{
		String source =
		// 012345678901234567890123456789012345678901234567890123456 789 012
		"' This is a single quoted Sass string with double quote \" '\n";

		for (int i = 0; i <= 58; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_SINGLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 59);
	}

	@Test
	public void testPartitioningOfDoubleQuotedString()
	{
		String source =
		// 0 1234567890123456789012345678901234567 8 901234567890
		"\" This is a double quoted Sass string\"\n";
		for (int i = 0; i <= 37; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_DOUBLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 38);
	}

	@Test
	public void testPartitioningOfEmptyDoubleQuotedString()
	{
		String source =
		// 0 1 2 34567890123456789012345678901234567 8901234567890
		"\"\"\n";
		for (int i = 0; i <= 1; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_DOUBLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 2);
	}

	@Test
	public void testPartitioningOfDoubleQuotedStringWithEscape()
	{
		String source =
		// 0 12345678901234567890123456789012345678901234567890 1 23 4 5
		"\" This is a double quoted Sass string with escape \\\" \"\n";

		for (int i = 0; i <= 53; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_DOUBLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 54);
	}

	@Test
	public void testPartitioningOfDoubleQuotedStringWithSingleQuote()
	{
		String source =
		// 0 1234567890123456789012345678901234567890123456789012345678 9 01
		"\" This is a double quoted Sass string with single quote ' \"\n";

		for (int i = 0; i <= 58; i++)
		{
			assertContentType(SassSourceConfiguration.STRING_DOUBLE, source, i);
		}
		assertContentType(SassSourceConfiguration.DEFAULT, source, 59);
	}

	@Test
	public void testPartitioningOfAllPartitions()
	{
		String source =
		// 012345678901 23456789012 3456789012345678 9012345678 9012345 6 7890
		" /* emitted */\n // silent\n val = 'single'\n other = \"double\"\n";

		assertContentType(SassSourceConfiguration.DEFAULT, source, 0);
		assertContentType(SassSourceConfiguration.MULTI_LINE_COMMENT, source, 1);
		assertContentType(SassSourceConfiguration.MULTI_LINE_COMMENT, source, 13);
		assertContentType(SassSourceConfiguration.DEFAULT, source, 15);
		assertContentType(SassSourceConfiguration.SINGLE_LINE_COMMENT, source, 16);
		assertContentType(SassSourceConfiguration.SINGLE_LINE_COMMENT, source, 24);
		assertContentType(SassSourceConfiguration.DEFAULT, source, 26);
		assertContentType(SassSourceConfiguration.DEFAULT, source, 32);
		assertContentType(SassSourceConfiguration.STRING_SINGLE, source, 33);
		assertContentType(SassSourceConfiguration.STRING_SINGLE, source, 40);
		assertContentType(SassSourceConfiguration.DEFAULT, source, 41);
		assertContentType(SassSourceConfiguration.DEFAULT, source, 50);
		assertContentType(SassSourceConfiguration.STRING_DOUBLE, source, 51);
		assertContentType(SassSourceConfiguration.STRING_DOUBLE, source, 58);
		assertContentType(SassSourceConfiguration.DEFAULT, source, 59);
	}

}
