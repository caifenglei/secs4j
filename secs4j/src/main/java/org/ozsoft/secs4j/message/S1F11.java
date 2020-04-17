package org.ozsoft.secs4j.message;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsPrimaryMessage;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;
import org.ozsoft.secs4j.format.U4;

/**
 * S1F11 - Status Variable Namelist Request, If host sends L:0 to request all SVIDs.
 * 
 * @author caifenglei
 *
 */
public class S1F11 extends SecsPrimaryMessage
{
	private static final int STREAM = 1;
	
	private static final int FUNCTION = 11;
	
	private static final boolean WITH_REPLY = true;
	
	private static final String DESCRIPTION = "Status Variable Namelist Request";
	
	private List<U4> svids = new ArrayList<U4>();

	@Override
	/**
	 * return my secondary message
	 */
	protected SecsReplyMessage handle() throws SecsException {
		
		S1F12 s1f12 = new S1F12();
		s1f12.setSvids(svids);
		
		return s1f12;
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
	/**
	 * parse received data
	 * <SVID> - Status variable ID 
	 * 
	 * Format:
	 * {L:n
	 * 		SVID
	 * }
	 */
	protected void parseData(Data<?> data) throws SecsParseException {

		if (data == null) {
            throw new SecsParseException("Missing data");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        L l = (L) data;
        if (l.length() == 0) {
            // get all status variable definitions
        } else {
        	
        	int len = l.length();
        	for (int i = 0; i < len; i++) {
        		
        		Data<?> dataItem = l.getItem(i);
        		if(!(dataItem instanceof U4)) {
        			throw new SecsParseException("SVID must be of type U4");
        		}
       
        		setSVID((U4) dataItem);
            }
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

		L l = new L();
		for (int i = 0; i < svids.size(); i++) {
			l.addItem(svids.get(i));
		}
	
		return l;
	}
	
	public void setSVID(U4 svid)
	{
		this.svids.add(svid);
	}

}
