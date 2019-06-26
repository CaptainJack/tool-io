package ru.capjack.tool.io

interface OutputByteBuffer {
	fun writeByte(value: Byte)
	
	fun writeInt(value: Int)
	
	fun writeLong(value: Long)
	
	fun writeArray(value: ByteArray, offset: Int = 0, size: Int = value.size - offset)
	
	fun writeBuffer(value: InputByteBuffer)
}