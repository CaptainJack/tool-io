package ru.capjack.tool.io

import ru.capjack.tool.lang.EMPTY_BYTE_ARRAY

object DummyByteBuffer : ByteBuffer, InputByteBuffer.ArrayView, OutputByteBuffer.ArrayView {
	override val readable: Boolean
		get() = false
	
	override val readableSize: Int
		get() = 0
	
	override val readerIndex: Int
		get() = 0
	
	override val readableArrayView: InputByteBuffer.ArrayView
		get() = this
	
	override val array: ByteArray
		get() = EMPTY_BYTE_ARRAY
	
	override val writerIndex: Int
		get() = 0
	
	override val writeableArrayView: OutputByteBuffer.ArrayView
		get() = this
	
	override fun clear() {
	}
	
	override fun isReadable(size: Int): Boolean {
		return false
	}
	
	override fun readByte(): Byte {
		throw BufferUnderflowException(1, 0)
	}
	
	override fun readInt(): Int {
		throw BufferUnderflowException(4, 0)
	}
	
	override fun readLong(): Long {
		throw BufferUnderflowException(8, 0)
	}
	
	override fun readToArray(target: ByteArray, offset: Int, size: Int) {
		if (size != 0) {
			throw BufferUnderflowException(size, 0)
		}
	}
	
	override fun readToBuffer(target: OutputByteBuffer, size: Int) {
		if (size != 0) {
			throw BufferUnderflowException(size, 0)
		}
	}
	
	override fun skipRead(size: Int) {
		if (size != 0) {
			throw BufferUnderflowException(size, 0)
		}
	}
	
	override fun backRead(size: Int) {
		if (size != 0) {
			throw BufferUnderflowException(size, 0, true)
		}
	}
	
	override fun commitRead(size: Int) {
		if (size != 0) {
			throw BufferUnderflowException(size, 0)
		}
	}
	
	override fun flush() {
	}
	
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