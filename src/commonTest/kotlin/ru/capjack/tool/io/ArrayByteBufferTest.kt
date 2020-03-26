package ru.capjack.tool.io

import ru.capjack.tool.lang.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class ArrayByteBufferTest {
	@Test
	fun flush() {
		val buffer = ArrayByteBuffer(12)
		
		buffer.writeInt(1)
		buffer.writeInt(2)
		
		buffer.readInt()
		buffer.writeInt(3)
		
		buffer.flush()
		
		assertEquals(0, buffer.readerIndex)
		assertEquals(8, buffer.writerIndex)
		assertEquals("000000020000000300000003", buffer.array.toHexString())
	}
}