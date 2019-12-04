package ru.capjack.tool.io

interface FramedInputByteBuffer : InputByteBuffer {
	val frame: InputByteBufferFrame
}

