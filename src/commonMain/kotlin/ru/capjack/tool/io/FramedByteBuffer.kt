package ru.capjack.tool.io

class FramedByteBuffer(initialCapacity: Int = 10) : ByteBuffer(initialCapacity), FramedInputByteBuffer {
	override val framedView: InputByteBuffer = InputByteBufferFramedView(this)
}