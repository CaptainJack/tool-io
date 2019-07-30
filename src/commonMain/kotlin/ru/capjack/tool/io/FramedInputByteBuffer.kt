package ru.capjack.tool.io

interface FramedInputByteBuffer : InputByteBuffer {
	val framedView: InputByteBuffer
}

