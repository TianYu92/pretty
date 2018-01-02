package edu.ecnu.yt.pretty.common.message.codec;

import java.util.List;

import edu.ecnu.yt.pretty.common.message.PrettyHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


/**
 * 用于解决粘包和拆包问题
 * @author yutian
 */
public class PrettyMessageFrameDecoder extends ByteToMessageDecoder {
	
	private int getMessageLength(ByteBuf in) {
		int bodySize;
		// 5th (0~4) position is body size field;
		try {
			// 此处不能用getInt.虽然它不改变readerIndex。但由于使用的msg可能是上一次的
			// msg。只是readerIndex改变，所以使用get会获得前一次的bodySize是错的。需要
			// 使用skip + read方法。
//			bodySize = msg.getInt(16);
			in.skipBytes(16);
			bodySize = in.readInt();
		} catch (IndexOutOfBoundsException e) {
			// the size is less than 20byte. means that the header has not whole loaded.
			return -1;
		}		
		int rareBytes = in.readableBytes();
		int length = bodySize + PrettyHeader.SIZE;
		return rareBytes < bodySize ? -1 : length;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		// do not modified the readerindex of bytebuf for reusing 
		// by ByteToMessageDecoder 
		in.markReaderIndex();
		int length = getMessageLength(in);
		in.resetReaderIndex(); //因为还是要把header读回，所以复位readerIndex
		if (length < 0) {
			return;
		}
		out.add(in.readRetainedSlice(length));
	}

	public static String getName() {
		return "frame-decoder";
	}
}