package edu.ecnu.yt.pretty.common.message;

public class PrettyHeader {
	
	public static final int SIZE = 20;
	public static final int MAGIC_NUMBER = 0x1121;
	public static final short DEFAULT_VERSION = 100;
	
	/// TOTAL: 22BYTE
	// 2Byte
	private final short magic = MAGIC_NUMBER;
	// 2Byte
	private final short version;
	// 1Byte
	private final MessageType messageType;
	// 1Byte
	private final ConnectionType connectionType;
	// here need reserved.
	// 2Byte
	public static final int RESERVED = 2;
	// 8Byte
	private final long messageId;
	// 4Byte
	private final int bodySize;
	
	private PrettyHeader(short version, MessageType messageType, ConnectionType connectionType, long messageId, int bodySize) {
		this.messageType = messageType;
		this.connectionType = connectionType;
		this.messageId = messageId;
		this.version = version;
		this.bodySize = bodySize;
	}

	public static Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		private MessageType messageType = MessageType.UNKNOWN;
		
		private long messageId = -1;
		
		private ConnectionType connectionType = ConnectionType.SHORT;
		
		private short version = DEFAULT_VERSION;
		private int bodySize = 0;
		
		private Builder() {}

		public Builder setVersion(short version) {
			this.version = version;
			return this;
		}
		public Builder setMessageType(MessageType type) {
			this.messageType = type;
			return this;
		}
		public Builder setMessageId(long messageId) {
			this.messageId = messageId;
			return this;
		}
		public Builder setConnectionType(ConnectionType connectionType) {
			this.connectionType = connectionType;
			return this;
		}
		
		public Builder setBodySize(int bodySize) {
			this.bodySize = bodySize;
			return this;
		}
		
		public PrettyHeader build() {
			return new PrettyHeader(version, messageType, connectionType, messageId, bodySize);
		}
	}
	
	public static enum MessageType {
		NORMAL_REQ, 
		NORMAL_RES,
		ONE_WAY, 
		HANDSHAKE_REQ, 
		HANDSHAKE_RES, 
		HEARTBEAT_REQ,
		HEARTBEAT_RES,
		UNKNOWN;
		
		private byte value = 0;

	    private MessageType()  {
	        this.value = (byte) ordinal();
	    }

	    public static MessageType valueOf(byte value) {
	    	for (MessageType type : MessageType.values()) {
	    		if (type.value() == value) return type;
	    	}
	    	return null;
	    }

	    public byte value() {
	        return this.value;
	    }
	}
	
	public static enum ConnectionType {
		LONG, SHORT;
		
		private byte value = 0;

	    private ConnectionType()  {
	        this.value = (byte) ordinal();
	    }

	    public static ConnectionType valueOf(byte value) {
	    	for (ConnectionType type : ConnectionType.values()) {
	    		if (type.value() == value) {
	    			return type;
				}
	    	}
	    	return null;
	    }

	    public byte value() {
	        return this.value;
	    }
	}

	public MessageType messageType() {
		return messageType;
	}
	
	public int bodySize() {
		return bodySize;
	}

	public long messageId() {
		return messageId;
	}

	public short version() {
		return version;
	}

	public short magic() {
		return magic;
	}

	public ConnectionType connectionType() {
		return connectionType;
	}

}
