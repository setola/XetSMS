//	jSMSEngine API.
//	An open-source API package for sending and receiving SMS via a GSM device.
//	Copyright (C) 2002-2006, Thanasis Delenikas, Athens/GREECE
//		Web Site: http://www.jsmsengine.org
//
//	jSMSEngine is distributed under the GPL license.
//
//	This program is free software; you can redistribute it and/or
//	modify it under the terms of the GNU General Public License
//	version 2 as published by the Free Software Foundation
//
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with this program; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
//

package org.jsmsengine;

import java.util.*;

/**
	This class encapsulates the basic characteristics of an SMS message. A message
	is further subclassed to an "Incoming" message and an "Outgoing" message.
	<br><br>
	This class is <strong>never</strong> used directly. Please use one of its descendants.

	@see	CIncomingMessage
	@see	CStatusReportMessage
	@see	COutgoingMessage
	@see	CPhoneBook
*/
public class CMessage
{
	public static final int MESSAGE_ENCODING_7BIT = 1;
	public static final int MESSAGE_ENCODING_8BIT = 2;
	public static final int MESSAGE_ENCODING_UNICODE = 3;

	public static final int TYPE_INCOMING = 1;
	public static final int TYPE_OUTGOING = 2;
	public static final int TYPE_STATUS_REPORT = 3;

	private int type;
	protected String id;
	protected int memIndex;
	protected Date date;
	protected String originator;
	protected String recipient;
	protected String text;
	protected int messageEncoding;

	protected int mpRefNo;
	protected int mpMaxNo;
	protected int mpSeqNo;
	protected String mpMemIndex;

	/**
		Default constructor of the class.

		@param	type	the type (incoming/outgoing) of the message.
		@param	date	the creation date of the message.
		@param	originator	the originator's number. Applicable only for incoming messages.
		@param	recipient	the recipient's number. Applicable only for outgoing messages.
		@param	text	the actual text of the message.
		@param	memIndex		the index of the memory location in the GSM device where
						this message is stored. Applicable only for incoming messages.

		<br><br>Notes:<br>
		<ul>
			<li>Phone numbers are represented in their international format (e.g. +306974... for Greece).</li>
			<li>"Recipient" may be an entry from the phonebook.</li>
		</ul>
	*/
	public CMessage(int type, Date date, String originator, String recipient, String text, int memIndex)
	{
		this.type = type;
		this.date = date;
		this.originator = originator;
		this.recipient = recipient;
		this.text = text;
		this.memIndex = memIndex;
		this.messageEncoding = MESSAGE_ENCODING_7BIT;

		mpRefNo = 0;
		mpMaxNo = 0;
		mpSeqNo = 0;
		mpMemIndex = "";
	}

	/**
		Returns the type of the message. Type is either incoming or outgoing, as denoted
		by the class' static values INCOMING and OUTGOING.

		@return  the type of the message.
	*/
	public int getType() { return type; }

	/**
		Returns the id of the message.

		@return  the id of the message.
	*/
	public String getId() { return id; }

	/**
		Returns the memory index of the GSM device, where the message is stored.
		Applicable only for incoming messages.

		@return  the memory index of the message.
	*/
	public int getMemIndex() { return memIndex; }

	/**
		Returns the date of the message. For incoming messages, this is the sent date.
		For outgoing messages, this is the creation date.

		@return  the date of the message.
	*/
	public Date getDate() { return date; }

	/**
		Returns the Originator of the message.

		@return  the originator of the message.
	*/
	public String getOriginator() { return originator; }

	/**
		Returns the Recipient of the message.

		@return  the recipient of the message.
	*/
	public String getRecipient() { return recipient; }

	/**
		Returns the actual text of the message (ASCII).

		@return  the text of the message.
	*/
	public String getText() { return text; }

	/**
		Returns the text of the message, in hexadecimal format.

		@return  the text of the message (HEX format).
	*/
	public String getHexText() { return CGSMAlphabets.text2Hex(text, CGSMAlphabets.GSM7BITDEFAULT); }

	/**
		Returns the encoding method of the message. Returns of the constants
		MESSAGE_ENCODING_7BIT, MESSAGE_ENCODING_8BIT, MESSAGE_ENCODING_UNICODE.
		This is meaningful only when working in PDU mode.

		@return  the message encoding.
	*/
	public int getMessageEncoding() { return messageEncoding; }

	public int getMpRefNo() { return mpRefNo; }
	public int getMpMaxNo() { return mpMaxNo; }
	public int getMpSeqNo() { return mpSeqNo; }
	public String getMpMemIndex() { return mpMemIndex; }

	/**
		Set the id of the message.

		@param	id	the id of the message.
	*/
	public void setId(String id) { this.id = id; }

	/**
		Set the text of the message.

		@param	text	the text of the message.
	*/
	public void setText(String text) { this.text = text; }

	/**
		Adds (concatenates) to the text of the message.

		@param	text	the text to be added.
	*/
	public void addText(String text) { this.text += text; }

	/**
		Set the date of the message.

		@param	date	the date of the message.
	*/
	public void setDate(Date date) { this.date = date; }

	/**
		Set the message encoding. Should be one of the constants
		MESSAGE_ENCODING_7BIT, MESSAGE_ENCODING_8BIT, MESSAGE_ENCODING_UNICODE.
		This is meaningful only when working in PDU mode - default is 7bit.

		@param	messageEncoding	one of the message encoding contants.
	*/
	public void setMessageEncoding(int messageEncoding) { this.messageEncoding = messageEncoding; }

	protected void setMpSeqNo(int mpSeqNo) { this.mpSeqNo = mpSeqNo; }
	protected void setMemIndex(int memIndex) { this.memIndex = memIndex; }
	protected void setMpMemIndex(int memIndex)
	{
		this.mpMemIndex += (mpMemIndex.length() == 0 ? "" : ",") + memIndex; }

	public String toString()
	{
		String str;

		str = "** GSM MESSAGE **\n";
		str += "  Type: " + (type == TYPE_INCOMING ? "Incoming." : (type == TYPE_OUTGOING ? "Outgoing." : "Status Report.")) + "\n";
		if (type == TYPE_INCOMING) str += " RefNo: " + mpRefNo + " / MaxNo: " + mpMaxNo + " / SeqNo: " + mpSeqNo + "\n";
		str += "  Id: " + id + "\n";
		str += "  Memory Index: " + memIndex + "\n";
		str += "  MP Memory Index: " + mpMemIndex + "\n";
		str += "  Date: " + date + "\n";
		str += "  Originator: " + originator + "\n";
		str += "  Recipient: " + recipient + "\n";
		str += "  Text: " + text + "\n";
		//str += "  Hex Text: " + CGSMAlphabets.text2Hex(text, CGSMAlphabets.GSM7BITDEFAULT) + "\n";
		str += "  Encoding: " + messageEncoding + "\n";
		str += "***\n";
		return str;
	}
}
