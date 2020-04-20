// This file is part of the secs4j project, an open source SECS/GEM
// library written in Java.
//
// Copyright 2013 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.secs4j.message;

import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsPrimaryMessage;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;
import org.ozsoft.secs4j.format.U4;

/**
 * S1F3 Selected Equipment Status Request
 * 
 * sent by Host Only
 *  
 * @author Caifenglei
 */
public class S1F3 extends SecsPrimaryMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 3;
    
    private static final boolean WITH_REPLY = true;
    
    private static final String DESCRIPTION = "Selected Equipment Status Request";
    
    private L svids = new L();
    
    @Override
    public int getStream() {
        return STREAM;
    }

    @Override
    public int getFunction() {
        return FUNCTION;
    }

    @Override
    public boolean withReply() {
        return WITH_REPLY;
    }

    @Override
    public String getDescripton() {
        return DESCRIPTION;
    }

    @Override
    /**
     * Format:
	 * {L:n
	 * 		SVID <U4>
	 * }
     */
    protected void parseData(Data<?> data) throws SecsParseException {
        if (data == null) {
            throw new SecsParseException("Data missing for S1F3");
        }
        
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() == 0) {
        	throw new SecsParseException("At lease one SVID required");
        } else {
        	
        	int len = l.length();
        	for (int i = 0; i < len; i++) {
        		
        		Data<?> dataItem = l.getItem(i);
        		if(!(dataItem instanceof U4)) {
        			throw new SecsParseException("SVID must be of type U4");
        		}
            }
        	setSvids(l);
        }
    }
    
    @Override
	/**
	 * form request data to send at the HOST side
	 * 
	 * Format:
	 * {L:n
	 * 		SVID <U4>
	 * }
	 */
	protected Data<?> getData() throws SecsParseException {

    	LOG.info("S1F3 get data length: " + svids.length());
		return svids;
	}

	@Override
	/**
	 * return my secondary message
	 */
	protected SecsReplyMessage handle() throws SecsException {
		
		S1F4 s1f4 = new S1F4();
		s1f4.setSvids(svids);
		
		return s1f4;
	}

	public void setSvids(L svidList) 
	{
		this.svids = svidList;
	}

}
