package ru.capjack.tool.io

fun InputByteBuffer.readToArray(): ByteArray {
	return ByteArray(readableSize).also { readArray(it) }
}