package ru.capjack.tool.io

interface InputByteBuffer {
	val readable: Boolean
	
	val readableSize: Int
	
	val readableArrayView: ArrayView
	
	fun isReadable(size: Int): Boolean
	
	fun readByte(): Byte
	
	fun readInt(): Int
	
	fun readLong(): Long
	
	fun readArray(target: ByteArray, offset: Int = 0, size: Int = target.size - offset)
	
	fun readBuffer(target: OutputByteBuffer, size: Int = readableSize)
	
	fun skipRead(size: Int = readableSize)
	
	fun backRead(size: Int)
	
	interface ArrayView {
		val array: ByteArray
		val readerIndex: Int
		
		fun commitRead(size: Int)
	}
}
