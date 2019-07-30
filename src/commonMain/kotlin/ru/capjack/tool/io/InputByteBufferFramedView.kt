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
		checkRead(1)
		return buffer.readByte().also {
			commitRead(1)
		}
	}
	
	override fun readInt(): Int {
		checkRead(4)
		return buffer.readInt().also {
			commitRead(4)
		}
	}
	
	override fun readLong(): Long {
		checkRead(8)
		return buffer.readLong().also {
			commitRead(8)
		}
	}
	
	override fun readArray(target: ByteArray, offset: Int, size: Int) {
		checkRead(size)
		buffer.readArray(target, offset, size)
		commitRead(size)
	}
	
	override fun readBuffer(target: OutputByteBuffer, size: Int) {
		checkRead(size)
		buffer.readBuffer(target, size)
		commitRead(size)
	}
	
	override fun readSkip(size: Int) {
		checkRead(size)
		buffer.readSkip(size)
		commitRead(size)
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
	
	private fun checkRead(size: Int) {
		if (size < 0) {
			throw IllegalArgumentException("Reading size is negative")
		}
		if (size > readableSize) {
			throw BufferReadingException(size, readableSize)
		}
	}
	
	private fun commitRead(v: Int) {
		size -= v
		if (size == 0) {
			state = State.SIZE
		}
	}
	
	
	private enum class State {
		SIZE, BODY, READ
	}
	
}