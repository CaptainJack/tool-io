package ru.capjack.tool.io

fun InputByteBuffer.readToArray(size: Int = readableSize): ByteArray {
	return ByteArray(size).also { readArray(it) }
}