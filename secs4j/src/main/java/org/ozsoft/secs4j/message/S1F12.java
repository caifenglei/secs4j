package org.ozsoft.secs4j.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	private static final boolean WITH_REPLY = true;
	
	private static final String DESCRIPTION = "Status Variable Namelist Reply";
	
	private List<U4> svids = new ArrayList<U4>();
	
	private Data<?> variables = new L();
	
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
	 * data from host call
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
        L l = (L) data;
        if (l.length() == 0) {
            // no status variable definitions
        } else {
        	
        	int len = l.length();
        	List<U4> svids = new ArrayList<U4>();
        	for (int i = 0; i < len; i++) {
        		
        		Data<?> dataItem = l.getItem(i);
        		if(!(dataItem instanceof L)) {
        			throw new SecsParseException("Second level must be of type L");
        		}
        		
        		Data<?> svidItem = ((L) dataItem).getItem(0);
        		if(!(svidItem instanceof U4)) {
        			throw new SecsParseException("SVID data must be of type U4");
        		}
        		svids.add((U4) svidItem);
        		
        		Data<?> svnameItem = ((L) dataItem).getItem(1);
        		if(!(svnameItem instanceof A)) {
        			throw new SecsParseException("SVNAME data must be of type A");
        		}
        		
        		Data<?> unitsItem = ((L) dataItem).getItem(2);
        		if(!(unitsItem instanceof A)) {
        			throw new SecsParseException("UNITS data must be of type A");
        		}
//        		String units = ((A)unitsItem).getValue();
            }
        	this.setVariables(l);
        }
	}

	@Override
	/**
	 * form request data to reply
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

		L l = new L();
		
		if(0 == svids.size()) {
			
			for(Map.Entry<Long, String> item: StatusVariableList.entrySet()) {
				L sl = new L();
				sl.addItem(new U4(item.getKey()));
				sl.addItem(new A(item.getValue()));
				sl.addItem(new A("1"));
				l.addItem(sl);
			}
		}else {
			for(U4 item: svids) {
				
				L sl = new L();
				Long ival = item.getValue(0);
				String iname = StatusVariableList.get(ival);
				sl.addItem(item);
				if(iname == null) {
					sl.addItem(new A("0"));
					sl.addItem(new A("0"));
				} else {
					sl.addItem(new A(iname));
					sl.addItem(new A("1"));
				}
				l.addItem(sl);
			}
		}
		
		return l;
	}
	
	public List<U4> getSvids() {
		return svids;
	}

	public void setSvids(List<U4> svids) {
		this.svids = svids;
	}

	public Data<?> getVariables() {
		return variables;
	}

	public void setVariables(Data<?> variables) {
		this.variables = variables;
	}

}
