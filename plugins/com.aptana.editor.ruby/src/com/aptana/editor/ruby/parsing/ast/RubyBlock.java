/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.ruby.parsing.ast;

import com.aptana.ruby.core.IRubyElement;

public class RubyBlock extends RubyElement
{

	public RubyBlock(int start, int end)
	{
		super(start, end);
	}

	@Override
	public short getNodeType()
	{
		return IRubyElement.BLOCK;
	}
}
