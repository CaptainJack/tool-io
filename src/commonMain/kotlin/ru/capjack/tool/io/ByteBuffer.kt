package ru.capjack.tool.io

interface ByteBuffer : InputByteBuffer, OutputByteBuffer {
	fun clear()
}