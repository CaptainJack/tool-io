package ru.capjack.tool.io

import ru.capjack.tool.lang.alsoIf
import ru.capjack.tool.lang.lefIf

class InputByteBufferFramedView(
	private val buffer: InputByteBuffer
) : InputByteBuffer {
	
	private var state = State.SIZE
	private var size = 0
	
	override val readable: Boolean
		get() = when (state) {
			State.SIZE -> fillSize()
			State.BODY -> fillBody()
			State.READ -> true
		}
	
	override val readableSize: Int
		get() = if (readable) size else 0
	
	override fun isReadable(size: Int): Boolean {
		return readableSize >= size
	}
	
	override fun readByte(): Byte {
		checkSize(1)
		return buffer.readByte().also {
			decreaseSize(1)
		}
	}
	
	override fun readInt(): Int {
		checkSize(4)
		return buffer.readInt().also {
			decreaseSize(4)
		}
	}
	
	override fun readLong(): Long {
		checkSize(8)
		return buffer.readLong().also {
			decreaseSize(8)
		}
	}
	
	override fun readArray(target: ByteArray, offset: Int, size: Int) {
		checkSize(size)
		buffer.readByte()
		decreaseSize(size)
	}
	
	override fun readBuffer(target: OutputByteBuffer, size: Int) {
		checkSize(size)
		buffer.readBuffer(target, size)
		decreaseSize(size)
	}
	
	override fun rollbackRead(size: Int) {
		buffer.rollbackRead(size)
		this.size += size
	}
	
	private fun fillSize(): Boolean {
		return buffer.isReadable(4).lefIf {
			size = buffer.readInt()
			state = State.BODY
			fillBody()
		}
	}
	
	private fun fillBody(): Boolean {
		return buffer.isReadable(size).alsoIf {
			state = State.READ
		}
	}
	
	private fun checkSize(size: Int) {
		if (size > readableSize) {
			throw BufferReadingException(size, readableSize)
		}
	}
	
	private fun decreaseSize(v: Int) {
		size -= v
		if (size == 0) {
			state = State.SIZE
		}
	}
	
	
	private enum class State {
		SIZE, BODY, READ
	}
	
}