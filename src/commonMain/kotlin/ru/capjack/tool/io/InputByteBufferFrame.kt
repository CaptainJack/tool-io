package ru.capjack.tool.io

interface InputByteBufferFrame : InputByteBuffer {
	fun fill(): Boolean
}