package ru.capjack.tool.io

import ru.capjack.tool.lang.EMPTY_BYTE_ARRAY

object EmptyInputByteBuffer : InputByteBuffer, InputByteBuffer.ArrayView {
	override val readable: Boolean = false
	override val readableSize: Int = 0
	override val readableArrayView: InputByteBuffer.ArrayView = this
	override val readerIndex: Int = 0
	
	override val array: ByteArray
		get() = EMPTY_BYTE_ARRAY
	
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
	
	override fun readArray(target: ByteArray, offset: Int, size: Int) {
		if (size != 0) {
			throw BufferUnderflowException(size, 0)
		}
	}
	
	override fun readBuffer(target: OutputByteBuffer, size: Int) {
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
}
