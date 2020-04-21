package org.ozsoft.secs4j.message;

import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsPrimaryMessage;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;
import org.ozsoft.secs4j.format.U4;

/**
 * S1F23 - Collection Event Namelist Request, Host sends L:0 to imply all CEIDs. (proposed ballot item 4824B, Oct 2011)
 * @author caifenglei
 *
 */
public class S1F23 extends SecsPrimaryMessage
{
	private static final int STREAM = 1;
	
	private static final int FUNCTION = 23;
	
	private static final boolean WITH_REPLY = true;
	
	private static final String DESCRIPTION = "Collection Event Namelist Request";
	
	private L ceids = new L();

	@Override
	protected SecsReplyMessage handle() throws SecsException {
		
		S1F24 s1f24 = new S1F24();
		s1f24.setCeids(ceids);
		
		return s1f24;
	}

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
	protected void parseData(Data<?> data) throws SecsParseException {
		
		if (data == null) {
            throw new SecsParseException("Missing data for S1F23");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() == 0) {
            // get all event definitions
        } else {
        	
        	int len = l.length();
        	for (int i = 0; i < len; i++) {
        		
        		Data<?> dataItem = l.getItem(i);
        		if(!(dataItem instanceof U4)) {
        			throw new SecsParseException("CEID must be of type U4");
        		}
            }
        	setCeids(l);
        }
		
	}

	@Override
	protected Data<?> getData() throws SecsParseException {
		return ceids;
	}
	
	public void setCeids(L ceidList) 
	{
		
		this.ceids = ceidList;
	}
}
