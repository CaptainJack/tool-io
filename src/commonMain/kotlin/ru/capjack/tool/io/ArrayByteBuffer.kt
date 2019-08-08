package ru.capjack.tool.io

import ru.capjack.tool.lang.then

open class ArrayByteBuffer(initialCapacity: Int = 10) : InputByteBuffer, InputByteBuffer.ArrayView, OutputByteBuffer, OutputByteBuffer.ArrayView {
	companion object {
		inline operator fun invoke(block: ArrayByteBuffer.() -> Unit): ArrayByteBuffer {
			return ArrayByteBuffer().apply(block)
		}
		
		inline operator fun invoke(initialCapacity: Int, block: ArrayByteBuffer.() -> Unit): ArrayByteBuffer {
			return ArrayByteBuffer(initialCapacity).apply(block)
		}
		
		operator fun invoke(from: InputByteBuffer): ArrayByteBuffer {
			return ArrayByteBuffer(from.readableSize) { writeBuffer(from) }
		}
		
		operator fun invoke(bytes: ByteArray): ArrayByteBuffer {
			return ArrayByteBuffer(bytes.size) { writeArray(bytes) }
		}
	}
	
	
	private var _array = ByteArray(initialCapacity)
	private var _readerIndex = 0
	private var _writerIndex = 0
	
	
	override val readerIndex: Int
		get() = _readerIndex
	
	override val writerIndex: Int
		get() = _writerIndex
	
	override val readable: Boolean
		get() = _readerIndex < _writerIndex
	
	override val readableSize: Int
		get() = _writerIndex - _readerIndex
	
	override val readableArrayView: InputByteBuffer.ArrayView
		get() = this
	
	override val writeableArrayView: OutputByteBuffer.ArrayView
		get() = this
	
	override var array: ByteArray
		get() = _array
		set(value) {
			clear()
			_array = value
			_writerIndex = _array.size
		}
	
	
	fun clear() {
		_readerIndex = 0
		_writerIndex = 0
	}
	
	override fun isReadable(size: Int): Boolean {
		return readableSize >= size
	}
	
	override fun readByte(): Byte {
		checkRead(1)
		return _array[_readerIndex].then {
			commitRead(1)
		}
	}
	
	override fun readInt(): Int {
		checkRead(4)
		return _array.getInt(_readerIndex).then {
			commitRead(4)
		}
	}
	
	override fun readLong(): Long {
		checkRead(8)
		return _array.getLong(_readerIndex).then {
			commitRead(8)
		}
	}
	
	override fun readArray(target: ByteArray, offset: Int, size: Int) {
		if (size != 0) {
			checkRead(size)
			_array.copyInto(target, offset, _readerIndex, _readerIndex + size)
			commitRead(size)
		}
	}
	
	override fun readBuffer(target: OutputByteBuffer, size: Int) {
		if (size != 0) {
			checkRead(size)
			target.writeArray(_array, _readerIndex, size)
			commitRead(size)
		}
	}
	
	override fun skipRead(size: Int) {
		if (size != 0) {
			checkRead(size)
			commitRead(size)
		}
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
	
	override fun writeByte(value: Byte) {
		ensureWrite(1)
		_array[_writerIndex] = value
		commitWrite(1)
	}
	
	override fun writeInt(value: Int) {
		ensureWrite(4)
		_array.putInt(_writerIndex, value)
		commitWrite(4)
	}
	
	override fun writeLong(value: Long) {
		ensureWrite(8)
		_array.putLong(_writerIndex, value)
		commitWrite(8)
	}
	
	override fun writeArray(value: ByteArray, offset: Int, size: Int) {
		if (size != 0) {
			ensureWrite(size)
			value.copyInto(_array, _writerIndex, offset, offset + size)
			commitWrite(size)
		}
	}
	
	override fun writeBuffer(value: InputByteBuffer) {
		val size = value.readableSize
		if (size != 0) {
			ensureWrite(size)
			value.readArray(_array, _writerIndex, size)
			commitWrite(size)
		}
	}
	
	override fun ensureWrite(size: Int) {
		if (size < 0) {
			throw BufferUnderflowException("Writing size is negative ($size)")
		}
		if (_readerIndex == _writerIndex && _readerIndex != 0) {
			clear()
		}
		
		val minCapacity = _writerIndex + size
		val oldCapacity = _array.size
		if (minCapacity > oldCapacity) {
			var newCapacity = oldCapacity + oldCapacity.shl(1)
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity
			}
			_array = _array.copyOf(newCapacity)
		}
		else if (minCapacity < 0) {
			throw BufferOverflowException("Buffer capacity overflow (current: $oldCapacity, additional: $size, max: ${Int.MAX_VALUE})")
		}
	}
	
	override fun skipWrite(size: Int) {
		if (size != 0) {
			ensureWrite(size)
			commitWrite(size)
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
	
	private fun commitRead(size: Int) {
		_readerIndex += size
	}
	
	private fun commitWrite(size: Int) {
		_writerIndex += size
	}
}
