package org.ozsoft.secs4j.message;

import java.util.HashMap;
import java.util.Map;

import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.A;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;
import org.ozsoft.secs4j.format.U4;

/**
 * S1F12 - Status Variable Name List Reply, A:0 for SVNAME and UNITS indicates unknown SVID
 * 
 * @author caifenglei
 *
 */
public class S1F12 extends SecsReplyMessage
{
	private static final int STREAM = 1;
	
	private static final int FUNCTION = 12;
	
	private static final boolean WITH_REPLY = false;
	
	private static final String DESCRIPTION = "Status Variable Namelist Reply";
	
	private L svids = new L();
	
	private L variables = new L();
	
	private static final Map<Long, String> StatusVariableList;
	
	static {
		StatusVariableList = new HashMap<Long, String>();
		StatusVariableList.put(1001L, "EquipmentOperationStatus");
		StatusVariableList.put(1002L, "EquipmentOperator");
		StatusVariableList.put(1003L, "EquipmentDateTime");
	}

	@Override
	protected void handle() throws SecsException {
		
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
	 * 		{L:3
	 * 			SVID <U4> - status variable ID
	 * 			SVNAME <A> - status variable name
	 * 			UNITS <A> - units identifier (refer E5 section 9)
	 * 		}
	 * }
	 */
	protected void parseData(Data<?> data) throws SecsParseException {

		if (data == null) {
            throw new SecsParseException("Missing data");
        }
        if (!(data instanceof L)) {
            throw new SecsParseException("Root data item must be of type L");
        }
        
        L vl = (L) data;
        if (vl.length() == 0) {
            // no status variable definitions
        } else {
        	
        	int len = vl.length();
        	L sl = new L();
        	for (int i = 0; i < len; i++) {
        		
        		Data<?> dataItem = vl.getItem(i);
        		if(!(dataItem instanceof L)) {
        			throw new SecsParseException("Second level must be of type L");
        		}
        		
        		//value item
        		L vi = (L) dataItem;
        		Data<?> svidItem = vi.getItem(0);
        		if(!(svidItem instanceof U4)) {
        			throw new SecsParseException("SVID data must be of type U4");
        		}
        		sl.addItem(svidItem);
        		
        		Data<?> svnameItem = vi.getItem(1);
        		if(!(svnameItem instanceof A)) {
        			throw new SecsParseException("SVNAME data must be of type A");
        		}
        		
        		Data<?> unitsItem = vi.getItem(2);
        		if(!(unitsItem instanceof A)) {
        			throw new SecsParseException("UNITS data must be of type A");
        		}
//        		String units = ((A)unitsItem).getValue();
            }
        	this.setVariables(vl);
        	this.setSvids(sl);
        }
	}

	@Override
	/**
	 * get data to send
	 * 
	 * Format:
	 * {L:n
	 * 		{L:3
	 * 			SVID <U4>
	 * 			SVNAME <A>
	 * 			UNITS <A>
	 * 		}
	 * }
	 * 
	 */
	protected Data<?> getData() throws SecsParseException {

		L vl = new L();
		if(0 == svids.length()) {
			
			for(Map.Entry<Long, String> item: StatusVariableList.entrySet()) {
				L sl = new L();
				sl.addItem(new U4(item.getKey()));
				sl.addItem(new A(item.getValue()));
				sl.addItem(new A("1"));
				vl.addItem(sl);
			}
		}else {
			for(Data<?> item: svids.getValue()) {
				
				L sl = new L();
				Long ival = ((U4)item).getValue(0);
				String iname = StatusVariableList.get(ival);
				sl.addItem(item);
				if(iname == null) {
					sl.addItem(new A("0"));
					sl.addItem(new A("0"));
				} else {
					sl.addItem(new A(iname));
					sl.addItem(new A("1"));
				}
				vl.addItem(sl);
			}
		}
		
		return vl;
	}
	
	/**
	 * SVID List
	 * @return
	 */
	public L getSvids() {
		return svids;
	}

	public void setSvids(L svidList) {
		this.svids = svidList;
	}

	/**
	 * Variable List
	 * @return
	 */
	public L getVariables() {
		return variables;
	}

	public void setVariables(L variableList) {
		this.variables = variableList;
	}

}
