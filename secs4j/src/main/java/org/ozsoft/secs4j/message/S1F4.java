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

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.A;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;
import org.ozsoft.secs4j.format.U4;
import org.psoft.secs4j.database.EquipmentVariable;

/**
 * S1F4 Selected Equipment Status Data
 * 
 * sent by Host Only
 *  
 * @author Caifenglei
 */
public class S1F4 extends SecsReplyMessage {

    private static final int STREAM = 1;

    private static final int FUNCTION = 4;
    
    private static final boolean WITH_REPLY = false;
    
    private static final String DESCRIPTION = "Selected Equipment Status Data";
    
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
	 * 		SV <A>
	 * }
     */
    protected void parseData(Data<?> data) throws SecsParseException {
        if (data == null) {
            throw new SecsParseException("Data missing for S1F4");
        }
        
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() == 0) {
        	//no status data exist
        } else {
        	
        	int len = l.length();
        	for (int i = 0; i < len; i++) {
        		
        		Data<?> dataItem = l.getItem(i);
        		if(!(dataItem instanceof A)) {
        			throw new SecsParseException("SV must be of type A");
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
	 * 		SV <A>
	 * }
	 */
	protected Data<?> getData() throws SecsParseException {

    	ArrayList<Long> params = new ArrayList<Long>();
    	for(Data<?> svid: svids.getValue()) {
    		params.add(((U4) svid).getValue(0));
    	}
    	List<?> records =  EquipmentVariable.getNewestRecords(params);
    	
    	LOG.info("queried records: " + records.size());
    	L svs = new L();
    	for(int i = 0; i < records.size(); i++) {
    		
    		EquipmentVariable ev = (EquipmentVariable) records.get(i);
    		String vv = ev.getVariableValue();
    		svs.addItem(new A(vv));
    	}
    	
		return svs;
	}
	
	public void setSvids(L svidList)
	{
		this.svids = svidList;
	}

    @Override
    protected void handle()  throws SecsException{
        
    }

	public L getSvids() {
		return svids;
	}

}
