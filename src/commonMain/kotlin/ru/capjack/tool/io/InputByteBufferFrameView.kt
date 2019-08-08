package ru.capjack.tool.io

import ru.capjack.tool.lang.alsoIf
import ru.capjack.tool.lang.lefIf
import ru.capjack.tool.lang.then

class InputByteBufferFrameView(
	private val buffer: InputByteBuffer
) : InputByteBuffer {
	
	private enum class State { WAIT_SIZE, WAIT_BODY, READ }
	
	private var state = State.WAIT_SIZE
	private var frameSize = 0
	private var frameReaderIndex = 0
	
	override val readable: Boolean
		get() = when (state) {
			State.WAIT_SIZE -> takeSize()
			State.WAIT_BODY -> takeBody()
			State.READ      -> true
		}
	
	override val readableSize: Int
		get() = if (readable) frameSize - frameReaderIndex else 0
	
	override val readableArrayView: InputByteBuffer.ArrayView
		get() = buffer.readableArrayView
	
	override fun isReadable(size: Int): Boolean {
		return readableSize >= size
	}
	
	override fun readByte(): Byte {
		checkRead(1)
		return buffer.readByte().then {
			commitRead(1)
		}
	}
	
	override fun readInt(): Int {
		checkRead(4)
		return buffer.readInt().then {
			commitRead(4)
		}
	}
	
	override fun readLong(): Long {
		checkRead(8)
		return buffer.readLong().then {
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
	
	override fun skipRead(size: Int) {
		checkRead(size)
		buffer.skipRead(size)
		commitRead(size)
	}
	
	override fun backRead(size: Int) {
		if (size < 0) {
			throw BufferUnderflowException(size, true)
		}
		if (size > frameReaderIndex) {
			throw BufferUnderflowException(size, frameReaderIndex, true)
		}
		frameReaderIndex -= size
	}
	
	private fun takeSize(): Boolean {
		return buffer.isReadable(4).lefIf {
			val v = buffer.readInt()
			when {
				v > 0  -> {
					frameSize = v
					state = State.WAIT_BODY
					takeBody()
				}
				v == 0 -> {
					state = State.WAIT_SIZE
					false
				}
				else   -> throw BufferUnderflowException("Frame size is negative ($v)")
			}
		}
	}
	
	private fun takeBody(): Boolean {
		return buffer.isReadable(frameSize).alsoIf {
			state = State.READ
		}
	}
	
	private fun checkRead(size: Int) {
		if (size < 0) {
			throw BufferUnderflowException(size)
		}
		if (size > readableSize) {
			throw BufferUnderflowException(size, readableSize)
		}
	}
	
	private fun commitRead(v: Int) {
		frameSize -= v
		if (frameSize == 0) {
			state = State.WAIT_SIZE
		}
	}
}
