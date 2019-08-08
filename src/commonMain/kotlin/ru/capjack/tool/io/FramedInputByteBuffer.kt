package ru.capjack.tool.io

interface FramedInputByteBuffer : InputByteBuffer {
	val frameView: InputByteBuffer
}

