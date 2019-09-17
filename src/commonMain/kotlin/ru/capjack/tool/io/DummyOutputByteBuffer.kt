package ru.capjack.tool.io

import ru.capjack.tool.lang.EMPTY_BYTE_ARRAY

class DummyOutputByteBuffer : OutputByteBuffer, OutputByteBuffer.ArrayView {
	override val writerIndex: Int
		get() = 0
	
	override val writeableArrayView: OutputByteBuffer.ArrayView
		get() = this
	
	override val array: ByteArray
		get() = EMPTY_BYTE_ARRAY
	
	override fun writeByte(value: Byte) {
		throw UnsupportedOperationException()
	}
	
	override fun writeInt(value: Int) {
		throw UnsupportedOperationException()
	}
	
	override fun writeLong(value: Long) {
		throw UnsupportedOperationException()
	}
	
	override fun writeArray(value: ByteArray, offset: Int, size: Int) {
		throw UnsupportedOperationException()
	}
	
	override fun writeBuffer(value: InputByteBuffer) {
		throw UnsupportedOperationException()
	}
	
	override fun ensureWrite(size: Int) {
		throw UnsupportedOperationException()
	}
	
	override fun skipWrite(size: Int) {
		throw UnsupportedOperationException()
	}
	
	override fun commitWrite(size: Int) {
		throw UnsupportedOperationException()
	}
}