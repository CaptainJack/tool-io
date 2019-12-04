package ru.capjack.tool.io

class FramedArrayByteBuffer(initialCapacity: Int = 10) : ArrayByteBuffer(initialCapacity), FramedInputByteBuffer {
	override val frame: InputByteBufferFrame = InputByteBufferFrameImpl(this)
}