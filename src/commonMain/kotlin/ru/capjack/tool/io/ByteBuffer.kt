package ru.capjack.tool.io

interface ByteBuffer : InputByteBuffer, OutputByteBuffer {
	override val arrayView: ArrayView?
	
	fun clear()
	
	fun flush()
	
	interface ArrayView : InputByteBuffer.ArrayView, OutputByteBuffer.ArrayView
}