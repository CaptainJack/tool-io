package ru.capjack.tool.io

object EmptyInputByteBuffer : InputByteBuffer {
	override fun rollbackRead(size: Int) {
		if (size > 0) {
			throw BufferReadingException(size, 0)
		}
	}
	
	override val readable: Boolean = false
	
	override val readableSize: Int = 0
	
	override fun isReadable(size: Int): Boolean {
		return false
	}
	
	override fun readByte(): Byte {
		throw BufferReadingException(1, 0)
	}
	
	override fun readInt(): Int {
		throw BufferReadingException(4, 0)
	}
	
	override fun readLong(): Long {
		throw BufferReadingException(8, 0)
	}
	
	override fun readArray(target: ByteArray, offset: Int, size: Int) {
		if (size != 0) {
			throw BufferReadingException(size, 0)
		}
	}
	
	override fun readBuffer(target: OutputByteBuffer, size: Int) {
		if (size != 0) {
			throw BufferReadingException(size, 0)
		}
	}
}

