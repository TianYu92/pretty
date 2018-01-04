package edu.ecnu.yt.pretty.common.message.codec;

import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import edu.ecnu.yt.pretty.common.message.PrettyMessage;
import edu.ecnu.yt.pretty.common.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author yt
 * @date 2017-12-23 22:57:13
 */
public class PrettyMessageEncoder extends MessageToByteEncoder<PrettyMessage<?>> {
		
	public void encodeHeader(ByteBuf buf, PrettyHeader header, int bodySize) {
		buf.writeShort(header.magic()); // 2byte
		buf.writeShort(header.version()); // 2byte
		buf.writeByte(header.messageType().value()); // 1byte
		buf.writeByte(header.connectionType().value()); // 1byte
		// reserved
		buf.writeBytes(new byte[PrettyHeader.RESERVED]); // 2byte
		buf.writeLong(header.messageId()); // 8byte
		buf.writeInt(bodySize); // 4byte
		// 20byte -> 160bit.
	}

	/**
	 * Is sharable boolean.
	 *
	 * @return the boolean
	 */
	@Override
	public boolean isSharable() {
		return true;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, PrettyMessage<?> msg, ByteBuf out) throws Exception {
				
		byte[] bodyBuf = null;
		int bodySize = 0;
		
		if (msg.body() != null) {
			bodyBuf = ProtostuffUtil.serializer(msg.body());
			bodySize = bodyBuf.length;
		}
		
		encodeHeader(out, msg.header(), bodySize);
		
		if (bodySize > 0) {
			out.writeBytes(bodyBuf);
		}
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	public static String getName() {
		return "message-encoder";
	}
	
}
