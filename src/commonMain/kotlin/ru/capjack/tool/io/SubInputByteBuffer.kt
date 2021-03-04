package ru.capjack.tool.io

class SubInputByteBuffer() : InputByteBuffer, InputByteBuffer.ArrayView {
	private var source: InputByteBuffer = DummyInputByteBuffer
	private var size = 0
	private var _readerIndex = 0
	
	constructor(source: InputByteBuffer, size: Int) : this() {
		bindSource(source, size)
	}
	
	fun bindSource(source: InputByteBuffer, size: Int) {
		if (source.isReadable(size)) {
			this.source = source
			this.size = size
			_readerIndex = 0
		}
		else {
			throw BufferUnderflowException(size, source.readableSize)
		}
	}
	
	fun unbindSource() {
		source = DummyInputByteBuffer
		size = 0
		_readerIndex = 0
	}
	
	override val readable: Boolean
		get() = readableSize > 0
	
	override val readableSize: Int
		get() = size - _readerIndex
	
	
	override val readableArrayView: InputByteBuffer.ArrayView
		get() = this
	
	override val readerIndex: Int
		get() = source.readableArrayView.readerIndex
	
	override val array: ByteArray
		get() = source.readableArrayView.array
	
	override fun commitRead(size: Int) {
		skipRead(size)
	}
	
	
	override fun isReadable(size: Int): Boolean {
		return readableSize >= size
	}
	
	override fun readByte(): Byte {
		checkRead(1)
		return source.readByte().also {
			completeRead(1)
		}
	}
	
	override fun readInt(): Int {
		checkRead(4)
		return source.readInt().also {
			completeRead(4)
		}
	}
	
	override fun readLong(): Long {
		checkRead(8)
		return source.readLong().also {
			completeRead(8)
		}
	}
	
	override fun readToArray(target: ByteArray, offset: Int, size: Int) {
		checkRead(size)
		source.readToArray(target, offset, size)
		completeRead(size)
	}
	
	override fun readToBuffer(target: OutputByteBuffer, size: Int) {
		checkRead(size)
		source.readToBuffer(target, size)
		completeRead(size)
	}
	
	override fun skipRead(size: Int) {
		checkRead(size)
		source.skipRead(size)
		completeRead(size)
	}
	
	override fun backRead(size: Int) {
		if (size < 0) {
			throw BufferUnderflowException(size, true)
		}
		if (size > _readerIndex) {
			throw BufferUnderflowException(size, _readerIndex, true)
		}
		_readerIndex -= size
	}
	
	private fun checkRead(size: Int) {
		if (size < 0) {
			throw BufferUnderflowException(size)
		}
		if (size > readableSize) {
			throw BufferUnderflowException(size, readableSize)
		}
	}
	
	private fun completeRead(v: Int) {
		_readerIndex += v
	}
}