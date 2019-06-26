package ru.capjack.tool.io

interface InputByteBuffer {
	val readable: Boolean
	
	val readableSize: Int
	
	fun isReadable(size: Int): Boolean
	
	fun readByte(): Byte
	
	fun readInt(): Int
	
	fun readLong(): Long
	
	fun readArray(target: ByteArray, offset: Int = 0, size: Int = target.size - offset)
	
	fun readBuffer(target: OutputByteBuffer, size: Int = readableSize)
	
	fun rollbackRead(size: Int)
}