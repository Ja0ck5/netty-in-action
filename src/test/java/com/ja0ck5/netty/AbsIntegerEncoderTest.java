package com.ja0ck5.netty;

import com.ja0ck5.netty.encoder.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ja0ck5 on 2017/9/17.
 */
public class AbsIntegerEncoderTest {

    @Test
    public void testEncoded(){
        ByteBuf buf = Unpooled.buffer();
        for(int i=1;i<10;i++){
            buf.writeInt(i * -1);
        }

        // 创建一个 EmbeddedChannel 并安装一个要测试的 AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        // 写入 Buf ，并断言调用 writeOutBound
        assertTrue(channel.writeOutbound(buf));
        // 将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        // read bytes
        for(int i=1;i<10;i++){
            assertEquals(i, (int)channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }

}