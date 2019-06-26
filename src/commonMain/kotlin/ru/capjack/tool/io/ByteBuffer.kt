package ru.capjack.tool.io

open class ByteBuffer(initialCapacity: Int = 10) : InputByteBuffer, OutputByteBuffer {
	
	private var array = ByteArray(initialCapacity)
	private var readerCursor = 0
	private var writerCursor = 0
	
	override val readable: Boolean
		get() = readerCursor < writerCursor
	
	override val readableSize: Int
		get() = writerCursor - readerCursor
	
	
	open fun clear() {
		readerCursor = 0
		writerCursor = 0
	}
	
	override fun isReadable(size: Int): Boolean {
		return readableSize >= size
	}
	
	override fun readByte(): Byte {
		checkReadable(1)
		val value = array[readerCursor]
		commitReadable(1)
		return value
	}
	
	override fun readInt(): Int {
		checkReadable(4)
		val value = array.getInt(readerCursor)
		commitReadable(4)
		return value
	}
	
	override fun readLong(): Long {
		checkReadable(8)
		val value = array.getLong(readerCursor)
		commitReadable(8)
		return value
	}
	
	override fun readArray(target: ByteArray, offset: Int, size: Int) {
		if (size != 0) {
			if (size < 0) {
				throw IllegalArgumentException()
			}
			checkReadable(size)
			try {
				array.copyInto(target, offset, readerCursor, readerCursor + size)
			}
			catch (e: Exception) {
				println(e)
			}
			commitReadable(size)
		}
	}
	
	override fun readBuffer(target: OutputByteBuffer, size: Int) {
		if (size > 0) {
			target.writeArray(array, readerCursor, size)
			commitReadable(size)
		}
	}
	
	override fun rollbackRead(size: Int) {
		if (readerCursor < size) {
			throw BufferReadingException(size, readerCursor)
		}
		readerCursor -= size
	}
	
	override fun writeByte(value: Byte) {
		ensureWrite(1)
		array[writerCursor++] = value
	}
	
	override fun writeInt(value: Int) {
		ensureWrite(4)
		array.putInt(writerCursor, value)
		writerCursor += 4
	}
	
	override fun writeLong(value: Long) {
		ensureWrite(8)
		array.putLong(writerCursor, value)
		writerCursor += 8
	}
	
	override fun writeArray(value: ByteArray, offset: Int, size: Int) {
		if (size < 0) {
			throw IllegalArgumentException()
		}
		if (size != 0) {
			ensureWrite(size)
			value.copyInto(array, writerCursor, offset, offset + size)
			writerCursor += size
		}
	}
	
	override fun writeBuffer(value: InputByteBuffer) {
		val size = value.readableSize
		if (size > 0) {
			ensureWrite(size)
			value.readArray(array, writerCursor, size)
			writerCursor += size
		}
	}
	
	private fun checkReadable(size: Int) {
		if (readableSize < size) {
			throw BufferReadingException(size, readableSize)
		}
	}
	
	private fun commitReadable(size: Int) {
		readerCursor += size
	}
	
	private fun ensureWrite(size: Int) {
		if (readerCursor == writerCursor) {
			clear()
		}
		ensureArrayCapacity(writerCursor + size)
	}
	
	private fun ensureArrayCapacity(min: Int) {
		val old = array.size
		if (min > old) {
			var new = old + old.shl(1)
			if (new < 0) {
				new = min
			}
			array = array.copyOf(new)
		}
		else if (min < 0) {
			throw RuntimeException("Out of memory")
		}
	}
	
	companion object {
		inline operator fun invoke(block: ByteBuffer.() -> Unit): ByteBuffer {
			return ByteBuffer().apply(block)
		}
	}
}