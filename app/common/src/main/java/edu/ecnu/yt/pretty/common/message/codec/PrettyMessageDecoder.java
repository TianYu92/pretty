package edu.ecnu.yt.pretty.common.message.codec;

import java.util.List;

import edu.ecnu.yt.pretty.common.exceptions.Code;
import edu.ecnu.yt.pretty.common.exceptions.PrettyRpcException;
import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyMessage;
import edu.ecnu.yt.pretty.common.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author yt
 * @date 2017-12-13 20:44:35
 */
public abstract class PrettyMessageDecoder<T> extends ByteToMessageDecoder {
	protected abstract Class<T> getBodyType();

	protected abstract PrettyMessage<T> buildMessage(PrettyHeader header, T body);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		PrettyHeader header = decodeHeader(msg);
		int bodySize = header.bodySize();

		T body = null;

		if (bodySize > 0) {
			byte[] bytes = new byte[msg.readableBytes()];
			msg.readBytes(bytes);
			body = ProtostuffUtil.deserializer(bytes, getBodyType());
		}

		out.add(buildMessage(header, body));
	}

	private PrettyHeader decodeHeader(ByteBuf buf) {

		int headerSize = PrettyHeader.SIZE;
		ByteBuf headerBuf = PooledByteBufAllocator.DEFAULT.buffer(headerSize, headerSize);
		try {
			headerBuf.writeBytes(buf, headerSize);

			// crc code check
			if (headerBuf.readShort() != PrettyHeader.MAGIC_NUMBER) {
				throw new PrettyRpcException(Code.CODEC_ERROR, "Magic Number错误");
			}

			short version = headerBuf.readShort();
			PrettyHeader.MessageType msgType = PrettyHeader.MessageType.valueOf(headerBuf.readByte());
			PrettyHeader.ConnectionType connectionType = PrettyHeader.ConnectionType.valueOf(headerBuf.readByte());

			// discard reserved bytes
			headerBuf.skipBytes(PrettyHeader.RESERVED);
			long messageId = headerBuf.readLong();
			int bodySize = headerBuf.readInt();

			PrettyHeader.Builder builder = PrettyHeader.newBuilder();
			PrettyHeader header = builder.setBodySize(bodySize)
					.setMessageId(messageId)
					.setMessageType(msgType)
					.setConnectionType(connectionType)
					.setVersion(version)
					.build();
			
			return header;
		} finally {
			headerBuf.release();
		}
		
	}

}