package org.ozsoft.secs4j.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ozsoft.secs4j.SecsException;
import org.ozsoft.secs4j.SecsParseException;
import org.ozsoft.secs4j.SecsReplyMessage;
import org.ozsoft.secs4j.format.Data;
import org.ozsoft.secs4j.format.L;

/**
 * S1F24 - Collection Event Namelist Reply, Only associated DVVAL VIDs are
 * listed. A:0 for CENAME and L:0 for L:a indicates non-existent CEID. (proposed
 * ballot item 4824B, Oct 2011)
 * 
 * @author caifenglei
 *
 */
public class S1F24 extends SecsReplyMessage {
	private static final int STREAM = 1;

	private static final int FUNCTION = 24;

	private static final boolean WITH_REPLY = false;

	private static final String DESCRIPTION = "Collection Event Namelist Reply";

	private L ceids = new L();

	private static final HashMap<Long, HashMap<String, ArrayList<Long>>> EventCollection;

	static {
//		EventCollection = new HashMap<Long, HashMap<String, ArrayList<Long>>>();
		EventCollection = new HashMap<Long, HashMap<String, ArrayList<Long>>>();
		
//		EventCollection.put(3001L, "EquipmentOperationStatus");
//		StatusVariableList.put(1002L, "EquipmentOperator");
//		StatusVariableList.put(1003L, "EquipmentDateTime");
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
	protected void parseData(Data<?> data) throws SecsParseException {
		// TODO Auto-generated method stub

	}

	@Override
	protected Data<?> getData() throws SecsParseException {

		return null;
	}

	public void setCeids(L ceidList) {
		this.ceids = ceidList;
	}

}
